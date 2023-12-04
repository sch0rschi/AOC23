package org.example;

import org.apache.commons.collections4.SetUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GameCardUtils {
    public static GameCard mapToGameCard(String line) {
        var split = line.split("[:|]");
        var winningNumbersString = split[1].trim().split(" +");
        var ownNumbersString = split[2].trim().split(" +");
        var winningNumbers = Arrays.stream(winningNumbersString).map(Integer::parseInt).collect(Collectors.toSet());
        var ownNumbers = Arrays.stream(ownNumbersString).map(Integer::parseInt).collect(Collectors.toSet());
        return GameCard.builder().winningNumbers(winningNumbers).ownNumbers(ownNumbers).build();
    }

    public static int calculateGameCardScore(GameCard gameCard) {
        var intersection = SetUtils.intersection(gameCard.getWinningNumbers(), gameCard.getOwnNumbers());
        return intersection.isEmpty() ? 0 : 1 << (intersection.size() - 1);
    }
}
