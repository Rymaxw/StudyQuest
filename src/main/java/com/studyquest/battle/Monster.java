package com.studyquest.battle;

import java.util.Objects;

/** Target damage fondasi Modul 1; AI, reward, dan BattleEngine tetap milik Modul 4. */
public abstract class Monster {
    private final String name;
    private final int maxHp;
    private int hp;
    private final int attack;

    protected Monster(String name, int maxHp, int attack) {
        if (maxHp <= 0 || attack < 0) {
            throw new IllegalArgumentException("Stat monster tidak valid.");
        }
        this.name = Objects.requireNonNull(name);
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage tidak boleh negatif.");
        }
        hp = Math.max(0, hp - damage);
    }
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getAttack() { return attack; }
}
