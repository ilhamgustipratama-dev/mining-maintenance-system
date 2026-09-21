package com.mining.maintenance.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Nilai bawaan untuk development lokal (bukan rahasia).
    private static final String DEFAULT_URL =
            "jdbc:postgresql://localhost:5432/mining_maintenance";

    private static final String DEFAULT_USER =
            "postgres";

    public static Connection getConnection() throws SQLException {

        String url = getEnv("DB_URL", DEFAULT_URL);
        String user = getEnv("DB_USER", DEFAULT_USER);

        // Password sengaja TIDAK punya nilai bawaan.
        String password = System.getenv("DB_PASSWORD");

        if (password == null || password.isBlank()) {

            throw new SQLException(
                    "Environment variable DB_PASSWORD belum diset."
            );
        }

        return DriverManager.getConnection(url, user, password);
    }

    // Membaca environment variable; kalau kosong pakai nilai bawaan.
    private static String getEnv(
            String name,
            String defaultValue
    ) {

        String value = System.getenv(name);

        if (value == null || value.isBlank()) {

            return defaultValue;
        }

        return value;
    }

    public static void main(String[] args) {

        try (Connection connection = getConnection()) {

            System.out.println("Database connected successfully!");

        } catch (SQLException e) {

            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}