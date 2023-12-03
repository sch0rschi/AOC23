package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Day3Part1 {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("src/main/resources/day3");
        var lines = Files.readAllLines(path);
        var gridEntryLists = lines.stream().map(GridUtils::mapToGridEntries).toList();
        int sum = 0;
        for (var currentLineIndex = 0; currentLineIndex < gridEntryLists.size(); currentLineIndex++) {
            var currentLine = gridEntryLists.get(currentLineIndex);
            var previousLine = currentLineIndex > 0 ? gridEntryLists.get(currentLineIndex - 1) : List.of();
            var nextLine = currentLineIndex < gridEntryLists.size() - 1 ? gridEntryLists.get(currentLineIndex + 1) : List.of();
            var symbolLines = Stream.concat(Stream.concat(previousLine.stream(), currentLine.stream()), nextLine.stream())
                    .filter(element -> element instanceof Symbols || element instanceof Gear)
                    .map(entry -> (GridEntry) entry)
                    .toList();
            sum += currentLine.stream()
                    .filter(entry -> entry instanceof PartNumber)
                    .map(entry -> (PartNumber) entry)
                    .filter(potentialPart -> symbolLines.stream().anyMatch(symbol -> GridUtils.isTouching(potentialPart, symbol)))
                    .mapToInt(PartNumber::getNumber)
                    .sum();
        }
        System.out.println(sum);
    }
}