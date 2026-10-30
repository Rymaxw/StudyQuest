package com.studyquest.system;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDataRepository implements DataRepository {
    private final String baseDir;

    public FileDataRepository(String baseDir) {
        this.baseDir = baseDir;
        try {
            Files.createDirectories(Paths.get(baseDir));
        } catch (IOException ignored) {}
    }

    @Override
    public void save(GameState state) {
        if (state == null || state.getUser() == null) return;
        Path filePath = Paths.get(baseDir, state.getUser().getUsername() + ".dat");
        Path tempPath = Paths.get(baseDir, state.getUser().getUsername() + ".dat.tmp");

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(tempPath))) {
            oos.writeObject(state);
            Files.move(tempPath, filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING, java.nio.file.StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GameState load(String username) {
        Path filePath = Paths.get(baseDir, username + ".dat");
        if (!Files.exists(filePath)) return null;

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (GameState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean exists(String username) {
        return Files.exists(Paths.get(baseDir, username + ".dat"));
    }
}
