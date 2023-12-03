package org.example;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class GridEntry {
    private GridCoordinates gridCoordinates;
}
