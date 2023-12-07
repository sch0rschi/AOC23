package org.example;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Hand {
    Long bigSetCount;
    Long smallSetCount;
    Long[] numbers;
    Long bid;
}
