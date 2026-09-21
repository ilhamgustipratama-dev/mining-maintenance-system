package com.mining.maintenance.service;

import com.mining.maintenance.model.MaintenanceRecord;
import com.mining.maintenance.repository.MaintenanceRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    // Status maintenance yang sah (disimpan dalam huruf kecil).
    // Diambil dari data aktual: SELECT DISTINCT status FROM maintenance_records
    // menghasilkan hanya "active" dan "completed".
    public static final List<String> VALID_STATUSES =
            List.of("active", "completed");


    // =========================
    // CONSTRUCTOR
    // =========================

    public MaintenanceService() {

        this(new MaintenanceRepository());
    }

    // Untuk testing: repository bisa diganti versi palsu.
    public MaintenanceService(
            MaintenanceRepository maintenanceRepository
    ) {

        this.maintenanceRepository =
                maintenanceRepository;
    }


    // =========================
    // ADD
    // =========================

    public boolean addMaintenance(
            MaintenanceRecord maintenance
    ) {

        if (validateMaintenance(maintenance) != null) {

            return false;
        }

        normalizeMaintenance(maintenance);

        return maintenanceRepository.save(
                maintenance
        );
    }


    // =========================
    // GET ALL
    // =========================

    public List<MaintenanceRecord>
    getAllMaintenance() {

        return maintenanceRepository.findAll();
    }


    // =========================
    // FIND BY ID
    // =========================

    public MaintenanceRecord
    findMaintenanceById(
            int id
    ) {

        return maintenanceRepository.findById(
                id
        );
    }


    // =========================
    // FIND BY ASSET
    // =========================

    public List<MaintenanceRecord>
    getMaintenanceByAssetId(
            int assetId
    ) {

        return maintenanceRepository.findByAssetId(
                assetId
        );
    }


    // =========================
    // UPDATE
    // =========================

    public boolean updateMaintenance(
            MaintenanceRecord maintenance
    ) {

        if (validateMaintenance(maintenance) != null) {

            return false;
        }

        normalizeMaintenance(maintenance);

        return maintenanceRepository.update(
                maintenance
        );
    }


    // =========================
    // DELETE
    // =========================

    public boolean deleteMaintenance(
            int id
    ) {

        return maintenanceRepository.delete(
                id
        );
    }


    // =========================
    // TOTAL
    // =========================

    public int getTotalMaintenance() {

        return maintenanceRepository
                .countMaintenance();
    }


    // =========================
    // TOTAL COST
    // =========================

    public double getTotalMaintenanceCost() {

        return maintenanceRepository
                .getTotalMaintenanceCost();
    }


    // =========================
    // COUNT BY STATUS
    // =========================

    public int getMaintenanceCountByStatus(
            String status
    ) {

        return maintenanceRepository
                .countMaintenanceByStatus(
                        status
                );
    }


    // =========================
    // COST BY ASSET
    // =========================

    public Map<String, Double>
    getMaintenanceCostByAsset(
            String assetCode
    ) {

        return maintenanceRepository
                .getMaintenanceCostByAsset(
                        assetCode
                );
    }


    // =========================
    // COST BY TYPE
    // =========================

    public Map<String, Double>
    getMaintenanceCostByType() {

        return maintenanceRepository
                .getMaintenanceCostByType();
    }


    // =========================
    // COST PER ASSET (terbesar dulu)
    // =========================

    public Map<String, Double>
    getMaintenanceCostPerAsset() {

        return maintenanceRepository
                .getMaintenanceCostPerAsset();
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
    // Dipakai untuk add DAN update.

    public String validateMaintenance(
            MaintenanceRecord maintenance
    ) {

        if (maintenance == null) {

            return "Maintenance data is required.";
        }

        if (maintenance.getAssetId() <= 0) {

            return "Asset is required.";
        }

        if (isBlank(maintenance.getMaintenanceType())) {

            return "Maintenance Type cannot be empty.";
        }

        try {

            LocalDate.parse(
                    maintenance.getMaintenanceDate().trim()
            );

        } catch (
                DateTimeParseException
                | NullPointerException e
        ) {

            return "Invalid maintenance date. Use YYYY-MM-DD.";
        }

        if (isBlank(maintenance.getDescription())) {

            return "Description cannot be empty.";
        }

        if (isBlank(maintenance.getTechnician())) {

            return "Technician cannot be empty.";
        }

        if (maintenance.getCost() < 0) {

            return "Cost cannot be negative.";
        }

        if (!isValidStatus(maintenance.getStatus())) {

            return "Invalid status. Allowed: "
                    + String.join(
                    ", ",
                    VALID_STATUSES
            );
        }

        return null;
    }


    private boolean isBlank(
            String value
    ) {

        return value == null
                || value.trim().isEmpty();
    }


    private void normalizeMaintenance(
            MaintenanceRecord maintenance
    ) {

        maintenance.setMaintenanceType(
                maintenance.getMaintenanceType().trim()
        );

        maintenance.setDescription(
                maintenance.getDescription().trim()
        );

        maintenance.setTechnician(
                maintenance.getTechnician().trim()
        );

        maintenance.setStatus(
                maintenance.getStatus().trim().toLowerCase()
        );
    }
}