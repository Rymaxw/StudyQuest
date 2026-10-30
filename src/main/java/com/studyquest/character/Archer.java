package com.studyquest.character;

import com.studyquest.battle.Monster;
import java.util.Random;

public class Archer extends Character {
    private static final long serialVersionUID = 1L;
    private double critChance = 0.25;
    private transient Random random = new Random();

    public Archer(String name) {
        super(name, 100, 40, 12, 8);
        addSkill(new PiercingShot());
    }

    private Random getRandom() {
        if (random == null) random = new Random();
        return random;
    }

    @Override
    public int basicAttack(Monster target) {
        boolean isCrit = getRandom().nextDouble() < critChance;
        int damage = isCrit ? getAttack() * 2 : getAttack();
        if (target != null) {
            target.takeDamage(damage);
        }
        return damage;
    }

    @Override
    public String getClassName() {
        return "Archer";
    }

    @Override
    protected void applyLevelUpGrowth() {
        this.maxHp += 14;
        this.attack += 3;
        this.intelligence += 2;
    }

    public double getCritChance() {
        return critChance;
    }

    public void setCritChance(double critChance) {
        this.critChance = critChance;
    }
}
