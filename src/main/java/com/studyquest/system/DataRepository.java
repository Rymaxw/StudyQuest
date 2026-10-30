package com.studyquest.system;

public interface DataRepository {
    void save(GameState state);
    GameState load(String username);
    boolean exists(String username);
}
