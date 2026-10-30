package com.studyquest.character;

import com.studyquest.battle.Monster;

public class PiercingShot implements Skill {
    private static final long serialVersionUID = 1L;
    private final int mpCost = 12;
    private final double multiplier = 2.0;

    @Override
    public String getName() {
        return "Piercing Shot";
    }

    @Override
    public int getMpCost() {
        return mpCost;
    }

    @Override
    public int use(Character user, Monster target) {
        // Piercing Shot deals guaranteed critical (double)
        int damage = (int) (user.getAttack() * multiplier);
        if (target != null) {
            target.takeDamage(damage);
        }
        return damage;
    }
}
