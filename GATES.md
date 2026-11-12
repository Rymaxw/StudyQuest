# Gates: StudyQuest Modul 1

OWNS: src/main/java/com/studyquest/character/**, src/test/java/com/studyquest/**, src/main/java/com/studyquest/MainApp.java, src/main/java/com/studyquest/ui/**, src/main/resources/styles/theme.css, src/main/java/com/studyquest/system/**, src/main/java/com/studyquest/item/**, src/main/java/com/studyquest/battle/Monster.java, GATES.md

Scope: Implementasi Modul 1 sesuai panduan Revisi 1.3 tanpa mengubah signature TRD §7, implementasi modul lain, commit, atau push tanpa izin Max.

Prerequisites: Java 27 dan Maven. Max mengizinkan fondasi lintas modul untuk menjalankan Modul 1 dalam pesan lanjutan. Fitur penuh quest, inventory/shop, battle, achievement/statistik tetap milik modul terkait. Maven sesi berasal dari Apache Maven Central, arsip SHA-512 diperiksa sebelum ekstraksi; PATH sesi harus memuat folder bin Maven tersebut.

- [x] G1: Maven tersedia dan memakai Java 27
  CHECK: mvn -version
  EXPECT: /Java version: 27(?:[. ,]|$)/
  EVIDENCE: automatic-evidence=v1; definition-sha256=5e5ba7b3e25e4e5a839fba13331e08ec5aef0673c9aec27a4242a07050c81590; exit=0; EXPECT=matched; output-sha256=be6a0211c6df3148b5845e877ea9cffcac01b4c0ca1824b29e9388181280c492; output-bytes=392; shell=/bin/sh; cwd=/Users/jurymaxwell/College/Pemrograman Berbasis Objek/Tugas/StudyQuest; path=e28815cf6097/17 entries

- [x] G2: Test karakter dan autentikasi Modul 1 berhasil, termasuk seluruh kasus minimum panduan
  CHECK: mvn -Dtest='com.studyquest.character.*Test' -DfailIfNoTests=true test
  EXPECT: BUILD SUCCESS
  EVIDENCE: automatic-evidence=v1; definition-sha256=b18ed6ad68b2c65f952dbdee6834896edc1da08d00fdd61d3734d84f5d4ea843; exit=0; EXPECT=matched; output-sha256=6dca6c5386f942f850262f269867a55b9b57646c2462f083d5cc38bc141c59f6; output-bytes=3241; shell=/bin/sh; cwd=/Users/jurymaxwell/College/Pemrograman Berbasis Objek/Tugas/StudyQuest; path=e28815cf6097/17 entries

- [x] G3: Build bersih dan seluruh test repository berhasil dengan output asli tersedia untuk laporan
  CHECK: mvn clean test
  EXPECT: BUILD SUCCESS
  EVIDENCE: automatic-evidence=v1; definition-sha256=a87a3e7387e86fa2e905e45a13e75acdadbf8381ef62a3d0f694e045c42f2218; exit=0; EXPECT=matched; output-sha256=2038be63bb0d8d108b3925685738a333daaf2a6f95c1f9cea246771a4d3ae46a; output-bytes=5798; shell=/bin/sh; cwd=/Users/jurymaxwell/College/Pemrograman Berbasis Objek/Tugas/StudyQuest; path=e28815cf6097/17 entries

- [x] G4: Form akun, karakter, profil, rute klik markas, dan simpan saat tutup jendela berhasil pada jendela JavaFX nyata
  CHECK: mvn -Dtest=ModuleOneUiTest -DfailIfNoTests=true test
  EXPECT: BUILD SUCCESS
  EVIDENCE: automatic-evidence=v1; definition-sha256=45898eb4c1c766ec3d01a555a078ddf600020a1ac5e11e6b003eccfe7a8ed1e1; exit=0; EXPECT=matched; output-sha256=2f89513142271ee4d5916371545da6b78547edea886ca4451be014f9e3c183be; output-bytes=4453; shell=/bin/sh; cwd=/Users/jurymaxwell/College/Pemrograman Berbasis Objek/Tugas/StudyQuest; path=e28815cf6097/17 entries

- [ ] G6: Input berjalan, tabrakan, enam interaksi E setara klik, dan berhenti saat kehilangan fokus/panel/dialog terbukti pada desktop dengan fokus OS aktif
  EVIDENCE: Bukti parsial dari Max: "aku udah uji coba ko awsd dan e aman". Gerak WASD dan interaksi E diterima sebagai uji manual pengguna, dicatat pada G9. Belum ada konfirmasi tombol panah, tabrakan, kesetaraan E/klik pada keenam objek, Tab/Enter, serta penghentian gerak saat panel/dialog aktif atau jendela kehilangan fokus. Gate lengkap belum terpenuhi.

- [x] G9: Gerak WASD dan interaksi E berfungsi dalam uji manual Max
  EVIDENCE: Max mengonfirmasi langsung dalam percakapan setelah mencoba aplikasi: "aku udah uji coba ko awsd dan e aman". Bukti manual ini hanya mencakup WASD dan E, bukan kelulusan ulang test otomatis atau seluruh skenario G6.

- [x] G5: Perubahan hanya pada file yang diizinkan, kontrak yang diimplementasikan tetap, serialisasi dan aturan bisnis sesuai dokumen dengan interpretasi rumus dilaporkan, serta tidak ada commit atau push tanpa izin
  EVIDENCE: Branch feature/character-account-shell; pemeriksaan file produksi membatasi perubahan pada Modul 1, theme.css, dan fondasi yang diizinkan Max. Method TRD yang diimplementasikan mempertahankan parameter dan return type; kontrak fitur modul 2 sampai 5 tidak diklaim lengkap. Test serialization, hash PBKDF2, stat, XP, skill, save failure, recovery dan repository lulus. PiercingShot 2x total, pembulatan damage ke bawah, serta HP equipment mengubah maxHp tanpa heal dicatat sebagai interpretasi untuk review Max. Tidak ada commit/push dijalankan.

- [ ] G7: Markas memakai aset final yang disetujui Max dan terhubung dengan panel serta agregat data tim, termasuk evaluasi overdue sebelum ringkasan dan menyerah saat tutup battle
  EVIDENCE: pending

ABANDON: G6 Fokus jendela desktop macOS tidak konsisten diberikan kepada JVM test. WASD dan E sudah dikonfirmasi manual oleh Max (G9), tetapi hasil tombol panah, tabrakan, keenam rute E/klik, Tab/Enter, panel/dialog dan focus-loss masih perlu dikonfirmasi. Test otomatis yang skipped tetap tidak dihitung lulus; gate input lengkap tetap handoff.
ABANDON: G7 Aset final membutuhkan pilihan/persetujuan Max; agregat quest/shop/tracker/stats, panel S05 sampai S12 dan BattleEngine belum tersedia dari tim. Fondasi Modul 1 berjalan mandiri, tetapi integrasi lengkap dan O-STYLE final adalah handoff, bukan selesai.

## Keputusan dan batas integrasi

### Cleanup keterbacaan yang disetujui Max

- [x] G8: Kode produksi memakai nama lokal deskriptif, blok aksi multi-langkah terbaca, dan komentar menjelaskan kontrak atau alasan yang tidak terlihat dari kode
  EVIDENCE: Cleanup 17 file produksi dibanding snapshot sebelum edit. c/s/u/p menjadi character/skill/enteredUsername/enteredPassword; remainingXp, candidateState, expectedHash, dataPath dan primaryFailure menjelaskan nilai. HubView mengekstrak handler simpan/keluar/input; repository memakai isValidSave/replaceSave. Blok multi-aksi dipecah, getter sederhana tetap ringkas. Komentar berulang Monster/Router dihapus; alasan backup terbaca melalui method; Javadoc TRD dan catatan batas integrasi dipertahankan. Test dan resource tidak berubah; mvn clean test: 27 total, 0 failures, 0 errors, 1 skipped (fokus OS), BUILD SUCCESS.

- Verifikasi cleanup memakai ulang G2/G3/G4 setelah source berubah. Signature publik dan field serialisasi tetap dipertahankan. Konfirmasi manual Max untuk WASD dan E dicatat pada G9; tidak diperluas menjadi bukti semua skenario input G6.

- Izin fondasi diterima: Inventory, Item, Monster, DataRepository, GameState, FileDataRepository, dan GameController seperlunya untuk Modul 1.
- GameState awal hanya agregat akun/karakter/inventory; anggota modul lain wajib menambahkan agregat dan rewire listener/katalog saat tersedia. Tidak mengklaim skema penuh BSD selesai.
- S05 sampai S12 belum tersedia: Router memberi informasi belum tersedia, bukan panel fitur palsu. Evaluasi keterlambatan dan penanganan battle pada tutup jendela menunggu integrasi modul 2/4/5.
- UI memakai placeholder geometris berlabel dan font sistem; belum memakai aset pixel final, logo, atau font berlisensi eksternal.
- PiercingShot ditafsirkan sebagai 2 x ATK total, critical terjamin, tanpa pengali critical tambahan. Akan dilaporkan ke Max untuk review rumus yang kurang eksplisit.
- Damage pecahan dibulatkan ke bawah karena kontrak mengembalikan int. modifyStat(HP) mengubah maxHp dan menjepit hp, bukan heal gratis. Max perlu meninjau dua rincian ini bersama pemilik equipment/battle.
- Lingkungan test UI membutuhkan desktop JavaFX aktif; tidak ditambahkan dependensi headless/TestFX atau Swing. Robot mencoba fokus jendela; bila OS menolak, satu test gerak ditandai skipped dan tidak dihitung sebagai bukti gate G6.
- Delivery gate UI: kontras pasangan teks terukur 6,48 sampai 14,92; form punya label, status proses/error, kontrol kembali, dan placeholder jujur. Interaksi form/rute klik/simpan teruji melalui JavaFX Button.fire pada Stage nyata, bukan seluruhnya klik mouse fisik. WASD dan E dikonfirmasi lewat uji manual Max; skenario input lainnya dan penilaian visual final masih handoff.
