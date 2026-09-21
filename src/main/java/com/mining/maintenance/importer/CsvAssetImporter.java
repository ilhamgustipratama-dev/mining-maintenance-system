package com.mining.maintenance.importer;

import com.mining.maintenance.exception.DatabaseException;
import com.mining.maintenance.model.Asset;
import com.mining.maintenance.service.AssetService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvAssetImporter {

    private static final int EXPECTED_COLUMNS = 13;

    private final AssetService assetService;


    // =========================
    // CONSTRUCTOR
    // =========================

    public CsvAssetImporter(
            AssetService assetService
    ) {

        this.assetService = assetService;
    }


    // =========================
    // IMPORT ASSETS
    // =========================

    public void importAssets(
            String filePath
    ) {

        int imported = 0;
        int skipped = 0;

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new FileReader(
                                        filePath
                                )
                        )
        ) {

            String line;
            int lineNumber = 0;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                // Buang BOM (sering ada pada CSV dari Excel)
                if (firstLine && line.startsWith("\uFEFF")) {

                    line = line.substring(1);
                }

                if (line.trim().isEmpty()) {

                    continue;
                }

                // =========================
                // SKIP HEADER
                // =========================

                if (firstLine) {

                    firstLine = false;

                    if (
                            line.toLowerCase()
                                    .contains("assetcode")
                    ) {

                        continue;
                    }
                }

                String[] data =
                        parseCsvLine(line);

                // =========================
                // COLUMN COUNT
                // =========================

                if (data.length != EXPECTED_COLUMNS) {

                    System.out.println(
                            "Skipped line "
                                    + lineNumber
                                    + ": expected "
                                    + EXPECTED_COLUMNS
                                    + " columns, found "
                                    + data.length
                    );

                    skipped++;

                    continue;
                }

                String assetCode = data[0].trim();
                String assetName = data[1].trim();

                // =========================
                // REQUIRED FIELDS
                // =========================

                if (
                        assetCode.isEmpty()
                                || assetName.isEmpty()
                ) {

                    System.out.println(
                            "Skipped line "
                                    + lineNumber
                                    + ": Asset Code or Asset Name is empty."
                    );

                    skipped++;

                    continue;
                }

                // =========================
                // DUPLICATE
                // =========================

                if (assetService.assetCodeExists(assetCode)) {

                    System.out.println(
                            "Skipped duplicate Asset Code: "
                                    + assetCode
                    );

                    skipped++;

                    continue;
                }

                // =========================
                // NUMBERS
                // =========================

                double purchasePrice;
                double operatingHours;

                try {

                    purchasePrice =
                            Double.parseDouble(
                                    data[7].trim()
                            );

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Skipped line "
                                    + lineNumber
                                    + " ("
                                    + assetCode
                                    + "): invalid purchase price '"
                                    + data[7].trim()
                                    + "'"
                    );

                    skipped++;

                    continue;
                }

                try {

                    operatingHours =
                            Double.parseDouble(
                                    data[11].trim()
                            );

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Skipped line "
                                    + lineNumber
                                    + " ("
                                    + assetCode
                                    + "): invalid operating hours '"
                                    + data[11].trim()
                                    + "'"
                    );

                    skipped++;

                    continue;
                }

                // =========================
                // BUILD ASSET
                // =========================

                Asset asset =
                        new Asset(
                                0,
                                assetCode,
                                assetName,
                                data[2].trim(),
                                data[3].trim(),
                                data[4].trim(),
                                data[5].trim(),
                                data[6].trim(),
                                purchasePrice,
                                data[8].trim(),
                                data[9].trim(),
                                data[10].trim(),
                                operatingHours,
                                data[12].trim()
                        );

                // =========================
                // BUSINESS VALIDATION (Service)
                // =========================

                String error =
                        assetService.validateAsset(
                                asset
                        );

                if (error != null) {

                    System.out.println(
                            "Skipped line "
                                    + lineNumber
                                    + " ("
                                    + assetCode
                                    + "): "
                                    + error
                    );

                    skipped++;

                    continue;
                }

                // =========================
                // SAVE
                // =========================

                try {

                    if (assetService.addAsset(asset)) {

                        imported++;

                        System.out.println(
                                "Imported: "
                                        + assetCode
                        );

                    } else {

                        skipped++;

                        System.out.println(
                                "Skipped line "
                                        + lineNumber
                                        + " ("
                                        + assetCode
                                        + "): failed to save."
                        );
                    }

                } catch (DatabaseException e) {

                    skipped++;

                    System.out.println(
                            "Skipped line "
                                    + lineNumber
                                    + " ("
                                    + assetCode
                                    + "): database error - "
                                    + e.getCause().getMessage()
                    );
                }
            }

        } catch (IOException e) {

            System.out.println();

            System.out.println(
                    "Failed to read CSV file."
            );

            System.out.println(
                    "Reason: " + e.getMessage()
            );

            return;
        }

        System.out.println();

        System.out.println(
                "CSV Import Completed."
        );

        System.out.println(
                "Imported : " + imported
        );

        System.out.println(
                "Skipped  : " + skipped
        );
    }


    // =========================
    // PARSE ONE CSV LINE
    // Mendukung field dalam tanda kutip:
    //   a,"Pit A, Block 2",b  -> 3 kolom
    //   ""  di dalam kutip     -> satu tanda kutip
    // =========================

    static String[] parseCsvLine(
            String line
    ) {

        List<String> fields =
                new ArrayList<>();

        StringBuilder current =
                new StringBuilder();

        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char c = line.charAt(i);

            if (inQuotes) {

                if (c == '"') {

                    if (
                            i + 1 < line.length()
                                    && line.charAt(i + 1) == '"'
                    ) {

                        current.append('"');

                        i++;

                    } else {

                        inQuotes = false;
                    }

                } else {

                    current.append(c);
                }

            } else {

                if (c == '"') {

                    inQuotes = true;

                } else if (c == ',') {

                    fields.add(current.toString());

                    current.setLength(0);

                } else {

                    current.append(c);
                }
            }
        }

        fields.add(current.toString());

        return fields.toArray(new String[0]);
    }
}
