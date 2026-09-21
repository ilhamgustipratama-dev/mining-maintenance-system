package com.mining.maintenance.service;

import com.mining.maintenance.model.Asset;
import com.mining.maintenance.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    // Status asset yang sah (disimpan dalam huruf kecil)
    public static final List<String> VALID_STATUSES =
            List.of("active", "maintenance", "inactive");


    // =========================
    // CONSTRUCTOR
    // =========================

    public AssetService() {

        this(new AssetRepository());
    }

    // Constructor ini memudahkan testing: repository bisa diganti
    // dengan versi palsu tanpa database (dipakai di Tahap 4).
    // @Autowired memberi tahu Spring untuk memakai constructor ini
    // saat membuat AssetService secara otomatis.
    @Autowired
    public AssetService(
            AssetRepository assetRepository
    ) {

        this.assetRepository =
                assetRepository;
    }


    // =========================
    // ADD ASSET
    // =========================

    public boolean addAsset(
            Asset asset
    ) {

        if (validateAsset(asset) != null) {

            return false;
        }

        normalizeAsset(asset);

        return assetRepository.save(
                asset
        );
    }


    // =========================
    // GET ALL ASSETS
    // =========================

    public List<Asset> getAllAssets() {

        return assetRepository.findAll();
    }


    // =========================
    // SEARCH ASSETS
    // =========================

    public List<Asset> searchAssets(
            String keyword
    ) {

        if (
                keyword == null ||
                        keyword.trim().isEmpty()
        ) {

            return List.of();
        }

        return assetRepository.search(
                keyword.trim()
        );
    }


    // =========================
    // FIND BY ASSET CODE
    // =========================

    public Asset findByAssetCode(
            String assetCode
    ) {

        return assetRepository.findByAssetCode(
                assetCode
        );
    }


    // =========================
    // FIND BY ID
    // =========================

    public Asset findById(
            int id
    ) {

        return assetRepository.findById(
                id
        );
    }


    // =========================
    // ASSET CODE EXISTS
    // =========================

    public boolean assetCodeExists(
            String assetCode
    ) {

        if (
                assetCode == null ||
                        assetCode.trim().isEmpty()
        ) {

            return false;
        }

        return assetRepository.findByAssetCode(
                assetCode.trim()
        ) != null;
    }


    // =========================
    // UPDATE ASSET
    // =========================

    public boolean updateAsset(
            Asset asset
    ) {

        if (validateAsset(asset) != null) {

            return false;
        }

        normalizeAsset(asset);

        return assetRepository.update(
                asset
        );
    }


    // =========================
    // DELETE ASSET
    // =========================

    public boolean deleteAsset(
            int id
    ) {

        // Riwayat maintenance tidak boleh hilang diam-diam.
        if (countMaintenanceRecords(id) > 0) {

            return false;
        }

        return assetRepository.delete(
                id
        );
    }


    // =========================
    // COUNT MAINTENANCE RECORDS OF ASSET
    // =========================

    public int countMaintenanceRecords(
            int assetId
    ) {

        return assetRepository
                .countMaintenanceByAssetId(
                        assetId
                );
    }


    // =========================
    // TOTAL ASSETS
    // =========================

    public int getTotalAssets() {

        return assetRepository.count();
    }


    // =========================
    // ACTIVE ASSETS
    // =========================

    public int getActiveAssets() {

        return assetRepository.countByStatus(
                "active"
        );
    }


    // =========================
    // MAINTENANCE ASSETS
    // =========================

    public int getMaintenanceAssets() {

        return assetRepository.countByStatus(
                "maintenance"
        );
    }


    // =========================
    // INACTIVE ASSETS
    // =========================

    public int getInactiveAssets() {

        return assetRepository.countByStatus(
                "inactive"
        );
    }


    // =========================
    // TOTAL OPERATING HOURS
    // =========================

    public double getTotalOperatingHours() {

        return assetRepository.getTotalOperatingHours();
    }


    // =========================
    // TOTAL ASSET VALUE
    // =========================

    public double getTotalAssetValue() {

        List<Asset> assets =
                assetRepository.findAll();

        double total = 0;

        for (
                Asset asset : assets
        ) {

            total += asset.getPurchasePrice();
        }

        return total;
    }


    // =========================
    // AVERAGE ASSET VALUE
    // =========================

    public double getAverageAssetValue() {

        int totalAssets =
                getTotalAssets();

        if (totalAssets == 0) {

            return 0;
        }

        return getTotalAssetValue()
                / totalAssets;
    }


    // =========================
    // ACTIVE ASSET VALUE
    // =========================

    public double getActiveAssetValue() {

        List<Asset> assets =
                assetRepository.findAll();

        double total = 0;

        for (
                Asset asset : assets
        ) {

            if (
                    asset.getStatus()
                            .equalsIgnoreCase(
                                    "active"
                            )
            ) {

                total += asset.getPurchasePrice();
            }
        }

        return total;
    }


    // =========================
    // MAINTENANCE ASSET VALUE
    // =========================

    public double getMaintenanceAssetValue() {

        List<Asset> assets =
                assetRepository.findAll();

        double total = 0;

        for (
                Asset asset : assets
        ) {

            if (
                    asset.getStatus()
                            .equalsIgnoreCase(
                                    "maintenance"
                            )
            ) {

                total += asset.getPurchasePrice();
            }
        }

        return total;
    }


    // =========================
    // INACTIVE ASSET VALUE
    // =========================

    public double getInactiveAssetValue() {

        List<Asset> assets =
                assetRepository.findAll();

        double total = 0;

        for (
                Asset asset : assets
        ) {

            if (
                    asset.getStatus()
                            .equalsIgnoreCase(
                                    "inactive"
                            )
            ) {

                total += asset.getPurchasePrice();
            }
        }

        return total;
    }


    // =========================
    // VALIDATION
    // =========================

    public boolean isValidStatus(
            String status
    ) {

        if (status == null) {

            return false;
        }

        return VALID_STATUSES.contains(
                status.trim().toLowerCase()
        );
    }


    // Mengembalikan pesan error, atau null jika data valid.
    // Dipakai untuk add DAN update: asset dengan id yang sama
    // dengan pemilik kode tidak dianggap duplikat.

    public String validateAsset(
            Asset asset
    ) {

        if (asset == null) {

            return "Asset data is required.";
        }

        if (isBlank(asset.getAssetCode())) {

            return "Asset Code cannot be empty.";
        }

        if (isBlank(asset.getAssetName())) {

            return "Asset Name cannot be empty.";
        }

        if (asset.getPurchasePrice() < 0) {

            return "Purchase Price cannot be negative.";
        }

        if (asset.getOperatingHours() < 0) {

            return "Operating Hours cannot be negative.";
        }

        if (!isValidStatus(asset.getStatus())) {

            return "Invalid status. Allowed: "
                    + String.join(
                    ", ",
                    VALID_STATUSES
            );
        }

        try {

            LocalDate.parse(
                    asset.getPurchaseDate().trim()
            );

        } catch (
                DateTimeParseException
                | NullPointerException e
        ) {

            return "Invalid purchase date. Use YYYY-MM-DD.";
        }

        Asset owner =
                assetRepository.findByAssetCode(
                        asset.getAssetCode().trim()
                );

        if (
                owner != null
                        && owner.getId() != asset.getId()
        ) {

            return "Asset Code already exists: "
                    + asset.getAssetCode().trim();
        }

        return null;
    }


    private boolean isBlank(
            String value
    ) {

        return value == null
                || value.trim().isEmpty();
    }


    private void normalizeAsset(
            Asset asset
    ) {

        asset.setAssetCode(
                asset.getAssetCode().trim()
        );

        asset.setAssetName(
                asset.getAssetName().trim()
        );

        asset.setStatus(
                asset.getStatus().trim().toLowerCase()
        );
    }


    // =========================
    // HIGHEST VALUE ASSET
    // =========================

    public Asset getHighestValueAsset() {

        Asset highest = null;

        for (
                Asset asset : assetRepository.findAll()
        ) {

            if (
                    highest == null
                            || asset.getPurchasePrice()
                            > highest.getPurchasePrice()
            ) {

                highest = asset;
            }
        }

        return highest;
    }
}