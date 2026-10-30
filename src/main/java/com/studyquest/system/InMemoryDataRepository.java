package com.studyquest.system;

import java.util.HashMap;
import java.util.Map;

public class InMemoryDataRepository implements DataRepository {
    private final Map<String, GameState> store = new HashMap<>();

    @Override
    public void save(GameState state) {
        if (state != null && state.getUser() != null) {
            store.put(state.getUser().getUsername(), state);
        }
    }

    @Override
    public GameState load(String username) {
        return store.get(username);
    }

    @Override
    public boolean exists(String username) {
        return store.containsKey(username);
    }
}
