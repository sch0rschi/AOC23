package org.example;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class PartNumber extends GridEntry {
    private GridCoordinates gridCoordinates;
    private int number;
}
