package com.studyquest.item;

import com.studyquest.character.Character;
import java.io.Serializable;

public abstract class Item implements Purchasable, Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String name;
    private final String description;
    private final int price;
    private final Rarity rarity;
    private final int minLevel;

    public Item(String id, String name, String description, int price, Rarity rarity, int minLevel) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rarity = rarity;
        this.minLevel = minLevel;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public boolean canBePurchasedBy(Character c) {
        if (c == null) return false;
        return c.getLevel() >= minLevel && c.getInventory().getGold() >= price;
    }

    public abstract String getItemType();

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Rarity getRarity() { return rarity; }
    public int getMinLevel() { return minLevel; }

    @Override
    public String toString() {
        return "[" + rarity + "] " + name + " (" + price + " gold)";
    }
}
