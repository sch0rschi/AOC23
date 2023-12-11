package org.example;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Day11Part1 {
    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day11");
        var lines = Files.readAllLines(path);

        var linesArray = duplicateEmptyLines(lines);
        var rowArray = transpose(linesArray);
        rowArray = duplicateEmptyLines(rowArray);
        linesArray = transpose(rowArray);

        List<Pair<Integer, Integer>> galaxyCoordinates = new ArrayList<>();
        for (int y = 0; y < linesArray.length; y++) {
            for (int x = 0; x < linesArray[y].length; x++) {
                if (linesArray[y][x] == '#') {
                    galaxyCoordinates.add(Pair.of(y, x));
                }
            }
        }

        int sum = 0;
        for(var galaxy1 : galaxyCoordinates) {
            for (var galaxy2 : galaxyCoordinates) {
                sum += Math.abs(galaxy1.getLeft() - galaxy2.getLeft()) + Math.abs(galaxy1.getRight() - galaxy2.getRight());
            }
        }

        System.out.println(sum/2);
    }

    private static Character[][] duplicateEmptyLines(List<String> lines) {
        return lines.stream().mapMulti((line, downstream) -> {
                    var lineArray = line.chars().mapToObj(c -> (char) c).toArray(Character[]::new);
                    downstream.accept(lineArray);
                    if (line.chars().mapToObj(c -> (char) c).noneMatch(c -> c == '#')) {
                        for(int i = 0; i <1_000_000-1;i++) {
                            downstream.accept(lineArray);
                        }
                    }
                }).map(line -> (Character[]) line)
                .toArray(Character[][]::new);
    }

    private static Character[][] duplicateEmptyLines(Character[][] lines) {
        return Arrays.stream(lines).mapMulti((line, downstream) -> {
                    downstream.accept(line);
                    if (Arrays.stream(line).noneMatch(c -> c == '#')) {
                        for(int i = 0; i <1_000_000-1;i++) {
                            downstream.accept(line);
                        }
                    }
                }).map(line -> (Character[]) line)
                .toArray(Character[][]::new);
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
