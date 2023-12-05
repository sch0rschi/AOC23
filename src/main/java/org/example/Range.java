package org.example;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
class Range {
    private long start;
    private long end;

    void shiftByOffset(long offset) {
        start += offset;
        end += offset;
    }
}
