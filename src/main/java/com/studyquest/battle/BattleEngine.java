package com.studyquest.battle;

import com.studyquest.character.Character;
import com.studyquest.quest.Reward;

public class BattleEngine {
    private Character player;
    private Monster monster;
    private int turnCount;
    private BattleState state;
    private final BattleLog log = new BattleLog();
    private boolean defending;

    public void start(Character p, Monster m) {
        this.player = p;
        this.monster = m;
        this.turnCount = 1;
        this.state = BattleState.PLAYER_TURN;
        this.defending = false;
        log.clear();
        log.add("Pertarungan dimulai! " + p.getName() + " VS " + m.getName());
    }

    public void playerAction(BattleAction action, int param) {
        if (state != BattleState.PLAYER_TURN) return;
        defending = false;

        switch (action) {
            case ATTACK -> {
                int dmg = player.basicAttack(monster);
                log.add(player.getName() + " menyerang dan menghasilkan " + dmg + " damage!");
            }
            case SKILL -> {
                int dmg = player.useSkill(param, monster);
                if (dmg > 0) {
                    log.add(player.getName() + " memakai skill dan menghasilkan " + dmg + " damage!");
                } else {
                    log.add(player.getName() + " gagal memakai skill (MP tidak cukup)!");
                }
            }
            case DEFEND -> {
                defending = true;
                log.add(player.getName() + " mengambil posisi bertahan (damage monster berkurang 50%)!");
            }
            default -> log.add(player.getName() + " bersiap!");
        }

        checkEnd();
        if (state == BattleState.PLAYER_TURN) {
            state = BattleState.MONSTER_TURN;
            monsterTurn();
        }
    }

    private void monsterTurn() {
        if (monster.isDefeated()) {
            checkEnd();
            return;
        }

        BattleAction action = monster.chooseAction(turnCount);
        if (action == BattleAction.SPECIAL) {
            if (monster instanceof Slime) {
                int heal = monster.specialAbility(player);
                log.add(monster.getName() + " menggunakan regenerasi dan memulihkan " + heal + " HP!");
            } else {
                int dmg = monster.specialAbility(player);
                if (defending) dmg /= 2;
                player.takeDamage(dmg);
                log.add(monster.getName() + " melancarkan jurus khusus sebesar " + dmg + " damage!");
            }
        } else {
            int dmg = monster.getAttackPower();
            if (defending) dmg /= 2;
            player.takeDamage(dmg);
            log.add(monster.getName() + " menyerang balik sebesar " + dmg + " damage!");
        }

        turnCount++;
        checkEnd();
        if (state == BattleState.MONSTER_TURN) {
            state = BattleState.PLAYER_TURN;
        }
    }

    private void checkEnd() {
        if (monster.isDefeated()) {
            state = BattleState.WON;
            log.add("Selamat! " + monster.getName() + " berhasil dikalahkan!");
        } else if (player.getHp() <= 0) {
            state = BattleState.LOST;
            log.add("Karaktermu gugur dalam pertempuran!");
        }
    }

    public boolean isFinished() {
        return state == BattleState.WON || state == BattleState.LOST;
    }

    public BattleResult getResult() {
        if (!isFinished()) return null;
        boolean won = (state == BattleState.WON);
        Reward reward = won ? monster.getReward() : new Reward(0, 0, null);
        return new BattleResult(won, turnCount, reward);
    }

    public BattleLog getLog() { return log; }
    public BattleState getState() { return state; }
    public Character getPlayer() { return player; }
    public Monster getMonster() { return monster; }
}
