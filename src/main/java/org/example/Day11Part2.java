package org.example;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

public class Day11Part2 {
    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day11");
        var lines = Files.readAllLines(path);

        var linesArray = makeMatrix(lines);
        Set<Integer> emptyRows = new HashSet<>();
        IntStream.range(0, lines.size()).forEach(row -> {
            if (lines.get(row).chars().noneMatch(intChar -> '#' == intChar)) {
                emptyRows.add(row);
            }
        });

        var columnArray = transpose(linesArray);
        Set<Integer> emptyColumns = new HashSet<>();
        IntStream.range(0, columnArray.length).forEach(row -> {
            if (Arrays.stream(columnArray[row]).noneMatch(intChar -> '#' == intChar)) {
                emptyColumns.add(row);
            }
        });

        List<Pair<Integer, Integer>> galaxyCoordinates = new ArrayList<>();
        for (int y = 0; y < linesArray.length; y++) {
            for (int x = 0; x < linesArray[y].length; x++) {
                if (linesArray[y][x] == '#') {
                    galaxyCoordinates.add(Pair.of(y, x));
                }
            }
        }

        long sum = 0;
        for (var galaxy1 : galaxyCoordinates) {
            for (var galaxy2 : galaxyCoordinates) {
                long emptyRowsCount = IntStream.range(Math.min(galaxy1.getLeft(), galaxy2.getLeft()),
                                Math.max(galaxy1.getLeft(), galaxy2.getLeft()))
                        .filter(emptyRows::contains)
                        .count();
                long emptyColumnsCount = IntStream.range(Math.min(galaxy1.getRight(), galaxy2.getRight()),
                                Math.max(galaxy1.getRight(), galaxy2.getRight()))
                        .filter(emptyColumns::contains)
                        .count();

                long deltaRows = Math.abs(galaxy1.getLeft() - galaxy2.getLeft());
                long deltaColumns = Math.abs(galaxy1.getRight() - galaxy2.getRight());
                long cleanDeltaRows = deltaRows - emptyRowsCount;
                long cleanDeltaColumns = deltaColumns - emptyColumnsCount;
                sum += cleanDeltaRows;
                sum += cleanDeltaColumns;
                sum += emptyColumnsCount * 1_000_000;
                sum += emptyRowsCount * 1_000_000;
            }
        }
        System.out.println(sum / 2);
    }

    private static Character[][] makeMatrix(List<String> lines) {
        return lines.stream().map(Day11Part2::makeArray).toArray(Character[][]::new);
    }

    private static Character[] makeArray(String string) {
        return string.chars().mapToObj(c -> (char) c).toArray(Character[]::new);
    }

    private static Character[][] transpose(Character[][] array) {
        int rows = array.length;
        int cols = array[0].length;

        Character[][] transposedArray = new Character[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposedArray[j][i] = array[i][j];
            }
        }

        return transposedArray;
    }
}
