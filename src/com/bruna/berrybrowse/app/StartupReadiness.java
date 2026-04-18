package com.bruna.berrybrowse.app;

import java.util.Scanner;

public class StartupReadiness {

    public static void waitSystemReady() {
        try {

            // Espera o Explorer subir
            while (!isExplorerRunning()) {
                Thread.sleep(2000);
            }

            // Estabilização do sistema
            Thread.sleep(10000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static boolean isExplorerRunning() {
        try {
            Process process = new ProcessBuilder(
                    "tasklist",
                    "/FI", "IMAGENAME eq explorer.exe"
            ).start();

            Scanner scanner = new Scanner(process.getInputStream());

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().toLowerCase();

                if (line.contains("explorer.exe")) {
                    scanner.close();
                    return true;
                }
            }

            scanner.close();

        } catch (Exception e) {
            System.out.println("Erro ao verificar Explorer: " + e.getMessage());
        }

        return false;
    }
}