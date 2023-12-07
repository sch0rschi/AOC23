package org.example;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class HandUtils {

    static final Comparator<Hand> HAND_COMPARATOR = Comparator.comparing(Hand::getBigSetCount)
            .thenComparing(Hand::getSmallSetCount)
            .thenComparing((h1, h2) -> Arrays.compare(h1.getNumbers(), h2.getNumbers()));

    static Long JACK_RANK = 11L;
    static final Map<Character, Long> CARD_MAPPING = new HashMap<>(Map.ofEntries(
            Map.entry('2', 2L),
            Map.entry('3', 3L),
            Map.entry('4', 4L),
            Map.entry('5', 5L),
            Map.entry('6', 6L),
            Map.entry('7', 7L),
            Map.entry('8', 8L),
            Map.entry('9', 9L),
            Map.entry('T', 10L),
            Map.entry('J', JACK_RANK),
            Map.entry('Q', 12L),
            Map.entry('K', 13L),
            Map.entry('A', 14L)
    ));

    static Hand parseHandSimple(String handString) {
        var handAndBidString = handString.split(" ");
        var bid = Long.parseLong(handAndBidString[1]);
        var numbers = handAndBidString[0].chars().mapToObj(c -> (char) c).map(CARD_MAPPING::get).toArray(Long[]::new);
        var sortedCounters = Arrays.stream(numbers)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .values().stream()
                .sorted(Comparator.reverseOrder())
                .limit(2)
                .toList();
        Hand.HandBuilder builder = Hand.builder()
                .bid(bid)
                .bigSetCount(sortedCounters.get(0))
                .numbers(numbers);
        if(sortedCounters.size()>1) {
            builder.smallSetCount(sortedCounters.get(1));
        }
        return builder.build();
    }

    static Hand parseHandPart2(String handString) {
        var handAndBidString = handString.split(" ");
        var bid = Long.parseLong(handAndBidString[1]);
        var numbers = handAndBidString[0].chars().mapToObj(c -> (char) c).map(CARD_MAPPING::get).toArray(Long[]::new);
        var counters = Arrays.stream(numbers).collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        Long numberOfJacks = counters.getOrDefault(JACK_RANK, 0L);
        var sortedCounters = counters.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(JACK_RANK))
                .map(Map.Entry::getValue)
                .sorted(Comparator.reverseOrder())
                .limit(2)
                .toList();
        Hand.HandBuilder builder = Hand.builder()
                .bid(bid)
                .numbers(numbers);
        if (sortedCounters.isEmpty()) {
            builder.bigSetCount(5L);
            builder.smallSetCount(0L);
        } else if(sortedCounters.size() == 1) {
            builder.bigSetCount(sortedCounters.get(0) + numberOfJacks);
            builder.smallSetCount(0L);
        } else {
            builder.bigSetCount(sortedCounters.get(0) + numberOfJacks);
            builder.smallSetCount(sortedCounters.get(1));
        }
        return builder.build();
    }
}
