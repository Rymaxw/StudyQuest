package com.studyquest.quest;

import com.studyquest.character.Character;
import com.studyquest.system.EventType;
import com.studyquest.system.GameEvent;
import com.studyquest.system.GameEventListener;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class QuestManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Quest> quests = new ArrayList<>();
    private int nextId = 1;
    private final transient List<GameEventListener> listeners = new ArrayList<>();

    public void addQuest(Quest q) {
        quests.add(q);
    }

    public boolean updateQuest(Quest q) {
        for (int i = 0; i < quests.size(); i++) {
            if (quests.get(i).getId() == q.getId()) {
                quests.set(i, q);
                return true;
            }
        }
        return false;
    }

    public boolean removeQuest(int id) {
        return quests.removeIf(q -> q.getId() == id);
    }

    public Quest findById(int id) {
        return quests.stream().filter(q -> q.getId() == id).findFirst().orElse(null);
    }

    public List<Quest> getActiveQuests() {
        return quests.stream().filter(q -> q.getStatus() == QuestStatus.ACTIVE).toList();
    }

    public List<Quest> filterByType(String t) {
        return quests.stream().filter(q -> q.getType().toLowerCase().contains(t.toLowerCase())).toList();
    }

    public List<Quest> sortByDeadline() {
        List<Quest> sorted = new ArrayList<>(quests);
        sorted.sort(Comparator.comparing(Quest::getDeadline));
        return sorted;
    }

    public Reward completeQuest(int id, Character c) {
        Quest q = findById(id);
        if (q == null) return null;
        Reward reward = q.complete(c);
        if (reward != null) {
            fireEvent(new GameEvent(EventType.QUEST_COMPLETED, reward.getXp(), LocalDate.now()));
        }
        return reward;
    }

    public int checkOverdue(Character c) {
        int overdueCount = 0;
        for (Quest q : quests) {
            if (q.getStatus() == QuestStatus.ACTIVE && q.isOverdue()) {
                q.setStatus(QuestStatus.FAILED);
                overdueCount++;
                if (c != null) {
                    c.takeDamage(10);
                }
                fireEvent(new GameEvent(EventType.QUEST_FAILED, 1, LocalDate.now()));
            }
        }
        return overdueCount;
    }

    public void addListener(GameEventListener l) {
        if (!getListeners().contains(l)) {
            getListeners().add(l);
        }
    }

    private List<GameEventListener> getListeners() {
        return listeners != null ? listeners : new ArrayList<>();
    }

    private void fireEvent(GameEvent e) {
        for (GameEventListener l : getListeners()) {
            l.onEvent(e);
        }
    }

    public int getNextId() { return nextId++; }
    public List<Quest> getQuests() { return quests; }
}
