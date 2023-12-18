package org.example;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode
@ToString
class Coordinate implements Comparable<Coordinate> {

    int row;
    int column;

    Coordinate copyWithRow(int newRow) {
        return new Coordinate(newRow, column);
    }

    Coordinate copyWithColumn(int newColumn) {
        return new Coordinate(row, newColumn);
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
