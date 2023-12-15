package org.example;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day15Part1 {
    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day15");
        var lines = Files.readString(path).strip();
        String[] instructions = lines.split(",");
        int sum = Arrays.stream(instructions).mapToInt(Day15Part1::hash).peek(System.out::println).sum();
        System.out.println(sum);
    }

    private static int hash(String s) {
        return s.chars().reduce(0, (accu, add) -> {
            accu += add;
            accu *= 17;
            accu %= 256;
            return accu;
        });
    }
}
