package org.example;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.util.Optional;

@Value
@EqualsAndHashCode
@ToString
class Coordinate implements Comparable<Coordinate> {

    int row;
    int column;

    Optional<Coordinate> shiftLeft() {
        if (column > 0) {
            return Optional.of(new Coordinate(row, column-1));
        } else {
            return Optional.empty();
        }
    }

    Optional<Coordinate> shiftUp() {
        if (row > 0) {
            return Optional.of(new Coordinate(row - 1, column));
        } else {
            return Optional.empty();
        }
    }

    Optional<Coordinate> shiftDown(int maxRowExclusive) {
        if (row < maxRowExclusive - 1) {
            return Optional.of(new Coordinate(row + 1, column));
        } else {
            return Optional.empty();
        }
    }

    Optional<Coordinate> shiftRight(int maxColumnExclusive) {
        if (column < maxColumnExclusive - 1) {
            return Optional.of(new Coordinate(row, column + 1));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public int compareTo(Coordinate that) {
        if (this.row != that.row) {
            return (this.row < that.row ? -1 : 1);
        }

        if (this.column != that.column) {
            return (this.column < that.column ? -1 : 1);
        }

        return 0;
    }
}
