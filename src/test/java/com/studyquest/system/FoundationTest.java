package com.studyquest.system;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import com.studyquest.character.User;
import com.studyquest.item.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FoundationTest {
    @TempDir Path folder;

    @Test void closeAndReopenAccountRetainsCharacterAndInventory() throws Exception {
        var repository=new FileDataRepository(folder);
        var controller=new GameController(repository);
        controller.register("Max","secret"); controller.createCharacter("Mage","Jury");
        var c=controller.getState().getUser().getCharacter(); c.gainXP(110); c.getInventory().addGold(23);
        c.getInventory().addItem(new TestItem("book"));
        controller.saveNow(); controller.logout(); assertNull(controller.getState());
        var reopened=new GameController(new FileDataRepository(folder)); reopened.login("MAX","secret");
        var copy=reopened.getState().getUser().getCharacter();
        assertEquals("Jury",copy.getName()); assertEquals("Mage",copy.getClassName());
        assertEquals(2,copy.getLevel()); assertEquals(10,copy.getXp()); assertEquals(23,copy.getInventory().getGold());
        assertEquals("book",copy.getInventory().getItems().getFirst().getId());
        assertThrows(IllegalStateException.class,()->reopened.createCharacter("Warrior","Again"));
    }

    @Test void damagedDataFallsBackAndSavePreservesHealthyBackup() throws Exception {
        var repository=new FileDataRepository(folder); var controller=new GameController(repository);
        controller.register("max","secret"); controller.createCharacter("Warrior","Max"); controller.saveNow();
        Path data=folder.resolve("max.dat"), backup=folder.resolve("max.dat.bak");
        byte[] healthy=Files.readAllBytes(backup); Files.writeString(data,"broken");
        controller.login("max","secret"); assertTrue(controller.getState().isRecoveredFromBackup());
        controller.saveNow(); assertArrayEquals(healthy,Files.readAllBytes(backup));
        assertNotNull(repository.load("max").getUser().getCharacter());
        assertFalse(Files.exists(folder.resolve("max.dat.tmp")));
    }

    @Test void bothDamagedRemainUntouchedAndBackupOnlyCannotBeRegistered() throws Exception {
        var repository=new FileDataRepository(folder);
        Files.writeString(folder.resolve("max.dat"),"broken"); Files.writeString(folder.resolve("max.dat.bak"),"bad backup");
        assertThrows(IOException.class,()->repository.load("max"));
        assertEquals("broken",Files.readString(folder.resolve("max.dat")));
        assertEquals("bad backup",Files.readString(folder.resolve("max.dat.bak")));
        Files.delete(folder.resolve("max.dat"));
        assertTrue(repository.exists("max"));
        assertThrows(IllegalArgumentException.class,()->new GameController(repository).register("max","secret"));
    }

    @Test void rejectsWrongAccountAndPathTraversal() throws Exception {
        var repository=new FileDataRepository(folder);
        repository.save(new GameState(new User("alice","testhash")));
        Files.copy(folder.resolve("alice.dat"),folder.resolve("bob.dat"));
        assertThrows(IOException.class,()->repository.load("bob"));
        assertThrows(IllegalArgumentException.class,()->repository.exists("../alice"));
    }

    @Test void failedCreationAndLogoutKeepOriginalSession() {
        var delegate=new FileDataRepository(folder);
        class Failable implements DataRepository {
            boolean fail;
            public void save(GameState state) throws IOException { if(fail)throw new IOException("disk"); delegate.save(state); }
            public GameState load(String u)throws IOException { return delegate.load(u); }
            public boolean exists(String u){return delegate.exists(u);}
        }
        var repository=new Failable(); var controller=new GameController(repository);
        controller.register("max","secret"); var original=controller.getState(); repository.fail=true;
        assertThrows(UncheckedIOException.class,()->controller.createCharacter("Mage","Max"));
        assertSame(original,controller.getState()); assertNull(original.getUser().getCharacter());
        assertThrows(UncheckedIOException.class,controller::logout); assertSame(original,controller.getState());
        repository.fail=false; controller.createCharacter("Mage","Max");
        assertNotNull(controller.getState().getUser().getCharacter());
    }

    @Test void missingSessionOrInvalidClassCannotCreateCharacter() {
        var controller=new GameController(new FileDataRepository(folder));
        assertThrows(IllegalStateException.class,()->controller.createCharacter("Warrior","Max"));
        controller.register("max","secret");
        assertThrows(IllegalArgumentException.class,()->controller.createCharacter("Paladin","Max"));
        assertNull(controller.getState().getUser().getCharacter());
    }

    @Test void inventoryCapacityGoldAndConsumptionAreValidated() {
        var inventory=new Inventory();
        for(int i=0;i<20;i++) inventory.addItem(new TestItem("item"+i));
        assertTrue(inventory.isFull()); assertThrows(InventoryFullException.class,()->inventory.addItem(new TestItem("extra")));
        inventory.expandCapacity(5); assertEquals(25,inventory.getCapacity());
        assertThrows(IllegalArgumentException.class,()->inventory.expandCapacity(1));
        inventory.addGold(10); assertThrows(InsufficientGoldException.class,()->inventory.spendGold(11));
        assertEquals(10,inventory.getGold()); inventory.spendGold(10); assertEquals(0,inventory.getGold());
        assertThrows(IllegalArgumentException.class,()->inventory.addGold(-1));
        var character=new com.studyquest.character.Warrior("Max");
        assertFalse(inventory.useItem(inventory.getItems().getFirst(),character));
        class Potion extends Item implements Usable {
            private static final long serialVersionUID=1L;
            Potion(){super("potion","Potion",10,1);}
            public boolean use(com.studyquest.character.Character c){c.heal(10);return true;}
        }
        var potion=new Potion(); inventory.addItem(potion); character.takeDamage(20);
        assertTrue(inventory.useItem(potion,character)); assertEquals(110,character.getHp());
        assertFalse(inventory.useItem(potion,character)); assertEquals(20,inventory.getItems().size());
    }

    private static class TestItem extends Item {
        private static final long serialVersionUID=1L;
        TestItem(String id){super(id,id,10,1);}
    }
}
