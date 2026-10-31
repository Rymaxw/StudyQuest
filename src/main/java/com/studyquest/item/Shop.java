package com.studyquest.item;

import com.studyquest.character.Character;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shop implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Item> catalog;
    private double sellRatio;

    public Shop() {
        this.catalog = new ArrayList<>();
        this.sellRatio = 0.5;
        loadDefaultCatalog();
    }

    public void loadDefaultCatalog() {
        this.catalog.clear();
        this.catalog.add(new Consumable("POT_HP", "Potion HP", 30, EffectType.HEAL_HP, 50));
        this.catalog.add(new Consumable("POT_MP", "Potion MP", 30, EffectType.RESTORE_MP, 30));
        this.catalog.add(new Consumable(
                "XP_BOOST", "XP Booster", EffectType.XP_BOOST.getLabel() + " +50",
                60, Rarity.RARE, 1, EffectType.XP_BOOST, 50
        ));
        this.catalog.add(new Equipment("WPN_SWORD", "Iron Sword", 100, EquipSlot.WEAPON, 0, 8, 0));
        this.catalog.add(new Equipment("WPN_STAFF", "Mage Staff", 100, EquipSlot.WEAPON, 0, 0, 8));
        this.catalog.add(new Equipment("ARM_LEATHER", "Leather Armor", 80, EquipSlot.ARMOR, 30, 0, 0));
    }

    public List<Item> getCatalog() {
        return Collections.unmodifiableList(this.catalog);
    }

    public List<Item> filterByType(String type) {
        if (type == null || type.equalsIgnoreCase("ALL")) {
            return Collections.unmodifiableList(this.catalog);
        }
        List<Item> filtered = new ArrayList<>();
        for (Item item : this.catalog) {
            if (item.getItemType().equalsIgnoreCase(type)) {
                filtered.add(item);
            }
        }
        return Collections.unmodifiableList(filtered);
    }

    public Item findById(String id) {
        if (id == null) {
            return null;
        }
        for (Item item : this.catalog) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public void buy(Item i, Character c) throws InsufficientGoldException, InventoryFullException {
        if (i == null || c == null || c.getInventory() == null) {
            throw new IllegalArgumentException("Item dan karakter tidak boleh null.");
        }
        if (c.getLevel() < i.getMinLevel()) {
            throw new IllegalArgumentException("Level karakter belum mencukupi.");
        }

        Inventory inv = c.getInventory();
        if (inv.isFull()) {
            throw new InventoryFullException(inv.getCapacity());
        }

        inv.spendGold(i.getPrice());
        inv.addItem(copyItem(i));
    }

    public int sell(Item i, Character c) {
        if (i == null || c == null || c.getInventory() == null || !c.getInventory().contains(i)) {
            return 0;
        }

        Inventory inv = c.getInventory();
        Item target = resolveSellTarget(i, inv);

        if (target instanceof Equipment eq && eq.isEquipped()) {
            inv.unequip(eq.getSlot(), c);
        }

        inv.removeItem(target);
        int earned = sellPrice(target);
        inv.addGold(earned);
        return earned;
    }

    private int sellPrice(Item i) {
        return (int) Math.floor(i.getPrice() * this.sellRatio);
    }

    private Item copyItem(Item i) {
        if (i instanceof Consumable cons) {
            return new Consumable(
                    cons.getId(),
                    cons.getName(),
                    cons.getDescription(),
                    cons.getPrice(),
                    cons.getRarity(),
                    cons.getMinLevel(),
                    cons.getEffectType(),
                    cons.getEffectValue()
            );
        }
        if (i instanceof Equipment eq) {
            return new Equipment(
                    eq.getId(),
                    eq.getName(),
                    eq.getDescription(),
                    eq.getPrice(),
                    eq.getRarity(),
                    eq.getMinLevel(),
                    eq.getSlot(),
                    eq.getBonusHp(),
                    eq.getBonusAtk(),
                    eq.getBonusInt()
            );
        }
        return i;
    }

    private Item resolveSellTarget(Item query, Inventory inv) {
        for (Item item : inv.getItems()) {
            if (item == query) {
                return item;
            }
        }
        for (Item item : inv.getItems()) {
            if (item.equals(query) && item instanceof Equipment eq && !eq.isEquipped()) {
                return item;
            }
        }
        for (Item item : inv.getItems()) {
            if (item.equals(query)) {
                return item;
            }
        }
        return query;
    }
}
