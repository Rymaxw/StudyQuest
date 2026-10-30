package com.studyquest.item;

import com.studyquest.character.Character;
import com.studyquest.character.StatType;

public class Equipment extends Item implements Usable {
    private static final long serialVersionUID = 1L;

    private final EquipSlot slot;
    private final int bonusHp;
    private final int bonusAtk;
    private final int bonusInt;

    public Equipment(String id, String name, String description, int price, Rarity rarity, int minLevel,
                     EquipSlot slot, int bonusHp, int bonusAtk, int bonusInt) {
        super(id, name, description, price, rarity, minLevel);
        this.slot = slot;
        this.bonusHp = bonusHp;
        this.bonusAtk = bonusAtk;
        this.bonusInt = bonusInt;
    }

    @Override
    public boolean canUse(Character target) {
        return target != null && target.getLevel() >= getMinLevel();
    }

    @Override
    public boolean use(Character target) {
        if (!canUse(target)) return false;
        target.getInventory().equip(this, target);
        return true;
    }

    public void equip(Character c) {
        if (bonusHp > 0) c.modifyStat(StatType.HP, bonusHp);
        if (bonusAtk > 0) c.modifyStat(StatType.ATK, bonusAtk);
        if (bonusInt > 0) c.modifyStat(StatType.INT, bonusInt);
    }

    public void unequip(Character c) {
        if (bonusHp > 0) c.modifyStat(StatType.HP, -bonusHp);
        if (bonusAtk > 0) c.modifyStat(StatType.ATK, -bonusAtk);
        if (bonusInt > 0) c.modifyStat(StatType.INT, -bonusInt);
    }

    @Override
    public String getItemType() {
        return "Equipment (" + slot + ")";
    }

    public EquipSlot getSlot() { return slot; }
    public int getBonusHp() { return bonusHp; }
    public int getBonusAtk() { return bonusAtk; }
    public int getBonusInt() { return bonusInt; }
}
