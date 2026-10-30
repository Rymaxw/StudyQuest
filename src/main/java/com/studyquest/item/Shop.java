package com.studyquest.item;

import com.studyquest.character.Character;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shop implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Item> catalog = new ArrayList<>();
    private final double sellRatio = 0.5;

    public Shop() {
        loadDefaultCatalog();
    }

    public void loadDefaultCatalog() {
        catalog.clear();
        catalog.add(new Consumable("c1", "Health Potion", "Memulihkan 40 HP", 20, Rarity.COMMON, 1, EffectType.HEAL_HP, 40));
        catalog.add(new Consumable("c2", "Mana Potion", "Memulihkan 25 MP", 25, Rarity.COMMON, 1, EffectType.RESTORE_MP, 25));
        catalog.add(new Consumable("c3", "XP Booster", "Mendapatkan 50 XP instan", 50, Rarity.RARE, 2, EffectType.XP_BOOST, 50));
        catalog.add(new Equipment("e1", "Iron Sword", "Pedang besi (+6 ATK)", 100, Rarity.COMMON, 1, EquipSlot.WEAPON, 0, 6, 0));
        catalog.add(new Equipment("e2", "Mage Staff", "Tongkat sihir (+8 INT)", 100, Rarity.COMMON, 1, EquipSlot.WEAPON, 0, 0, 8));
        catalog.add(new Equipment("e3", "Leather Armor", "Pelindung kulit (+30 HP)", 80, Rarity.COMMON, 1, EquipSlot.ARMOR, 30, 0, 0));
    }

    public void buy(Item item, Character character) throws InsufficientGoldException, InventoryFullException {
        if (character == null) return;
        if (!item.canBePurchasedBy(character)) {
            throw new InsufficientGoldException(item.getPrice(), character.getInventory().getGold());
        }
        character.getInventory().spendGold(item.getPrice());
        character.getInventory().addItem(item);
    }

    public int sell(Item item, Character character) {
        if (character == null || !character.getInventory().getItems().contains(item)) {
            return 0;
        }
        int price = sellPrice(item);
        character.getInventory().removeItem(item);
        character.getInventory().addGold(price);
        return price;
    }

    public int sellPrice(Item item) {
        return (int) (item.getPrice() * sellRatio);
    }

    public List<Item> getCatalog() { return catalog; }
    public List<Item> filterByType(String type) {
        return catalog.stream().filter(i -> i.getItemType().toLowerCase().contains(type.toLowerCase())).toList();
    }
}
