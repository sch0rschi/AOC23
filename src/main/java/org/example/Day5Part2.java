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

        Instant start = Instant.now();
        Path path = Paths.get("src/main/resources/day5");
        var lines = String.join("\n", Files.readAllLines(path));
        String[] split = lines.split("\n\n");

        Almanac almanac = AlmanacUtils.parseAlmanac(split);
        fixSeedRanges(almanac);

        var unprocessedRanges = new LinkedList<Range>();
        var doneRanges = new LinkedList<>(almanac.getSeeds());
        for (var mappingLevel : AlmanacUtils.getMaps(almanac)) {
            unprocessedRanges = doneRanges;
            doneRanges = new LinkedList<>();
            for (var mapping : mappingLevel) {
                ListIterator<Range> iterator = unprocessedRanges.listIterator();
                while (iterator.hasNext()) {
                    var range = iterator.next();
                    iterator.remove();
                    Pair<Optional<Range>, List<Range>> overlapAndRemaining = RangeUtils.calculateOverlapAndRemaining(mapping.getRange(), range);
                    Optional<Range> overlapOptional = overlapAndRemaining.getLeft();
                    if (overlapOptional.isPresent()) {
                        overlapOptional.get().shiftByOffset(mapping.getOffset());
                        doneRanges.add(overlapOptional.get());
                    }
                    for(var remainingRange : overlapAndRemaining.getRight()) {
                        iterator.add(remainingRange);
                    }
                }
            }
            doneRanges.addAll(unprocessedRanges);
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