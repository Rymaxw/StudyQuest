package com.studyquest.character;

import java.util.Objects;
import java.util.Random;
import com.studyquest.battle.Monster;

public final class Archer extends Character {
    private static final long serialVersionUID = 1L;
    public static final int INITIAL_HP = 100, INITIAL_ATK = 12, INITIAL_INT = 8, INITIAL_MP = 40;
    public static final int HP_GROWTH = 14, ATK_GROWTH = 3, INT_GROWTH = 2;
    public static final int CRITICAL_MULTIPLIER = 2;
    private final double critChance = 0.25;
    private final Random random;
    public Archer(String name) { this(name, new Random()); }
    Archer(String name, Random random) {
        super(name, INITIAL_HP, INITIAL_ATK, INITIAL_INT, INITIAL_MP, new PiercingShot());
        this.random = Objects.requireNonNull(random);
    }
    /** Peluang critical 25%, damage critical dua kali ATK. */
    public int basicAttack(Monster target) {
        Objects.requireNonNull(target, "Target wajib diisi.");
        boolean critical = Double.compare(random.nextDouble(), critChance) < 0;
        return hit(target, (double) getAttack() * (critical ? CRITICAL_MULTIPLIER : 1));
    }
    public String getClassName() { return "Archer"; }
    protected void applyLevelUpGrowth() { grow(HP_GROWTH, ATK_GROWTH, INT_GROWTH); }
}
