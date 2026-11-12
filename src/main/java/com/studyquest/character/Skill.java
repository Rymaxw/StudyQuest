package com.studyquest.character;

import java.io.Serializable;
import com.studyquest.battle.Monster;

public interface Skill extends Serializable {
    String getName();
    int getMpCost();
    double getMultiplier();
    int damage(Character character);

    static int scaledDamage(int stat, double multiplier) {
        double damage = stat * multiplier;
        if (damage > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Damage terlalu besar.");
        }
        return (int) damage;
    }

    /** Mengurangi HP target; konsumsi MP divalidasi oleh Character.useSkill. */
    default int execute(Character character, Monster target) {
        int damage = damage(character);
        target.takeDamage(damage);
        return damage;
    }
}
