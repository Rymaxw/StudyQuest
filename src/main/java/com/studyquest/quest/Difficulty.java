package com.studyquest.quest;

public enum Difficulty {
    EASY(20, 10),
    MEDIUM(40, 20),
    HARD(70, 35),
    EXTREME(100, 50);

    private final int baseXp;
    private final int baseGold;

    Difficulty(int baseXp, int baseGold) {
        this.baseXp = baseXp;
        this.baseGold = baseGold;
    }

    public int getBaseXp() { return baseXp; }
    public int getBaseGold() { return baseGold; }
}
