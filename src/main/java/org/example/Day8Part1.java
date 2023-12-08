package org.example;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.example.HandUtils.HAND_COMPARATOR;

public class Day8Part1 {
    @SneakyThrows
    public static void main (String... args) {
        Path path = Paths.get("src/main/resources/day8");
        var lines = Files.readAllLines(path);

        int[] instructions = lines.get(0).chars().map(c -> c == (int) 'L' ? 0 : 1).toArray();

        var map = new HashMap<String, String[]>();
        for (int i = 2; i < lines.size(); i++) {
            Pattern pattern = Pattern.compile("[\\p{L}\\p{N}]+");
            Matcher matcher = pattern.matcher(lines.get(i));
            String[] cooridinates = matcher.results().map(MatchResult::group).toArray(String[]::new);
            map.put(cooridinates[0], new String[]{cooridinates[1], cooridinates[2]});
        }

        int iterationCounter = 0;
        String currentCoordinate = "AAA";
        while (!currentCoordinate.equals("ZZZ")) {
            iterationCounter++;
            for(var instruction : instructions) {
                currentCoordinate = map.get(currentCoordinate)[instruction];
            }
        }
        System.out.println(iterationCounter * instructions.length);
    }
}
