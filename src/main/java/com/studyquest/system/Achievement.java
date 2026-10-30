package com.studyquest.system;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Achievement implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String title;
    private final String description;
    private boolean unlocked = false;
    private LocalDate unlockedDate;

    public Achievement(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public void unlock() {
        if (!unlocked) {
            this.unlocked = true;
            this.unlockedDate = LocalDate.now();
        }
    }

    public abstract boolean checkCondition(StudyStats s);
    public abstract double getProgress(StudyStats s);

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isUnlocked() { return unlocked; }
    public LocalDate getUnlockedDate() { return unlockedDate; }
}
