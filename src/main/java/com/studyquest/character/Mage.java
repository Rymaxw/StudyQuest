package com.studyquest.character;

import com.studyquest.battle.Monster;

public final class Mage extends Character {
    private static final long serialVersionUID = 1L;
    public static final int INITIAL_HP = 80, INITIAL_ATK = 6, INITIAL_INT = 16, INITIAL_MP = 60;
    public static final int HP_GROWTH = 10, ATK_GROWTH = 1, INT_GROWTH = 5;
    private final double spellPower = 1.5;
    public Mage(String name) { super(name, INITIAL_HP, INITIAL_ATK, INITIAL_INT, INITIAL_MP, new Fireball()); }
    /** INT x 1,5; damage pecahan dibulatkan ke bawah. */
    public int basicAttack(Monster target) { return hit(target, getIntelligence() * spellPower); }
    public String getClassName() { return "Mage"; }
    protected void applyLevelUpGrowth() { grow(HP_GROWTH, ATK_GROWTH, INT_GROWTH); }
}
