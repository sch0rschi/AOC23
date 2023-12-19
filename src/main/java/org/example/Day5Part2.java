package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Day5Part2 {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("src/main/resources/day5");
        var lines = String.join("\n", Files.readAllLines(path));
        String[] split = lines.split("\n\n");

        Almanac almanac = AlmanacUtils.parseAlmanac(split);
        fixSeedRanges(almanac);

        Instant start = Instant.now();

        var unprocessedRanges = new LinkedList<Range>();
        var doneRanges = new LinkedList<>(almanac.getSeeds());
        for (var mappingLevel : AlmanacUtils.getMaps(almanac)) {
            var tempList = unprocessedRanges;
            unprocessedRanges = doneRanges;
            doneRanges = tempList;
            doneRanges.clear();

            var iterator = unprocessedRanges.listIterator();
            while (iterator.hasNext()) {
                var range = iterator.next();
                for (var mapping : mappingLevel) {

                    var overlapAndRemaining = RangeUtils.calculateOverlapAndRemainingsAndShift(mapping, range);
                    if (overlapAndRemaining != null) {
                        iterator.remove();
                        overlapAndRemaining.getLeft().shiftByOffset(mapping.getOffset());
                        doneRanges.add(overlapAndRemaining.getLeft());
                        for (var remainingRange : overlapAndRemaining.getRight()) {
                            iterator.add(remainingRange);
                            iterator.previous();
                        }
                        break;
                    } else if (mapping.getStart() > range.getEnd()) {
                        break;
                    }
                }
            }
            doneRanges.addAll(unprocessedRanges);
        }
        var min = Long.MAX_VALUE;
        for (var doneRange : doneRanges) {
            min = Math.min(min, doneRange.getStart());
        }

        Instant end = Instant.now();
        System.out.println(min);
        System.out.println(Duration.between(start, end));
    }

    private static void fixSeedRanges(Almanac almanac) {
        var startSeedRanges = new ArrayList<Range>();
        for (int seedIndex = 0; seedIndex < almanac.getSeeds().size(); seedIndex += 2) {
            var start = almanac.getSeeds().get(seedIndex);
            var range = almanac.getSeeds().get(seedIndex + 1);
            startSeedRanges.add(new Range(start.getStart(), start.getStart() + range.getStart() - 1));
        }
        almanac.setSeeds(startSeedRanges);
    }
}