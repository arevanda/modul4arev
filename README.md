# 🎟️ Aplikasi Pemesanan Tiket (Jetpack Compose)

Aplikasi Android sederhana yang dibangun menggunakan **Jetpack Compose** untuk mendemonstrasikan pengelolaan *state* (State Hoisting) pada antarmuka pengguna yang dinamis. Aplikasi ini menyimulasikan alur pemesanan tiket mulai dari input data, validasi, proses asinkron, hingga status keberhasilan.

---

## 📸 Tangkapan Layar (Screenshots)

| Halaman Awal | Validasi Error | Memproses Pesanan | Pesanan Berhasil |
| :---: | :---: | :---: | :---: |
| ![Halaman Awal](Screenshot%202026-09-24%20151358.png) | ![Validasi Error](Screenshot%202026-09-24%20151420.png) | ![Memproses Pesanan](Screenshot%202026-09-24%20151431.png) | ![Pesanan Berhasil](Screenshot%202026-09-24%20151436.png) |

---

## 🌟 Fitur Utama

- **Validasi Input Real-time:** Menampilkan peringatan warna merah jika pengguna mencoba memesan tiket tanpa mengisi nama.
- **Kontrol Kuantitas Tiket:** Pengguna dapat menambah atau mengurangi jumlah tiket (dengan batas minimal 1 tiket).
- **Simulasi Proses Asinkron:** Menampilkan indikator *loading* (proses selama 5 detik) dan menonaktifkan seluruh input saat pesanan sedang diproses.
- **Feedback Visual Dinamis:** Kotak status di bagian bawah layar secara otomatis berubah warna dan ikon menyesuaikan kondisi saat ini (Idle, Error, Processing, Success).

---

## 🛠️ Teknologi & Konsep Compose

Proyek ini berfokus pada penerapan praktik terbaik pengelolaan arsitektur UI di Jetpack Compose:

| Konsep Jetpack Compose | Implementasi dalam Proyek |
| :--- | :--- |
| **State Hoisting** | Memisahkan komponen pengelola *state* (`TicketBookingScreen` sebagai *Parent*) dari komponen UI yang pasif (`TicketBookingContent` sebagai *Child*). *Child* hanya menerima data dan mengirimkan *event* kembali ke *Parent*. |
| **rememberSaveable** | Menyimpan *state* input nama dan jumlah pesanan sehingga data tidak tereset atau hilang saat pengguna memutar layar perangkat (perubahan konfigurasi). |
| **LaunchedEffect** | Menangani efek samping (*side-effects*) dengan menjalankan *coroutine* secara aman. Digunakan untuk memicu penundaan (*delay*) 5 detik ketika status aplikasi berubah menjadi `PROCESSING`. |

---

## 🚀 Cara Menjalankan Aplikasi

1. Pastikan Anda telah menginstal **Android Studio** versi terbaru.
2. Buat proyek Jetpack Compose kosong (*Empty Compose Activity*).
3. Salin kode implementasi Composable ke dalam `MainActivity.kt`.
4. Jalankan aplikasi (klik tombol *Run* ▶️) pada emulator atau perangkat Android fisik.

---

## 📱 Alur Penggunaan (State Flow)

1. **Halaman Awal:** Pengguna melihat form kosong. Status di bawah menunjukkan "Silakan pesan tiket" (Warna Abu-abu).
2. **Validasi Error:** Jika tombol "Pesan Tiket" ditekan namun field nama kosong, status berubah menjadi "Nama Masih Kosong" (Warna Merah).
3. **Proses Memesan:** Setelah nama diisi dan tombol ditekan, tombol pesanan terkunci. Muncul indikator *loading* "Memproses pesanan........." (Warna Biru).
4. **Pesanan Berhasil:** Setelah penundaan 5 detik, proses selesai dan status berubah menjadi "Tiket telah dipesan" beserta ikon centang (Warna Hijau).
