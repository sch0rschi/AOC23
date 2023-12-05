package org.example;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class GameCard {
    private Set<Integer> winningNumbers;
    private Set<Integer> ownNumbers;
    private int score;
    private int matchings;
}
