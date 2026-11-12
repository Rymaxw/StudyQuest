package com.studyquest.character;

public final class PiercingShot implements Skill {
    private static final long serialVersionUID = 1L;
    public static final int MP_COST = 12;
    public static final double MULTIPLIER = 2.0;
    public String getName() { return "PiercingShot"; }
    public int getMpCost() { return MP_COST; }
    public double getMultiplier() { return MULTIPLIER; }
    public int damage(Character character) { return Skill.scaledDamage(character.getAttack(), MULTIPLIER); }
}
