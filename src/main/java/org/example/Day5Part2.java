package org.example;

import org.apache.commons.lang3.tuple.Pair;

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
        var unprocessedRanges = new ArrayList<Range>();
        var doneRanges = new ArrayList<>(almanac.getSeeds());
        for (var mappingLevel : AlmanacUtils.getMaps(almanac)) {
            unprocessedRanges = doneRanges;
            doneRanges = new ArrayList<>();
            for (var mapping : mappingLevel) {
                var iterator = unprocessedRanges.listIterator();
                while (iterator.hasNext()) {
                    var range = iterator.next();

                   var overlapAndRemaining = RangeUtils.calculateOverlapAndRemaining(mapping.getRange(), range);
                    var overlap = overlapAndRemaining.getLeft();
                    if (overlap != null) {
                        iterator.remove();
                        overlap.shiftByOffset(mapping.getOffset());
                        doneRanges.add(overlap);
                        for(var remainingRange : overlapAndRemaining.getRight()) {
                            iterator.add(remainingRange);
                        }
                    }
                }
            }
        }
        var min = doneRanges.stream().mapToLong(Range::getStart).min().orElse(-1);
        System.out.println(min);
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end));
    }

    private static void fixSeedRanges(Almanac almanac) {
        var startSeedRanges = new ArrayList<Range>();
        for (int seedIndex = 0; seedIndex < almanac.getSeeds().size(); seedIndex += 2) {
            var start = almanac.getSeeds().get(seedIndex);
            var range = almanac.getSeeds().get(seedIndex + 1);
            startSeedRanges.add(Range.builder().start(start.getStart()).end(start.getStart() + range.getStart() - 1).build());
        }
        almanac.setSeeds(startSeedRanges);
    }
}