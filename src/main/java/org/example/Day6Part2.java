package org.example;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

public class Day6Part2 {
    public static void main(String[] args) throws IOException {

        Instant start = Instant.now();
        Path path = Paths.get("src/main/resources/day6");
        var lines = Files.readAllLines(path);

        var maxTime = lines.get(0).replaceAll("\\D", "");
        var record = lines.get(1).replaceAll("\\D", "");

        long maxTimeLong = Long.parseLong(maxTime);
        long recordLong = Long.parseLong(record);
        var possibilities = calculateRange(maxTimeLong, recordLong);
        Instant end = Instant.now();
        System.out.println(possibilities);
        System.out.println(Duration.between(start, end));
    }

    private static long calculateRange(long maxTime, long record) {
        var y = (double)maxTime/2;
        double range = Math.sqrt(y * y - record);
        var min = (int)Math.floor(y - range + 1);
        var max = (int)Math.ceil(y + range - 1);
        return Math.max(0, max - min + 1);
    }
}