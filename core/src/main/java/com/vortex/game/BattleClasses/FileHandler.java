package com.vortex.game.BattleClasses;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileHandler {
    private static final String FILE_NAME = "game_stats.txt";

    public static void saveStats(int skillPoints, int wins, int defeats, long durationInSeconds) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bw)) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timeStamp = LocalDateTime.now().format(dtf);

            out.println("Match Date: " + timeStamp);
            out.println("Total Skill Points Used: " + skillPoints);
            out.println("Total Wins: " + wins);
            out.println("Total Defeats: " + defeats);
            out.println("Time Duration (seconds): " + durationInSeconds);
            out.println("----------------------------------");

            System.out.println("Game stats saved successfully!");

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void readStats() {
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
