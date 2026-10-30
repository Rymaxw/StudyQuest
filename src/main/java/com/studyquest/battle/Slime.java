package com.studyquest.battle;

import com.studyquest.character.Character;

public class Slime extends Monster {
    private static final long serialVersionUID = 1L;
    private final int regenAmount = 5;

    public Slime(int level) {
        super("King Slime", level, 60, 8, MonsterType.SLIME, 50, 30);
    }

    @Override
    public int attack(Character target) {
        target.takeDamage(attackPower);
        return attackPower;
    }

    @Override
    public int specialAbility(Character target) {
        this.hp = Math.min(maxHp, this.hp + regenAmount);
        return regenAmount;
    }

    @Override
    public BattleAction chooseAction(int turn) {
        if (turn % 2 == 0 && hp < maxHp) {
            return BattleAction.SPECIAL;
        }
        return BattleAction.ATTACK;
    }
}
