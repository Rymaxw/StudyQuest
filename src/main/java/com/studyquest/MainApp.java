package com.studyquest;

import com.studyquest.battle.*;
import com.studyquest.character.*;
import com.studyquest.character.Character;
import com.studyquest.item.*;
import com.studyquest.quest.*;
import com.studyquest.system.*;

import java.time.LocalDate;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   STUDYQUEST - APLIKASI TUGAS KULIAH RPG (PBO)   ");
        System.out.println("==================================================");

        // 1. Inisialisasi Sistem & Modul 1 (Karakter & Akun)
        DataRepository repo = new InMemoryDataRepository();
        AuthService auth = new AuthService(repo);

        System.out.println("\n[1] Mendaftarkan Pengguna Baru...");
        User user = auth.register("rymaxw", "pbo123");
        Character hero = new Warrior("Aragorn Max");
        user.setCharacter(hero);
        System.out.println("Berhasil login sebagai: " + user.getUsername());
        System.out.println("Karakter: " + hero.getName() + " | Class: " + hero.getClassName());
        System.out.println("Status Awal: HP=" + hero.getHp() + "/" + hero.getMaxHp() +
                ", MP=" + hero.getMp() + "/" + hero.getMaxMp() +
                ", ATK=" + hero.getAttack() + ", INT=" + hero.getIntelligence());

        // 2. Modul 2: Quest Management
        System.out.println("\n[2] Menambahkan Quest Baru...");
        GameState state = GameState.newGame(user);
        QuestManager qm = state.getQuestManager();

        DailyQuest q1 = new DailyQuest(qm.getNextId(), "Belajar Abstract Class & Interface", "Kerjakan unguided praktikum PBO", LocalDate.now().plusDays(1), Difficulty.HARD);
        qm.addQuest(q1);

        System.out.println("Quest aktif: " + q1.getTitle() + " (" + q1.getType() + " - " + q1.getDifficulty() + ")");
        System.out.println("Menyelesaikan quest...");
        Reward r = qm.completeQuest(q1.getId(), hero);
        System.out.println("Hasil Reward: " + r);
        System.out.println("Karakter Level=" + hero.getLevel() + ", XP=" + hero.getXp() + "/" + hero.xpToNextLevel() + ", Gold=" + hero.getInventory().getGold());

        // 3. Modul 3: Shop & Inventory
        System.out.println("\n[3] Mengunjungi Shop...");
        Shop shop = state.getShop();
        Item potion = shop.getCatalog().get(0);
        System.out.println("Membeli item: " + potion.getName() + " seharga " + potion.getPrice() + " Gold");
        try {
            shop.buy(potion, hero);
            System.out.println("Item berhasil dibeli! Sisa gold: " + hero.getInventory().getGold());
        } catch (Exception e) {
            System.out.println("Gagal beli: " + e.getMessage());
        }

        // 4. Modul 4: Boss Battle
        System.out.println("\n[4] Memulai Boss Battle (Tugas Besar Mode)...");
        Monster boss = MonsterFactory.create(MonsterType.SLIME, hero.getLevel());
        BattleEngine battle = new BattleEngine();
        battle.start(hero, boss);

        int turn = 1;
        while (!battle.isFinished() && turn <= 10) {
            battle.playerAction(BattleAction.SKILL, 0); // PowerStrike
            turn++;
        }

        BattleResult bRes = battle.getResult();
        if (bRes != null && bRes.isWon()) {
            System.out.println("Pertarungan Menang dalam " + bRes.getTurns() + " turn!");
            state.getTracker().onEvent(new GameEvent(EventType.BOSS_DEFEATED, 1, LocalDate.now()));
        }

        // 5. Modul 5: Achievement & Save Data
        System.out.println("\n[5] Statistik & Achievement...");
        System.out.println("Total Quest Selesai: " + state.getStats().getTotalCompleted());
        System.out.println("Boss Dikalahkan: " + state.getStats().getBossesDefeated());
        System.out.println("Achievement Terbuka:");
        for (Achievement a : state.getTracker().getUnlocked()) {
            System.out.println(" - [UNLOCKED] " + a.getTitle() + ": " + a.getDescription());
        }

        System.out.println("\n==================================================");
        System.out.println("Seluruh 5 Modul Terintegrasi & Berfungsi Normal!");
        System.out.println("==================================================");
    }
}
