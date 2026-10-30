package com.studyquest.system;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class StudyStats implements Serializable {
    private static final long serialVersionUID = 1L;

    private int totalCompleted = 0;
    private int totalFailed = 0;
    private int currentStreak = 0;
    private int longestStreak = 0;
    private int bossesDefeated = 0;
    private int totalXpEarned = 0;
    private LocalDate lastActiveDate;
    private final Map<LocalDate, Integer> dailyHistory = new HashMap<>();

    public void recordCompleted(LocalDate d, int xp) {
        totalCompleted++;
        totalXpEarned += xp;
        dailyHistory.put(d, dailyHistory.getOrDefault(d, 0) + xp);

        if (lastActiveDate != null && lastActiveDate.plusDays(1).equals(d)) {
            currentStreak++;
        } else if (lastActiveDate == null || !lastActiveDate.equals(d)) {
            currentStreak = 1;
        }
        longestStreak = Math.max(longestStreak, currentStreak);
        lastActiveDate = d;
    }

    public void recordFailed() {
        totalFailed++;
        currentStreak = 0;
    }

    public void recordBossDefeated() {
        bossesDefeated++;
    }

    public double getCompletionRate() {
        int total = totalCompleted + totalFailed;
        return total == 0 ? 0.0 : (double) totalCompleted / total * 100.0;
    }

    public Map<LocalDate, Integer> getWeeklySummary() {
        return dailyHistory;
    }

    public int getTotalCompleted() { return totalCompleted; }
    public int getTotalFailed() { return totalFailed; }
    public int getCurrentStreak() { return currentStreak; }
    public int getLongestStreak() { return longestStreak; }
    public int getBossesDefeated() { return bossesDefeated; }
    public int getTotalXpEarned() { return totalXpEarned; }
}
