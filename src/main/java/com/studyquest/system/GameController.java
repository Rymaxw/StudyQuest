package com.studyquest.system;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import com.studyquest.character.Archer;
import com.studyquest.character.AuthService;
import com.studyquest.character.Character;
import com.studyquest.character.Mage;
import com.studyquest.character.Warrior;

/** Facade sesi dan akun; method modul lain ditambahkan oleh pemiliknya. */
public final class GameController {
    private final DataRepository repository;
    private final AuthService auth;
    private GameState state;

    public GameController(DataRepository repository) {
        this.repository = Objects.requireNonNull(repository);
        this.auth = new AuthService(repository);
    }

    /** Sesi aktif atau null sebelum login; UI hanya membaca lewat getter. */
    public GameState getState() {
        return state;
    }

    /** Publikasikan sesi hanya setelah registrasi tersimpan. */
    public void register(String username, String password) {
        state = auth.register(username, password);
    }

    /** Gagal login tidak mengganti sesi; evaluasi overdue menunggu integrasi Modul 2. */
    public void login(String username, String password) {
        GameState loadedState = auth.login(username, password);
        loadedState.rewire();
        state = loadedState;
    }

    /** Karakter hanya dibuat sekali; simpan kandidat dahulu untuk mencegah perubahan parsial. */
    public void createCharacter(String jobClass, String name) {
        if (state == null) {
            throw new IllegalStateException("Masuk ke akun terlebih dahulu.");
        }
        if (state.getUser().getCharacter() != null) {
            throw new IllegalStateException("Karakter sudah dibuat.");
        }
        Character character = switch (jobClass == null ? "" : jobClass) {
            case "Warrior" -> new Warrior(name);
            case "Mage" -> new Mage(name);
            case "Archer" -> new Archer(name);
            default -> throw new IllegalArgumentException("Pilih class karakter.");
        };
        GameState candidateState = new GameState(state.getUser().withCharacter(character));
        try {
            repository.save(candidateState);
        } catch (IOException exception) {
            throw new UncheckedIOException("Karakter belum tersimpan. Coba lagi.", exception);
        }
        state = candidateState;
    }

    /** Tidak melakukan apa pun tanpa sesi; error disk diteruskan ke UI. */
    public void saveNow() throws IOException {
        if (state != null) {
            repository.save(state);
        }
    }

    /** Gagal simpan mempertahankan sesi agar pengguna dapat mencoba lagi. */
    public void logout() {
        try {
            saveNow();
        } catch (IOException exception) {
            throw new UncheckedIOException("Gagal menyimpan. Kamu belum keluar.", exception);
        }
        state = null;
    }
}
