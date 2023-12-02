package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day2Part1 {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("src/main/resources/day2");
        var lines = Files.readAllLines(path);
        int sum = lines.stream().map(Day2Part1::mapToGame).filter(GameValidator::isValidGame).mapToInt(Game::getId).sum();
        System.out.println(sum);
    }

    public static Game mapToGame(String line) {
        Game.GameBuilder gameBuilder = Game.builder();
        String[] gameSplit = line.split(":");
        String idString = gameSplit[0].replace("Game ", "");
        gameBuilder.id(Integer.parseInt(idString));
        gameBuilder.gameReveals(Arrays.stream(gameSplit[1].split(";")).map(Day2Part1::mapToGameReveal).toList());

        return gameBuilder.build();
    }

    private static GameReveal mapToGameReveal(String s) {
        GameReveal.GameRevealBuilder gameRevealBuilder = GameReveal.builder();
        Arrays.stream(s.split(",")).forEach(revealString -> {
            String[] countAndColor = revealString.trim().split(" ");
            int count = Integer.parseInt(countAndColor[0]);
            String color = countAndColor[1];
            switch (color) {
                case "green" -> gameRevealBuilder.greenCount(count);
                case "red" -> gameRevealBuilder.redCount(count);
                case "blue" -> gameRevealBuilder.blueCount(count);
            }
        });
        return gameRevealBuilder.build();
    }
}