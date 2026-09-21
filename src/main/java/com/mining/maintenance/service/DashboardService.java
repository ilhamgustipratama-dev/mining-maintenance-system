package com.mining.maintenance.service;

import com.mining.maintenance.model.Asset;
import com.mining.maintenance.model.DashboardSummary;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Business logic dashboard: mengambil data dari AssetService dan
 * MaintenanceService, lalu menghitung KPI. Controller hanya menampilkan.
 */
public class DashboardService {

    private static final int TOP_ASSET_LIMIT = 5;

    private final AssetService assetService;
    private final MaintenanceService maintenanceService;


    public DashboardService(
            AssetService assetService,
            MaintenanceService maintenanceService
    ) {

        this.assetService = assetService;
        this.maintenanceService = maintenanceService;
    }


    public DashboardSummary buildSummary() {

        // ---------- ASSET ----------

        int totalAssets =
                assetService.getTotalAssets();

        int activeAssets =
                assetService.getActiveAssets();

        double totalOperatingHours =
                assetService.getTotalOperatingHours();

        List<Asset> assets =
                assetService.getAllAssets();

        double totalAssetValue = 0;
        Asset highestValueAsset = null;

        for (Asset asset : assets) {

            totalAssetValue += asset.getPurchasePrice();

            if (
                    highestValueAsset == null
                            || asset.getPurchasePrice()
                            > highestValueAsset.getPurchasePrice()
            ) {

                highestValueAsset = asset;
            }
        }

        // ---------- MAINTENANCE ----------

        int totalMaintenance =
                maintenanceService.getTotalMaintenance();

        int completedMaintenance =
                maintenanceService
                        .getMaintenanceCountByStatus("completed");

        double totalMaintenanceCost =
                maintenanceService.getTotalMaintenanceCost();

        Map<String, Double> costPerAsset =
                maintenanceService.getMaintenanceCostPerAsset();

        String highestCostAsset = null;
        double highestCostAmount = 0;
        Map<String, Double> topCostAssets =
                new LinkedHashMap<>();

        for (Map.Entry<String, Double> entry
                : costPerAsset.entrySet()) {

            if (highestCostAsset == null) {

                highestCostAsset = entry.getKey();
                highestCostAmount = entry.getValue();
            }

            if (topCostAssets.size() < TOP_ASSET_LIMIT) {

                topCostAssets.put(
                        entry.getKey(),
                        entry.getValue()
                );
            }
        }

        // ---------- KPI (aman dari pembagian nol) ----------

        return new DashboardSummary(
                totalAssets,
                activeAssets,
                assetService.getMaintenanceAssets(),
                assetService.getInactiveAssets(),
                totalOperatingHours,
                totalAssetValue,
                highestValueAsset,

                totalMaintenance,
                maintenanceService
                        .getMaintenanceCountByStatus("active"),
                completedMaintenance,
                totalMaintenanceCost,
                highestCostAsset,
                highestCostAmount,
                maintenanceService.getMaintenanceCostByType(),
                topCostAssets,

                percentage(activeAssets, totalAssets),
                percentage(completedMaintenance, totalMaintenance),
                totalAssets > 0
                        ? totalOperatingHours / totalAssets
                        : 0,
                totalMaintenance > 0
                        ? totalMaintenanceCost / totalMaintenance
                        : 0
        );
    }


    private double percentage(
            int part,
            int total
    ) {

        if (total <= 0) {

            return 0;
        }

        return (double) part / total * 100;
    }
}
