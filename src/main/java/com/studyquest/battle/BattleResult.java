package com.studyquest.battle;

import com.studyquest.quest.Reward;
import java.io.Serializable;

public class BattleResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private final boolean won;
    private final int turns;
    private final Reward reward;

    public BattleResult(boolean won, int turns, Reward reward) {
        this.won = won;
        this.turns = turns;
        this.reward = reward;
    }

    public boolean isWon() { return won; }
    public int getTurns() { return turns; }
    public Reward getReward() { return reward; }
}
