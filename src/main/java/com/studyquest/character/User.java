package com.studyquest.character;

import java.io.Serializable;
import java.time.LocalDate;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String username;
    private final String passwordHash;
    private Character character;
    private final LocalDate createdAt;

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDate.now();
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String raw) {
        return this.passwordHash.equals(AuthService.hashPassword(raw));
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
