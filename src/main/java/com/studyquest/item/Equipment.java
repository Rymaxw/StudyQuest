package com.studyquest.item;

import com.studyquest.character.Character;
import com.studyquest.character.StatType;
import java.util.ArrayList;
import java.util.List;

public class Equipment extends Item implements Usable {
    private static final long serialVersionUID = 1L;

    private EquipSlot slot;
    private int bonusHp;
    private int bonusAtk;
    private int bonusInt;
    private boolean equipped;

    public Equipment(String id, String name, int price, EquipSlot slot, int hp, int atk, int intl) {
        super(id, name, buildDefaultDescription(slot, hp, atk, intl), price, Rarity.COMMON, 1);
        if (slot == null) {
            throw new IllegalArgumentException("EquipSlot tidak boleh null.");
        }
        this.slot = slot;
        this.bonusHp = hp;
        this.bonusAtk = atk;
        this.bonusInt = intl;
        this.equipped = false;
    }

    public Equipment(String id, String name, String description, int price, Rarity rarity, int minLevel,
                     EquipSlot slot, int hp, int atk, int intl) {
        super(id, name, description, price, rarity, minLevel);
        if (slot == null) {
            throw new IllegalArgumentException("EquipSlot tidak boleh null.");
        }
        this.slot = slot;
        this.bonusHp = hp;
        this.bonusAtk = atk;
        this.bonusInt = intl;
        this.equipped = false;
    }

    private static String buildDefaultDescription(EquipSlot slot, int hp, int atk, int intl) {
        List<String> parts = new ArrayList<>();
        if (hp != 0) {
            parts.add((hp > 0 ? "+" : "") + hp + " HP");
        }
        if (atk != 0) {
            parts.add((atk > 0 ? "+" : "") + atk + " ATK");
        }
        if (intl != 0) {
            parts.add((intl > 0 ? "+" : "") + intl + " INT");
        }
        String prefix = slot != null ? slot.getLabel() : "Equipment";
        return parts.isEmpty() ? prefix : prefix + " (" + String.join(", ", parts) + ")";
    }

    @Override
    public boolean canUse(Character target) {
        return target != null && target.getLevel() >= getMinLevel();
    }

    @Override
    public boolean use(Character target) {
        if (!canUse(target)) {
            return false;
        }
        if (target.getInventory() != null) {
            target.getInventory().equip(this, target);
        } else {
            equip(target);
        }
        return true;
    }

    public void equip(Character c) {
        if (c != null && !this.equipped) {
            if (this.bonusHp != 0) {
                c.modifyStat(StatType.HP, this.bonusHp);
            }
            if (this.bonusAtk != 0) {
                c.modifyStat(StatType.ATK, this.bonusAtk);
            }
            if (this.bonusInt != 0) {
                c.modifyStat(StatType.INT, this.bonusInt);
            }
            this.equipped = true;
        }
    }

    public void unequip(Character c) {
        if (c != null && this.equipped) {
            if (this.bonusHp != 0) {
                c.modifyStat(StatType.HP, -this.bonusHp);
            }
            if (this.bonusAtk != 0) {
                c.modifyStat(StatType.ATK, -this.bonusAtk);
            }
            if (this.bonusInt != 0) {
                c.modifyStat(StatType.INT, -this.bonusInt);
            }
            this.equipped = false;
        }
    }

    public boolean isEquipped() {
        return this.equipped;
    }

    public EquipSlot getSlot() {
        return this.slot;
    }

    public int getBonusHp() {
        return this.bonusHp;
    }

    public int getBonusAtk() {
        return this.bonusAtk;
    }

    public int getBonusInt() {
        return this.bonusInt;
    }

    @Override
    public String getItemType() {
        return "EQUIPMENT";
    }
}
