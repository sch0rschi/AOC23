package org.example;

public class GameValidator {
    static boolean isValidGame(Game game) {
        return game.getGameReveals().stream().allMatch(GameValidator::isValidGameReveal);
    }

    static boolean isValidGameReveal(GameReveal gameReveal) {
        return gameReveal.getBlueCount() <= 14
                && gameReveal.getRedCount() <= 12
                && gameReveal.getGreenCount() <= 13;
    }
}

