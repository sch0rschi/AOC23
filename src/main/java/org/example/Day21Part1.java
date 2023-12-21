package org.example;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Day21Part1 {

    @SneakyThrows
    public static void main(String... args) {
        int[][] garden = ParseUtils.parseFileToCharMatrix("src/main/resources/day21");
        Coordinate startingCoordinates = getStartingCoordinates(garden);
        Set<Coordinate> currentReachable = new HashSet<>();
        currentReachable.add(startingCoordinates);
        Set<Coordinate> nextReachable;

        int sum = 1;
        for(int step = 1; step <= 6; step++) {
            nextReachable = new HashSet<>();
            for (var coordinate : currentReachable) {
                Collection<Coordinate> expansions = getExpansions(coordinate, garden);
                for (Coordinate expansion : expansions) {
                    garden[expansion.getRow()][expansion.getColumn()] = 'O';
                }
                nextReachable.addAll(expansions);
                if(step%2==0) {
                    sum += expansions.size();
                }
            }
            currentReachable = nextReachable;
        }

        MatrixUtils.printPattern(garden);
        System.out.println(sum);
    }

    private static Collection<Coordinate> getExpansions(Coordinate coordinate, int[][] garden) {
        return Stream.of(coordinate.shiftUp(), coordinate.shiftLeft(), coordinate.shiftDown(garden.length), coordinate.shiftRight(garden[0].length))
                .filter(Optional::isPresent).map(Optional::get)
                .filter(newCoordinate -> garden[newCoordinate.getRow()][newCoordinate.getColumn()] == '.')
                .toList();
    }

    private static Coordinate getStartingCoordinates(int[][] garden) {
        for(int row = 0; row < garden.length; row++) {
            for (int column = 0; column < garden[row].length; column++) {
                if(garden[row][column] == 'S') {
                    return new Coordinate(row, column);
                }
            }
        }
        throw new RuntimeException();
    }
}
