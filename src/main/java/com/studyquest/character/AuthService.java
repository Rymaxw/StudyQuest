package com.studyquest.character;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import com.studyquest.system.DataRepository;
import com.studyquest.system.GameState;

public final class AuthService {
    public static final int ITERATIONS = 120_000;
    public static final int SALT_BYTES = 16;
    public static final int HASH_BITS = 256;
    private static final String LOGIN_ERROR = "Username atau password salah.";
    private final DataRepository repository;
    private final SecureRandom random = new SecureRandom();

    public AuthService(DataRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    /** Membuat akun huruf kecil dan menyimpannya sebelum mengembalikan sesi tanpa karakter. */
    public GameState register(String username, String password) {
        String normalizedUsername = User.normalizeUsername(username);
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter.");
        }
        if (repository.exists(normalizedUsername)) {
            throw new IllegalArgumentException("Username sudah dipakai.");
        }
        GameState state = new GameState(new User(normalizedUsername, hash(password)));
        try {
            repository.save(state);
        } catch (IOException exception) {
            throw new UncheckedIOException("Akun belum tersimpan. Coba lagi.", exception);
        }
        return state;
    }

    /** Kredensial salah memakai pesan umum; kegagalan muat bukan pendaftaran akun baru. */
    public GameState login(String username, String password) {
        String normalizedUsername;
        try {
            normalizedUsername = User.normalizeUsername(username);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(LOGIN_ERROR);
        }
        if (password == null || !repository.exists(normalizedUsername)) {
            throw new IllegalArgumentException(LOGIN_ERROR);
        }
        GameState state;
        try {
            state = repository.load(normalizedUsername);
        } catch (IOException exception) {
            throw new UncheckedIOException("Data akun gagal dimuat. Data lama tidak diubah.", exception);
        }
        if (!verify(password, state.getUser().getPasswordHash())) {
            throw new IllegalArgumentException(LOGIN_ERROR);
        }
        return state;
    }

    private String hash(String password) {
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);
        byte[] passwordHash = derive(password, salt);
        return "pbkdf2-sha256$" + ITERATIONS + "$" + Base64.getEncoder().encodeToString(salt)
                + "$" + Base64.getEncoder().encodeToString(passwordHash);
    }

    private boolean verify(String password, String encodedHash) {
        String[] fields = encodedHash.split("\\$", -1);
        if (fields.length != 4 || !fields[0].equals("pbkdf2-sha256") || !fields[1].equals(String.valueOf(ITERATIONS))) {
            throw new IllegalStateException("Format hash akun tidak didukung. Data tidak diubah.");
        }
        try {
            byte[] salt = Base64.getDecoder().decode(fields[2]);
            byte[] expectedHash = Base64.getDecoder().decode(fields[3]);
            if (salt.length != SALT_BYTES || expectedHash.length != HASH_BITS / Byte.SIZE) {
                throw new IllegalArgumentException();
            }
            return MessageDigest.isEqual(expectedHash, derive(password, salt));
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("Hash akun rusak. Data tidak diubah.", exception);
        }
    }

    private static byte[] derive(String password, byte[] salt) {
        char[] passwordCharacters = password.toCharArray();
        PBEKeySpec keySpecification = new PBEKeySpec(passwordCharacters, salt, ITERATIONS, HASH_BITS);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(keySpecification).getEncoded();
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Hash password tidak tersedia.", exception);
        } finally {
            keySpecification.clearPassword();
            Arrays.fill(passwordCharacters, '\0');
        }
    }
}
