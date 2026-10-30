package com.studyquest.character;

import com.studyquest.battle.Monster;

public class PowerStrike implements Skill {
    private static final long serialVersionUID = 1L;
    private final int mpCost = 10;
    private final double multiplier = 2.0;

    @Override
    public String getName() {
        return "Power Strike";
    }

    @Override
    public int getMpCost() {
        return mpCost;
    }

    @Override
    public int use(Character user, Monster target) {
        int damage = (int) (user.getAttack() * multiplier);
        if (target != null) {
            target.takeDamage(damage);
        }
        return damage;
    }
}
