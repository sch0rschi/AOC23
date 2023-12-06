package org.example;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class RangeUtils {
    static Pair<Range, List<Range>> calculateOverlapAndRemaining(Almanac.RangeMapping mapping, Range mapped) {
        var overlapStart = Math.max(mapping.getStart(), mapped.getStart());
        var overlapEnd = Math.min(mapping.getEnd(), mapped.getEnd());
        if (overlapStart <= overlapEnd) {
            var remaining = new ArrayList<Range>(2);
            var overlap = new Range(overlapStart, overlapEnd);
            if (mapped.getStart() < overlap.getStart()) {
                remaining.add(new Range(mapped.getStart(),overlap.getStart() - 1));
            }
            if (mapped.getEnd() > overlap.getEnd()) {
                remaining.add(new Range(overlap.getEnd() + 1, mapped.getEnd()));
            }
            return Pair.of(overlap, remaining);
        }
        return null;
    }
}
