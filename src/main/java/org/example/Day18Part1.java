package org.example;

import lombok.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.example.Constants.*;

public class Day18Part1 {

    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day18");
        var lines = Files.readAllLines(path);

        List<Dig> digs = lines.stream().map(Day18Part1::mapToDig).toList();

        Coordinate currentStartCoordinate = new Coordinate(0, 0);
        for (Dig hole : digs) {
            dig(currentStartCoordinate, hole);
            currentStartCoordinate = hole.getEnd();
        }


        for (int i = 0; i < digs.size(); i++) {
            var prev = digs.get((i - 1 + digs.size()) % digs.size());
            var succ = digs.get((i + 1 + digs.size()) % digs.size());
            var actual = digs.get(i);
            actual.setPole(HORIZONTAL_DIRECTIONS.contains(actual.getDirection()) && prev.getDirection() != succ.getDirection());
        }

        Map<Integer, List<Dig>> rowToDigs = new HashMap<>();
        for (var dig : digs) {
            for (int row = Math.min(dig.getStart().getRow(), dig.getEnd().getRow());
                 row <= Math.max(dig.getStart().getRow(), dig.getEnd().getRow());
                 row++
            ) {
                rowToDigs.computeIfAbsent(row, (ignore) -> new ArrayList<>()).add(dig);
            }
        }
        rowToDigs.values().forEach(rowDigs -> rowDigs.sort(Comparator.comparingInt(dig -> dig.getStart().getColumn() + dig.getEnd().getColumn())));

        var minRow = digs.stream().mapToInt(dig -> dig.getStart().getRow()).min().orElseThrow();
        var maxRow = digs.stream().mapToInt(dig -> dig.getStart().getRow()).max().orElseThrow();

        var sum = 0;
        for (int row = minRow; row <= maxRow; row++) {
            var isInside = false;
            int from = 0;
            for (var dig : rowToDigs.get(row)) {
                sum += Math.abs(dig.getStart().getColumn() - dig.getEnd().getColumn()) + 1;
                if (isInside) {
                    sum +=  Math.max(0,
                            Math.min(dig.getStart().getColumn(), dig.getEnd().getColumn()) - from);
                }
                from = Math.max(dig.getStart().getColumn(), dig.getEnd().getColumn()) + 1;
                isInside = dig.isPole() == isInside;
            }
            if (isInside) {
                throw new RuntimeException();
            }
        }
        sum -= digs.size();

        System.out.println(sum);
    }

    private static void dig(Coordinate currentStartCoordinate, Dig hole) {
        switch (hole.getDirection()) {
            case U -> {
                hole.setStart(currentStartCoordinate);
                hole.setEnd(new Coordinate(currentStartCoordinate.getRow() - hole.length, currentStartCoordinate.getColumn()));
            }
            case L -> {
                hole.setStart(currentStartCoordinate);
                hole.setEnd(new Coordinate(currentStartCoordinate.getRow(), currentStartCoordinate.getColumn() - hole.length));
            }
            case D -> {
                hole.setStart(currentStartCoordinate);
                hole.setEnd(new Coordinate(currentStartCoordinate.getRow() + hole.length, currentStartCoordinate.getColumn()));
            }
            case R -> {
                hole.setStart(currentStartCoordinate);
                hole.setEnd(new Coordinate(currentStartCoordinate.getRow(), currentStartCoordinate.getColumn() + hole.length));
            }
            default -> throw new RuntimeException();
        }
    }

    private static Dig mapToDig(String line) {
        String[] split = line.split(" ");
        char direction = split[0].toLowerCase().charAt(0);
        int length = Integer.parseInt(split[1]);

        return Dig.builder().direction(direction).length(length).build();
    }

    @Builder
    @EqualsAndHashCode
    @ToString
    @Getter
    static class Dig {
        @Setter
        private Coordinate start;
        @Setter
        private Coordinate end;
        private char direction;
        private int length;
        @Setter
        private boolean isPole;
    }
}
