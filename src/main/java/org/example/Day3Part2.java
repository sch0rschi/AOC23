package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class Day3Part2 {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("src/main/resources/day3");
        var lines = Files.readAllLines(path);
        var gridEntryLists = lines.stream().map(GridUtils::mapToGridEntries).toList();
        long sum = 0;
        for (var currentLineIndex = 0; currentLineIndex < gridEntryLists.size(); currentLineIndex++) {
            var currentLine = gridEntryLists.get(currentLineIndex);
            var previousLine = currentLineIndex > 0 ? gridEntryLists.get(currentLineIndex - 1) : List.of();
            var nextLine = currentLineIndex < gridEntryLists.size() - 1 ? gridEntryLists.get(currentLineIndex + 1) : List.of();
            var partLines = Stream.concat(Stream.concat(previousLine.stream(), currentLine.stream()), nextLine.stream())
                    .filter(element -> element instanceof GridPartEntry)
                    .map(entry -> (GridPartEntry) entry)
                    .toList();
            sum += currentLine.stream()
                    .filter(entry -> entry instanceof GridGearEntry)
                    .map(entry -> (GridGearEntry) entry)
                    .<List<GridPartEntry>>mapMulti((potentialGridGearEntry, downstream) -> {
                        var partArray = partLines.stream().filter(gridPartEntry -> GridUtils.isTouching(gridPartEntry, potentialGridGearEntry)).toList();
                        if (partArray.size() == 2) {
                            downstream.accept(partArray);
                        }
                    })
                    .mapToInt(partPair -> partPair.stream().mapToInt(GridPartEntry::getNumber).reduce(1, (f1, f2) -> f1 * f2))
                    .sum();
        }
        System.out.println(sum);
    }
}
