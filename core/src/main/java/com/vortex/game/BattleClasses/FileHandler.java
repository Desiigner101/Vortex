package com.vortex.game.BattleClasses;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileHandler {
    private static final String FILE_NAME = "game_stats.txt";

    // Global trackers
    private static long startTime = -1;
    private static int totalSkillPointsUsed = 0;
    private static int totalWins = 0;
    private static int totalDefeats = 0;

    public static void startGameTimer() {
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }
    }

    public static void addMatchStats(int skillPointsUsed, boolean won) {
        totalSkillPointsUsed += skillPointsUsed;
        if (won) {
            totalWins++;
        } else {
            totalDefeats++;
        }
    }

    public static void saveFinalGameStats() {
        long endTime = System.currentTimeMillis();

        if (startTime == -1) {
            System.err.println("Game timer not started. Setting default start time.");
            startTime = endTime;
        }

        long durationSeconds = (endTime - startTime) / 1000;
        double durationMinutes = durationSeconds / 60.0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write("Game Summary Date: " + formattedDate);
            writer.newLine();
            writer.write("Total Skill Points Used: " + totalSkillPointsUsed);
            writer.newLine();
            writer.write("Total Wins: " + totalWins);
            writer.newLine();
            writer.write("Total Defeats: " + totalDefeats);
            writer.newLine();
            writer.write(String.format("Total Time Duration: %d seconds (%.2f minutes)", durationSeconds, durationMinutes));
            writer.newLine();
            writer.write("================================");
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving final game stats: " + e.getMessage());
        }

        // Reset for the next full playthrough
        startTime = -1;
        totalSkillPointsUsed = 0;
        totalWins = 0;
        totalDefeats = 0;
    }

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
