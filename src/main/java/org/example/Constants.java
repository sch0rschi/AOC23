package org.example;

import java.util.List;
import java.util.Set;

public class Constants {

    static final String LEFT = "left";
    static final String UP = "up";
    static final String RIGHT = "right";
    static final String DOWN = "down";

    static final char L = 'l';
    static final char U = 'u';
    static final char R = 'r';
    static final char D = 'd';
    static final char N = 'n';

    static final List<Character> DIRECTIONS = List.of(L,D,R,U);
    static final Set<Character> HORIZONTAL_DIRECTIONS = Set.of(L, R);
    static final Set<Character> VERTICAL_DIRECTIONS = Set.of(U, D);

}
