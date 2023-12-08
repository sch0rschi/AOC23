package org.example;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8Part2 {
    @SneakyThrows
    public static void main (String... args) {
        System.out.println("Problem is not stated good. One can get the cycle lengths of each starting point.");
        System.out.println("Luckily at each end of a cycle we stop at an **Z Coordinate");
        System.out.println("There are no other stops at **Z coordinates.");
        System.out.println("Cycle lengths are all primes (huh?) so the solution is the product of all cycle lengths times the instruction length.");
    }
}
