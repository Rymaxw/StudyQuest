package com.studyquest.quest;

import com.studyquest.battle.MonsterType;
import com.studyquest.item.Equipment;
import com.studyquest.item.EquipSlot;
import com.studyquest.item.Rarity;
import java.time.LocalDate;

public class BossQuest extends Quest {
    private static final long serialVersionUID = 1L;
    private final MonsterType bossType;
    private boolean bossDefeated = false;

    public BossQuest(int id, String title, String description, LocalDate deadline, Difficulty difficulty, MonsterType bossType) {
        super(id, title, description, deadline, difficulty);
        this.bossType = bossType;
    }

    public void markBossDefeated() {
        this.bossDefeated = true;
    }

    @Override
    public Reward calculateReward() {
        Equipment drop = new Equipment("drop_" + getId(), "Pahlawan Crest", "Crest kemenangan boss (+20 HP, +5 ATK)", 150, Rarity.EPIC, 1, EquipSlot.ACCESSORY, 20, 5, 5);
        return new Reward(getDifficulty().getBaseXp() * 4, getDifficulty().getBaseGold() * 4, drop);
    }

    @Override
    public String getType() {
        return "Boss Quest (" + bossType + ")";
    }

    public MonsterType getBossType() { return bossType; }
    public boolean isBossDefeated() { return bossDefeated; }
}
