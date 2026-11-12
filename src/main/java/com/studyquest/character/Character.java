package com.studyquest.character;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import com.studyquest.battle.Monster;
import com.studyquest.item.Inventory;

public abstract class Character implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int BASE_XP = 100;
    public static final int XP_GROWTH = 50;
    private final String name;
    private int level = 1;
    private int xp;
    private int maxHp;
    private int hp;
    private final int maxMp;
    private int mp;
    private int attack;
    private int intelligence;
    private final Inventory inventory = new Inventory();
    private final List<Skill> skills;

    protected Character(String name, int hp, int attack, int intelligence, int mp, Skill skill) {
        if (name == null || name.strip().length() < 2 || name.strip().length() > 20) {
            throw new IllegalArgumentException("Nama karakter harus 2 sampai 20 karakter.");
        }
        if (hp <= 0 || attack < 0 || intelligence < 0 || mp < 0) {
            throw new IllegalArgumentException("Stat awal tidak valid.");
        }
        this.name = name.strip();
        this.maxHp = hp;
        this.hp = hp;
        this.maxMp = mp;
        this.mp = mp;
        this.attack = attack;
        this.intelligence = intelligence;
        this.skills = List.of(Objects.requireNonNull(skill));
    }

    /** Menghabiskan ambang XP berulang; setiap level memulihkan HP dan MP penuh. */
    public final boolean gainXP(int amount) {
        requireNonNegative(amount);
        long remainingXp = (long) xp + amount;
        boolean leveledUp = false;
        while (remainingXp >= xpToNextLevel()) {
            remainingXp -= xpToNextLevel();
            levelUp();
            leveledUp = true;
        }
        xp = (int) remainingXp;
        return leveledUp;
    }

    /** Penalti hanya mengurangi XP level berjalan; tidak menurunkan level. */
    public final void loseXP(int amount) {
        requireNonNegative(amount);
        xp = Math.max(0, xp - amount);
    }

    public final int xpToNextLevel() {
        return Math.addExact(BASE_XP, Math.multiplyExact(XP_GROWTH, level - 1));
    }

    protected final void levelUp() {
        level++;
        applyLevelUpGrowth();
        hp = maxHp;
        mp = maxMp;
    }

    protected abstract void applyLevelUpGrowth();

    protected final void grow(int hpGrowth, int attackGrowth, int intelligenceGrowth) {
        maxHp = Math.addExact(maxHp, hpGrowth);
        attack = Math.addExact(attack, attackGrowth);
        intelligence = Math.addExact(intelligence, intelligenceGrowth);
    }

    /** HP mengubah kapasitas maksimum, lalu menjepit HP saat ini; tidak memulihkan HP. */
    public final void modifyStat(StatType type, int delta) {
        Objects.requireNonNull(type, "Jenis stat wajib diisi.");
        int currentValue = switch (type) {
            case HP -> maxHp;
            case ATK -> attack;
            case INT -> intelligence;
        };
        int updatedValue = Math.addExact(currentValue, delta);
        int minimumValue = type == StatType.HP ? 1 : 0;
        if (updatedValue < minimumValue) {
            throw new IllegalArgumentException("Stat tidak boleh di bawah batas minimum.");
        }
        switch (type) {
            case HP -> {
                maxHp = updatedValue;
                hp = Math.min(hp, maxHp);
            }
            case ATK -> attack = updatedValue;
            case INT -> intelligence = updatedValue;
        }
    }

    /** Damage nonnegatif; HP berhenti pada nol. */
    public final void takeDamage(int damage) {
        requireNonNegative(damage);
        hp = Math.max(0, hp - damage);
    }

    /** Pemulihan berhenti pada maxHp tanpa overflow. */
    public final void heal(int amount) {
        requireNonNegative(amount);
        hp = (int) Math.min(maxHp, (long) hp + amount);
    }

    /** Pemulihan berhenti pada maxMp tanpa overflow. */
    public final void restoreMp(int amount) {
        requireNonNegative(amount);
        mp = (int) Math.min(maxMp, (long) mp + amount);
    }

    /** Pemulihan minimum setelah kalah atau menyerah, tanpa reward. */
    public final void setHpToOne() {
        hp = 1;
    }

    /** Mengurangi HP target dan mengembalikan damage mentah; pecahan dibulatkan ke bawah. */
    public abstract int basicAttack(Monster target);
    public abstract String getClassName();

    protected final int hit(Monster target, double damage) {
        Objects.requireNonNull(target, "Target wajib diisi.");
        if (damage > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Damage terlalu besar.");
        }
        int roundedDamage = (int) damage;
        target.takeDamage(roundedDamage);
        return roundedDamage;
    }

    /** Indeks berbasis nol; aksi tidak valid tidak menghabiskan MP atau melukai target. */
    public final int useSkill(int index, Monster target) {
        Objects.requireNonNull(target, "Target wajib diisi.");
        if (index < 0 || index >= skills.size()) {
            throw new IllegalArgumentException("Skill tidak tersedia.");
        }
        Skill skill = skills.get(index);
        if (mp < skill.getMpCost()) {
            throw new IllegalStateException("MP kurang untuk memakai skill.");
        }
        int damage = skill.execute(this, target);
        mp -= skill.getMpCost();
        return damage;
    }

    private static void requireNonNegative(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Jumlah tidak boleh negatif.");
        }
    }

    public final String getName() { return name; }
    /** Level karakter selalu minimal satu. */
    public final int getLevel() { return level; }
    public final int getXp() { return xp; }
    public final int getHp() { return hp; }
    public final int getMaxHp() { return maxHp; }
    public final int getMp() { return mp; }
    public final int getMaxMp() { return maxMp; }
    public final int getAttack() { return attack; }
    public final int getIntelligence() { return intelligence; }
    public final List<Skill> getSkills() { return skills; }
    /** Inventory dimiliki satu karakter dan ikut diserialisasi bersama karakter. */
    public final Inventory getInventory() { return inventory; }
}
