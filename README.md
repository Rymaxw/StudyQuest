# StudyQuest - Aplikasi Manajemen Tugas Kuliah Berbasis Gamifikasi RPG

Proyek Tugas Besar Pemrograman Berorientasi Objek (PBO)
Program Studi S1 Sains Data - Telkom University Purwokerto

## Anggota Kelompok & Pembagian Modul

| No | Nama Lengkap | NIM | Peran / Modul | Package |
|---|---|---|---|---|
| **1** | **Jury Maxwell** *(Ketua)* | 103132400023 | Modul 1: Karakter & Akun | `com.studyquest.character` |
| 2 | Muhammad 'Izzuddin Nabil | 103132400032 | Modul 2: Manajemen Quest | `com.studyquest.quest` |
| 3 | Farrel Labib Alhaidar | 103132400009 | Modul 3: Inventory & Shop | `com.studyquest.item` |
| 4 | Immanuel Raditya Deo Pratama | 103132430001 | Modul 4: Boss Battle | `com.studyquest.battle` |
| 5 | Jeremy Marcello Waani | 2311110003 | Modul 5: Achievement, Statistik & Data | `com.studyquest.system` |

---

## Struktur Direktori Awal

```text
StudyQuest/
├── pom.xml
├── README.md
├── Docs/                   <-- PRD, TRD, AFD, DBD, BSD, IPD, dan Panduan Role per Modul
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── studyquest/
│   │   │           ├── character/  <-- Modul 1 (Max)
│   │   │           ├── quest/      <-- Modul 2 (Nabil)
│   │   │           ├── item/       <-- Modul 3 (Farrel)
│   │   │           ├── battle/     <-- Modul 4 (Deo)
│   │   │           ├── system/     <-- Modul 5 (Marcell)
│   │   │           └── ui/         <-- Komponen tampilan JavaFX
│   │   └── resources/
│   │       ├── assets/
│   │       └── styles/
│   └── test/
│       └── java/
│           └── com/
│               └── studyquest/
```

---

## Panduan Kerja Tim (Workflow Git)

1. **Clone Repository:**
   ```bash
   git clone https://github.com/Rymaxw/StudyQuest.git
   cd StudyQuest
   ```

2. **Baca Acuan Dokumentasi:**
   Mulai dari `Docs/7. Panduan Role per Modul - StudyQuest.md`, bagian "Aturan bersama" dan bagian modulmu. Bagian itu menunjuk bagian PRD, TRD, AFD, DBD, BSD, dan IPD yang perlu dibaca. Kalau memakai coding agent, salin prompt di akhir bagian modulmu.

   Acuan terbaru: Revisi 1.3. Data disimpan lokal, pelemahan boss hanya mengurangi HP, dan aturan Daily serta klaim hadiah telah disetujui. Max memegang `MainApp`, `Router`, `HubView`, dan `theme.css`; Marcell memegang kerangka `GameController`. Detailnya ada di PRD §14 dan panduan role.

3. **Buat Branch Sendiri:**
   Jangan pernah push langsung ke branch `main`. Setiap anggota bekerja pada branch modulnya masing-masing lalu mengajukan Pull Request ke `main`.
