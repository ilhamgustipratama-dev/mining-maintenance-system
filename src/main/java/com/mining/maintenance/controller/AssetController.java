package com.mining.maintenance.controller;

import com.mining.maintenance.importer.CsvAssetImporter;
import com.mining.maintenance.model.Asset;
import com.mining.maintenance.service.AssetService;
import com.mining.maintenance.util.InputHelper;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class AssetController {

    private final AssetService assetService;


    // =========================
    // CONSTRUCTOR
    // =========================

    public AssetController(
            AssetService assetService
    ) {

        this.assetService =
                assetService;
    }


    // =========================
    // ADD ASSET
    // =========================

    public void addAsset(
            Scanner scanner
    ) {

        System.out.println();

        System.out.println(
                "Add Asset"
        );

        String assetCode;

        do {

            System.out.print(
                    "Asset Code: "
            );

            assetCode =
                    scanner.nextLine().trim();

            if (assetCode.isEmpty()) {

                System.out.println(
                        "Asset Code cannot be empty."
                );

            } else if (
                    assetService.assetCodeExists(
                            assetCode
                    )
            ) {

                System.out.println(
                        "Asset Code already exists."
                );

                assetCode = "";
            }

        } while (assetCode.isEmpty());


        String assetName;

        do {

            System.out.print(
                    "Asset Name: "
            );

            assetName =
                    scanner.nextLine().trim();

            if (assetName.isEmpty()) {

                System.out.println(
                        "Asset Name cannot be empty."
                );
            }

        } while (assetName.isEmpty());


        System.out.print(
                "Category: "
        );

        String category =
                scanner.nextLine().trim();


        System.out.print(
                "Type: "
        );

        String type =
                scanner.nextLine().trim();


        System.out.print(
                "Brand: "
        );

        String brand =
                scanner.nextLine().trim();


        System.out.print(
                "Model: "
        );

        String model =
                scanner.nextLine().trim();


        System.out.print(
                "Serial Number: "
        );

        String serialNumber =
                scanner.nextLine().trim();


        double purchasePrice =
                InputHelper.readDouble(
                        scanner,
                        "Purchase Price: "
                );


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

            } else if (!assetService.isValidStatus(status)) {

                System.out.println(
                        "Invalid status. Allowed: "
                                + String.join(
                                ", ",
                                AssetService.VALID_STATUSES
                        )
                );

                status = "";
            }

        } while (status.isEmpty());


        System.out.print(
                "Location: "
        );

        String location =
                scanner.nextLine().trim();


        String purchaseDate;

        while (true) {

            System.out.print(
                    "Purchase Date (YYYY-MM-DD): "
            );

            purchaseDate =
                    scanner.nextLine().trim();

            try {

                java.sql.Date.valueOf(
                        purchaseDate
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


        double operatingHours =
                InputHelper.readDouble(
                        scanner,
                        "Operating Hours: "
                );


        System.out.print(
                "Description: "
        );

        String description =
                scanner.nextLine().trim();


        Asset asset =
                new Asset(
                        0,
                        assetCode,
                        assetName,
                        category,
                        type,
                        brand,
                        model,
                        serialNumber,
                        purchasePrice,
                        status,
                        location,
                        purchaseDate,
                        operatingHours,
                        description
                );


        String validationError =
                assetService.validateAsset(
                        asset
                );

        if (validationError != null) {

            System.out.println();

            System.out.println(
                    validationError
            );

            System.out.println(
                    "Failed to add asset."
            );

            return;
        }

        boolean added =
                assetService.addAsset(
                        asset
                );


        System.out.println();

        if (added) {

            System.out.println(
                    "Asset successfully added."
            );

        } else {

            System.out.println(
                    "Failed to add asset."
            );
        }
    }


    // =========================
    // VIEW ASSETS
    // =========================

    public void viewAssets() {

        System.out.println();

        System.out.println(
                "Asset List"
        );


        List<Asset> assets =
                assetService.getAllAssets();


        if (assets.isEmpty()) {

            System.out.println(
                    "No assets found."
            );

            return;
        }


        for (
                Asset asset
                : assets
        ) {

            System.out.println();

            System.out.println(
                    "ID: "
                            + asset.getId()
            );

            System.out.println(
                    "Asset Code: "
                            + asset.getAssetCode()
            );

            System.out.println(
                    "Asset Name: "
                            + asset.getAssetName()
            );

            System.out.println(
                    "Category: "
                            + asset.getCategory()
            );

            System.out.println(
                    "Type: "
                            + asset.getType()
            );

            System.out.println(
                    "Brand: "
                            + asset.getBrand()
            );

            System.out.println(
                    "Model: "
                            + asset.getModel()
            );

            System.out.println(
                    "Serial Number: "
                            + asset.getSerialNumber()
            );

            System.out.printf(
                    Locale.US,
                    "Purchase Price: Rp %,.2f%n",
                    asset.getPurchasePrice()
            );

            System.out.println(
                    "Status: "
                            + asset.getStatus()
            );

            System.out.println(
                    "Location: "
                            + asset.getLocation()
            );

            System.out.println(
                    "Purchase Date: "
                            + asset.getPurchaseDate()
            );

            System.out.printf(
                    Locale.US,
                    "Operating Hours: %,.2f%n",
                    asset.getOperatingHours()
            );

            System.out.println(
                    "Description: "
                            + asset.getDescription()
            );
        }


        System.out.println();

        System.out.println(
                "Total Assets: "
                        + assets.size()
        );
    }


    // =========================
    // SEARCH ASSET
    // =========================

    public void searchAsset(
            Scanner scanner
    ) {

        System.out.println();

        System.out.println(
                "Search Assets"
        );

        System.out.println(
                "Search by Code, Name, Category, Status, or Location"
        );

        System.out.print(
                "Keyword: "
        );


        String keyword =
                scanner.nextLine().trim();


        if (keyword.isEmpty()) {

            System.out.println(
                    "Keyword cannot be empty."
            );

            return;
        }


        List<Asset> assets =
                assetService.searchAssets(
                        keyword
                );


        if (assets.isEmpty()) {

            System.out.println();

            System.out.println(
                    "No assets found."
            );

            return;
        }


        System.out.println();

        System.out.println(
                "Search Results"
        );

        System.out.println(
                "-------------------------"
        );


        for (
                Asset asset
                : assets
        ) {

            System.out.println();

            System.out.println(
                    "ID: "
                            + asset.getId()
            );

            System.out.println(
                    "Asset Code     : "
                            + asset.getAssetCode()
            );

            System.out.println(
                    "Asset Name     : "
                            + asset.getAssetName()
            );

            System.out.println(
                    "Category       : "
                            + asset.getCategory()
            );

            System.out.println(
                    "Type           : "
                            + asset.getType()
            );

            System.out.println(
                    "Brand          : "
                            + asset.getBrand()
            );

            System.out.println(
                    "Model          : "
                            + asset.getModel()
            );

            System.out.println(
                    "Status         : "
                            + asset.getStatus()
            );

            System.out.println(
                    "Location       : "
                            + asset.getLocation()
            );

            System.out.printf(
                    Locale.US,
                    "Purchase Price : Rp %,.2f%n",
                    asset.getPurchasePrice()
            );

            System.out.printf(
                    Locale.US,
                    "Operating Hours: %,.2f%n",
                    asset.getOperatingHours()
            );
        }


        System.out.println();

        System.out.println(
                "-------------------------"
        );

        System.out.println(
                "Total Results: "
                        + assets.size()
        );
    }


    // =========================
    // UPDATE ASSET
    // =========================

    public void updateAsset(
            Scanner scanner
    ) {

        System.out.println();

        System.out.println(
                "Update Asset"
        );


        int id =
                InputHelper.readInt(
                        scanner,
                        "Asset ID: "
                );


        Asset existing =
                assetService.findById(
                        id
                );


        if (existing == null) {

            System.out.println(
                    "Asset not found."
            );

            return;
        }


        System.out.println();

        System.out.println(
                "Current Asset Code: "
                        + existing.getAssetCode()
        );

        System.out.println(
                "Current Asset Name: "
                        + existing.getAssetName()
        );


        System.out.println();


        String assetCode;

        while (true) {

            System.out.print(
                    "New Asset Code: "
            );

            assetCode =
                    scanner.nextLine().trim();

            if (assetCode.isEmpty()) {

                System.out.println(
                        "Asset Code cannot be empty."
                );

                continue;
            }

            Asset owner =
                    assetService.findByAssetCode(
                            assetCode
                    );

            if (
                    owner != null
                            && owner.getId() != id
            ) {

                System.out.println(
                        "Asset Code already used by another asset."
                );

                continue;
            }

            break;
        }


        String assetName;

        do {

            System.out.print(
                    "New Asset Name: "
            );

            assetName =
                    scanner.nextLine().trim();

            if (assetName.isEmpty()) {

                System.out.println(
                        "Asset Name cannot be empty."
                );
            }

        } while (assetName.isEmpty());


        System.out.print(
                "New Category: "
        );

        String category =
                scanner.nextLine().trim();


        System.out.print(
                "New Type: "
        );

        String type =
                scanner.nextLine().trim();


        System.out.print(
                "New Brand: "
        );

        String brand =
                scanner.nextLine().trim();


        System.out.print(
                "New Model: "
        );

        String model =
                scanner.nextLine().trim();


        System.out.print(
                "New Serial Number: "
        );

        String serialNumber =
                scanner.nextLine().trim();


        double purchasePrice =
                InputHelper.readDouble(
                        scanner,
                        "New Purchase Price: "
                );


        String status;

        while (true) {

            System.out.print(
                    "New Status ("
                            + String.join(
                            "/",
                            AssetService.VALID_STATUSES
                    )
                            + "): "
            );

            status =
                    scanner.nextLine().trim();

            if (assetService.isValidStatus(status)) {

                break;
            }

            System.out.println(
                    "Invalid status. Allowed: "
                            + String.join(
                            ", ",
                            AssetService.VALID_STATUSES
                    )
            );
        }


        System.out.print(
                "New Location: "
        );

        String location =
                scanner.nextLine().trim();


        String purchaseDate;

        while (true) {

            System.out.print(
                    "New Purchase Date (YYYY-MM-DD): "
            );

            purchaseDate =
                    scanner.nextLine().trim();

            try {

                java.sql.Date.valueOf(
                        purchaseDate
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


        double operatingHours =
                InputHelper.readDouble(
                        scanner,
                        "New Operating Hours: "
                );


        System.out.print(
                "New Description: "
        );

        String description =
                scanner.nextLine().trim();


        Asset updated =
                new Asset(
                        id,
                        assetCode,
                        assetName,
                        category,
                        type,
                        brand,
                        model,
                        serialNumber,
                        purchasePrice,
                        status,
                        location,
                        purchaseDate,
                        operatingHours,
                        description
                );


        String updateError =
                assetService.validateAsset(
                        updated
                );

        if (updateError != null) {

            System.out.println();

            System.out.println(
                    updateError
            );

            System.out.println(
                    "Failed to update asset."
            );

            return;
        }

        boolean result =
                assetService.updateAsset(
                        updated
                );


        System.out.println();


        if (result) {

            System.out.println(
                    "Asset successfully updated."
            );

        } else {

            System.out.println(
                    "Failed to update asset."
            );
        }
    }


    // =========================
    // DELETE ASSET
    // =========================

    public void deleteAsset(
            Scanner scanner
    ) {

        System.out.println();

        System.out.println(
                "Delete Asset"
        );


        int id =
                InputHelper.readInt(
                        scanner,
                        "Asset ID: "
                );


        Asset asset =
                assetService.findById(
                        id
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


        int maintenanceCount =
                assetService.countMaintenanceRecords(
                        id
                );

        if (maintenanceCount > 0) {

            System.out.println();

            System.out.println(
                    "Cannot delete this asset."
            );

            System.out.println(
                    "It has "
                            + maintenanceCount
                            + " maintenance record(s)."
            );

            System.out.println(
                    "Delete would erase its maintenance history."
            );

            System.out.println(
                    "Tip: set its status to 'inactive' via Update Asset instead."
            );

            return;
        }

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
                assetService.deleteAsset(
                        id
                );


        if (deleted) {

            System.out.println(
                    "Asset successfully deleted."
            );

        } else {

            System.out.println(
                    "Failed to delete asset."
            );
        }
    }


    // =========================
    // TOTAL ASSETS
    // =========================

    public void totalAssets() {

        int total =
                assetService.getTotalAssets();


        System.out.println();

        System.out.println(
                "Total Assets: "
                        + total
        );
    }


    // =========================
    // ASSET STATISTICS
    // =========================

    public void assetStatistics() {

        int totalAssets =
                assetService.getTotalAssets();


        int activeAssets =
                assetService.getActiveAssets();


        int maintenanceAssets =
                assetService.getMaintenanceAssets();


        int inactiveAssets =
                assetService.getInactiveAssets();


        double totalOperatingHours =
                assetService.getTotalOperatingHours();


        System.out.println();

        System.out.println(
                "Asset Statistics"
        );

        System.out.println(
                "-------------------------"
        );


        System.out.println(
                "Total Assets       : "
                        + totalAssets
        );

        System.out.println(
                "Active Assets      : "
                        + activeAssets
        );

        System.out.println(
                "Maintenance Assets : "
                        + maintenanceAssets
        );

        System.out.println(
                "Inactive Assets    : "
                        + inactiveAssets
        );

        System.out.printf(
                Locale.US,
                "Total Operating Hrs: %,.2f%n",
                totalOperatingHours
        );
    }


    // =========================
    // IMPORT ASSETS FROM CSV
    // =========================

    public void importAssetsFromCSV(
            Scanner scanner
    ) {

        System.out.println();

        System.out.println(
                "Import Assets from CSV"
        );

        System.out.println(
                "-------------------------"
        );

        System.out.print(
                "CSV File Path: "
        );

        String filePath =
                scanner.nextLine().trim();

        if (filePath.isEmpty()) {

            System.out.println(
                    "File path cannot be empty."
            );

            return;
        }

        CsvAssetImporter importer =
                new CsvAssetImporter(
                        assetService
                );

        importer.importAssets(
                filePath
        );
    }


    // =========================
    // ASSET FINANCIAL SUMMARY
    // =========================

    public void assetFinancialSummary() {

        int totalAssets =
                assetService.getTotalAssets();

        if (totalAssets == 0) {

            System.out.println();

            System.out.println(
                    "No assets found."
            );

            return;
        }

        Asset highestValueAsset =
                assetService.getHighestValueAsset();

        System.out.println();
        System.out.println("==============================================");
        System.out.println("          ASSET FINANCIAL SUMMARY");
        System.out.println("==============================================");
        System.out.println();

        System.out.println(
                "Total Assets           : " + totalAssets
        );

        System.out.printf(
                Locale.US,
                "Total Asset Value     : Rp %,.2f%n",
                assetService.getTotalAssetValue()
        );

        System.out.printf(
                Locale.US,
                "Average Asset Value   : Rp %,.2f%n",
                assetService.getAverageAssetValue()
        );

        if (highestValueAsset != null) {

            System.out.println();
            System.out.println("Highest Value Asset");
            System.out.println("----------------------------------------------");

            System.out.println(
                    "Asset Code            : "
                            + highestValueAsset.getAssetCode()
            );

            System.out.println(
                    "Asset Name            : "
                            + highestValueAsset.getAssetName()
            );

            System.out.printf(
                    Locale.US,
                    "Purchase Price        : Rp %,.2f%n",
                    highestValueAsset.getPurchasePrice()
            );
        }

        System.out.println();
        System.out.println("==============================================");
    }
}
