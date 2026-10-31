package com.studyquest.item;

import com.studyquest.character.Character;
import com.studyquest.character.Mage;
import com.studyquest.character.Warrior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryAndShopTest {

    private Warrior hero;
    private Inventory inventory;
    private Shop shop;

    @BeforeEach
    void setUp() {
        hero = new Warrior("Arthur");
        inventory = hero.getInventory();
        shop = new Shop();
    }

    // Kapasitas Inventory
    @Test
    @DisplayName("1. Menambahkan 20 item berhasil dan item ke-21 melempar InventoryFullException")
    void testInventoryCapacityAndFullException() throws InventoryFullException {
        assertEquals(20, inventory.getCapacity());
        assertEquals(0, inventory.getGold());
        assertFalse(inventory.isFull());

        for (int i = 1; i <= 20; i++) {
            inventory.addItem(new Consumable("POT_" + i, "Potion " + i, 10, EffectType.HEAL_HP, 20));
        }

        assertEquals(20, inventory.size());
        assertTrue(inventory.isFull());

        Consumable overflowItem = new Consumable("POT_21", "Potion 21", 10, EffectType.HEAL_HP, 20);
        InventoryFullException ex = assertThrows(
                InventoryFullException.class,
                () -> inventory.addItem(overflowItem)
        );
        assertEquals(20, ex.getCapacity());
        assertTrue(ex.getMessage().contains("20"));
    }

    // Transaksi Pembelian & Penjualan (Shop.buy & Shop.sell)
    @Test
    @DisplayName("2a. Berhasil membeli saat Gold dan Level cukup (Gold berkurang & salinan item masuk Inventory)")
    void testShopBuySuccessAndDeepCopy() throws Exception {
        inventory.addGold(150);
        Item catalogSword = shop.findById("WPN_SWORD");
        assertNotNull(catalogSword);
        assertEquals(100, catalogSword.getPrice());

        shop.buy(catalogSword, hero);

        assertEquals(50, inventory.getGold());
        assertEquals(1, inventory.size());
        assertTrue(inventory.contains(catalogSword));

        Item ownedSword = inventory.getItems().get(0);
        assertNotSame(catalogSword, ownedSword);
        assertEquals(catalogSword, ownedSword);

        inventory.useItem(ownedSword, hero);
        assertTrue(((Equipment) ownedSword).isEquipped());
        assertFalse(((Equipment) catalogSword).isEquipped());
    }

    @Test
    @DisplayName("2b. Gagal membeli dan melempar InsufficientGoldException saat Gold kurang")
    void testShopBuyInsufficientGold() {
        inventory.addGold(20);
        Item potion = shop.findById("POT_HP"); // Harga 30 Gold

        InsufficientGoldException ex = assertThrows(
                InsufficientGoldException.class,
                () -> shop.buy(potion, hero)
        );
        assertEquals(30, ex.getRequired());
        assertEquals(20, ex.getCurrent());
        assertEquals(20, inventory.getGold());
        assertEquals(0, inventory.size());
    }

    @Test
    @DisplayName("2c. Menjual item mengembalikan 50% harga beli dan menghapus item dari Inventory")
    void testShopSellReturnsHalfPriceAndRemovesItem() throws Exception {
        inventory.addGold(100);
        Item armor = shop.findById("ARM_LEATHER"); // Harga 80 Gold
        shop.buy(armor, hero);
        assertEquals(20, inventory.getGold());
        assertEquals(1, inventory.size());

        Item ownedArmor = inventory.getItems().get(0);
        int earned = shop.sell(ownedArmor, hero);

        assertEquals(40, earned); // 50% dari 80
        assertEquals(60, inventory.getGold()); // 20 + 40
        assertEquals(0, inventory.size());
        assertFalse(inventory.contains(ownedArmor));
    }

    // Pemakaian Consumable (Inventory.useItem)
    @Test
    @DisplayName("3. Memakai Potion HP memulihkan HP Character dan menghapus potion dari Inventory")
    void testUseConsumableHealsAndRemovesItem() throws Exception {
        inventory.addGold(50);
        Item potionHp = shop.findById("POT_HP"); // Heal +50 HP, 30 Gold
        shop.buy(potionHp, hero);

        hero.takeDamage(70); // Warrior 120 -> 50 HP
        assertEquals(50, hero.getHp());

        boolean used = inventory.useItem(inventory.getItems().get(0), hero);
        assertTrue(used);
        assertEquals(100, hero.getHp()); // 50 + 50 = 100 HP
        assertEquals(0, inventory.size());
    }

    // Pemasangan & Penggantian Equipment (equip & unequip)
    @Test
    @DisplayName("4. Memasang Iron Sword (+8 ATK) dan mengganti senjata tanpa bug stat stacking")
    void testEquipAndSwapWeaponWithoutStatStacking() throws Exception {
        int baseAtk = hero.getAttack(); // Warrior awal = 14 ATK
        int baseInt = hero.getIntelligence(); // Warrior awal = 4 INT

        Equipment ironSword = new Equipment("WPN_SWORD", "Iron Sword", 100, EquipSlot.WEAPON, 0, 8, 0);
        Equipment steelBlade = new Equipment("WPN_STEEL", "Steel Blade", 200, EquipSlot.WEAPON, 0, 15, 0);

        inventory.addItem(ironSword);
        inventory.addItem(steelBlade);

        // Pasang Iron Sword -> +8 ATK
        inventory.equip(ironSword, hero);
        assertEquals(baseAtk + 8, hero.getAttack());
        assertTrue(ironSword.isEquipped());
        assertSame(ironSword, inventory.getEquipped(EquipSlot.WEAPON));

        // Pasang ulang senjata yang sama -> tidak boleh double stack!
        inventory.equip(ironSword, hero);
        assertEquals(baseAtk + 8, hero.getAttack());

        // Ganti ke Steel Blade (+15 ATK) -> Iron Sword dilepas (-8), Steel Blade dipasang (+15)
        inventory.equip(steelBlade, hero);
        assertEquals(baseAtk + 15, hero.getAttack());
        assertFalse(ironSword.isEquipped());
        assertTrue(steelBlade.isEquipped());
        assertSame(steelBlade, inventory.getEquipped(EquipSlot.WEAPON));

        // Unequip slot WEAPON -> kembali ke baseAtk murni
        inventory.unequip(EquipSlot.WEAPON, hero);
        assertEquals(baseAtk, hero.getAttack());
        assertEquals(baseInt, hero.getIntelligence());
        assertFalse(steelBlade.isEquipped());
        assertNull(inventory.getEquipped(EquipSlot.WEAPON));
    }

    // Edge Cases Tambahan (Atomisitas, Duplikat Item, Level, Serialisasi)
    @Test
    @DisplayName("Edge Case: Gagal beli karena tas penuh tidak memotong Gold pemain (Atomik)")
    void testShopBuyWhenInventoryFullDoesNotDeductGold() throws Exception {
        Inventory smallInv = new Inventory(1);
        Character customHero = new Mage("Merlin") {
            @Override
            public Inventory getInventory() {
                return smallInv;
            }
        };
        smallInv.addGold(200);
        smallInv.addItem(new Consumable("POT_1", "Dummy", 10, EffectType.HEAL_HP, 10));

        assertThrows(
                InventoryFullException.class,
                () -> shop.buy(shop.findById("POT_HP"), customHero)
        );
        assertEquals(200, smallInv.getGold(), "Gold tidak boleh terpotong jika inventory penuh!");
    }

    @Test
    @DisplayName("Edge Case: Gagal beli jika level karakter kurang & gagal pakai potion saat gugur")
    void testLevelRequirementAndDeadCharacterGuard() throws Exception {
        Equipment epicSword = new Equipment(
                "WPN_EXCALIBUR", "Excalibur", "Pedang legendaris",
                500, Rarity.EPIC, 5, EquipSlot.WEAPON, 50, 30, 10
        );
        inventory.addGold(1000);
        assertFalse(epicSword.canBePurchasedBy(hero));
        assertThrows(IllegalArgumentException.class, () -> shop.buy(epicSword, hero));

        // Karakter gugur (HP = 0) tidak bisa memakai potion
        Consumable potion = new Consumable("POT_HP", "Potion HP", 30, EffectType.HEAL_HP, 50);
        inventory.addItem(potion);
        hero.takeDamage(999);
        assertEquals(0, hero.getHp());
        assertFalse(inventory.useItem(potion, hero));
        assertEquals(1, inventory.size(), "Potion tidak boleh hilang jika gagal digunakan!");
    }

    @Test
    @DisplayName("Edge Case: Memiliki 2 pedang kembar, menjual yang cadangan tidak melepas yang sedang dipakai")
    void testSellingDuplicateUnequippedEquipmentKeepsEquippedInstanceSafe() throws Exception {
        inventory.addGold(300);
        Item catalogSword = shop.findById("WPN_SWORD");

        shop.buy(catalogSword, hero);
        shop.buy(catalogSword, hero);
        assertEquals(2, inventory.size());

        Equipment sword1 = (Equipment) inventory.getItems().get(0);
        Equipment sword2 = (Equipment) inventory.getItems().get(1);

        // Pasang sword1, lalu jual sword2
        inventory.equip(sword1, hero);
        int atkWhileEquipped = hero.getAttack();

        int earned = shop.sell(sword2, hero);
        assertEquals(50, earned);
        assertEquals(1, inventory.size());
        assertTrue(sword1.isEquipped());
        assertSame(sword1, inventory.getItems().get(0));
        assertSame(sword1, inventory.getEquipped(EquipSlot.WEAPON));
        assertEquals(atkWhileEquipped, hero.getAttack());

        shop.sell(sword1, hero);
        assertEquals(0, inventory.size());
        assertNull(inventory.getEquipped(EquipSlot.WEAPON));
        assertFalse(sword1.isEquipped());
        assertEquals(14, hero.getAttack());
    }

    @Test
    @DisplayName("Edge Case: Filter katalog/inventory & Serialisasi ObjectOutputStream berjalan mulus")
    void testFilteringEnumsAndJavaSerialization() throws Exception {
        assertEquals("Common", Rarity.COMMON.getLabel());
        assertEquals("Weapon", EquipSlot.WEAPON.getLabel());
        assertEquals("Heal HP", EffectType.HEAL_HP.getLabel());

        assertEquals(6, shop.filterByType("ALL").size());
        assertEquals(3, shop.filterByType("CONSUMABLE").size());
        assertEquals(3, shop.filterByType("EQUIPMENT").size());

        inventory.addGold(250);
        shop.buy(shop.findById("POT_MP"), hero);
        shop.buy(shop.findById("ARM_LEATHER"), hero);
        List<Item> equipments = inventory.getItemsByType("EQUIPMENT");
        assertEquals(1, equipments.size());
        inventory.useItem(equipments.get(0), hero);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(hero);
            oos.writeObject(shop);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            Warrior loadedHero = (Warrior) ois.readObject();
            Shop loadedShop = (Shop) ois.readObject();

            assertEquals(hero.getMaxHp(), loadedHero.getMaxHp());
            assertEquals(inventory.getGold(), loadedHero.getInventory().getGold());
            assertEquals(2, loadedHero.getInventory().size());
            assertNotNull(loadedHero.getInventory().getEquipped(EquipSlot.ARMOR));
            assertEquals(6, loadedShop.getCatalog().size());
        }
    }
}

