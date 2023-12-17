package org.example;

import lombok.*;

import java.util.*;

import static org.example.Constants.*;

public class Day16Part1 {

    @SneakyThrows
    public static void main(String... args) {
        int[][] contraption = ParseUtils.parseFileToCharMatrix("src/main/resources/day16");

        var map = mapContraptionToMap(contraption);

        Set<Coordinate> visitedCoordinates = new TreeSet<>();
        Set<Node> visitedNodes = new TreeSet<>();
        Stack<Node> stack = new Stack<>();
        stack.push(new Node(RIGHT, new Coordinate(0, 0)));

        while (!stack.isEmpty()) {
            Node pop = stack.pop();
            if (!visitedNodes.contains(pop)) {
                visitedNodes.add(pop);
                visitedCoordinates.add(pop.coordinate());
                map.get(pop).forEach(stack::push);
            }
        }

        System.out.println(visitedCoordinates.size());
    }

    private static Map<Node, List<Node>> mapContraptionToMap(int[][] contraption) {
        Map<Node, List<Node>> map = new HashMap<>();
        for (int row = 0; row < contraption.length; row++) {
            for (int column = 0; column < contraption[row].length; column++) {
                Node left = new Node(LEFT, new Coordinate(row, column - 1));
                Node up = new Node(UP, new Coordinate(row - 1, column));
                Node right = new Node(RIGHT, new Coordinate(row, column + 1));
                Node down = new Node(DOWN, new Coordinate(row + 1, column));
                switch (contraption[row][column]) {

                    case '.': {
                        map.put(new Node(LEFT, new Coordinate(row, column)), new ArrayList<>(List.of(left)));
                        map.put(new Node(UP, new Coordinate(row, column)), new ArrayList<>(List.of(up)));
                        map.put(new Node(RIGHT, new Coordinate(row, column)), new ArrayList<>(List.of(right)));
                        map.put(new Node(DOWN, new Coordinate(row, column)), new ArrayList<>(List.of(down)));
                        break;
                    }
                    case '/': {
                        map.put(new Node(LEFT, new Coordinate(row, column)), new ArrayList<>(List.of(down)));
                        map.put(new Node(UP, new Coordinate(row, column)), new ArrayList<>(List.of(right)));
                        map.put(new Node(RIGHT, new Coordinate(row, column)), new ArrayList<>(List.of(up)));
                        map.put(new Node(DOWN, new Coordinate(row, column)), new ArrayList<>(List.of(left)));
                        break;
                    }
                    case '\\': {
                        map.put(new Node(LEFT, new Coordinate(row, column)), new ArrayList<>(List.of(up)));
                        map.put(new Node(UP, new Coordinate(row, column)), new ArrayList<>(List.of(left)));
                        map.put(new Node(RIGHT, new Coordinate(row, column)), new ArrayList<>(List.of(down)));
                        map.put(new Node(DOWN, new Coordinate(row, column)), new ArrayList<>(List.of(right)));
                        break;
                    }
                    case '|': {
                        var split = new ArrayList<>(List.of(down, up));
                        map.put(new Node(LEFT, new Coordinate(row, column)), split);
                        map.put(new Node(UP, new Coordinate(row, column)), new ArrayList<>(List.of(up)));
                        map.put(new Node(RIGHT, new Coordinate(row, column)), split);
                        map.put(new Node(DOWN, new Coordinate(row, column)), new ArrayList<>(List.of(down)));
                        break;
                    }
                    case '-': {
                        var split = new ArrayList<>(List.of(right, left));
                        map.put(new Node(LEFT, new Coordinate(row, column)), new ArrayList<>(List.of(left)));
                        map.put(new Node(UP, new Coordinate(row, column)), split);
                        map.put(new Node(RIGHT, new Coordinate(row, column)), new ArrayList<>(List.of(right)));
                        map.put(new Node(DOWN, new Coordinate(row, column)), split);
                    }
                }
            }
        }
        map.values().forEach(list -> list.removeIf(to -> to.coordinate().getRow() < 0
                || to.coordinate().getRow() >= contraption.length
                || to.coordinate().getColumn() < 0
                || to.coordinate().getColumn() >= contraption[to.coordinate().getRow()].length));
        return map;
    }

    record Node(String direction, Coordinate coordinate) implements Comparable<Node> {
        @Override
        public int compareTo(Node that) {
            int directionComparison = this.direction.compareTo(that.direction);
            if (directionComparison != 0) {
                return directionComparison < 0 ? -1 : 1;
            }

            return this.coordinate.compareTo(that.coordinate);
        }
    }
}
