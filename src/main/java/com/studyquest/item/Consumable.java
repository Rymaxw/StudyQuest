package com.studyquest.item;

import com.studyquest.character.Character;

public class Consumable extends Item implements Usable {
    private static final long serialVersionUID = 1L;

    private EffectType effectType;
    private int effectValue;

    public Consumable(String id, String name, int price, EffectType type, int value) {
        super(id, name, type != null ? type.getLabel() + " +" + value : "", price, Rarity.COMMON, 1);
        if (type == null) {
            throw new IllegalArgumentException("EffectType tidak boleh null.");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Effect value tidak boleh negatif.");
        }
        this.effectType = type;
        this.effectValue = value;
    }

    public Consumable(String id, String name, String description, int price, Rarity rarity, int minLevel,
                      EffectType type, int value) {
        super(id, name, description, price, rarity, minLevel);
        if (type == null) {
            throw new IllegalArgumentException("EffectType tidak boleh null.");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Effect value tidak boleh negatif.");
        }
        this.effectType = type;
        this.effectValue = value;
    }

    @Override
    public boolean canUse(Character target) {
        return target != null && target.getHp() > 0 && target.getLevel() >= getMinLevel();
    }

    @Override
    public boolean use(Character target) {
        if (!canUse(target)) {
            return false;
        }
        switch (this.effectType) {
            case HEAL_HP -> target.heal(this.effectValue);
            case RESTORE_MP -> target.restoreMp(this.effectValue);
            case XP_BOOST -> target.gainXP(this.effectValue);
        }
        return true;
    }

    public EffectType getEffectType() {
        return this.effectType;
    }

    public int getEffectValue() {
        return this.effectValue;
    }

    @Override
    public String getItemType() {
        return "CONSUMABLE";
    }
}
