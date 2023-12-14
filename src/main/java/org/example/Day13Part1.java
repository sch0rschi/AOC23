package org.example;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day13Part1 {
    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day13");
        var lines = Files.readAllLines(path);

        LinkedList<List<List<Boolean>>> patterns = new LinkedList<>();
        patterns.add(new ArrayList<>());
        for (var line : lines) {
            if (line.isBlank()) {
                patterns.add(new ArrayList<>());
            } else {
                patterns.getLast().add(line.chars().mapToObj(c -> c == '#' ? Boolean.FALSE : Boolean.TRUE).collect(Collectors.toCollection(ArrayList::new)));
            }
        }

        long sum = patterns.stream().map(MatrixUtils::mapToArrays).mapToLong(Day13Part1::calculateScore).peek(System.out::println).sum();
        System.out.println(sum);

    }

    private static long calculateScore(Boolean[][] pattern) {
        int height = pattern.length;
        int width = pattern[0].length;
        HashSet<Integer> remainingVerticalSymmetries = IntStream.range(0, width - 1).boxed().collect(Collectors.toCollection(HashSet<Integer>::new));
        for (var row : pattern) {
            remainingVerticalSymmetries.removeIf(symmetryColumn -> isNotSymmetryRowSymmetry(symmetryColumn, row));
        }

        Boolean[][] transposed = MatrixUtils.transpose(pattern);
        HashSet<Integer> remainingHorizontalSymmetries = IntStream.range(0, height - 1).boxed().collect(Collectors.toCollection(HashSet<Integer>::new));
        for (var row : transposed) {
            remainingHorizontalSymmetries.removeIf(symmetryColumn -> isNotSymmetryRowSymmetry(symmetryColumn, row));
        }

        return remainingVerticalSymmetries.stream().mapToInt(x -> x).map(x -> x + 1).sum()
                + remainingHorizontalSymmetries.stream().mapToInt(x -> x).map(x -> x + 1).sum() * 100L;
    }

    private static boolean isNotSymmetryRowSymmetry(Integer symmetryColumn, Boolean[] row) {
        int width = row.length;
        for (int offset = 0; symmetryColumn - offset >= 0 && symmetryColumn + offset + 1 < width; offset++) {
            if (row[symmetryColumn - offset] != row[symmetryColumn + offset + 1]) {
                return true;
            }
        }
        return false;
    }
}
