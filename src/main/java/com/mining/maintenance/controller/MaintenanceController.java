package com.mining.maintenance.controller;

import com.mining.maintenance.model.Asset;
import com.mining.maintenance.model.DashboardSummary;
import com.mining.maintenance.model.MaintenanceRecord;
import com.mining.maintenance.service.AssetService;
import com.mining.maintenance.service.DashboardService;
import com.mining.maintenance.service.MaintenanceService;
import com.mining.maintenance.util.InputHelper;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class MaintenanceController {

    private final MaintenanceService maintenanceService;
    private final AssetService assetService;
    private final DashboardService dashboardService;


    // =========================
    // CONSTRUCTOR
    // =========================

    public MaintenanceController(
            MaintenanceService maintenanceService,
            AssetService assetService
    ) {

        this.maintenanceService =
                maintenanceService;

        this.assetService =
                assetService;

        this.dashboardService =
                new DashboardService(
                        assetService,
                        maintenanceService
                );
    }


    // =========================
    // ADD MAINTENANCE
    // =========================

    public void addMaintenance(
            Scanner scanner
    ) {

        System.out.println();

        System.out.println(
                "Add Maintenance"
        );


        System.out.print(
                "Asset Code: "
        );

        String assetCode =
                scanner.nextLine().trim();


        Asset asset =
                assetService.findByAssetCode(
                        assetCode
                );


        if (asset == null) {

            System.out.println(
                    "Asset not found."
            );

            return;
        }


        System.out.println();

        System.out.println(
                "Asset Code: "
                        + asset.getAssetCode()
        );

        System.out.println(
                "Asset Name: "
                        + asset.getAssetName()
        );


        System.out.println();


        // =========================
        // MAINTENANCE TYPE
        // =========================

        String maintenanceType;

        do {

            System.out.print(
                    "Maintenance Type: "
            );

            maintenanceType =
                    scanner.nextLine().trim();


            if (maintenanceType.isEmpty()) {

                System.out.println(
                        "Maintenance Type cannot be empty."
                );
            }

        } while (maintenanceType.isEmpty());


        // =========================
        // MAINTENANCE DATE
        // =========================

        String maintenanceDate;

        while (true) {

            System.out.print(
                    "Maintenance Date (YYYY-MM-DD): "
            );

            maintenanceDate =
                    scanner.nextLine().trim();


            try {

                java.sql.Date.valueOf(
                        maintenanceDate
                );

                break;

            } catch (
                    IllegalArgumentException e
            ) {

                System.out.println(
                        "Invalid date. Please use YYYY-MM-DD."
                );
            }
        }


        // =========================
        // DESCRIPTION
        // =========================

        String description;

        do {

            System.out.print(
                    "Description: "
            );

            description =
                    scanner.nextLine().trim();


            if (description.isEmpty()) {

                System.out.println(
                        "Description cannot be empty."
                );
            }

        } while (description.isEmpty());


        // =========================
        // TECHNICIAN
        // =========================

        String technician;

        do {

            System.out.print(
                    "Technician: "
            );

            technician =
                    scanner.nextLine().trim();


            if (technician.isEmpty()) {

                System.out.println(
                        "Technician cannot be empty."
                );
            }

        } while (technician.isEmpty());


        // =========================
        // COST
        // =========================

        double cost;

        while (true) {

            cost =
                    InputHelper.readDouble(
                            scanner,
                            "Cost: "
                    );


            if (cost < 0) {

                System.out.println(
                        "Cost cannot be negative."
                );

            } else {

                break;
            }
        }


        // =========================
        // STATUS
        // =========================

        String status;

        do {

            System.out.print(
                    "Status: "
            );

            status =
                    scanner.nextLine().trim();

            if (status.isEmpty()) {

                System.out.println(
                        "Status cannot be empty."
                );

            } else if (!maintenanceService.isValidStatus(status)) {

                System.out.println(
                        "Invalid status. Allowed: "
                                + String.join(
                                ", ",
                                MaintenanceService.VALID_STATUSES
                        )
                );

                status = "";
            }

        } while (status.isEmpty());


        // =========================
        // CREATE OBJECT
        // =========================

        MaintenanceRecord maintenance =
                new MaintenanceRecord(
                        0,
                        asset.getId(),
                        maintenanceType,
                        maintenanceDate,
                        description,
                        technician,
                        cost,
                        status
                );


        // =========================
        // SAVE
        // =========================

        boolean added =
                maintenanceService.addMaintenance(
                        maintenance
                );


        System.out.println();


        if (added) {

            System.out.println(
                    "Maintenance successfully added."
            );

        } else {

            System.out.println(
                    "Failed to add maintenance."
            );
        }
    }


    // =========================
    // VIEW MAINTENANCE
    // =========================

    public void viewMaintenance() {

        System.out.println();

        System.out.println(
                "Maintenance List"
        );


        List<MaintenanceRecord> records =
                maintenanceService.getAllMaintenance();


        if (records.isEmpty()) {

            System.out.println(
                    "No maintenance records found."
            );

            return;
        }


        for (
                MaintenanceRecord record
                : records
        ) {

            System.out.println();

            System.out.println(
                    "Maintenance ID: "
                            + record.getId()
            );

            System.out.println(
                    "Asset ID: "
                            + record.getAssetId()
            );

            System.out.println(
                    "Maintenance Type: "
                            + record.getMaintenanceType()
            );

            System.out.println(
                    "Maintenance Date: "
                            + record.getMaintenanceDate()
            );

            System.out.println(
                    "Description: "
                            + record.getDescription()
            );

            System.out.println(
                    "Technician: "
                            + record.getTechnician()
            );

            System.out.printf(
                    Locale.US,
                    "Cost: Rp %,.2f%n",
                    record.getCost()
            );

            System.out.println(
                    "Status: "
                            + record.getStatus()
            );
        }


        System.out.println();

        System.out.println(
                "Total Maintenance: "
                        + records.size()
        );
    }


    // =========================
    // MAINTENANCE HISTORY
    // =========================

    public void maintenanceHistoryByAsset(
            Scanner scanner
    ) {

        System.out.println();

        System.out.println(
                "Maintenance History by Asset"
        );


        System.out.print(
                "Asset Code: "
        );


        String assetCode =
                scanner.nextLine().trim();


        Asset asset =
                assetService.findByAssetCode(
                        assetCode
                );


        if (asset == null) {

            System.out.println(
                    "Asset not found."
            );

            return;
        }


        System.out.println();

        System.out.println(
                "Asset Code: "
                        + asset.getAssetCode()
        );

        System.out.println(
                "Asset Name: "
                        + asset.getAssetName()
        );


        List<MaintenanceRecord> records =
                maintenanceService
                        .getMaintenanceByAssetId(
                                asset.getId()
                        );


        if (records.isEmpty()) {

            System.out.println();

            System.out.println(
                    "No maintenance history found."
            );

            return;
        }


        for (
                MaintenanceRecord record
                : records
        ) {

            System.out.println();

            System.out.println(
                    "Maintenance ID: "
                            + record.getId()
            );

            System.out.println(
                    "Type: "
                            + record.getMaintenanceType()
            );

            System.out.println(
                    "Date: "
                            + record.getMaintenanceDate()
            );

            System.out.println(
                    "Description: "
                            + record.getDescription()
            );

            System.out.println(
                    "Technician: "
                            + record.getTechnician()
            );

            System.out.printf(
                    Locale.US,
                    "Cost: Rp %,.2f%n",
                    record.getCost()
            );

            System.out.println(
                    "Status: "
                            + record.getStatus()
            );
        }


        System.out.println();

        System.out.println(
                "Total Maintenance for Asset: "
                        + records.size()
        );
    }


    // =========================
    // TOTAL MAINTENANCE
    // =========================

    public void totalMaintenance() {

        int totalMaintenance =
                maintenanceService
                        .getTotalMaintenance();


        System.out.println();

        System.out.println(
                "Total Maintenance: "
                        + totalMaintenance
        );
    }


    // =========================
    // MAINTENANCE STATISTICS
    // =========================

    public void maintenanceStatistics() {

        int totalMaintenance =
                maintenanceService
                        .getTotalMaintenance();


        double totalCost =
                maintenanceService
                        .getTotalMaintenanceCost();


        int activeMaintenance =
                maintenanceService
                        .getMaintenanceCountByStatus(
                                "active"
                        );


        int completedMaintenance =
                maintenanceService
                        .getMaintenanceCountByStatus(
                                "completed"
                        );


        System.out.println();

        System.out.println(
                "Maintenance Statistics"
        );

        System.out.println(
                "-------------------------"
        );


        System.out.printf(
                Locale.US,
                "Total Maintenance Records : %d%n",
                totalMaintenance
        );


        System.out.printf(
                Locale.US,
                "Total Maintenance Cost    : Rp %,.2f%n",
                totalCost
        );


        System.out.printf(
                Locale.US,
                "Active Maintenance        : %d%n",
                activeMaintenance
        );


        System.out.printf(
                Locale.US,
                "Completed Maintenance     : %d%n",
                completedMaintenance
        );
    }


    // =========================
    // UPDATE MAINTENANCE
    // =========================

    public void updateMaintenance(
            Scanner scanner
    ) {

        System.out.println();

        System.out.println(
                "Update Maintenance"
        );


        int maintenanceId =
                InputHelper.readInt(
                        scanner,
                        "Maintenance ID: "
                );


        MaintenanceRecord maintenance =
                maintenanceService
                        .findMaintenanceById(
                                maintenanceId
                        );


        if (maintenance == null) {

            System.out.println(
                    "Maintenance not found."
            );

            return;
        }


        System.out.println();

        System.out.println(
                "Current Maintenance Data"
        );


        System.out.println(
                "Maintenance Type: "
                        + maintenance.getMaintenanceType()
        );

        System.out.println(
                "Maintenance Date: "
                        + maintenance.getMaintenanceDate()
        );

        System.out.println(
                "Description: "
                        + maintenance.getDescription()
        );

        System.out.println(
                "Technician: "
                        + maintenance.getTechnician()
        );

        System.out.printf(
                Locale.US,
                "Cost: Rp %,.2f%n",
                maintenance.getCost()
        );

        System.out.println(
                "Status: "
                        + maintenance.getStatus()
        );


        System.out.println();


        // =========================
        // TYPE
        // =========================

        String maintenanceType;

        do {

            System.out.print(
                    "New Maintenance Type: "
            );

            maintenanceType =
                    scanner.nextLine().trim();


            if (maintenanceType.isEmpty()) {

                System.out.println(
                        "Maintenance Type cannot be empty."
                );
            }

        } while (maintenanceType.isEmpty());


        // =========================
        // DATE
        // =========================

        String maintenanceDate;

        while (true) {

            System.out.print(
                    "New Maintenance Date (YYYY-MM-DD): "
            );

            maintenanceDate =
                    scanner.nextLine().trim();


            try {

                java.sql.Date.valueOf(
                        maintenanceDate
                );

                break;

            } catch (
                    IllegalArgumentException e
            ) {

                System.out.println(
                        "Invalid date. Please use YYYY-MM-DD."
                );
            }
        }


        // =========================
        // DESCRIPTION
        // =========================

        String description;

        do {

            System.out.print(
                    "New Description: "
            );

            description =
                    scanner.nextLine().trim();


            if (description.isEmpty()) {

                System.out.println(
                        "Description cannot be empty."
                );
            }

        } while (description.isEmpty());


        // =========================
        // TECHNICIAN
        // =========================

        String technician;

        do {

            System.out.print(
                    "New Technician: "
            );

            technician =
                    scanner.nextLine().trim();


            if (technician.isEmpty()) {

                System.out.println(
                        "Technician cannot be empty."
                );
            }

        } while (technician.isEmpty());


        // =========================
        // COST
        // =========================

        double cost;

        while (true) {

            cost =
                    InputHelper.readDouble(
                            scanner,
                            "New Cost: "
                    );


            if (cost < 0) {

                System.out.println(
                        "Cost cannot be negative."
                );

            } else {

                break;
            }
        }


        // =========================
        // STATUS
        // =========================

        String status;

        do {

            System.out.print(
                    "New Status: "
            );

            status =
                    scanner.nextLine().trim();

            if (status.isEmpty()) {

                System.out.println(
                        "Status cannot be empty."
                );

            } else if (!maintenanceService.isValidStatus(status)) {

                System.out.println(
                        "Invalid status. Allowed: "
                                + String.join(
                                ", ",
                                MaintenanceService.VALID_STATUSES
                        )
                );

                status = "";
            }

        } while (status.isEmpty());


        // =========================
        // CREATE UPDATED OBJECT
        // =========================

        MaintenanceRecord updatedMaintenance =
                new MaintenanceRecord(
                        maintenance.getId(),
                        maintenance.getAssetId(),
                        maintenanceType,
                        maintenanceDate,
                        description,
                        technician,
                        cost,
                        status
                );


        boolean updated =
                maintenanceService.updateMaintenance(
                        updatedMaintenance
                );


        System.out.println();


        if (updated) {

            System.out.println(
                    "Maintenance successfully updated."
            );

        } else {

            System.out.println(
                    "Failed to update maintenance."
            );
        }
    }


    // =========================
    // DELETE MAINTENANCE
    // =========================

    public void deleteMaintenance(
            Scanner scanner
    ) {

        System.out.println();

        System.out.println(
                "Delete Maintenance"
        );


        int maintenanceId =
                InputHelper.readInt(
                        scanner,
                        "Maintenance ID: "
                );


        MaintenanceRecord maintenance =
                maintenanceService
                        .findMaintenanceById(
                                maintenanceId
                        );


        if (maintenance == null) {

            System.out.println(
                    "Maintenance not found."
            );

            return;
        }


        System.out.println();

        System.out.println(
                "Maintenance Type: "
                        + maintenance.getMaintenanceType()
        );

        System.out.println(
                "Maintenance Date: "
                        + maintenance.getMaintenanceDate()
        );

        System.out.println(
                "Technician: "
                        + maintenance.getTechnician()
        );


        System.out.print(
                "Are you sure? (yes/no): "
        );


        String confirmation =
                scanner.nextLine().trim();


        if (
                !confirmation.equalsIgnoreCase(
                        "yes"
                )
        ) {

            System.out.println(
                    "Delete cancelled."
            );

            return;
        }


        boolean deleted =
                maintenanceService.deleteMaintenance(
                        maintenanceId
                );


        if (deleted) {

            System.out.println(
                    "Maintenance successfully deleted."
            );

        } else {

            System.out.println(
                    "Failed to delete maintenance."
            );
        }
    }


    // =========================
    // MAINTENANCE COST BY ASSET
    // =========================

    public void maintenanceCostByAsset(
            Scanner scanner
    ) {

        System.out.println();

        System.out.println(
                "Maintenance Cost by Asset"
        );

        System.out.println(
                "-------------------------"
        );


        System.out.print(
                "Asset Code: "
        );


        String assetCode =
                scanner.nextLine().trim();


        Asset asset =
                assetService.findByAssetCode(
                        assetCode
                );


        if (asset == null) {

            System.out.println(
                    "Asset not found."
            );

            return;
        }


        List<MaintenanceRecord> records =
                maintenanceService
                        .getMaintenanceByAssetId(
                                asset.getId()
                        );


        if (records.isEmpty()) {

            System.out.println();

            System.out.println(
                    "No maintenance records found for this asset."
            );

            return;
        }


        double totalCost = 0;


        for (
                MaintenanceRecord record
                : records
        ) {

            totalCost +=
                    record.getCost();
        }


        System.out.println();

        System.out.println(
                "Asset Code     : "
                        + asset.getAssetCode()
        );

        System.out.println(
                "Asset Name     : "
                        + asset.getAssetName()
        );

        System.out.println(
                "Maintenance Qty: "
                        + records.size()
        );

        System.out.printf(
                Locale.US,
                "Total Cost     : Rp %,.2f%n",
                totalCost
        );
    }


    // =========================
    // MAINTENANCE COST BY TYPE
    // =========================

    public void maintenanceCostByType() {

        System.out.println();

        System.out.println(
                "Maintenance Cost by Type"
        );

        System.out.println(
                "-------------------------"
        );


        Map<String, Double> costByType =
                maintenanceService
                        .getMaintenanceCostByType();


        if (costByType.isEmpty()) {

            System.out.println(
                    "No maintenance records found."
            );

            return;
        }


        double totalCost = 0;


        for (
                Map.Entry<String, Double> entry
                : costByType.entrySet()
        ) {

            System.out.printf(
                    Locale.US,
                    "%-30s : Rp %,.2f%n",
                    entry.getKey(),
                    entry.getValue()
            );


            totalCost +=
                    entry.getValue();
        }


        System.out.println(
                "-------------------------"
        );


        System.out.printf(
                Locale.US,
                "%-30s : Rp %,.2f%n",
                "Total Maintenance Cost",
                totalCost
        );
    }


    // =========================
    // DASHBOARD
    // Controller hanya menampilkan; semua hitungan ada di DashboardService.
    // =========================

    public void dashboard() {

        DashboardSummary d =
                dashboardService.buildSummary();

        String line =
                "----------------------------------------------";

        System.out.println();
        System.out.println("==============================================");
        System.out.println("       ASSET & MAINTENANCE DASHBOARD");
        System.out.println("==============================================");


        // ---------- ASSET SUMMARY ----------

        System.out.println();
        System.out.println("ASSET SUMMARY");
        System.out.println(line);

        System.out.println("Total Assets           : " + d.totalAssets());
        System.out.println("Active Assets          : " + d.activeAssets());
        System.out.println("Maintenance Assets     : " + d.maintenanceAssets());
        System.out.println("Inactive Assets        : " + d.inactiveAssets());

        System.out.printf(
                Locale.US,
                "Total Operating Hours  : %,.2f hours%n",
                d.totalOperatingHours()
        );

        System.out.printf(
                Locale.US,
                "Total Asset Value      : Rp %,.2f%n",
                d.totalAssetValue()
        );

        if (d.highestValueAsset() != null) {

            System.out.printf(
                    Locale.US,
                    "Highest Value Asset    : %s - %s (Rp %,.2f)%n",
                    d.highestValueAsset().getAssetCode(),
                    d.highestValueAsset().getAssetName(),
                    d.highestValueAsset().getPurchasePrice()
            );

        } else {

            System.out.println("Highest Value Asset    : -");
        }


        // ---------- MAINTENANCE SUMMARY ----------

        System.out.println();
        System.out.println("MAINTENANCE SUMMARY");
        System.out.println(line);

        System.out.println("Total Maintenance      : " + d.totalMaintenance());
        System.out.println("Active Maintenance     : " + d.activeMaintenance());
        System.out.println("Completed Maintenance  : " + d.completedMaintenance());

        System.out.printf(
                Locale.US,
                "Total Maintenance Cost : Rp %,.2f%n",
                d.totalMaintenanceCost()
        );

        if (d.highestCostAsset() != null) {

            System.out.printf(
                    Locale.US,
                    "Highest Maint. Cost    : %s (Rp %,.2f)%n",
                    d.highestCostAsset(),
                    d.highestCostAmount()
            );

        } else {

            System.out.println("Highest Maint. Cost    : -");
        }


        // ---------- KEY PERFORMANCE ----------

        System.out.println();
        System.out.println("KEY PERFORMANCE");
        System.out.println(line);

        System.out.printf(
                Locale.US,
                "Asset Active Rate      : %.2f%%%n",
                d.assetActiveRate()
        );

        System.out.printf(
                Locale.US,
                "Maintenance Completion : %.2f%%%n",
                d.maintenanceCompletionRate()
        );

        System.out.printf(
                Locale.US,
                "Average Operating Hrs  : %,.2f hours%n",
                d.averageOperatingHours()
        );

        System.out.printf(
                Locale.US,
                "Average Maintenance Cost: Rp %,.2f%n",
                d.averageMaintenanceCost()
        );


        // ---------- COST BY TYPE ----------

        System.out.println();
        System.out.println("MAINTENANCE COST BY TYPE");
        System.out.println(line);

        if (d.costByType().isEmpty()) {

            System.out.println("No maintenance data.");

        } else {

            for (Map.Entry<String, Double> entry
                    : d.costByType().entrySet()) {

                System.out.printf(
                        Locale.US,
                        "%-22s : Rp %,.2f%n",
                        entry.getKey(),
                        entry.getValue()
                );
            }
        }


        // ---------- TOP ASSETS BY COST ----------

        System.out.println();
        System.out.println("TOP ASSETS BY MAINTENANCE COST");
        System.out.println(line);

        if (d.topCostAssets().isEmpty()) {

            System.out.println("No maintenance data.");

        } else {

            int rank = 1;

            for (Map.Entry<String, Double> entry
                    : d.topCostAssets().entrySet()) {

                System.out.printf(
                        Locale.US,
                        "%d. %s%n   Rp %,.2f%n",
                        rank++,
                        entry.getKey(),
                        entry.getValue()
                );
            }
        }

        System.out.println();
        System.out.println("==============================================");
    }
}
