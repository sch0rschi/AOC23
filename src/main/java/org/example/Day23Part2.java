package org.example;

import lombok.SneakyThrows;
import lombok.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day23Part2 {


    @SneakyThrows
    public static void main(String... args) {
        int[][] map = ParseUtils.parseFileToCharMatrix("src/main/resources/day23");
        boolean[][] forkMap = new boolean[map.length][map[0].length];
        List<Path>[][] jumpMap = (List<Path>[][]) new List[map.length][map[0].length];

        int startIndex = Arrays.stream(map[0]).boxed().toList().indexOf((int) '.');
        int endIndex = Arrays.stream(map[map.length - 1]).boxed().toList().indexOf((int) '.');

        Coordinate startCoordinate = new Coordinate(0, startIndex);
        Coordinate targetCoordinate = new Coordinate(map.length - 1, endIndex);
        markForks(map, forkMap, jumpMap);
        jumpMap[0][startIndex] = new ArrayList<>();
        createJumpMap(map, forkMap, jumpMap, startCoordinate);

        int max = traverse(map, jumpMap, startCoordinate, targetCoordinate);
        System.out.println(max-1);
    }

    private static void createJumpMap(int[][] map, boolean[][] forkMap, List<Path>[][] jumpMap, Coordinate startCoordinate) {
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[row].length; column++) {
                if (forkMap[row][column]) {
                    Coordinate source = new Coordinate(row, column);
                    List<Coordinate> expansions = createExpansions(map, source);
                    for(var expansion : expansions) {
                        Path path = findPath(map, forkMap, source, expansion);
                        if(path.getTo().equals(startCoordinate)) {
                            jumpMap[startCoordinate.getRow()][startCoordinate.getColumn()].add(new Path(startCoordinate, source, path.getLength()));
                        }
                        jumpMap[source.getRow()][source.getColumn()].add(new Path(source, path.getTo(), path.getLength() + 1));
                    }
                }
            }
        }
    }

    private static Path findPath(int[][] map, boolean[][] forkMap, Coordinate source, Coordinate expansion) {
        map[source.getRow()][source.getColumn()] = '#';
        List<Coordinate> path = new ArrayList<>();
        while (!forkMap[expansion.getRow()][expansion.getColumn()]) {
            path.add(expansion);
            map[expansion.getRow()][expansion.getColumn()] = '#';
            List<Coordinate> expansions = createExpansions(map, expansion);
            if (expansions.size() == 1) {
                expansion = expansions.get(0);
            } else {
                break;
            }
        }
        path.forEach(c -> map[c.getRow()][c.getColumn()] = '.');
        map[source.getRow()][source.getColumn()] = '.';
        return new Path(source, expansion, path.size());
    }

    static void markForks(int[][] map, boolean[][] forkMap, List<Path>[][] jumpMap) {
        for (int row = 0; row < map.length; row++) {
            for (int column = 0; column < map[row].length; column++) {
                if (map[row][column] != '#') {
                    Coordinate source = new Coordinate(row, column);
                    List<Coordinate> expansions = createExpansions(map, source);
                    if (expansions.size() > 2) {
                        forkMap[source.getRow()][source.getColumn()] = true;
                        jumpMap[source.getRow()][source.getColumn()] = new ArrayList<>();
                    }
                }
            }
        }
    }

    static int traverse(int[][] map, List<Path>[][] jumpMap, Coordinate currentCoordinate, Coordinate targetCoordinate) {
        if (currentCoordinate.equals(targetCoordinate)) {
            return 0;
        } else if (map[currentCoordinate.getRow()][currentCoordinate.getColumn()] == '#') {
            return Integer.MIN_VALUE;
        }
        var paths = jumpMap[currentCoordinate.getRow()][currentCoordinate.getColumn()];
        int backtrackSymbol = map[currentCoordinate.getRow()][currentCoordinate.getColumn()];
        map[currentCoordinate.getRow()][currentCoordinate.getColumn()] = '#';
        int max = 0;
        for (var path : paths) {
            max = Math.max(max, traverse(map, jumpMap, path.getTo(), targetCoordinate) + path.getLength());
        }
        map[currentCoordinate.getRow()][currentCoordinate.getColumn()] = backtrackSymbol;
        return max > 0 ? max : Integer.MIN_VALUE;
    }

    private static List<Coordinate> createExpansions(int[][] map, Coordinate source) {
        List<Coordinate> expansions = new ArrayList<>();
        var srcRow = source.getRow();
        var srcColumn = source.getColumn();

        if (srcRow - 1 >= 0 && map[srcRow - 1][srcColumn] != '#') {
            expansions.add(new Coordinate(srcRow - 1, srcColumn));
        }
        if (srcRow + 1 < map.length && map[srcRow + 1][srcColumn] != '#') {
            expansions.add(new Coordinate(srcRow + 1, srcColumn));
        }
        if (srcColumn - 1 >= 0 && map[srcRow][srcColumn - 1] != '#') {
            expansions.add(new Coordinate(srcRow, srcColumn - 1));
        }
        if (srcColumn + 1 < map[srcRow].length && map[srcRow][srcColumn + 1] != '#') {
            expansions.add(new Coordinate(srcRow, srcColumn + 1));
        }
        return expansions;
    }

    @Value
    static class Path {
        Coordinate from;
        Coordinate to;
        int length;
    }


}
