package org.example;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

import static org.example.HandUtils.*;

public class Day7Part2 {
    @SneakyThrows
    public static void main (String... args) {
        Path path = Paths.get("src/main/resources/day7");
        var lines = Files.readAllLines(path);

        JACK_RANK = -1L;
        CARD_MAPPING.put('J', JACK_RANK);
        List<Hand> list = lines.stream().map(HandUtils::parseHandPart2).sorted(HAND_COMPARATOR).toList();
        long sum = IntStream.range(0, list.size()).mapToLong(i -> (i + 1) * list.get(i).getBid()).sum();
        System.out.println(sum);
    }
}
