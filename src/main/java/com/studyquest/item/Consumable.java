package com.studyquest.item;

import com.studyquest.character.Character;

public class Consumable extends Item implements Usable {
    private static final long serialVersionUID = 1L;

    private final EffectType effectType;
    private final int effectValue;

    public Consumable(String id, String name, String description, int price, Rarity rarity, int minLevel,
                      EffectType effectType, int effectValue) {
        super(id, name, description, price, rarity, minLevel);
        this.effectType = effectType;
        this.effectValue = effectValue;
    }

    @Override
    public boolean canUse(Character target) {
        return target != null;
    }

    @Override
    public boolean use(Character target) {
        if (!canUse(target)) return false;
        switch (effectType) {
            case HEAL_HP -> target.heal(effectValue);
            case RESTORE_MP -> target.restoreMp(effectValue);
            case XP_BOOST -> target.gainXP(effectValue);
        }
        return true;
    }

    @Override
    public String getItemType() {
        return "Consumable";
    }

    public EffectType getEffectType() { return effectType; }
    public int getEffectValue() { return effectValue; }
}
