package com.studyquest.item;

import com.studyquest.character.Character;
import java.io.Serializable;
import java.util.Objects;

public abstract class Item implements Purchasable, Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private int price;
    private Rarity rarity;
    private int minLevel;

    public Item(String id, String name, String description, int price, Rarity rarity, int minLevel) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID item tidak boleh kosong.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nama item tidak boleh kosong.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Harga item tidak boleh negatif.");
        }
        if (rarity == null) {
            throw new IllegalArgumentException("Rarity item tidak boleh null.");
        }
        if (minLevel < 1) {
            throw new IllegalArgumentException("Minimal level item minimal 1.");
        }

        this.id = id;
        this.name = name;
        this.description = description != null ? description : "";
        this.price = price;
        this.rarity = rarity;
        this.minLevel = minLevel;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    public Rarity getRarity() {
        return this.rarity;
    }

    public int getMinLevel() {
        return this.minLevel;
    }

    @Override
    public boolean canBePurchasedBy(Character c) {
        return c != null
                && c.getLevel() >= this.minLevel
                && c.getInventory() != null
                && c.getInventory().getGold() >= this.price;
    }

    public abstract String getItemType();

    @Override
    public String toString() {
        return String.format("%s [%s | %s] (%d Gold)",
                this.name, getItemType(), this.rarity.getLabel(), this.price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item other)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
