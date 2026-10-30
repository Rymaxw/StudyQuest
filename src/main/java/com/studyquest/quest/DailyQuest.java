package com.studyquest.quest;

import java.time.LocalDate;

public class DailyQuest extends Quest {
    private static final long serialVersionUID = 1L;
    private int streakCount = 0;

    public DailyQuest(int id, String title, String description, LocalDate deadline, Difficulty difficulty) {
        super(id, title, description, deadline, difficulty);
    }

    @Override
    public Reward calculateReward() {
        double streakBonus = 1.0 + (0.1 * streakCount);
        return new Reward(getDifficulty().getBaseXp(), getDifficulty().getBaseGold(), null).multiply(streakBonus);
    }

    @Override
    public String getType() {
        return "Daily";
    }

    public void resetForNextDay() {
        this.streakCount++;
    }

    public int getStreakCount() { return streakCount; }
    public void setStreakCount(int streakCount) { this.streakCount = streakCount; }
}
