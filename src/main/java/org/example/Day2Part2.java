package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Day2Part2 {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("src/main/resources/day2");
        var lines = Files.readAllLines(path);
        List<Game> games = lines.stream().map(GameUtils::mapToGame).toList();
        games.forEach(GameUtils::setPower);
        int sum = games.stream().mapToInt(Game::getPower).sum();
        System.out.println(sum);
    }

}