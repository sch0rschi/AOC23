package org.example;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class HandUtils {

    private static final Map<Character, Long> CARD_MAPPING = Map.ofEntries(
            Map.entry('2', 2L),
            Map.entry('3', 3L),
            Map.entry('4', 4L),
            Map.entry('5', 5L),
            Map.entry('6', 6L),
            Map.entry('7', 7L),
            Map.entry('8', 8L),
            Map.entry('9', 9L),
            Map.entry('T', 10L),
            Map.entry('J', 11L),
            Map.entry('Q', 12L),
            Map.entry('K', 13L),
            Map.entry('A', 14L)
    );

    static Hand parseHand(String handString) {
        var handAndBidString = handString.split(" ");
        var bid = Long.parseLong(handAndBidString[1]);
        var numbers = handAndBidString[0].chars().mapToObj(c -> (char) c).map(CARD_MAPPING::get).toArray(Long[]::new);
        var counters = Arrays.stream(numbers).collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        var sortedCounters = counters.values().stream().sorted(Comparator.reverseOrder()).limit(2).toList();
        Hand.HandBuilder builder = Hand.builder()
                .bid(bid)
                .bigSetCount(sortedCounters.get(0))
                .numbers(numbers);
        if(sortedCounters.size()>1) {
            builder.smallSetCount(sortedCounters.get(1));
        }
        return builder.build();
    }
}
