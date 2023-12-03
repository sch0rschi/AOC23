package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day3Part1 {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("src/main/resources/day3");
        var lines = Files.readAllLines(path);
        var gridEntryLists = lines.stream().map(Day3Part1::mapToGridEntries).toList();
        int sum = 0;
        for (var currentLineIndex = 0; currentLineIndex < gridEntryLists.size(); currentLineIndex++) {
            var currentLine = gridEntryLists.get(currentLineIndex);
            var previousLine = currentLineIndex > 0 ? gridEntryLists.get(currentLineIndex - 1) : List.of();
            var nextLine = currentLineIndex < gridEntryLists.size() - 1 ? gridEntryLists.get(currentLineIndex + 1) : List.of();
            var symbolLines = Stream.concat(Stream.concat(previousLine.stream(), currentLine.stream()), nextLine.stream())
                    .filter(element -> element instanceof Symbols)
                    .map(entry -> (Symbols) entry)
                    .toList();
            sum += currentLine.stream()
                    .filter(entry -> entry instanceof PartNumber)
                    .map(entry -> (PartNumber) entry)
                    .filter(potentialPart -> symbolLines.stream().anyMatch(symbol -> Day3Part1.isTouching(potentialPart, symbol)))
                    .mapToInt(PartNumber::getNumber)
                    .peek(System.out::println)
                    .sum();
        }
        System.out.println(sum);
    }

    private static boolean isTouching(PartNumber potentialPart, GridEntry symbol) {
        GridCoordinates ppgc = potentialPart.getGridCoordinates();
        GridCoordinates sgc = symbol.getGridCoordinates();
        return ppgc.getColumn() <= sgc.getColumn() && ppgc.getColumn() + ppgc.getLength() >= sgc.getColumn()
                || sgc.getColumn() <= ppgc.getColumn() && sgc.getColumn() + sgc.getLength() >= ppgc.getColumn();
    }

    private static List<GridEntry> mapToGridEntries(String line) {
        Pattern pattern = Pattern.compile("\\d+|\\D+");
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
                        } else {
                            downstream.accept(Symbols.builder()
                                    .gridCoordinates(gridCoordinates)
                                    .build());
                        }
                    }
                })
                .toList();
    }
}