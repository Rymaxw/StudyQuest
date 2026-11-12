package com.studyquest.character;

public final class Fireball implements Skill {
    private static final long serialVersionUID = 1L;
    public static final int MP_COST = 15;
    public static final double MULTIPLIER = 2.5;
    public String getName() { return "Fireball"; }
    public int getMpCost() { return MP_COST; }
    public double getMultiplier() { return MULTIPLIER; }
    public int damage(Character character) { return Skill.scaledDamage(character.getIntelligence(), MULTIPLIER); }
}
