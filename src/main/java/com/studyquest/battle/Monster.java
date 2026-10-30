package com.studyquest.battle;

import com.studyquest.character.Character;
import com.studyquest.quest.Reward;
import java.io.Serializable;

public abstract class Monster implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final int level;
    protected int hp;
    protected int maxHp;
    protected int attackPower;
    private final MonsterType type;
    private final int rewardXp;
    private final int rewardGold;

    public Monster(String name, int level, int maxHp, int attackPower, MonsterType type, int rewardXp, int rewardGold) {
        this.name = name;
        this.level = level;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackPower = attackPower;
        this.type = type;
        this.rewardXp = rewardXp;
        this.rewardGold = rewardGold;
    }

    public void takeDamage(int dmg) {
        if (dmg <= 0) return;
        this.hp = Math.max(0, this.hp - dmg);
    }

    public boolean isDefeated() {
        return hp <= 0;
    }

    public Reward getReward() {
        return new Reward(rewardXp, rewardGold, null);
    }

    public abstract int attack(Character target);
    public abstract int specialAbility(Character target);
    public abstract BattleAction chooseAction(int turn);

    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttackPower() { return attackPower; }
    public MonsterType getType() { return type; }
}
