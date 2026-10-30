# StudyQuest - Aplikasi Manajemen Tugas Kuliah Berbasis Gamifikasi RPG

Proyek Tugas Besar Pemrograman Berorientasi Objek (PBO)
Teknik Informatika / Sains Data - Telkom University Purwokerto

## Anggota Kelompok & Pembagian Modul

| No | Anggota | Peran / Modul | Package | Konsep OOP Utama |
|---|---|---|---|---|
| **1** | **Jury Maxwell (103132400023)** | **Modul 1: Karakter & Akun** | `com.studyquest.character` | Abstract Class, Inheritance, Interface, Template Method |
| 2 | Anggota 2 | Modul 2: Manajemen Quest | `com.studyquest.quest` | Abstract Class, Polymorphism, Factory, Observer (Publisher) |
| 3 | Anggota 3 | Modul 3: Inventory & Shop | `com.studyquest.item` | Interface Ganda, Composition, Custom Exception |
| 4 | Anggota 4 | Modul 4: Boss Battle | `com.studyquest.battle` | Abstract Class, Polymorphism, State Machine, Factory |
| 5 | Anggota 5 | Modul 5: Achievement, Statistik & Data | `com.studyquest.system` | Interface, Observer (Listener), Repository Pattern, Serialization |

---

## Struktur Direktori

```text
StudyQuest/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── studyquest/
│   │   │           ├── MainApp.java
│   │   │           ├── character/  <-- Modul 1 (Jury Maxwell)
│   │   │           ├── quest/      <-- Modul 2 (Anggota 2)
│   │   │           ├── item/       <-- Modul 3 (Anggota 3)
│   │   │           ├── battle/     <-- Modul 4 (Anggota 4)
│   │   │           ├── system/     <-- Modul 5 (Anggota 5)
│   │   │           └── ui/         <-- Panel JavaFX (Dikerjakan bersama per modul)
│   │   └── resources/
│   └── test/
│       └── java/
│           └── com/
│               └── studyquest/
│                   └── StudyQuestTest.java
```

---

## Panduan Kerja Tim (Workflow Git)

1. **Clone Repository:**
   ```bash
   git clone https://github.com/Rymaxw/StudyQuest.git
   cd StudyQuest
   ```

2. **Buat Branch Sendiri:**
   - Anggota 1: `feature/character-auth`
   - Anggota 2: `feature/quest-management`
   - Anggota 3: `feature/inventory-shop`
   - Anggota 4: `feature/boss-battle`
   - Anggota 5: `feature/achievement-data`

3. **Kompilasi & Uji Coba:**
   ```bash
   mvn clean compile
   mvn test
   mvn exec:java -Dexec.mainClass="com.studyquest.MainApp"
   ```

4. **Kirim Perubahan (Pull Request):**
   Setelah fitur dan test berjalan hijau, push branch ke GitHub dan ajukan Pull Request ke `main` untuk direview oleh Ketua.
