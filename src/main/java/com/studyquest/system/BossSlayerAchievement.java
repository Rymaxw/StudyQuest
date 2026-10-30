package com.studyquest.system;

public class BossSlayerAchievement extends Achievement {
    private static final long serialVersionUID = 1L;
    private final int requiredKills;

    public BossSlayerAchievement(String id, String title, String description, int requiredKills) {
        super(id, title, description);
        this.requiredKills = requiredKills;
    }

    @Override
    public boolean checkCondition(StudyStats s) {
        return s.getBossesDefeated() >= requiredKills;
    }

    @Override
    public double getProgress(StudyStats s) {
        return Math.min(1.0, (double) s.getBossesDefeated() / requiredKills);
    }
}
