package com.studyquest.quest;

import com.studyquest.item.Item;
import java.io.Serializable;

public class Reward implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int xp;
    private final int gold;
    private final Item bonusItem;

    public Reward(int xp, int gold, Item bonusItem) {
        this.xp = xp;
        this.gold = gold;
        this.bonusItem = bonusItem;
    }

    public Reward multiply(double factor) {
        return new Reward((int) (this.xp * factor), (int) (this.gold * factor), this.bonusItem);
    }

    public Reward plus(Reward other) {
        if (other == null) return this;
        Item item = this.bonusItem != null ? this.bonusItem : other.bonusItem;
        return new Reward(this.xp + other.xp, this.gold + other.gold, item);
    }

    public int getXp() { return xp; }
    public int getGold() { return gold; }
    public Item getBonusItem() { return bonusItem; }

    @Override
    public String toString() {
        return "+" + xp + " XP, +" + gold + " Gold" + (bonusItem != null ? ", Item: " + bonusItem.getName() : "");
    }
}
