package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Day4Part2 {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("src/main/resources/day4");
        var lines = Files.readAllLines(path);
        var scores = lines.stream()
                .map(GameCardUtils::mapToGameCard)
                .mapToInt(GameCard::getMatchings)
                .toArray();

        var cardCount = new int[scores.length];
        Arrays.fill(cardCount, 1);

        for (int cardIndex = 0; cardIndex < scores.length; cardIndex++) {
            var score = scores[cardIndex];
            var adding = cardCount[cardIndex];
            for (int stepCardIndex = 1;
                 stepCardIndex <= score && cardIndex + stepCardIndex < scores.length;
                 stepCardIndex++) {
                cardCount[cardIndex + stepCardIndex] += adding;
            }
        }

        int sum = Arrays.stream(cardCount).sum();
        System.out.println(sum);
    }
}