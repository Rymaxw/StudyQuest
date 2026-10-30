package com.studyquest.character;

import com.studyquest.battle.Monster;

public class Fireball implements Skill {
    private static final long serialVersionUID = 1L;
    private final int mpCost = 15;
    private final double multiplier = 2.5;

    @Override
    public String getName() {
        return "Fireball";
    }

    @Override
    public int getMpCost() {
        return mpCost;
    }

    @Override
    public int use(Character user, Monster target) {
        int damage = (int) (user.getIntelligence() * multiplier);
        if (target != null) {
            target.takeDamage(damage);
        }
        return damage;
    }
}
