package com.studyquest.quest;

import com.studyquest.character.Character;
import com.studyquest.item.InventoryFullException;
import java.io.Serializable;
import java.time.LocalDate;

public abstract class Quest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String title;
    private final String description;
    private final LocalDate deadline;
    private final Difficulty difficulty;
    private QuestStatus status;
    private LocalDate completedAt;

    public Quest(int id, String title, String description, LocalDate deadline, Difficulty difficulty) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.difficulty = difficulty;
        this.status = QuestStatus.ACTIVE;
    }

    public Reward complete(Character c) {
        if (status != QuestStatus.ACTIVE) return null;
        this.status = QuestStatus.COMPLETED;
        this.completedAt = LocalDate.now();

        Reward reward = calculateReward();
        if (isOverdue()) {
            reward = applyLatePenalty(reward);
        }

        if (c != null) {
            c.gainXP(reward.getXp());
            c.getInventory().addGold(reward.getGold());
            if (reward.getBonusItem() != null) {
                try {
                    c.getInventory().addItem(reward.getBonusItem());
                } catch (InventoryFullException ignored) {}
            }
        }
        return reward;
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(deadline);
    }

    protected Reward applyLatePenalty(Reward r) {
        return r.multiply(0.5);
    }

    public abstract Reward calculateReward();
    public abstract String getType();

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }
    public Difficulty getDifficulty() { return difficulty; }
    public QuestStatus getStatus() { return status; }
    public void setStatus(QuestStatus status) { this.status = status; }
    public LocalDate getCompletedAt() { return completedAt; }
}
