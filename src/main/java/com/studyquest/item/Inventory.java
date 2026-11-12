package com.studyquest.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.studyquest.character.Character;

/** Fondasi tas tersimpan; equipment dan transaksi Shop diintegrasikan oleh Modul 3. */
public final class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int INITIAL_CAPACITY = 20;
    public static final int MAX_CAPACITY = 25;
    private final List<Item> items = new ArrayList<>();
    private int capacity = INITIAL_CAPACITY;
    private int gold;

    /** Menambah gold nonnegatif; overflow ditolak sebelum perubahan. */
    public void addGold(int amount) {
        requireNonNegative(amount);
        gold = Math.addExact(gold, amount);
    }

    /** Gold tidak cukup tidak mengubah saldo. */
    public void spendGold(int amount) {
        requireNonNegative(amount);
        if (amount > gold) {
            throw new InsufficientGoldException("Gold tidak cukup.");
        }
        gold -= amount;
    }
    /** Satu objek memakai satu slot; tas penuh menolak tanpa perubahan. */
    public void addItem(Item item) {
        Objects.requireNonNull(item);
        if (isFull()) {
            throw new InventoryFullException("Tas penuh. Kosongkan satu slot terlebih dahulu.");
        }
        if (items.contains(item)) {
            throw new IllegalArgumentException("Objek item sudah ada di tas.");
        }
        items.add(item);
    }
    /** Kapasitas termasuk item yang belum dipakai. */
    public boolean isFull() { return items.size() >= capacity; }
    /** Item tidak dimiliki/tidak dapat dipakai tidak dihapus atau mengubah karakter. */
    public boolean useItem(Item item, Character character) {
        Objects.requireNonNull(character);
        if (!items.contains(item) || !(item instanceof Usable usable)) {
            return false;
        }
        if (!usable.use(character)) {
            return false;
        }
        items.remove(item);
        return true;
    }
    /** Perluasan positif, maksimal 25 slot. */
    public void expandCapacity(int amount) {
        if (amount <= 0 || (long) capacity + amount > MAX_CAPACITY) {
            throw new IllegalArgumentException("Kapasitas tas maksimal 25 slot.");
        }
        capacity += amount;
    }
    private static void requireNonNegative(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Jumlah tidak boleh negatif.");
        }
    }
    public int getGold() { return gold; }
    public int getCapacity() { return capacity; }
    public List<Item> getItems() { return List.copyOf(items); }
}
