package com.studyquest.item;

public class InventoryFullException extends Exception {
    private static final long serialVersionUID = 1L;

    private int capacity;

    public InventoryFullException(int capacity) {
        super("Inventory sudah penuh! Kapasitas maksimal: " + capacity + " item.");
        this.capacity = capacity;
    }

    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public String getMessage() {
        return "Inventory sudah penuh! Kapasitas maksimal: " + this.capacity + " item.";
    }
}
