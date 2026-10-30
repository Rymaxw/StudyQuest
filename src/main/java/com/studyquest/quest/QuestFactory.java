package com.studyquest.quest;

import com.studyquest.battle.MonsterType;
import java.time.LocalDate;

public class QuestFactory {
    public static Quest create(int id, String type, String title, String description, LocalDate deadline, Difficulty difficulty) {
        return switch (type.toLowerCase()) {
            case "daily" -> new DailyQuest(id, title, description, deadline, difficulty);
            case "weekly" -> new WeeklyQuest(id, title, description, deadline, difficulty);
            case "boss" -> new BossQuest(id, title, description, deadline, difficulty, MonsterType.SLIME);
            default -> new DailyQuest(id, title, description, deadline, difficulty);
        };
    }

    public static BossQuest createBoss(int id, String title, String description, LocalDate deadline, Difficulty difficulty, MonsterType bossType) {
        return new BossQuest(id, title, description, deadline, difficulty, bossType);
    }
}
