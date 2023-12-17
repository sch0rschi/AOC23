package org.example;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.Value;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day17Part1 {

    public static final char LEFT = 'l';
    public static final char UP = 'u';
    public static final char RIGHT = 'r';
    public static final char DOWN = 'd';
    public static final char START = 's';

    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day17");
        var lines = Files.readAllLines(path);
        int[][] heatLosses = lines.stream().map(Day17Part1::mapDigitsToIntegers).toArray(int[][]::new);

        PriorityQueue<SearchNode> openList = new PriorityQueue<>();
        Set<SearchNode> closedList = new HashSet<>();

        openList.add(new SearchNode(new Coordinate(0, 0), START, 0));

        int minHeatLoss = -1;
        var destination = new Coordinate(heatLosses.length - 1, heatLosses[heatLosses.length - 1].length - 1);
        while (!openList.isEmpty()) {
            SearchNode toExtend = openList.poll();
            if (destination.equals(toExtend.getCoordinate())) {
                minHeatLoss = toExtend.lowerBoundHeatLoss;
                break;
            }
            if (!closedList.contains(toExtend)) {
                closedList.add(toExtend);
                openList.addAll(getExtensions(toExtend, heatLosses));
            }
        }

        System.out.println(minHeatLoss);
    }

    public static int[] mapDigitsToIntegers(String digitString) {
        // Convert the string to an int array using Java streams
        return digitString.chars()
                .map(Character::getNumericValue)
                .toArray();
    }

    private static Collection<SearchNode> getExtensions(SearchNode toExtend, int[][] heatLosses) {
        int startRow = toExtend.getCoordinate().getRow();
        int startColumn = toExtend.getCoordinate().getColumn();
        Collection<SearchNode> extensions = new ArrayList<>();

        switch (toExtend.direction) {
            case UP:
            case DOWN: {
                addLeftRightExtensions(toExtend.getLowerBoundHeatLoss(), heatLosses, startColumn, startRow, extensions);
                break;
            }
            case RIGHT:
            case LEFT: {
                addUpDownExtensions(toExtend.getLowerBoundHeatLoss(), heatLosses, startRow, startColumn, extensions);
                break;
            }
            case START: {
                addLeftRightExtensions(toExtend.getLowerBoundHeatLoss(), heatLosses, startColumn, startRow, extensions);
                addUpDownExtensions(toExtend.getLowerBoundHeatLoss(), heatLosses, startRow, startColumn, extensions);
                break;
            }
            default:
                throw new RuntimeException();
        }

        return extensions;
    }

    private static void addLeftRightExtensions(int startHeatLoss, int[][] heatLosses, int startColumn, int startRow, Collection<SearchNode> extensions) {
        int accumulatedHeatLoss = startHeatLoss;
        for (int column = startColumn + 1; column < Math.min(heatLosses[startRow].length, startColumn + 4); column++) {
            Coordinate newCoordinate = new Coordinate(startRow, column);
            accumulatedHeatLoss += heatLosses[startRow][column];
            extensions.add(new SearchNode(newCoordinate, RIGHT, accumulatedHeatLoss));
        }
        accumulatedHeatLoss = startHeatLoss;
        for (int column = startColumn - 1; column > Math.max(-1, startColumn - 4); column--) {
            Coordinate newCoordinate = new Coordinate(startRow, column);
            accumulatedHeatLoss += heatLosses[startRow][column];
            extensions.add(new SearchNode(newCoordinate, LEFT, accumulatedHeatLoss));
        }
    }

    private static void addUpDownExtensions(int startHeatLoss, int[][] heatLosses, int startRow, int startColumn, Collection<SearchNode> extensions) {
        int accumulatedHeatLoss = startHeatLoss;
        for (int row = startRow + 1; row < Math.min(heatLosses.length, startRow + 4); row++) {
            Coordinate newCoordinate = new Coordinate(row, startColumn);
            accumulatedHeatLoss += heatLosses[row][startColumn];
            extensions.add(new SearchNode(newCoordinate, DOWN, accumulatedHeatLoss));
        }
        accumulatedHeatLoss = startHeatLoss;
        for (int row = startRow - 1; row > Math.max(-1, startRow - 4); row--) {
            Coordinate newCoordinate = new Coordinate(row, startColumn);
            accumulatedHeatLoss += heatLosses[row][startColumn];
            extensions.add(new SearchNode(newCoordinate, UP, accumulatedHeatLoss));
        }
    }

    @Value
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    static class SearchNode implements Comparable<SearchNode> {
        @EqualsAndHashCode.Include
        Coordinate coordinate;
        @EqualsAndHashCode.Include
        char direction;
        int lowerBoundHeatLoss;

        @Override
        public int compareTo(SearchNode that) {
            if (this.getLowerBoundHeatLoss() != that.getLowerBoundHeatLoss()) {
                return (this.getLowerBoundHeatLoss() < that.getLowerBoundHeatLoss() ? -1 : 1);
            }

            int coordinateComparison = this.getCoordinate().compareTo(that.getCoordinate());
            if (coordinateComparison != 0) {
                return coordinateComparison > 0 ? -1 : 1;
            }


            return 0;
        }
    }
}
