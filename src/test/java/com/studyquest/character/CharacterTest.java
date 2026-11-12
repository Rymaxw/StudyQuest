package com.studyquest.character;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.Random;
import com.studyquest.battle.Monster;
import org.junit.jupiter.api.Test;

class CharacterTest {
    private Monster target() { return new Monster("Cible", 10_000, 1) {}; }

    @Test void initialStatsAndGrowthForEveryClass() {
        Character[] characters = {new Warrior("Max"), new Mage("Max"), new Archer("Max")};
        int[][] initial = {{120,14,4,30}, {80,6,16,60}, {100,12,8,40}};
        int[][] growth = {{20,4,1}, {10,1,5}, {14,3,2}};
        for (int i = 0; i < characters.length; i++) {
            var c = characters[i];
            assertArrayEquals(initial[i], new int[]{c.getHp(),c.getAttack(),c.getIntelligence(),c.getMp()});
            assertFalse(c.gainXP(99)); assertTrue(c.gainXP(1));
            assertEquals(2, c.getLevel()); assertEquals(0, c.getXp()); assertEquals(150, c.xpToNextLevel());
            assertArrayEquals(new int[]{initial[i][0]+growth[i][0],initial[i][1]+growth[i][1],initial[i][2]+growth[i][2]},
                    new int[]{c.getMaxHp(),c.getAttack(),c.getIntelligence()});
        }
    }

    @Test void multipleLevelsCarryRemainderAndFullyRestore() {
        var c = new Warrior("Max");
        c.takeDamage(119); c.useSkill(0, target());
        assertTrue(c.gainXP(470));
        assertEquals(4, c.getLevel()); assertEquals(20, c.getXp());
        assertEquals(180, c.getHp()); assertEquals(c.getMaxMp(), c.getMp());
        assertEquals(26, c.getAttack()); assertEquals(7, c.getIntelligence());
        c.loseXP(Integer.MAX_VALUE); assertEquals(0, c.getXp()); assertEquals(4, c.getLevel());
    }

    @Test void hpMpLimitsAndNegativeInputs() {
        var c = new Mage("Max");
        c.takeDamage(Integer.MAX_VALUE); assertEquals(0,c.getHp());
        c.setHpToOne(); assertEquals(1,c.getHp());
        c.heal(Integer.MAX_VALUE); assertEquals(80,c.getHp());
        c.restoreMp(Integer.MAX_VALUE); assertEquals(60,c.getMp());
        assertThrows(IllegalArgumentException.class, () -> c.takeDamage(-1));
        assertThrows(IllegalArgumentException.class, () -> c.heal(-1));
        assertThrows(IllegalArgumentException.class, () -> c.restoreMp(-1));
        assertThrows(IllegalArgumentException.class, () -> c.gainXP(-1));
        assertThrows(IllegalArgumentException.class, () -> c.loseXP(-1));
    }

    @Test void damageAndDeterministicCriticalBoundary() {
        var target = target();
        assertEquals(16,new Warrior("Max").basicAttack(target));
        assertEquals(24,new Mage("Max").basicAttack(target));
        Random crit = new Random() { public double nextDouble() { return 0.249999; } };
        Random nonCrit = new Random() { public double nextDouble() { return 0.25; } };
        assertEquals(24,new Archer("Max", crit).basicAttack(target));
        assertEquals(12,new Archer("Max", nonCrit).basicAttack(target));
        assertEquals(10_000-16-24-24-12,target.getHp());
    }

    @Test void everySkillCostsMpAndRejectsInsufficientWithoutDamage() {
        Character[] characters = {new Warrior("Max"),new Mage("Max"),new Archer("Max")};
        int[] damage = {28,40,24}, cost = {10,15,12};
        for (int i=0;i<characters.length;i++) {
            var c=characters[i]; var target=target();
            while(c.getMp()>=cost[i]) assertEquals(damage[i],c.useSkill(0,target));
            int hp=target.getHp(), mp=c.getMp();
            assertThrows(IllegalStateException.class,()->c.useSkill(0,target));
            assertEquals(hp,target.getHp()); assertEquals(mp,c.getMp());
            assertThrows(IllegalArgumentException.class,()->c.useSkill(1,target));
            assertEquals(mp,c.getMp());
        }
    }

    @Test void statModificationClampsWithoutHealingAndRejectsInvalidAtomically() {
        var c=new Warrior("Max"); c.takeDamage(50);
        c.modifyStat(StatType.HP,20); assertEquals(140,c.getMaxHp()); assertEquals(70,c.getHp());
        c.modifyStat(StatType.HP,-100); assertEquals(40,c.getHp());
        assertThrows(IllegalArgumentException.class,()->c.modifyStat(StatType.HP,-40));
        assertEquals(40,c.getMaxHp());
        assertThrows(IllegalArgumentException.class,()->c.modifyStat(StatType.ATK,-15));
        assertEquals(14,c.getAttack());
        c.modifyStat(StatType.ATK,3); c.modifyStat(StatType.INT,4);
        assertEquals(17,c.getAttack()); assertEquals(8,c.getIntelligence());
    }

    @Test void nameAndCollectionsAreValidated() {
        assertThrows(IllegalArgumentException.class,()->new Warrior(" "));
        assertThrows(IllegalArgumentException.class,()->new Warrior("x".repeat(21)));
        assertEquals("Max",new Warrior(" Max ").getName());
        var c=new Warrior("Max");
        assertThrows(UnsupportedOperationException.class,()->c.getSkills().clear());
        assertNotNull(c.getInventory());
        assertNotSame(c.getInventory(),new Warrior("Other").getInventory());
    }

    @Test void excessiveDamageIsRejectedWithoutConsumingMpOrChangingTarget() {
        var c=new Warrior("Max");c.modifyStat(StatType.ATK,Integer.MAX_VALUE-c.getAttack());
        var target=target();
        assertThrows(IllegalArgumentException.class,()->c.useSkill(0,target));
        assertEquals(c.getMaxMp(),c.getMp());assertEquals(10_000,target.getHp());
        assertThrows(IllegalArgumentException.class,()->c.basicAttack(target));
        assertEquals(10_000,target.getHp());
    }

    @Test void serializationRetainsSubclassSkillsInventoryAndStats() throws Exception {
        for(Character c : new Character[]{new Warrior("Max"),new Mage("Max"),new Archer("Max")}) {
            c.gainXP(120); c.getInventory().addGold(17); c.useSkill(0,target());
            var bytes=new ByteArrayOutputStream();
            try(var out=new ObjectOutputStream(bytes)) { out.writeObject(c); }
            Character copy;
            try(var in=new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) { copy=(Character)in.readObject(); }
            assertEquals(c.getClass(),copy.getClass()); assertEquals(2,copy.getLevel()); assertEquals(20,copy.getXp());
            assertEquals(17,copy.getInventory().getGold()); assertEquals(c.getMp(),copy.getMp());
            assertEquals(c.getSkills().getFirst().getName(),copy.getSkills().getFirst().getName());
        }
    }
}
