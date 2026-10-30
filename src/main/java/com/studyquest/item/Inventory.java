package com.studyquest.item;

import com.studyquest.character.Character;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Item> items;
    private Map<EquipSlot, Equipment> equipped;
    private int gold;
    private int capacity;

    public Inventory() {
        this(20);
    }

    public Inventory(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Kapasitas inventory harus lebih dari 0.");
        }
        this.items = new ArrayList<>();
        this.equipped = new EnumMap<>(EquipSlot.class);
        this.gold = 0;
        this.capacity = capacity;
    }

    public void addItem(Item i) throws InventoryFullException {
        if (isFull()) {
            throw new InventoryFullException(this.capacity);
        }
        if (i != null) {
            this.items.add(i);
        }
    }

    public boolean removeItem(Item i) {
        if (i == null) {
            return false;
        }
        for (int idx = 0; idx < this.items.size(); idx++) {
            if (this.items.get(idx) == i) {
                this.items.remove(idx);
                return true;
            }
        }
        return this.items.remove(i);
    }

    public boolean contains(Item i) {
        return i != null && this.items.contains(i);
    }

    public boolean useItem(Item i, Character c) {
        if (i == null || c == null || !this.items.contains(i)) {
            return false;
        }

        Item targetItem = findOwnedInstance(i);
        if (targetItem instanceof Consumable cons) {
            if (cons.use(c)) {
                removeItem(targetItem);
                return true;
            }
            return false;
        }

        if (targetItem instanceof Equipment eq) {
            if (eq.canUse(c)) {
                this.equip(eq, c);
                return true;
            }
            return false;
        }

        return false;
    }

    public void equip(Equipment e, Character c) {
        if (e == null || c == null || !e.canUse(c)) {
            return;
        }

        Equipment old = this.equipped.get(e.getSlot());
        if (old == e && e.isEquipped()) {
            ensureItemConsistency(e);
            return;
        }

        if (old != null) {
            old.unequip(c);
        }

        e.equip(c);
        this.equipped.put(e.getSlot(), e);
        ensureItemConsistency(e);
    }

    public void unequip(EquipSlot s, Character c) {
        if (s == null || c == null) {
            return;
        }
        Equipment eq = this.equipped.remove(s);
        if (eq != null) {
            eq.unequip(c);
        }
    }

    public Equipment getEquipped(EquipSlot s) {
        return s != null ? this.equipped.get(s) : null;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(this.items);
    }

    public List<Item> getItemsByType(String type) {
        if (type == null) {
            return Collections.emptyList();
        }
        List<Item> filtered = new ArrayList<>();
        for (Item item : this.items) {
            if (item.getItemType().equalsIgnoreCase(type)) {
                filtered.add(item);
            }
        }
        return Collections.unmodifiableList(filtered);
    }

    public void addGold(int n) {
        if (n > 0) {
            this.gold += n;
        }
    }

    public void spendGold(int n) throws InsufficientGoldException {
        if (n < 0) {
            throw new IllegalArgumentException("Jumlah gold yang dibelanjakan tidak boleh negatif.");
        }
        if (n > this.gold) {
            throw new InsufficientGoldException(n, this.gold);
        }
        if (n > 0) {
            this.gold -= n;
        }
    }

    public int getGold() {
        return this.gold;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean isFull() {
        return this.items.size() >= this.capacity;
    }

    public int size() {
        return this.items.size();
    }

    private void ensureItemConsistency(Equipment e) {
        for (Item existing : this.items) {
            if (existing == e) {
                return;
            }
        }
        if (!isFull()) {
            this.items.add(e);
        }
    }

    private Item findOwnedInstance(Item query) {
        for (Item item : this.items) {
            if (item == query) {
                return item;
            }
        }
        if (query instanceof Equipment) {
            for (Item item : this.items) {
                if (item.equals(query) && item instanceof Equipment eq && !eq.isEquipped()) {
                    return item;
                }
            }
        }
        for (Item item : this.items) {
            if (item.equals(query)) {
                return item;
            }
        }
        return query;
    }
}
