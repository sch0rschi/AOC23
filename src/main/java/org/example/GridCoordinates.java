package org.example;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GridCoordinates {
    private int column;
    private int length;
}
