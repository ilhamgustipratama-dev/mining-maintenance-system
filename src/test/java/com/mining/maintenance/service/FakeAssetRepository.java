package com.mining.maintenance.service;

import com.mining.maintenance.model.Asset;
import com.mining.maintenance.repository.AssetRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Pengganti AssetRepository untuk testing: tidak menyentuh PostgreSQL
// sama sekali, semua data disimpan di memori (List).
class FakeAssetRepository extends AssetRepository {

    private final List<Asset> assets = new ArrayList<>();
    private final Map<Integer, Integer> maintenanceCountByAssetId = new HashMap<>();
    private int nextId = 1;

    @Override
    public boolean save(Asset asset) {

        asset.setId(nextId++);
        assets.add(asset);
        return true;
    }

    @Override
    public List<Asset> findAll() {

        return new ArrayList<>(assets);
    }

    @Override
    public List<Asset> search(String keyword) {

        return new ArrayList<>(assets);
    }

    @Override
    public Asset findByAssetCode(String assetCode) {

        for (Asset asset : assets) {

            if (asset.getAssetCode().equalsIgnoreCase(assetCode)) {

                return asset;
            }
        }

        return null;
    }

    @Override
    public Asset findById(int id) {

        for (Asset asset : assets) {

            if (asset.getId() == id) {

                return asset;
            }
        }

        return null;
    }

    @Override
    public boolean update(Asset asset) {

        for (int i = 0; i < assets.size(); i++) {

            if (assets.get(i).getId() == asset.getId()) {

                assets.set(i, asset);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean delete(int id) {

        return assets.removeIf(
                asset -> asset.getId() == id
        );
    }

    @Override
    public int count() {

        return assets.size();
    }

    @Override
    public int countByStatus(String status) {

        int count = 0;

        for (Asset asset : assets) {

            if (asset.getStatus().equalsIgnoreCase(status)) {

                count++;
            }
        }

        return count;
    }

    @Override
    public double getTotalOperatingHours() {

        double total = 0;

        for (Asset asset : assets) {

            total += asset.getOperatingHours();
        }

        return total;
    }

    @Override
    public int countMaintenanceByAssetId(int assetId) {

        return maintenanceCountByAssetId
                .getOrDefault(assetId, 0);
    }

    // Dipakai test untuk mensimulasikan asset yang sudah
    // punya riwayat maintenance (untuk tes deleteAsset).
    void setMaintenanceCount(int assetId, int count) {

        maintenanceCountByAssetId.put(assetId, count);
    }
}