package com.studyquest.character;

import com.studyquest.system.DataRepository;
import com.studyquest.system.GameState;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Map<String, User> users = new HashMap<>();
    private final DataRepository repository;

    public AuthService(DataRepository repository) {
        this.repository = repository;
    }

    public User register(String username, String password) {
        if (isUsernameTaken(username)) {
            throw new IllegalArgumentException("Username already taken: " + username);
        }
        User newUser = new User(username, hashPassword(password));
        users.put(username, newUser);

        if (repository != null) {
            GameState state = GameState.newGame(newUser);
            repository.save(state);
        }
        return newUser;
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user == null && repository != null && repository.exists(username)) {
            GameState state = repository.load(username);
            if (state != null && state.getUser() != null) {
                user = state.getUser();
                users.put(username, user);
            }
        }
        if (user == null || !user.checkPassword(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
    }

    public boolean isUsernameTaken(String username) {
        if (users.containsKey(username)) return true;
        return repository != null && repository.exists(username);
    }

    public static String hashPassword(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm missing", e);
        }
    }
}
