package org.example;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

public class Day6Part1 {
    public static void main(String[] args) throws IOException {

        Instant start = Instant.now();
        Path path = Paths.get("src/main/resources/day6");
        var lines = Files.readAllLines(path);

        var maxTimes = lines.get(0).split("\\s+");
        var records = lines.get(1).split("\\s+");

        int product = IntStream.range(1, maxTimes.length)
                .map(index -> calculateRange(Integer.parseInt(maxTimes[index]), Integer.parseInt(records[index])))
                .reduce(1, (i1, i2) -> i1 * i2);
        Instant end = Instant.now();
        System.out.println(product);
        System.out.println(Duration.between(start, end));
    }

    private static int calculateRange(int maxTime, int record) {
        var y = (double)maxTime/2;
        double range = Math.sqrt(y * y - record);
        var min = (int)Math.floor(y - range + 1);
        var max = (int)Math.ceil(y + range - 1);
        return Math.max(0, max - min + 1);
    }
}