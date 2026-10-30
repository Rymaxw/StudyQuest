package com.studyquest.item;

public class InventoryFullException extends Exception {
    private final int capacity;

    public InventoryFullException(int capacity) {
        super("Inventory penuh! Maksimal kapasitas: " + capacity);
        this.capacity = capacity;
    }

    public int getCapacity() { return capacity; }
}
