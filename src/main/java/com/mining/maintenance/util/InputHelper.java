package com.mining.maintenance.util;

import java.util.Scanner;

public class InputHelper {

    // =========================
    // READ INTEGER
    // =========================

    public static int readInt(
            Scanner scanner,
            String message
    ) {

        while (true) {

            System.out.print(message);

            if (!scanner.hasNextLine()) {

                System.out.println(
                        "\nInput stream closed."
                );

                System.exit(0);
            }

            String input =
                    scanner.nextLine().trim();

            if (input.isEmpty()) {

                System.out.println(
                        "Input cannot be empty. Please enter a number."
                );

                continue;
            }

            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid number. Please enter a valid integer."
                );
            }
        }
    }

    // =========================
    // READ DOUBLE
    // =========================

    public static double readDouble(
            Scanner scanner,
            String message
    ) {

        while (true) {

            System.out.print(message);

            if (!scanner.hasNextLine()) {

                System.out.println(
                        "\nInput stream closed."
                );

                System.exit(0);
            }

            String input =
                    scanner.nextLine().trim();

            if (input.isEmpty()) {

                System.out.println(
                        "Input cannot be empty. Please enter a number."
                );

                continue;
            }

            try {

                double value =
                        Double.parseDouble(input);

                if (value < 0) {

                    System.out.println(
                            "Number cannot be negative."
                    );

                    continue;
                }

                return value;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid number. Please enter a valid number."
                );
            }
        }
    }
}