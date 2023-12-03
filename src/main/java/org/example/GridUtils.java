package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GridUtils {
    static List<GridEntry> mapToGridEntries(String line) {
        Pattern pattern = Pattern.compile("\\*|\\d+|[^0-9*]+");
        var currentColumn = new AtomicInteger();
        return Arrays.stream(line.split("\\.")).<GridEntry>mapMulti((entryChain, downstream) -> {
                    Matcher matcher = pattern.matcher(entryChain);
                    while (matcher.find()) {
                        String match = matcher.group();
                        int indexOf = line.indexOf(match, currentColumn.get());
                        currentColumn.set(indexOf+match.length());
                        GridCoordinates gridCoordinates = GridCoordinates.builder()
                                .column(indexOf)
                                .length(match.length())
                                .build();
                        if (Character.isDigit(match.charAt(0))) {
                            int number = Integer.parseInt(match);
                            downstream.accept(PartNumber.builder()
                                    .number(number)
                                    .gridCoordinates(gridCoordinates)
                                    .build());
                        } else if ("*".equals(match)) {
                            downstream.accept(Gear.builder()
                                    .gridCoordinates(gridCoordinates)
                                    .build());
                        } else {
                            downstream.accept(Symbols.builder()
                                    .gridCoordinates(gridCoordinates)
                                    .build());
                        }
                    }
                })
                .toList();
    }

    static boolean isTouching(GridEntry entry1, GridEntry entry2) {
        GridCoordinates gc1 = entry1.getGridCoordinates();
        GridCoordinates gc2 = entry2.getGridCoordinates();
        return gc1.getColumn() <= gc2.getColumn() && gc1.getColumn() + gc1.getLength() >= gc2.getColumn()
                || gc2.getColumn() <= gc1.getColumn() && gc2.getColumn() + gc2.getLength() >= gc1.getColumn();
    }
}
