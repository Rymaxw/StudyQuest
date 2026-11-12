package com.studyquest.system;

import java.io.IOException;

public interface DataRepository {
    /** Simpan seluruh sesi atau laporkan kegagalan; jangan laporkan sukses sebelum selesai. */
    void save(GameState state) throws IOException;
    /** Muat akun yang diminta; kegagalan tidak membuat atau menimpa akun. */
    GameState load(String username) throws IOException;
    /** Periksa keberadaan akun lokal dengan username tervalidasi. */
    boolean exists(String username);
}
