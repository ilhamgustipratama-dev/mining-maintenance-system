package com.mining.maintenance.exception;

/**
 * Dilempar oleh Repository ketika operasi database gagal
 * (koneksi putus, constraint dilanggar, SQL salah, dll).
 *
 * Sebelumnya repository hanya mencetak error lalu mengembalikan
 * null / 0 / false, sehingga "database error" terlihat sama dengan
 * "data tidak ada". Sekarang kegagalan tidak bisa disembunyikan.
 */
public class DatabaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DatabaseException(
            String message,
            Throwable cause
    ) {

        super(message, cause);
    }
}
