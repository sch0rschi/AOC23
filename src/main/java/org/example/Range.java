package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
class Range {
    private long start;
    private long end;

    void shiftByOffset(long offset) {
        start += offset;
        end += offset;
    }
}
