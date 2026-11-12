package com.studyquest.character;

import com.studyquest.battle.Monster;

public final class Warrior extends Character {
    private static final long serialVersionUID = 1L;
    public static final int INITIAL_HP = 120, INITIAL_ATK = 14, INITIAL_INT = 4, INITIAL_MP = 30;
    public static final int HP_GROWTH = 20, ATK_GROWTH = 4, INT_GROWTH = 1;
    private final double rageBonus = 1.2;
    public Warrior(String name) { super(name, INITIAL_HP, INITIAL_ATK, INITIAL_INT, INITIAL_MP, new PowerStrike()); }
    /** ATK x 1,2; damage pecahan dibulatkan ke bawah. */
    public int basicAttack(Monster target) { return hit(target, getAttack() * rageBonus); }
    public String getClassName() { return "Warrior"; }
    protected void applyLevelUpGrowth() { grow(HP_GROWTH, ATK_GROWTH, INT_GROWTH); }
}
