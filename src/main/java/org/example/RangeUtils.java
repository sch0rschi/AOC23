package org.example;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RangeUtils {
    static Pair<Optional<Range>, List<Range>> calculateOverlapAndRemaining(Range mapping, Range mapped) {
        var overlapStart = Math.max(mapping.getStart(), mapped.getStart());
        var overlapEnd = Math.min(mapping.getEnd(), mapped.getEnd());
        if (overlapStart <= overlapEnd) {
            var remaining = new ArrayList<Range>(2);
            var overlap = Range.builder().start(overlapStart).end(overlapEnd).build();
            if(mapped.getStart() < overlap.getStart()) {
                remaining.add(Range.builder().start(mapped.getStart()).end(overlap.getStart()-1).build());
            }
            if(mapped.getEnd()>overlap.getEnd()) {
                remaining.add(Range.builder().start(overlap.getEnd() + 1).end(mapped.getEnd()).build());
            }
            return Pair.of(Optional.of(overlap), remaining);
        }else {
            return Pair.of(Optional.empty(), List.of(mapped));
        }
    }
}
