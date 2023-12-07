package org.example;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;

@Builder
@Getter
public class Hand implements Comparable<Hand> {

    public static final Comparator<Hand> HAND_COMPARATOR = Comparator.comparing(Hand::getBigSetCount)
            .thenComparing(Hand::getSmallSetCount)
            .thenComparing((h1, h2) -> Arrays.compare(h1.getNumbers(), h2.getNumbers()));

    Long bigSetCount;
    Long smallSetCount;
    Long[] numbers;
    Long bid;

    @Override
    public int compareTo(Hand o) {
        return HAND_COMPARATOR.compare(this, o);
    }
}
