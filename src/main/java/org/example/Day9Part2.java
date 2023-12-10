package org.example;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day9Part2 {
    @SneakyThrows
    public static void main (String... args) {
        Path path = Paths.get("src/main/resources/day9");
        var lines = Files.readAllLines(path);

        int sum = lines.stream().map(Day9Part2::mapToSequence).mapToInt(Day9Part2::calculateContinuation).sum();
        System.out.println(sum);
    }

    private static int calculateContinuation(LinkedList<Integer> startSequence) {
        var sequences = new LinkedList<LinkedList<Integer>>();
        sequences.addLast(startSequence);
        while (sequences.getLast().stream().anyMatch(integer -> integer != 0)) {
            var newSequence = new LinkedList<Integer>();
            for (int i = 1; i < sequences.getLast().size(); i++) {
                int difference = sequences.getLast().get(i) - sequences.getLast().get(i - 1);
                newSequence.add(difference);
            }
            sequences.addLast(newSequence);
        }
        int prevValue = 0;
        for(int i = sequences.size() - 2; i >= 0; i--) {
            prevValue =  sequences.get(i).getFirst() - prevValue;
        }
        return prevValue;
    }

    private static LinkedList<Integer> mapToSequence(String s) {
        String[] split = s.split("\\s+");
        return Arrays.stream(split).map(Integer::parseInt).collect(Collectors.toCollection(LinkedList::new));
    }
}
