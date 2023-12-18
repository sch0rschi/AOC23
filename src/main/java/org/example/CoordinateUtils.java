package org.example;

class CoordinateUtils {
    static int determinant(Coordinate c1, Coordinate c2) {
        return c1.getRow() * c2.getColumn() - c1.getColumn() * c2.getRow();
    }
}
