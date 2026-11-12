package com.studyquest.system;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import com.studyquest.character.User;

public final class FileDataRepository implements DataRepository {
    private final Path directory;

    public FileDataRepository(Path directory) {
        this.directory = Objects.requireNonNull(directory);
    }

    /** Tulis tmp, pertahankan cadangan valid, lalu ganti dat secara atomik bila didukung OS. */
    public synchronized void save(GameState state) throws IOException {
        Objects.requireNonNull(state);
        String username = state.getUser().getUsername();
        Path dataPath = path(username, ".dat");
        Path temporaryPath = path(username, ".dat.tmp");
        Path backupPath = path(username, ".dat.bak");
        Files.createDirectories(directory);
        try {
            try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(temporaryPath))) {
                output.writeObject(state);
            }
            if (Files.exists(dataPath) && isValidSave(dataPath, username)) {
                Files.copy(dataPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
            }
            replaceSave(temporaryPath, dataPath);
        } finally {
            Files.deleteIfExists(temporaryPath);
        }
    }

    private boolean isValidSave(Path dataPath, String username) {
        try {
            read(dataPath, username);
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    private void replaceSave(Path temporaryPath, Path dataPath) throws IOException {
        try {
            Files.move(temporaryPath, dataPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(temporaryPath, dataPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /** Coba dat lalu bak tanpa menulis apa pun; flag pemulihan digunakan UI setelah login valid. */
    public synchronized GameState load(String username) throws IOException {
        String normalizedUsername = User.normalizeUsername(username);
        try {
            return read(path(normalizedUsername, ".dat"), normalizedUsername);
        } catch (IOException primaryFailure) {
            try {
                GameState state = read(path(normalizedUsername, ".dat.bak"), normalizedUsername);
                state.markRecoveredFromBackup();
                return state;
            } catch (IOException backupFailure) {
                primaryFailure.addSuppressed(backupFailure);
                throw primaryFailure;
            }
        }
    }

    /** Cadangan saja tetap mengunci nama akun agar registrasi tidak menimpa progres lama. */
    public boolean exists(String username) {
        return Files.exists(path(username, ".dat")) || Files.exists(path(username, ".dat.bak"));
    }

    private Path path(String username, String suffix) {
        return directory.resolve(User.normalizeUsername(username) + suffix);
    }

    private GameState read(Path path, String username) throws IOException {
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(path))) {
            input.setObjectInputFilter(ObjectInputFilter.Config.createFilter(
                    "maxdepth=64;maxrefs=100000;maxbytes=10485760;com.studyquest.**;java.base/*;!*"));
            Object value = input.readObject();
            if (!(value instanceof GameState state) || state.getUser() == null
                    || !username.equals(state.getUser().getUsername())) {
                throw new IOException("Data bukan milik akun yang diminta.");
            }
            state.rewire();
            return state;
        } catch (ClassNotFoundException | RuntimeException exception) {
            throw new IOException("Data simpan tidak kompatibel atau rusak.", exception);
        }
    }
}
