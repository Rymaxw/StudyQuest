package com.studyquest.character;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

public final class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String username;
    private final String passwordHash;
    private final LocalDate createdAt;
    private Character character;

    public User(String username, String passwordHash) {
        this(username, passwordHash, LocalDate.now());
    }

    private User(String username, String passwordHash, LocalDate createdAt) {
        this.username = normalizeUsername(username);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.createdAt = createdAt;
    }

    public static String normalizeUsername(String username) {
        if (username == null || !username.matches("[A-Za-z0-9_]{3,20}")) {
            throw new IllegalArgumentException("Username harus 3 sampai 20 huruf, angka, atau garis bawah.");
        }
        return username.toLowerCase(Locale.ROOT);
    }

    public void setCharacter(Character character) {
        if (this.character != null) {
            throw new IllegalStateException("Karakter sudah dibuat.");
        }
        this.character = Objects.requireNonNull(character);
    }

    public User withCharacter(Character character) {
        if (this.character != null) {
            throw new IllegalStateException("Karakter sudah dibuat.");
        }
        User userWithCharacter = new User(username, passwordHash, createdAt);
        userWithCharacter.setCharacter(character);
        return userWithCharacter;
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public LocalDate getCreatedAt() { return createdAt; }
    public Character getCharacter() { return character; }
}
