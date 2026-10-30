package com.studyquest.character;

import com.studyquest.battle.Monster;

public class Mage extends Character {
    private static final long serialVersionUID = 1L;
    private double spellPower = 1.5;

    public Mage(String name) {
        super(name, 80, 60, 6, 16);
        addSkill(new Fireball());
    }

    @Override
    public int basicAttack(Monster target) {
        int damage = (int) (getIntelligence() * spellPower);
        if (target != null) {
            target.takeDamage(damage);
        }
        return damage;
    }

    @Override
    public String getClassName() {
        return "Mage";
    }

    @Override
    protected void applyLevelUpGrowth() {
        this.maxHp += 10;
        this.attack += 1;
        this.intelligence += 5;
    }

    public double getSpellPower() {
        return spellPower;
    }

    public void setSpellPower(double spellPower) {
        this.spellPower = spellPower;
    }
}
