package com.studyquest.character;

import com.studyquest.battle.Monster;
import com.studyquest.item.Inventory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Character implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int level;
    private int xp;
    protected int maxHp;
    protected int hp;
    protected int maxMp;
    protected int mp;
    protected int attack;
    protected int intelligence;
    private final Inventory inventory;
    private final List<Skill> skills;

    public Character(String name, int maxHp, int maxMp, int attack, int intelligence) {
        this.name = name;
        this.level = 1;
        this.xp = 0;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxMp = maxMp;
        this.mp = maxMp;
        this.attack = attack;
        this.intelligence = intelligence;
        this.inventory = new Inventory();
        this.skills = new ArrayList<>();
    }

    public boolean gainXP(int amount) {
        if (amount <= 0) return false;
        this.xp += amount;
        boolean leveledUp = false;
        while (this.xp >= xpToNextLevel()) {
            this.xp -= xpToNextLevel();
            levelUp();
            leveledUp = true;
        }
        return leveledUp;
    }

    private void levelUp() {
        this.level++;
        applyLevelUpGrowth();
        this.hp = this.maxHp;
        this.mp = this.maxMp;
    }

    public int xpToNextLevel() {
        return 100 + 50 * (level - 1);
    }

    public void takeDamage(int dmg) {
        if (dmg <= 0) return;
        this.hp = Math.max(0, this.hp - dmg);
    }

    public void heal(int amount) {
        if (amount <= 0) return;
        this.hp = Math.min(this.maxHp, this.hp + amount);
    }

    public void restoreMp(int amount) {
        if (amount <= 0) return;
        this.mp = Math.min(this.maxMp, this.mp + amount);
    }

    public void modifyStat(StatType t, int delta) {
        switch (t) {
            case HP -> {
                this.maxHp += delta;
                if (delta > 0) this.hp += delta;
                else this.hp = Math.min(this.hp, this.maxHp);
            }
            case ATK -> this.attack += delta;
            case INT -> this.intelligence += delta;
        }
    }

    public int useSkill(int idx, Monster target) {
        if (idx < 0 || idx >= skills.size()) return 0;
        Skill skill = skills.get(idx);
        if (this.mp < skill.getMpCost()) return 0;
        this.mp -= skill.getMpCost();
        return skill.use(this, target);
    }

    public void addSkill(Skill skill) {
        this.skills.add(skill);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public abstract int basicAttack(Monster target);
    public abstract String getClassName();
    protected abstract void applyLevelUpGrowth();

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getLevel() { return level; }
    public int getXp() { return xp; }
    public int getMaxHp() { return maxHp; }
    public int getHp() { return hp; }
    public int getMaxMp() { return maxMp; }
    public int getMp() { return mp; }
    public int getAttack() { return attack; }
    public int getIntelligence() { return intelligence; }
    public List<Skill> getSkills() { return skills; }
}
