package org.example;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day9Part1 {
    @SneakyThrows
    public static void main (String... args) {
        Path path = Paths.get("src/main/resources/day9");
        var lines = Files.readAllLines(path);

        int sum = lines.stream().map(Day9Part1::mapToSequence).mapToInt(Day9Part1::calculateContinuation).sum();
        System.out.println(sum);
    }

    private static int calculateContinuation(List<Integer> startSequence) {
        var sequences = new LinkedList<List<Integer>>();
        sequences.addLast(startSequence);
        while (sequences.getLast().stream().anyMatch(integer -> integer != 0)) {
            var newSequence = new ArrayList<Integer>();
            for (int i = 1; i < sequences.getLast().size(); i++) {
                int difference = sequences.getLast().get(i) - sequences.getLast().get(i - 1);
                newSequence.add(difference);
            }
            sequences.addLast(newSequence);
        }
        return sequences.stream().mapToInt(list -> list.get(list.size()-1)).sum();
    }

    private static List<Integer> mapToSequence(String s) {
        String[] split = s.split("\\s+");
        return Arrays.stream(split).map(Integer::parseInt).toList();
    }
}
