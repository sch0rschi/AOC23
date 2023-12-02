package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.max;

public class Day2Part2 {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("src/main/resources/day2");
        var lines = Files.readAllLines(path);
        List<Game> games = lines.stream().map(Day2Part2::mapToGame).toList();
        games.forEach(Day2Part2::setPower);
        int sum = games.stream().mapToInt(Game::getPower).sum();
        System.out.println(sum);
    }

    private static void setPower(Game game) {
        int maxBlueCount = 0;
        int maxRedCount = 0;
        int maxGreenCount = 0;
        for(var gameReveal : game.getGameReveals()) {
            maxBlueCount = max(maxBlueCount ,gameReveal.getBlueCount());
            maxRedCount = max(maxRedCount ,gameReveal.getRedCount());
            maxGreenCount = max(maxGreenCount ,gameReveal.getGreenCount());
        }
        game.power = maxBlueCount * maxRedCount * maxGreenCount;
    }

    public static Game mapToGame(String line) {
        Game.GameBuilder gameBuilder = Game.builder();
        String[] gameSplit = line.split(":");
        String idString = gameSplit[0].replace("Game ", "");
        gameBuilder.id(Integer.parseInt(idString));
        gameBuilder.gameReveals(Arrays.stream(gameSplit[1].split(";")).map(Day2Part2::mapToGameReveal).toList());

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