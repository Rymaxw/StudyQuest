package com.studyquest.character;

import com.studyquest.battle.Monster;

public class Warrior extends Character {
    private static final long serialVersionUID = 1L;
    private double rageBonus = 0.2;

    public Warrior(String name) {
        super(name, 120, 30, 14, 4);
        addSkill(new PowerStrike());
    }

    @Override
    public int basicAttack(Monster target) {
        int damage = (int) (getAttack() * (1.0 + rageBonus));
        if (target != null) {
            target.takeDamage(damage);
        }
        return damage;
    }

    @Override
    public String getClassName() {
        return "Warrior";
    }

    @Override
    protected void applyLevelUpGrowth() {
        this.maxHp += 20;
        this.attack += 4;
        this.intelligence += 1;
    }

    public double getRageBonus() {
        return rageBonus;
    }

    public void setRageBonus(double rageBonus) {
        this.rageBonus = rageBonus;
    }
}
