package com.studyquest.item;

import com.studyquest.character.Character;
import java.io.Serializable;
import java.util.*;

public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Item> items = new ArrayList<>();
    private final Map<EquipSlot, Equipment> equipped = new EnumMap<>(EquipSlot.class);
    private int gold = 100; // Starting gold
    private final int capacity = 20;

    public void addItem(Item i) throws InventoryFullException {
        if (isFull()) {
            throw new InventoryFullException(capacity);
        }
        items.add(i);
    }

    public boolean removeItem(Item i) {
        return items.remove(i);
    }

    public boolean useItem(Item i, Character c) {
        if (!items.contains(i)) return false;
        if (i instanceof Usable usable) {
            boolean success = usable.use(c);
            if (success && i instanceof Consumable) {
                items.remove(i);
            }
            return success;
        }
        return false;
    }

    public void equip(Equipment e, Character c) {
        Equipment old = equipped.get(e.getSlot());
        if (old != null) {
            old.unequip(c);
            items.add(old);
        }
        items.remove(e);
        equipped.put(e.getSlot(), e);
        e.equip(c);
    }

    public void unequip(EquipSlot s, Character c) throws InventoryFullException {
        if (isFull()) throw new InventoryFullException(capacity);
        Equipment e = equipped.remove(s);
        if (e != null) {
            e.unequip(c);
            items.add(e);
        }
    }

    public void addGold(int n) {
        if (n > 0) this.gold += n;
    }

    public void spendGold(int n) throws InsufficientGoldException {
        if (n < 0 || this.gold < n) {
            throw new InsufficientGoldException(n, this.gold);
        }
        this.gold -= n;
    }

    public int getGold() { return gold; }
    public boolean isFull() { return items.size() >= capacity; }
    public List<Item> getItems() { return Collections.unmodifiableList(items); }
    public Map<EquipSlot, Equipment> getEquipped() { return Collections.unmodifiableMap(equipped); }
    public int getCapacity() { return capacity; }
}
