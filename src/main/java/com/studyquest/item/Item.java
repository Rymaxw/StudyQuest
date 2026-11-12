package com.studyquest.item;

import java.io.Serializable;
import java.util.Objects;

/** Identitas dasar item untuk Inventory; perilaku shop/equipment tetap milik Modul 3. */
public abstract class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private final String name;
    private final int price;
    private final int minLevel;

    protected Item(String id, String name, int price, int minLevel) {
        if (price < 0 || minLevel < 1) {
            throw new IllegalArgumentException("Harga atau level item tidak valid.");
        }
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.price = price;
        this.minLevel = minLevel;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getMinLevel() { return minLevel; }
}
