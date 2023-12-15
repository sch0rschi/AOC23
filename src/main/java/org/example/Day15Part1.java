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
        var line = Files.readString(path).strip();
        String[] instructions = line.split(",");
        int sum = Arrays.stream(instructions).mapToInt(HashUtils::hash).peek(System.out::println).sum();
        System.out.println(sum);
    }
}
