package org.example;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RangeUtils {
    static Pair<Range, List<Range>> calculateOverlapAndRemainingsAndShift(Almanac.RangeMapping mapping, Range mapped) {
        var overlapStart = Math.max(mapping.getStart(), mapped.getStart());
        var overlapEnd = Math.min(mapping.getEnd(), mapped.getEnd());
        if (overlapStart <= overlapEnd) {
            var remaining = new ArrayList<Range>(2);
            var overlap = new Range(overlapStart, overlapEnd);
            if (mapped.getStart() < overlap.getStart()) {
                remaining.add(new Range(mapped.getStart(), overlap.getStart() - 1));
            }
            if (mapped.getEnd() > overlap.getEnd()) {
                remaining.add(new Range(overlap.getEnd() + 1, mapped.getEnd()));
            }
            return Pair.of(overlap, remaining);
        }
        return null;
    }

    static Pair<Range, List<Range>> calculateOverlapAndRemainings(Range tested, Range testing) {
        var overlapStart = Math.max(testing.getStart(), tested.getStart());
        var overlapEnd = Math.min(testing.getEnd(), tested.getEnd());
        Range overlap = null;
        var remaining = List.of(tested);
        if (overlapStart <= overlapEnd) {
            overlap = new Range(overlapStart, overlapEnd);
            remaining = new ArrayList<>(2);
            if (tested.getStart() < overlap.getStart()) {
                remaining.add(new Range(tested.getStart(), overlap.getStart() - 1));
            }
            if (tested.getEnd() > overlap.getEnd()) {
                remaining.add(new Range(overlap.getEnd() + 1, tested.getEnd()));
            }
        }
        return Pair.of(overlap, remaining);
    }
}
