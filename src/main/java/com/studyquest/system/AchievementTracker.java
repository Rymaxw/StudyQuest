package com.studyquest.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AchievementTracker implements GameEventListener, Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Achievement> achievements = new ArrayList<>();
    private final StudyStats stats;

    public AchievementTracker(StudyStats stats) {
        this.stats = stats;
        registerDefaults();
    }

    private void registerDefaults() {
        register(new QuestCountAchievement("a1", "Langkah Pertama", "Menyelesaikan 1 quest", 1));
        register(new QuestCountAchievement("a2", "Mahasiswa Rajin", "Menyelesaikan 10 quest", 10));
        register(new StreakAchievement("a3", "Konsisten", "Mencapai streak 3 hari", 3));
        register(new StreakAchievement("a4", "Besi Baja", "Mencapai streak 7 hari", 7));
        register(new BossSlayerAchievement("a5", "Penakluk Tugas Besar", "Mengalahkan 1 boss", 1));
    }

    @Override
    public void onEvent(GameEvent e) {
        switch (e.getType()) {
            case QUEST_COMPLETED -> stats.recordCompleted(e.getDate(), e.getValue());
            case QUEST_FAILED -> stats.recordFailed();
            case BOSS_DEFEATED -> stats.recordBossDefeated();
            default -> {}
        }
        evaluateAll();
    }

    public void register(Achievement a) {
        achievements.add(a);
    }

    public List<Achievement> getUnlocked() {
        return achievements.stream().filter(Achievement::isUnlocked).toList();
    }

    public List<Achievement> getLocked() {
        return achievements.stream().filter(a -> !a.isUnlocked()).toList();
    }

    public List<Achievement> evaluateAll() {
        List<Achievement> newlyUnlocked = new ArrayList<>();
        for (Achievement a : achievements) {
            if (!a.isUnlocked() && a.checkCondition(stats)) {
                a.unlock();
                newlyUnlocked.add(a);
            }
        }
        return newlyUnlocked;
    }

    public StudyStats getStats() { return stats; }
    public List<Achievement> getAchievements() { return achievements; }
}
