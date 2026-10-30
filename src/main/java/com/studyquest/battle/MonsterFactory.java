package com.studyquest.battle;

public class MonsterFactory {
    public static Monster create(MonsterType type, int playerLevel) {
        double scale = 1.0 + 0.1 * Math.max(0, playerLevel - 1);
        Monster m = switch (type) {
            case SLIME -> new Slime(playerLevel);
            case GOBLIN -> new Goblin(playerLevel);
            case DRAGON -> new Dragon(playerLevel);
        };
        m.maxHp = (int)(m.maxHp * scale);
        m.hp = m.maxHp;
        m.attackPower = (int)(m.attackPower * scale);
        return m;
    }
}
