package org.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.github.jamm.MemoryMeter;

import java.io.ByteArrayOutputStream;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;

public class Day14Part2 {

    public static final int HEIGHT = 100;
    public static final byte ROCK = 9;
    public static final byte EMPTY = 5;
    public static final byte STABLE = 0;

    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day14");
        var lines = Files.readAllLines(path);

        byte[][] columns1 = lines.stream().map(Day14Part2::parseLine).toArray(byte[][]::new);

        Instant start = Instant.now();

        MatrixWrapper matrixWrapper = new MatrixWrapper(columns1);
        var columns2 = new byte[HEIGHT][HEIGHT];

        MatrixUtils.rotateCounterClockwise(columns1, columns2);

        LinkedHashSet<MatrixWrapper> arrangements = new LinkedHashSet<>();
        rotateOnce(columns2, columns1);
        int counter = 1;
        while (arrangements.add(matrixWrapper.clone()) && counter <= 1_000_000_000) {
            rotateOnce(columns2, columns1);
            counter++;
        }

        long inserted = arrangements.stream().takeWhile(m -> !m.equals(matrixWrapper)).count();
        long loopLength = arrangements.size() - inserted;

        long remainder = 1_000_000_000L - inserted;
        remainder = remainder % loopLength;

        var iterator = arrangements.iterator();
        for (int i = 0; i < remainder + inserted; i++) {
            columns1 = iterator.next().matrix();
        }
        MatrixUtils.rotateCounterClockwise(columns1, columns2);
        MatrixUtils.rotateCounterClockwise(columns2, columns1);

        Instant end = Instant.now();
        System.out.println(Day14Part2.calculateWeight(columns1) + " took: " + Duration.between(start, end));
    }

    private static void rotateOnce(byte[][] matrix1, byte[][] matrix2) {
        shiftRocks(matrix1);
        MatrixUtils.rotateClockwise(matrix1, matrix2);
        shiftRocks(matrix2);
        MatrixUtils.rotateClockwise(matrix2, matrix1);
        shiftRocks(matrix1);
        MatrixUtils.rotateClockwise(matrix1, matrix2);
        shiftRocks(matrix2);
        MatrixUtils.rotateClockwise(matrix2, matrix1);
    }

    private static int calculateWeight(byte[][] matrix) {
        return IntStream.range(0, HEIGHT).map(row -> (HEIGHT - row) * (int) countRocks(matrix[row])).sum();
    }

    private static Object countRocks(byte[] matrix) {
        var sum = 0;
        for(var byt : matrix) {
            if(ROCK == byt) {
                sum++;
            }
        }
        return sum;
    }

    private static void shiftRocks(byte[][] matrix) {
        for (byte[] row : matrix) {
            int lastEmptyIndex = -1;

            for (int i = 0; i < row.length; i++) {
                int currentElement = row[i];

                if (currentElement == ROCK && lastEmptyIndex >= 0) {
                    row[lastEmptyIndex++] = ROCK;
                    row[i] = EMPTY;
                } else if (currentElement == EMPTY) {
                    lastEmptyIndex = (lastEmptyIndex < 0) ? i : lastEmptyIndex;
                } else {
                    lastEmptyIndex = -1;
                }
            }
        }
    }

    private static byte[] parseLine(String s) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        s.chars().mapToObj(c -> switch (c) {
                    case 'O':
                        yield ROCK;
                    case '#':
                        yield STABLE;
                    case '.':
                        yield EMPTY;
                    default:
                        throw new RuntimeException();
                }).forEach(byteArrayOutputStream::write);
        return byteArrayOutputStream.toByteArray();
    }

    @RequiredArgsConstructor
    @Getter
    static class Support {
        private final int position;
        private final boolean first;

        @Setter
        private int weight;
        @Setter
        private int nothing;
    }

    record MatrixWrapper(byte[][] matrix) {
        public MatrixWrapper clone() {
            return new MatrixWrapper(Arrays.stream(matrix()).map(byte[]::clone).toArray(byte[][]::new));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MatrixWrapper that = (MatrixWrapper) o;

            return Arrays.deepEquals(matrix, that.matrix);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(matrix);
        }
    }
}
