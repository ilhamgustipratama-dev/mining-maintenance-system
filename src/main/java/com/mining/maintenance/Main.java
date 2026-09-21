package com.mining.maintenance;

import com.mining.maintenance.controller.AssetController;
import com.mining.maintenance.exception.DatabaseException;
import com.mining.maintenance.controller.MaintenanceController;
import com.mining.maintenance.service.AssetService;
import com.mining.maintenance.service.MaintenanceService;

import java.util.Scanner;

public class Main {

    public static void main(
            String[] args
    ) {

        Scanner scanner =
                new Scanner(System.in);


        // =========================
        // SERVICES
        // =========================

        AssetService assetService =
                new AssetService();

        MaintenanceService maintenanceService =
                new MaintenanceService();


        // =========================
        // CONTROLLERS
        // =========================

        AssetController assetController =
                new AssetController(
                        assetService
                );

        MaintenanceController maintenanceController =
                new MaintenanceController(
                        maintenanceService,
                        assetService
                );


        // =========================
        // APPLICATION
        // =========================

        System.out.println(
                "Mining Asset & Maintenance Management System"
        );


        while (true) {

            System.out.println();

            System.out.println(
                    "1. Add Asset"
            );

            System.out.println(
                    "2. View Assets"
            );

            System.out.println(
                    "3. Search Asset"
            );

            System.out.println(
                    "4. Update Asset"
            );

            System.out.println(
                    "5. Delete Asset"
            );

            System.out.println(
                    "6. Total Assets"
            );

            System.out.println(
                    "7. Add Maintenance"
            );

            System.out.println(
                    "8. View Maintenance"
            );

            System.out.println(
                    "9. Maintenance History by Asset"
            );

            System.out.println(
                    "10. Total Maintenance"
            );

            System.out.println(
                    "11. Update Maintenance"
            );

            System.out.println(
                    "12. Delete Maintenance"
            );

            System.out.println(
                    "13. Import Assets from CSV"
            );

            System.out.println(
                    "14. Asset Statistics"
            );

            System.out.println(
                    "15. Maintenance Statistics"
            );

            System.out.println(
                    "16. Maintenance Cost by Asset"
            );

            System.out.println(
                    "17. Asset Financial Summary"
            );

            System.out.println(
                    "18. Asset & Maintenance Dashboard"
            );

            System.out.println(
                    "19. Maintenance Cost by Type"
            );

            System.out.println(
                    "20. Exit"
            );


            System.out.print(
                    "Choose: "
            );


            String choice =
                    scanner.nextLine().trim();


            try {

                switch (choice) {

                    // =========================
                    // ASSET
                    // =========================

                    case "1":

                        assetController.addAsset(
                                scanner
                        );

                        break;


                    case "2":

                        assetController.viewAssets();

                        break;


                    case "3":

                        assetController.searchAsset(
                                scanner
                        );

                        break;


                    case "4":

                        assetController.updateAsset(
                                scanner
                        );

                        break;


                    case "5":

                        assetController.deleteAsset(
                                scanner
                        );

                        break;


                    case "6":

                        assetController.totalAssets();

                        break;


                    // =========================
                    // MAINTENANCE
                    // =========================

                    case "7":

                        maintenanceController.addMaintenance(
                                scanner
                        );

                        break;


                    case "8":

                        maintenanceController.viewMaintenance();

                        break;


                    case "9":

                        maintenanceController.maintenanceHistoryByAsset(
                                scanner
                        );

                        break;


                    case "10":

                        maintenanceController.totalMaintenance();

                        break;


                    case "11":

                        maintenanceController.updateMaintenance(
                                scanner
                        );

                        break;


                    case "12":

                        maintenanceController.deleteMaintenance(
                                scanner
                        );

                        break;


                    // =========================
                    // CSV
                    // =========================

                    case "13":

                        assetController.importAssetsFromCSV(
                                scanner
                        );

                        break;


                    // =========================
                    // STATISTICS
                    // =========================

                    case "14":

                        assetController.assetStatistics();

                        break;


                    case "15":

                        maintenanceController.maintenanceStatistics();

                        break;


                    case "16":

                        maintenanceController.maintenanceCostByAsset(
                                scanner
                        );

                        break;


                    case "17":

                        assetController.assetFinancialSummary();

                        break;


                    case "18":

                        maintenanceController.dashboard();

                        break;


                    case "19":

                        maintenanceController.maintenanceCostByType();

                        break;


                    // =========================
                    // EXIT
                    // =========================

                    case "20":

                        System.out.println();

                        System.out.println(
                                "System closed."
                        );

                        scanner.close();

                        return;


                    default:

                        System.out.println();

                        System.out.println(
                                "Invalid choice. Please choose 1-20."
                        );
                }

            } catch (DatabaseException e) {

                System.out.println();

                System.out.println(
                        "Database error: " + e.getMessage()
                );

                if (e.getCause() != null) {

                    System.out.println(
                            "Detail: " + e.getCause().getMessage()
                    );
                }

                System.out.println(
                        "The operation was not completed."
                );
            }
        }
    }
}