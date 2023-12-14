package org.example;

import lombok.*;
import org.apache.commons.lang3.tuple.Triple;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14Part1 {
    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day14");
        var lines = Files.readAllLines(path);

        Integer[][] rows = lines.stream().map(Day14Part1::parseLine).toArray(Integer[][]::new);
        var columns = MatrixUtils.transpose(rows);

        int sum = Arrays.stream(columns).map(Day14Part1::shiftRocks).mapToInt(Day14Part1::calculateWeight).sum();

        System.out.println(sum);
    }

    private static int calculateWeight(List<Support> supports) {
        return supports.stream().mapToInt(support -> triangular(support.getPosition()) - triangular(support.getPosition() - support.getWeight())).sum();
    }

    private static int triangular(int n) {
        return (n * (n+1)) / 2;
    }

    private static List<Support> shiftRocks(Integer[] rowElements) {
        int height = rowElements.length;
        LinkedList<Support> supports = new LinkedList<>();
        supports.add(new Support(height));
        int accumulatedWeight = 0;
        for(int row = 0; row < height; row++) {
            var columnElement = rowElements[row];
            if(columnElement == 1) {
                accumulatedWeight++;
            } else if(columnElement == 0) {
                supports.getLast().setWeight(accumulatedWeight);
                supports.add(new Support(height - row - 1));
                accumulatedWeight= 0;
            }
        }
        supports.getLast().setWeight(accumulatedWeight);
        return supports;
    }

    private static Integer[] parseLine(String s) {
        return s.chars().map(c -> switch (c) {
                    case 'O':
                        yield 1;
                    case '#':
                        yield 0;
                    case '.':
                        yield -1;
                    default:
                        throw new RuntimeException();
                })
                .boxed()
                .toArray(Integer[]::new);
    }

    @RequiredArgsConstructor
    @Getter
    static class Support {
        private final int position;
        @Setter
        private int weight;
    }
}
