package com.mining.maintenance.service;

import com.mining.maintenance.model.MaintenanceRecord;
import com.mining.maintenance.repository.MaintenanceRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Pengganti MaintenanceRepository untuk testing: tidak menyentuh
// PostgreSQL sama sekali, semua data disimpan di memori (List).
class FakeMaintenanceRepository extends MaintenanceRepository {

    private final List<MaintenanceRecord> records = new ArrayList<>();
    private int nextId = 1;

    @Override
    public boolean save(MaintenanceRecord maintenance) {

        maintenance.setId(nextId++);
        records.add(maintenance);
        return true;
    }

    @Override
    public List<MaintenanceRecord> findAll() {

        return new ArrayList<>(records);
    }

    @Override
    public MaintenanceRecord findById(int id) {

        for (MaintenanceRecord record : records) {

            if (record.getId() == id) {

                return record;
            }
        }

        return null;
    }

    @Override
    public List<MaintenanceRecord> findByAssetId(int assetId) {

        List<MaintenanceRecord> result = new ArrayList<>();

        for (MaintenanceRecord record : records) {

            if (record.getAssetId() == assetId) {

                result.add(record);
            }
        }

        return result;
    }

    @Override
    public boolean update(MaintenanceRecord maintenance) {

        for (int i = 0; i < records.size(); i++) {

            if (records.get(i).getId() == maintenance.getId()) {

                records.set(i, maintenance);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean delete(int id) {

        return records.removeIf(
                record -> record.getId() == id
        );
    }

    @Override
    public int countMaintenance() {

        return records.size();
    }

    @Override
    public double getTotalMaintenanceCost() {

        double total = 0;

        for (MaintenanceRecord record : records) {

            total += record.getCost();
        }

        return total;
    }

    @Override
    public int countMaintenanceByStatus(String status) {

        int count = 0;

        for (MaintenanceRecord record : records) {

            if (record.getStatus().equalsIgnoreCase(status)) {

                count++;
            }
        }

        return count;
    }

    // Catatan: repository asli menggabungkan (JOIN) ke tabel assets untuk
    // mendapatkan asset_code sebagai key. Fake ini tidak punya data asset,
    // jadi dipakai assetId sebagai key sementara. Belum dipakai test
    // manapun saat ini; akan dirapikan kalau DashboardServiceTest butuh ini.
    @Override
    public Map<String, Double> getMaintenanceCostByAsset(String assetCode) {

        return new LinkedHashMap<>();
    }

    @Override
    public Map<String, Double> getMaintenanceCostByType() {

        Map<String, Double> result = new LinkedHashMap<>();

        for (MaintenanceRecord record : records) {

            result.merge(
                    record.getMaintenanceType(),
                    record.getCost(),
                    Double::sum
            );
        }

        return result;
    }

    @Override
    public Map<String, Double> getMaintenanceCostPerAsset() {

        Map<String, Double> result = new LinkedHashMap<>();

        for (MaintenanceRecord record : records) {

            result.merge(
                    String.valueOf(record.getAssetId()),
                    record.getCost(),
                    Double::sum
            );
        }

        return result;
    }
}