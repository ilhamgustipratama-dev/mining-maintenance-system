package com.mining.maintenance.model;

import java.util.Map;

/**
 * Kumpulan angka dashboard, hasil hitungan DashboardService.
 * "record" = class data yang hanya menyimpan nilai (immutable);
 * getter otomatis, misalnya summary.totalAssets().
 */
public record DashboardSummary(

        // ---- ASSET ----
        int totalAssets,
        int activeAssets,
        int maintenanceAssets,
        int inactiveAssets,
        double totalOperatingHours,
        double totalAssetValue,
        Asset highestValueAsset,          // null jika belum ada asset

        // ---- MAINTENANCE ----
        int totalMaintenance,
        int activeMaintenance,
        int completedMaintenance,
        double totalMaintenanceCost,
        String highestCostAsset,          // null jika belum ada maintenance
        double highestCostAmount,
        Map<String, Double> costByType,
        Map<String, Double> topCostAssets,

        // ---- KPI ----
        double assetActiveRate,
        double maintenanceCompletionRate,
        double averageOperatingHours,
        double averageMaintenanceCost
) {
}
