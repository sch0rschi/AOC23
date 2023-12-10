package org.example;

import lombok.SneakyThrows;
import org.apache.commons.math3.stat.descriptive.moment.SemiVariance;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10Part1 {

    public static final String RIGHT = "right";
    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String LEFT = "left";

    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day10");
        var lines = Files.readAllLines(path);
        ArrayList<ArrayList<Character>> gridMap = lines.stream()
                .map(line -> line.chars().mapToObj(c -> (char) c)
                        .collect(Collectors.toCollection(ArrayList<Character>::new)))
                .collect(Collectors.toCollection(ArrayList<ArrayList<Character>>::new));

        int sRow = IntStream.range(0, gridMap.size())
                .filter(index -> gridMap.get(index).stream().anyMatch(c -> 'S' == c))
                .findAny().orElse(-1);

        int sColumn = IntStream.range(0, gridMap.get(sRow).size())
                .filter(index -> 'S' == gridMap.get(sRow).get(index))
                .findAny().orElse(-1);

        int currentRow = sRow;
        int currentColumn = sColumn + 1;
        int lengthCounter = 1;
        Character character = gridMap.get(currentRow).get(currentColumn);
        String direction = RIGHT;
        while ('S' != character) {
            lengthCounter++;
            if (RIGHT.equals(direction) && '-' == character) {
                currentColumn++;
            } else if (RIGHT.equals(direction) && 'J' == character) {
                currentRow--;
                direction = UP;
            } else if (UP.equals(direction) && 'F' == character) {
                currentColumn++;
                direction = RIGHT;
            } else if (RIGHT.equals(direction) && '7' == character) {
                currentRow++;
                direction = DOWN;
            } else if (DOWN.equals(direction) && 'L' == character) {
                currentColumn++;
                direction = RIGHT;
            } else if (DOWN.equals(direction) && '|' == character) {
                currentRow++;
            } else if (DOWN.equals(direction) && '7' == character) {
                currentColumn++;
                direction = RIGHT;
            } else if (DOWN.equals(direction) && 'J' == character) {
                currentColumn--;
                direction = LEFT;
            } else if (LEFT.equals(direction) && 'L' == character) {
                currentRow--;
                direction = UP;
            } else if (UP.equals(direction) && '7' == character) {
                currentColumn--;
                direction = LEFT;
            } else if (LEFT.equals(direction) && 'F' == character) {
                currentRow++;
                direction = DOWN;
            } else if (LEFT.equals(direction) && '-' == character) {
                currentColumn--;
            } else if (UP.equals(direction) && '|' == character) {
                currentRow--;
            } else {
                System.out.println("direction: " + direction + " symbol: " + character + " is missing.");
                int i = 0;
            }
            character = gridMap.get(currentRow).get(currentColumn);
            System.out.println(currentRow + " " + currentColumn + " " +character);
        }
        System.out.println(lengthCounter / 2);
    }
}
