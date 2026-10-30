package com.studyquest.battle;

import com.studyquest.character.Character;

public class Dragon extends Monster {
    private static final long serialVersionUID = 1L;
    private final int breathCooldown = 3;
    private boolean enraged = false;

    public Dragon(int level) {
        super("Ancient Red Dragon", level, 180, 20, MonsterType.DRAGON, 200, 150);
    }

    @Override
    public int attack(Character target) {
        int dmg = enraged ? (int)(attackPower * 1.5) : attackPower;
        target.takeDamage(dmg);
        return dmg;
    }

    @Override
    public int specialAbility(Character target) {
        int dmg = 40;
        target.takeDamage(dmg);
        return dmg;
    }

    @Override
    public BattleAction chooseAction(int turn) {
        if (hp <= maxHp / 2) enraged = true;
        if (turn % breathCooldown == 0) return BattleAction.SPECIAL;
        return BattleAction.ATTACK;
    }
}
