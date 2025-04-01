package com.vortex.game.BattleClasses;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileHandler {
    private static final String FILE_NAME = "game_stats.txt";
    private static long startTime; // Track start time globally

    public static void startMatchTimer() {
        startTime = System.currentTimeMillis();
    }

    // Call this when the match ends
    public static void saveStats(int skillPointsUsed, int wins, int defeats) {
        long endTime = System.currentTimeMillis(); // Capture end time
        long duration = (endTime - startTime) / 1000; // Convert milliseconds to seconds

        // Format date for better readability
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write("Match Date: " + formattedDate);
            writer.newLine();
            writer.write("Total Skill Points Used: " + skillPointsUsed);
            writer.newLine();
            writer.write("Total Wins: " + wins);
            writer.newLine();
            writer.write("Total Defeats: " + defeats);
            writer.newLine();
            writer.write("Time Duration (seconds): " + duration);
            writer.newLine();
            writer.write("--------------------------------");
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving game stats: " + e.getMessage());
        }
    }

    // Read and print saved stats
    public static void readStats() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No game stats found.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
