package com.studyquest.character;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import com.studyquest.system.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AuthServiceTest {
    @TempDir Path folder;

    @Test void registerNormalizesAndRejectsDuplicateCaseAndInvalidInputs() {
        var auth=new AuthService(new FileDataRepository(folder));
        var state=auth.register("Max_123","secret");
        assertEquals("max_123",state.getUser().getUsername()); assertNull(state.getUser().getCharacter());
        assertNotNull(state.getUser().getCreatedAt());
        assertThrows(IllegalArgumentException.class,()->auth.register("MAX_123","secret"));
        for(String username:new String[]{"ab","a".repeat(21),"../max","max user","mäx"," max"})
            assertThrows(IllegalArgumentException.class,()->auth.register(username,"secret"));
        assertThrows(IllegalArgumentException.class,()->auth.register("other","short"));
    }

    @Test void hashesUseApprovedParametersAndUniqueSalts() {
        var auth=new AuthService(new FileDataRepository(folder));
        String a=auth.register("max","secret").getUser().getPasswordHash();
        String b=auth.register("other","secret").getUser().getPasswordHash();
        assertNotEquals(a,b); assertFalse(a.contains("secret"));
        String[] fields=a.split("\\$");
        assertEquals("pbkdf2-sha256",fields[0]); assertEquals("120000",fields[1]);
        assertEquals(16,Base64.getDecoder().decode(fields[2]).length);
        assertEquals(32,Base64.getDecoder().decode(fields[3]).length);
        assertEquals("max",auth.login("MAX","secret").getUser().getUsername());
    }

    @Test void everyWrongCredentialGetsSameMessage() {
        var auth=new AuthService(new FileDataRepository(folder)); auth.register("max","secret");
        String wrong=assertThrows(IllegalArgumentException.class,()->auth.login("max","wrong")).getMessage();
        assertEquals(wrong,assertThrows(IllegalArgumentException.class,()->auth.login("missing","secret")).getMessage());
        assertEquals(wrong,assertThrows(IllegalArgumentException.class,()->auth.login("../max","secret")).getMessage());
        assertEquals(wrong,assertThrows(IllegalArgumentException.class,()->auth.login("max",null)).getMessage());
    }

    @Test void corruptAccountIsNotOverwrittenOrRegisteredAsNew() throws Exception {
        var repository=new FileDataRepository(folder); var auth=new AuthService(repository);
        auth.register("max","secret"); Path data=folder.resolve("max.dat"); Files.writeString(data,"broken");
        assertThrows(UncheckedIOException.class,()->auth.login("max","secret"));
        assertThrows(IllegalArgumentException.class,()->auth.register("MAX","otherpass"));
        assertEquals("broken",Files.readString(data));
    }

    @Test void registrationSaveFailureDoesNotPublishSession() {
        DataRepository failing=new DataRepository() {
            public void save(GameState state) throws IOException { throw new IOException("disk"); }
            public GameState load(String username) throws IOException { throw new IOException("disk"); }
            public boolean exists(String username) { return false; }
        };
        var controller=new GameController(failing);
        assertThrows(UncheckedIOException.class,()->controller.register("max","secret"));
        assertNull(controller.getState());
    }

    @Test void unsupportedHashIsNotTreatedAsNewAccount() throws Exception {
        var repository=new FileDataRepository(folder);
        repository.save(new GameState(new User("max","oldformat")));
        assertThrows(IllegalStateException.class,()->new AuthService(repository).login("max","secret"));
    }
}
