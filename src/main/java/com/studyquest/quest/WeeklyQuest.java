package com.studyquest.quest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeeklyQuest extends Quest {
    private static final long serialVersionUID = 1L;
    private final List<String> subTasks = new ArrayList<>();
    private final List<Boolean> doneFlags = new ArrayList<>();

    public WeeklyQuest(int id, String title, String description, LocalDate deadline, Difficulty difficulty) {
        super(id, title, description, deadline, difficulty);
    }

    public void addSubTask(String task) {
        subTasks.add(task);
        doneFlags.add(false);
    }

    public void toggleSubTask(int i) {
        if (i >= 0 && i < doneFlags.size()) {
            doneFlags.set(i, !doneFlags.get(i));
        }
    }

    public double getProgress() {
        if (subTasks.isEmpty()) return 1.0;
        long done = doneFlags.stream().filter(b -> b).count();
        return (double) done / doneFlags.size();
    }

    @Override
    public Reward calculateReward() {
        double multiplier = (getProgress() >= 1.0) ? 2.5 : getProgress();
        return new Reward(getDifficulty().getBaseXp(), getDifficulty().getBaseGold(), null).multiply(multiplier);
    }

    @Override
    public String getType() {
        return "Weekly";
    }

    public List<String> getSubTasks() { return subTasks; }
    public List<Boolean> getDoneFlags() { return doneFlags; }
}
