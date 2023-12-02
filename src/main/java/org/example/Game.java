package org.example;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Game {
    private int id;
    private List<GameReveal> gameReveals;
}
