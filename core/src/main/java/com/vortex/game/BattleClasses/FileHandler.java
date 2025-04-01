package com.vortex.game.BattleClasses;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileHandler {
    private static final String FILE_NAME = "game_stats.txt";
    private static long startTime = -1; // Default to -1 to detect uninitialized timer

    public static void startMatchTimer() {
        if (startTime == -1) {  // Prevent multiple resets
            startTime = System.currentTimeMillis();
        }
    }

    // Save match stats
    public static void saveStats(int skillPointsUsed, int wins, int defeats) {
        long endTime = System.currentTimeMillis();

        // Prevent incorrect calculations if timer wasn't started properly
        if (startTime == -1) {
            System.err.println("Warning: Match timer was not started properly. Defaulting to 0 seconds.");
            startTime = endTime; // Set start time to avoid negative duration
        }

        long durationSeconds = (endTime - startTime) / 1000; // Convert milliseconds to seconds
        double durationMinutes = durationSeconds / 60.0; // Convert seconds to minutes

        // Reset timer for next match
        startTime = -1;

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
            writer.write(String.format("Time Duration: %d seconds (%.2f minutes)", durationSeconds, durationMinutes));
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
