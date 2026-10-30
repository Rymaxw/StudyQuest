package com.studyquest.system;

public class QuestCountAchievement extends Achievement {
    private static final long serialVersionUID = 1L;
    private final int requiredCount;

    public QuestCountAchievement(String id, String title, String description, int requiredCount) {
        super(id, title, description);
        this.requiredCount = requiredCount;
    }

    @Override
    public boolean checkCondition(StudyStats s) {
        return s.getTotalCompleted() >= requiredCount;
    }

    @Override
    public double getProgress(StudyStats s) {
        return Math.min(1.0, (double) s.getTotalCompleted() / requiredCount);
    }
}
