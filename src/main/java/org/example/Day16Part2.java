package org.example;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.Value;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.example.Constants.*;

public class Day16Part2 {

    @SneakyThrows
    public static void main(String... args) {

        int[][] contraption = ParseUtils.parseFileToCharMatrix("src/main/resources/day16");

        Instant start = Instant.now();

        var map = mapContraptionToMap(contraption);

        List<Node> startNodes = getStartNodes(contraption);

        var max = startNodes.stream().mapToInt(startNode -> countEnergizedTiles(startNode, map)).max().orElse(-1);

        Instant end = Instant.now();
        System.out.println(max + " took: " + Duration.between(start, end));
    }

    private static List<Node> getStartNodes(int[][] contraption) {
        List<Node> startNodes = new ArrayList<>();
        for (int row = 0; row < contraption.length; row++) {
            startNodes.add(new Node(R, new Coordinate(row, 0)));
            startNodes.add(new Node(L, new Coordinate(row, contraption[row].length - 1)));
        }
        for (int column = 0; column < contraption[0].length; column++) {
            startNodes.add(new Node(D, new Coordinate(0, column)));
            startNodes.add(new Node(L, new Coordinate(contraption.length - 1, column)));
        }
        return startNodes;
    }

    private static int countEnergizedTiles(Node startNode, Map<Node, List<Node>> map) {
        Set<Coordinate> visitedCoordinates = new HashSet<>();
        Set<Node> visitedNodes = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        stack.push(startNode);

        while (!stack.isEmpty()) {
            Node pop = stack.pop();
            if (!visitedNodes.contains(pop)) {
                visitedNodes.add(pop);
                visitedCoordinates.add(pop.getCoordinate());
                map.get(pop).forEach(stack::push);
            }
        }
        return visitedCoordinates.size();
    }

    private static Map<Node, List<Node>> mapContraptionToMap(int[][] contraption) {
        Map<Node, List<Node>> map = new HashMap<>();
        for (int row = 0; row < contraption.length; row++) {
            for (int column = 0; column < contraption[row].length; column++) {
                Node left = new Node(L, new Coordinate(row, column - 1));
                Node up = new Node(U, new Coordinate(row - 1, column));
                Node right = new Node(R, new Coordinate(row, column + 1));
                Node down = new Node(D, new Coordinate(row + 1, column));
                switch (contraption[row][column]) {

                    case '.': {
                        map.put(new Node(L, new Coordinate(row, column)), new ArrayList<>(List.of(left)));
                        map.put(new Node(U, new Coordinate(row, column)), new ArrayList<>(List.of(up)));
                        map.put(new Node(R, new Coordinate(row, column)), new ArrayList<>(List.of(right)));
                        map.put(new Node(D, new Coordinate(row, column)), new ArrayList<>(List.of(down)));
                        break;
                    }
                    case '/': {
                        map.put(new Node(L, new Coordinate(row, column)), new ArrayList<>(List.of(down)));
                        map.put(new Node(U, new Coordinate(row, column)), new ArrayList<>(List.of(right)));
                        map.put(new Node(R, new Coordinate(row, column)), new ArrayList<>(List.of(up)));
                        map.put(new Node(D, new Coordinate(row, column)), new ArrayList<>(List.of(left)));
                        break;
                    }
                    case '\\': {
                        map.put(new Node(L, new Coordinate(row, column)), new ArrayList<>(List.of(up)));
                        map.put(new Node(U, new Coordinate(row, column)), new ArrayList<>(List.of(left)));
                        map.put(new Node(R, new Coordinate(row, column)), new ArrayList<>(List.of(down)));
                        map.put(new Node(D, new Coordinate(row, column)), new ArrayList<>(List.of(right)));
                        break;
                    }
                    case '|': {
                        var split = new ArrayList<>(List.of(down, up));
                        map.put(new Node(L, new Coordinate(row, column)), split);
                        map.put(new Node(U, new Coordinate(row, column)), new ArrayList<>(List.of(up)));
                        map.put(new Node(R, new Coordinate(row, column)), split);
                        map.put(new Node(D, new Coordinate(row, column)), new ArrayList<>(List.of(down)));
                        break;
                    }
                    case '-': {
                        var split = new ArrayList<>(List.of(right, left));
                        map.put(new Node(L, new Coordinate(row, column)), new ArrayList<>(List.of(left)));
                        map.put(new Node(U, new Coordinate(row, column)), split);
                        map.put(new Node(R, new Coordinate(row, column)), new ArrayList<>(List.of(right)));
                        map.put(new Node(D, new Coordinate(row, column)), split);
                    }
                }
            }
        }
        map.values().forEach(list -> list.removeIf(to -> to.getCoordinate().getRow() < 0
                || to.getCoordinate().getRow() >= contraption.length
                || to.getCoordinate().getColumn() < 0
                || to.getCoordinate().getColumn() >= contraption[to.getCoordinate().getRow()].length));
        return map;
    }

    @Value
    @EqualsAndHashCode
    static class Node implements Comparable<Node> {

        char direction;
        Coordinate coordinate;

        @Override
        public int compareTo(Node that) {
            if (this.direction != that.direction) {
                return this.direction - that.direction;
            }
            return this.coordinate.compareTo(that.coordinate);
        }
    }

}
