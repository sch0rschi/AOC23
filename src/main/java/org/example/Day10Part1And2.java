package org.example;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10Part1And2 {

    public static final String RIGHT = "right";
    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String LEFT = "left";
    public static final String NOOP = "-";

    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day10");
        var lines = Files.readAllLines(path);
        ArrayList<ArrayList<Character>> gridMap = lines.stream()
                .map(line -> line.chars().mapToObj(c -> (char) c)
                        .collect(Collectors.toCollection(ArrayList<Character>::new)))
                .collect(Collectors.toCollection(ArrayList<ArrayList<Character>>::new));

        boolean[][] loopPathMarks = new boolean[gridMap.size()][gridMap.get(0).size()];
        String[][] loopDirectionMarks = new String[gridMap.size()][gridMap.get(0).size()];

        int sRow = IntStream.range(0, gridMap.size())
                .filter(index -> gridMap.get(index).stream().anyMatch(c -> 'S' == c))
                .findAny().orElse(-1);

        int sColumn = IntStream.range(0, gridMap.get(sRow).size())
                .filter(index -> 'S' == gridMap.get(sRow).get(index))
                .findAny().orElse(-1);

        int currentRow = sRow;
        int currentColumn = sColumn + 1;
        loopPathMarks[currentRow][currentColumn] = true;
        loopDirectionMarks[currentRow][currentColumn] = NOOP;
        int lengthCounter = 1;
        Character character = gridMap.get(currentRow).get(currentColumn);
        String direction = RIGHT;
        while ('S' != character) {
            lengthCounter++;
            if (RIGHT.equals(direction) && '-' == character) {
                loopDirectionMarks[currentRow][currentColumn] = NOOP;
                currentColumn++;
            } else if (RIGHT.equals(direction) && 'J' == character) {
                loopDirectionMarks[currentRow][currentColumn] = UP;
                currentRow--;
                direction = UP;
            } else if (UP.equals(direction) && 'F' == character) {
                loopDirectionMarks[currentRow][currentColumn] = UP;
                currentColumn++;
                direction = RIGHT;
            } else if (RIGHT.equals(direction) && '7' == character) {
                loopDirectionMarks[currentRow][currentColumn] = DOWN;
                currentRow++;
                direction = DOWN;
            } else if (DOWN.equals(direction) && 'L' == character) {
                loopDirectionMarks[currentRow][currentColumn] = DOWN;
                currentColumn++;
                direction = RIGHT;
            } else if (DOWN.equals(direction) && '|' == character) {
                loopDirectionMarks[currentRow][currentColumn] = DOWN;
                currentRow++;
            } else if (DOWN.equals(direction) && 'J' == character) {
                loopDirectionMarks[currentRow][currentColumn] = DOWN;
                currentColumn--;
                direction = LEFT;
            } else if (LEFT.equals(direction) && 'L' == character) {
                loopDirectionMarks[currentRow][currentColumn] = UP;
                currentRow--;
                direction = UP;
            } else if (UP.equals(direction) && '7' == character) {
                loopDirectionMarks[currentRow][currentColumn] = UP;
                currentColumn--;
                direction = LEFT;
            } else if (LEFT.equals(direction) && 'F' == character) {
                loopDirectionMarks[currentRow][currentColumn] = DOWN;
                currentRow++;
                direction = DOWN;
            } else if (LEFT.equals(direction) && '-' == character) {
                loopDirectionMarks[currentRow][currentColumn] = NOOP;
                currentColumn--;
            } else if (UP.equals(direction) && '|' == character) {
                loopDirectionMarks[currentRow][currentColumn] = UP;
                currentRow--;
            } else {
                System.out.println("direction: " + direction + " symbol: " + character + " is missing.");
                int i = 0;
            }
            loopPathMarks[currentRow][currentColumn] = true;
            character = gridMap.get(currentRow).get(currentColumn);
        }
        loopDirectionMarks[currentRow][currentColumn] = NOOP;

        System.out.println(lengthCounter/2);
        int sum = calculateNumberOfInnerTiles(loopPathMarks, loopDirectionMarks, gridMap);
        System.out.println(sum);
    }

    private static int calculateNumberOfInnerTiles(boolean[][] loopPathMarks, String[][] loopDirectionMarks, ArrayList<ArrayList<Character>> gridMap) {
        int counter = 0;
        for(int row = 0; row < gridMap.size(); row++) {
            boolean inner = false;
            String oldDirection = NOOP;
            for(int column = 0; column < gridMap.get(0).size(); column++) {
                if(loopPathMarks[row][column]) {
                    String direction = loopDirectionMarks[row][column];
                    Character character = gridMap.get(row).get(column);
                    if('|' == character) {
                        inner = !inner;
                        oldDirection = NOOP;
                    } else if(NOOP.equals(oldDirection) && !NOOP.equals(direction)){
                        oldDirection = direction;
                    } else if(!NOOP.equals(oldDirection) && oldDirection.equals(direction)) {
                        inner = !inner;
                        oldDirection = NOOP;
                    } else if(!NOOP.equals(oldDirection) && !NOOP.equals(direction)) {
                        oldDirection = NOOP;
                    }
                } else {
                    if(inner) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }
}
