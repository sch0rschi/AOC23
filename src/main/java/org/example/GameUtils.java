package org.example;

import java.util.Arrays;

import static java.lang.Math.max;

public class GameUtils {

    static boolean isValidGame(Game game) {
        return game.getGameReveals().stream().allMatch(GameUtils::isValidGameReveal);
    }

    static boolean isValidGameReveal(Game.GameReveal gameReveal) {
        return gameReveal.getBlueCount() <= 14
                && gameReveal.getRedCount() <= 12
                && gameReveal.getGreenCount() <= 13;
    }

    static Game mapToGame(String line) {
        Game.GameBuilder gameBuilder = Game.builder();
        String[] gameSplit = line.split(":");
        String idString = gameSplit[0].replace("Game ", "");
        gameBuilder.id(Integer.parseInt(idString));
        gameBuilder.gameReveals(Arrays.stream(gameSplit[1].split(";")).map(GameUtils::mapToGameReveal).toList());

        return gameBuilder.build();
    }

    private static Game.GameReveal mapToGameReveal(String s) {
        Game.GameReveal.GameRevealBuilder gameRevealBuilder = Game.GameReveal.builder();
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

    static void setPower(Game game) {
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
}
