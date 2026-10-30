package com.studyquest.system;

public class StreakAchievement extends Achievement {
    private static final long serialVersionUID = 1L;
    private final int requiredDays;

    public StreakAchievement(String id, String title, String description, int requiredDays) {
        super(id, title, description);
        this.requiredDays = requiredDays;
    }

    @Override
    public boolean checkCondition(StudyStats s) {
        return s.getCurrentStreak() >= requiredDays;
    }

    @Override
    public double getProgress(StudyStats s) {
        return Math.min(1.0, (double) s.getCurrentStreak() / requiredDays);
    }
}
