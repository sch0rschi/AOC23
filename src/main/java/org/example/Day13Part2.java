package org.example;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Triple;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day13Part2 {
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

        Instant start = Instant.now();
        long sum = patterns.stream().map(Day13Part2::mapToArrays).mapToLong(Day13Part2::calculateScore).sum();
        Instant end = Instant.now();
        System.out.println(sum + " took: " + Duration.between(start, end));
    }

    private static Boolean[][] mapToArrays(List<List<Boolean>> lists) {
        int height = lists.size();
        int width = lists.get(0).size();
        Boolean[][] booleans = new Boolean[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                booleans[y][x] = lists.get(y).get(x);
            }
        }
        return booleans;
    }

    private static long calculateScore(Boolean[][] pattern) {
        int height = pattern.length;
        int width = pattern[0].length;

        HashSet<Integer> previousVerticalSymmetries = IntStream.range(0, width - 1).boxed().collect(Collectors.toCollection(HashSet<Integer>::new));
        for (var row : pattern) {
            previousVerticalSymmetries.removeIf(symmetryColumn -> isNotSymmetryRowSymmetry(symmetryColumn, row));
        }

        Boolean[][] transposed = transpose(pattern);
        HashSet<Integer> previousHorizontalSymmetries = IntStream.range(0, height - 1).boxed().collect(Collectors.toCollection(HashSet<Integer>::new));
        for (var row : transposed) {
            previousHorizontalSymmetries.removeIf(symmetryColumn -> isNotSymmetryRowSymmetry(symmetryColumn, row));
        }


        var remainingVerticalPairs = getSymmetryWithSmudgePairs(height, width);
        for(int rowIndex = 0; rowIndex < pattern.length; rowIndex++) {
            final int rowIndexCopy = rowIndex;
            remainingVerticalPairs.removeIf(symmetryColumn -> isNotSymmetryRowSymmetry(symmetryColumn, rowIndexCopy, pattern[rowIndexCopy]));
        }

        @SuppressWarnings("SuspiciousNameCombination")
        var remainingHorizontalPairs = getSymmetryWithSmudgePairs(width, height);
        for(int rowIndex = 0; rowIndex < transposed.length; rowIndex++) {
            final int rowIndexCopy = rowIndex;
            remainingHorizontalPairs.removeIf(symmetryColumn -> isNotSymmetryRowSymmetry(symmetryColumn, rowIndexCopy, transposed[rowIndexCopy]));
        }

        return remainingVerticalPairs.stream().mapToInt(Triple::getLeft).map(x -> x + 1).filter(x -> !previousVerticalSymmetries.contains(x-1)).distinct().sum()
                + remainingHorizontalPairs.stream().mapToInt(Triple::getLeft).map(x -> x + 1).filter(x -> !previousHorizontalSymmetries.contains(x-1)).distinct().sum() * 100L;
    }

    private static HashSet<Triple<Integer, Integer, Integer>> getSymmetryWithSmudgePairs(int height, int width) {
        HashSet<Triple<Integer, Integer, Integer>> pairs = new HashSet<>();
        for (var symmetry = 0; symmetry < width - 1; symmetry++) {
            for (var smudgeRow = 0; smudgeRow < height; smudgeRow++) {
                for (var smudgeColumn = 0; smudgeColumn < width; smudgeColumn++) {
                    pairs.add(Triple.of(symmetry, smudgeRow, smudgeColumn));
                }
            }
        }
        return pairs;
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

    private static boolean isNotSymmetryRowSymmetry(Triple<Integer, Integer, Integer> symmetryAndSmudge, int row, Boolean[] rowEntries) {
        Integer smudgeRow = symmetryAndSmudge.getMiddle();
        Integer smudgeColumn = symmetryAndSmudge.getRight();
        Integer symmetryColumn = symmetryAndSmudge.getLeft();
        if (smudgeRow == row) {
            rowEntries[smudgeColumn] = !rowEntries[smudgeColumn];
        }
        int width = rowEntries.length;
        for (int offset = 0; symmetryColumn - offset >= 0 && symmetryColumn + offset + 1 < width; offset++) {
            if (rowEntries[symmetryColumn - offset] != rowEntries[symmetryColumn + offset + 1]) {
                if (smudgeRow == row) {
                    rowEntries[smudgeColumn] = !rowEntries[smudgeColumn];
                }
                return true;
            }
        }
        if (smudgeRow == row) {
            rowEntries[smudgeColumn] = !rowEntries[smudgeColumn];
        }
        return false;
    }

    private static Boolean[][] transpose(Boolean[][] array) {
        int rows = array.length;
        int cols = array[0].length;

        Boolean[][] transposedArray = new Boolean[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposedArray[j][i] = array[i][j];
            }
        }

        return transposedArray;
    }
}
