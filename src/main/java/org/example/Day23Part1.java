package org.example;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day23Part1 {


    @SneakyThrows
    public static void main(String... args) {
        int[][] map = ParseUtils.parseFileToCharMatrix("src/main/resources/day23");
        int startIndex = Arrays.stream(map[0]).boxed().toList().indexOf((int) '.');
        int endIndex = Arrays.stream(map[map.length - 1]).boxed().toList().indexOf((int) '.');

        int max = traverse(map, new Coordinate(0, startIndex), new Coordinate(map.length - 1, endIndex));
        System.out.println(max-1);
    }

    static int traverse(int[][] map, Coordinate currentCoordinate, Coordinate targetCoordinate) {
        if (currentCoordinate.equals(targetCoordinate)) {
            return 1;
        }
        List<Coordinate> expansions = createExpansions(map, currentCoordinate);
        int backtrackSymbol = map[currentCoordinate.getRow()][currentCoordinate.getColumn()];
        map[currentCoordinate.getRow()][currentCoordinate.getColumn()] = 'O';
        int max = 0;
        for (var expansion :expansions) {
            max = Math.max(max, traverse(map, expansion, targetCoordinate));
        }
        map[currentCoordinate.getRow()][currentCoordinate.getColumn()] = backtrackSymbol;
        return max > 0 ? max + 1 : Integer.MIN_VALUE;
    }

    private static List<Coordinate> createExpansions(int[][] map, Coordinate source) {
        List<Coordinate> expansions = new ArrayList<>();
        var srcRow = source.getRow();
        var srcColumn = source.getColumn();
        if (map[srcRow][srcColumn] == '^') {
            if(map[srcRow - 1][srcColumn] != '#' && map[srcRow - 1][srcColumn] != 'O') {
                expansions.add(new Coordinate(srcRow - 1, srcColumn));
            }
        } else if (map[srcRow][srcColumn] == '<') {
            if(map[srcRow][srcColumn - 1] != '#' && map[srcRow][srcColumn - 1] != 'O') {
                expansions.add(new Coordinate(srcRow, srcColumn - 1));
            }
        } else if (map[srcRow][srcColumn] == 'v') {
            if(map[srcRow + 1][srcColumn] != '#' && map[srcRow + 1][srcColumn] != 'O') {
                expansions.add(new Coordinate(srcRow + 1, srcColumn));
            }
        } else if (map[srcRow][srcColumn] == '>') {
            if(map[srcRow][srcColumn + 1] != '#' && map[srcRow][srcColumn + 1] != 'O') {
                expansions.add(new Coordinate(srcRow, srcColumn + 1));
            }
        } else {
            if (srcRow - 1 >= 0 && map[srcRow - 1][srcColumn] != '#' && map[srcRow - 1][srcColumn] != 'O') {
                expansions.add(new Coordinate(srcRow - 1, srcColumn));
            }
            if (srcRow + 1 < map.length && map[srcRow + 1][srcColumn] != '#' && map[srcRow + 1][srcColumn] != 'O') {
                expansions.add(new Coordinate(srcRow + 1, srcColumn));
            }
            if (srcColumn - 1 >= 0 && map[srcRow][srcColumn - 1] != '#' && map[srcRow][srcColumn - 1] != 'O') {
                expansions.add(new Coordinate(srcRow, srcColumn - 1));
            }
            if (srcColumn + 1 < map[srcRow].length && map[srcRow][srcColumn + 1] != '#' && map[srcRow][srcColumn + 1] != 'O') {
                expansions.add(new Coordinate(srcRow, srcColumn + 1));
            }
        }
        return expansions;
    }


}
