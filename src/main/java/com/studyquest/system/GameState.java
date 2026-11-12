package com.studyquest.system;

import java.io.Serializable;
import java.util.Objects;
import com.studyquest.character.User;

/** Agregat awal Modul 1; quest/shop/tracker/stats ditambahkan pemilik Modul 5 saat integrasi. */
public final class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    private final User user;
    private transient boolean recoveredFromBackup;

    public GameState(User user) { this.user = Objects.requireNonNull(user); }
    public User getUser() { return user; }
    public boolean isRecoveredFromBackup() { return recoveredFromBackup; }
    void markRecoveredFromBackup() { recoveredFromBackup = true; }

    /** Belum ada listener/katalog pada agregat Modul 1; memvalidasi akar hasil deserialisasi. */
    public void rewire() { Objects.requireNonNull(user, "Akun tidak ada dalam data simpan."); }
}
