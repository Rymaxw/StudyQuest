package com.studyquest.battle;

import com.studyquest.character.Character;

public class Goblin extends Monster {
    private static final long serialVersionUID = 1L;
    private final double critChance = 0.2;

    public Goblin(int level) {
        super("Hobgoblin Berserker", level, 80, 12, MonsterType.GOBLIN, 80, 50);
    }

    @Override
    public int attack(Character target) {
        target.takeDamage(attackPower);
        return attackPower;
    }

    @Override
    public int specialAbility(Character target) {
        int damage = (int) (attackPower * 1.5);
        target.takeDamage(damage);
        return damage;
    }

    @Override
    public BattleAction chooseAction(int turn) {
        if (turn % 3 == 0) return BattleAction.SPECIAL;
        return BattleAction.ATTACK;
    }
}
