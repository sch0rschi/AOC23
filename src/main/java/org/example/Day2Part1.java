package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Day2Part1 {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("src/main/resources/day2");
        var lines = Files.readAllLines(path);
        int sum = lines.stream().map(GameUtils::mapToGame).filter(GameUtils::isValidGame).mapToInt(Game::getId).sum();
        System.out.println(sum);
    }
}