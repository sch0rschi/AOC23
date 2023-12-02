package org.example;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GameReveal {
    private int blueCount;
    private int redCount;
    private int greenCount;
}
