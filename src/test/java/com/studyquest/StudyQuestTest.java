package com.studyquest;

import com.studyquest.battle.*;
import com.studyquest.character.*;
import com.studyquest.character.Character;
import com.studyquest.item.*;
import com.studyquest.quest.*;
import com.studyquest.system.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class StudyQuestTest {

    private User user;
    private Warrior warrior;
    private GameState state;

    @BeforeEach
    void setUp() {
        user = new User("testuser", AuthService.hashPassword("secret123"));
        warrior = new Warrior("Hero");
        user.setCharacter(warrior);
        state = GameState.newGame(user);
    }

    @Test
    void testCharacterLevelUp() {
        assertEquals(1, warrior.getLevel());
        int needed = warrior.xpToNextLevel();
        boolean leveled = warrior.gainXP(needed);
        assertTrue(leveled);
        assertEquals(2, warrior.getLevel());
        assertEquals(140, warrior.getMaxHp()); // 120 + 20
        assertEquals(18, warrior.getAttack());  // 14 + 4
    }

    @Test
    void testInventoryAndShop() throws Exception {
        Shop shop = state.getShop();
        Item potion = shop.getCatalog().get(0);
        int initialGold = warrior.getInventory().getGold();

        shop.buy(potion, warrior);
        assertEquals(initialGold - potion.getPrice(), warrior.getInventory().getGold());
        assertEquals(1, warrior.getInventory().getItems().size());

        // Test consumable effect
        warrior.takeDamage(30);
        int damagedHp = warrior.getHp();
        boolean used = warrior.getInventory().useItem(potion, warrior);
        assertTrue(used);
        assertTrue(warrior.getHp() > damagedHp);
        assertEquals(0, warrior.getInventory().getItems().size());
    }

    @Test
    void testQuestCompletionAndReward() {
        QuestManager qm = state.getQuestManager();
        DailyQuest quest = new DailyQuest(1, "Review PR", "Review code PR PBO", LocalDate.now().plusDays(2), Difficulty.MEDIUM);
        qm.addQuest(quest);

        int xpBefore = warrior.getXp();
        int goldBefore = warrior.getInventory().getGold();

        Reward r = qm.completeQuest(1, warrior);
        assertNotNull(r);
        assertEquals(QuestStatus.COMPLETED, quest.getStatus());
        assertTrue(warrior.getXp() > xpBefore || warrior.getLevel() > 1);
        assertTrue(warrior.getInventory().getGold() > goldBefore);
    }

    @Test
    void testBattleEngine() {
        Monster slime = MonsterFactory.create(MonsterType.SLIME, warrior.getLevel());
        BattleEngine battle = new BattleEngine();
        battle.start(warrior, slime);

        assertFalse(battle.isFinished());
        battle.playerAction(BattleAction.SKILL, 0); // PowerStrike
        assertTrue(slime.getHp() < slime.getMaxHp());
    }

    @Test
    void testAchievementUnlockedOnQuestComplete() {
        QuestManager qm = state.getQuestManager();
        DailyQuest q = new DailyQuest(1, "Test Q", "Test", LocalDate.now().plusDays(1), Difficulty.EASY);
        qm.addQuest(q);
        qm.completeQuest(1, warrior);

        boolean hasUnlocked = state.getTracker().getUnlocked().stream()
                .anyMatch(a -> a.getId().equals("a1"));
        assertTrue(hasUnlocked, "Achievement 'Langkah Pertama' should be unlocked!");
    }
}
