package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Day5Part1 {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("src/main/resources/day5");
        var lines = String.join("\n", Files.readAllLines(path));
        String[] split = lines.split("\n\n");

        Almanac almanac = AlmanacUtils.parseAlmanac(split);

        var min = almanac.getSeeds().stream().mapToLong(seed -> AlmanacUtils.getLocation(almanac, seed)).min().orElse(-1);

        System.out.println(min);
    }
}