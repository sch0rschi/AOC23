package org.example;

import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day12Part1 {
    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day12");
        var lines = Files.readAllLines(path);

        int sum = lines.stream().map(Day12Part1::parseInput).mapToInt(Day12Part1::calculatePossibleStatesCount).sum();
        System.out.println(sum);
    }

    private static int calculatePossibleStatesCount(SpringState state) {
        var numberOfGoodStates = 0;
        for (int i = 0; i < Math.pow(2, state.numberOfUnknown); i++) {
            // Convert the decimal number to binary representation
            String binary = Integer.toBinaryString(i);

            // Ensure the binary representation has the correct length
            while (binary.length() < state.numberOfUnknown) {
                binary = "0" + binary;
            }

            // Convert the binary string to a boolean array
            boolean[] arrangement = new boolean[state.numberOfUnknown];
            for (int j = 0; j < state.numberOfUnknown; j++) {
                arrangement[j] = binary.charAt(j) == '1';
            }

            int[] copy = Arrays.stream(state.getStates()).toArray();
            for (int place = 0; place < state.getNumberOfUnknown(); place++) {
                if (arrangement[place]) {
                    copy[state.getPlacesOfUnknown()[place]] = '#';
                } else {
                    copy[state.getPlacesOfUnknown()[place]] = '.';
                }
            }

            if (checkStates(copy, state.getChecks())) {
                numberOfGoodStates++;
            }
        }
        System.out.println(numberOfGoodStates);
        return numberOfGoodStates;
    }

    private static boolean checkStates(int[] states, int[] checks) {
        StringBuilder charString = new StringBuilder();
        for (int num : states) {
            charString.append((char) num);
        }
        String[] split = Arrays.stream(charString.toString().split("\\.+")).filter(s -> !s.isBlank()).toArray(String[]::new);
        if(split.length != checks.length) {
            return false;
        }
        for (int i = 0; i < checks.length; i++) {
            if (split[i].length() != checks[i]) {
                return false;
            }
        }
        return true;
    }

    private static SpringState parseInput(String s) {
        var split = s.split(" ");
        var checks = Arrays.stream(split[1].split(",")).mapToInt(Integer::parseInt).toArray();
        var states = split[0].chars().toArray();
        var numberOfUnknown = (int) Arrays.stream(states).filter(c -> '?' == c).count();
        var placesOfUnknown = new int[numberOfUnknown];
        var oldUnknown = 0;
        for (int i = 0; i < numberOfUnknown; i++) {
            placesOfUnknown[i] = s.indexOf('?', oldUnknown);
            oldUnknown = placesOfUnknown[i] + 1;
        }
        return SpringState.builder().states(states).checks(checks).numberOfUnknown(numberOfUnknown).placesOfUnknown(placesOfUnknown).build();
    }

    @Builder
    @Getter
    private static class SpringState {
        int numberOfUnknown;
        int[] placesOfUnknown;
        int[] states;
        int[] checks;
    }

}
