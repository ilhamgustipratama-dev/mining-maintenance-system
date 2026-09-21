-- Mining Asset & Maintenance Management System
-- Database schema (PostgreSQL 18)
--
-- Cara pakai:
-- 1. Buat database kosong bernama: mining_maintenance
-- 2. Jalankan file ini di database tersebut (pgAdmin Query Tool atau psql)

CREATE TABLE IF NOT EXISTS public.assets (
                                             id              SERIAL PRIMARY KEY,
                                             asset_code      VARCHAR(50)  NOT NULL UNIQUE,
    asset_name      VARCHAR(100) NOT NULL,
    category        VARCHAR(100),
    type            VARCHAR(100),
    brand           VARCHAR(100),
    model           VARCHAR(100),
    serial_number   VARCHAR(100),
    purchase_price  NUMERIC(15,2),
    status          VARCHAR(50),
    location        VARCHAR(100),
    purchase_date   DATE,
    operating_hours NUMERIC(12,2),
    description     TEXT
    );

CREATE TABLE IF NOT EXISTS public.maintenance_records (
                                                          id               SERIAL PRIMARY KEY,
                                                          asset_id         INTEGER      NOT NULL,
                                                          maintenance_type VARCHAR(50)  NOT NULL,
    maintenance_date DATE         NOT NULL,
    description      TEXT,
    technician       VARCHAR(100),
    cost             NUMERIC(15,2) DEFAULT 0,
    status           VARCHAR(30)  NOT NULL,
    CONSTRAINT fk_maintenance_asset
    FOREIGN KEY (asset_id)
    REFERENCES public.assets(id)
    ON DELETE RESTRICT
    );