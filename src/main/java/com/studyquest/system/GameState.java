package com.studyquest.system;

import com.studyquest.character.User;
import com.studyquest.item.Shop;
import com.studyquest.quest.QuestManager;
import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private User user;
    private QuestManager questManager;
    private Shop shop;
    private AchievementTracker tracker;
    private StudyStats stats;

    public GameState(User user, QuestManager questManager, Shop shop, AchievementTracker tracker, StudyStats stats) {
        this.user = user;
        this.questManager = questManager;
        this.shop = shop;
        this.tracker = tracker;
        this.stats = stats;
    }

    public static GameState newGame(User user) {
        StudyStats stats = new StudyStats();
        AchievementTracker tracker = new AchievementTracker(stats);
        QuestManager qm = new QuestManager();
        qm.addListener(tracker);
        Shop shop = new Shop();
        return new GameState(user, qm, shop, tracker, stats);
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public QuestManager getQuestManager() { return questManager; }
    public Shop getShop() { return shop; }
    public AchievementTracker getTracker() { return tracker; }
    public StudyStats getStats() { return stats; }
}
