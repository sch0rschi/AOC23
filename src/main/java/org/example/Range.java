package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
class Range {
    private long start;
    private long end;

    void shiftByOffset(long offset) {
        start += offset;
        end += offset;
    }

    long count() {
        return getEnd() - getStart() + 1;
    }
}
