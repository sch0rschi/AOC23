package org.example;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

public class Day7Part1 {
    @SneakyThrows
    public static void main (String... args) {
        Path path = Paths.get("src/main/resources/day7");
        var lines = Files.readAllLines(path);

        List<Hand> list = lines.stream().map(HandUtils::parseHand).sorted().toList();

        long sum = 0;
        for(int i = 0; i < list.size(); i++) {
            sum += list.get(i).bid * (i+1);
        }
        System.out.println(sum);
    }
}
