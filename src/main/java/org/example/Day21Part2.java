package org.example;

import lombok.SneakyThrows;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class Day21Part2 {

    @SneakyThrows
    public static void main(String... args) {
        // Use the Information gained by the outputs from the programme and combine it with
        // https://docs.google.com/spreadsheets/d/1M-oZTaCZP-zy0jpybn03z50H4_LxHGGEvxJDxd9Yei8
        int[][] garden = ParseUtils.parseFileToCharMatrix("src/main/resources/day21AlternativeWithoutStart");
        Set<Coordinate> currentReachable = new HashSet<>();
        Set<Coordinate> nextReachable;


        int sum = 0;
        Coordinate startCoordinate = new Coordinate(65, 0);
        currentReachable.add(startCoordinate);
        //for(int step = 1; step <= 65; step++) { // small triangle
        //for (int step = 1; step <= 196; step++) { // bigger pentagon
        // for(int step = 1; step <= 300; step++) { // full pattern
        for (int step = 1; step <= 130; step++) { // pointyboys
            nextReachable = new HashSet<>();
            for (var coordinate : currentReachable) {
                Collection<Coordinate> expansions = getExpansions(coordinate, garden);
                for (Coordinate expansion : expansions) {
                    garden[expansion.getRow()][expansion.getColumn()] = 'O';
                }
                nextReachable.addAll(expansions);
                if ((step + startCoordinate.getRow() + startCoordinate.getColumn()) % 2 == 1) { // parity changes, modify here accordingly, attention: starting coordinate!
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
}
