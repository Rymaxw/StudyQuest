package com.studyquest.battle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BattleLog implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<String> entries = new ArrayList<>();

    public void add(String msg) {
        entries.add(msg);
    }

    public List<String> getEntries() {
        return entries;
    }

    public void clear() {
        entries.clear();
    }
}
