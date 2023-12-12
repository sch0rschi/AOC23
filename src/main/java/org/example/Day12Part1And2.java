package org.example;

import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.Day12Part1And2.State.*;

public class Day12Part1And2 {
    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day12");
        var lines = Files.readAllLines(path);

        BigInteger sum = lines.stream().map(Day12Part1And2::parseInput).map(Day12Part1And2::calculatePossibleStatesCount).reduce(BigInteger.ZERO, BigInteger::add);
        System.out.println(sum);
        Instant start = Instant.now();
        sum = lines.stream().unordered().parallel().map(Day12Part1And2::modifyInput).map(Day12Part1And2::parseInput).map(Day12Part1And2::calculatePossibleStatesCount).reduce(BigInteger.ZERO, BigInteger::add);
        Instant end = Instant.now();
        System.out.println(sum + " took: " + Duration.between(start, end));
    }

    private static String modifyInput(String s) {
        String[] split = s.split(" ");
        String newStates = IntStream.range(0, 5).<String>mapToObj(i -> split[0]).collect(Collectors.joining("?")) + ".";
        String blockLengths = IntStream.range(0, 5).<String>mapToObj(i -> split[1]).collect(Collectors.joining(",")).trim();
        return newStates + " " + blockLengths;
    }

    private static BigInteger calculatePossibleStatesCount(SpringState springState) {
        BigInteger[][] cache = new BigInteger[springState.getStates().length][springState.getChecks().length];
        return calculatePossibleStatesCount(springState.getStates().length - 2, springState.getStates(), springState.getChecks().length - 1, springState.getChecks(), cache, springState.getSpringCheckSums(), springState.getSpringStatesSums());
    }

    private static BigInteger calculatePossibleStatesCount(int maxStateIndex, State[] states, int currentBlockIndex, int[] springBlocks, BigInteger[][] cache, int[] checkSums, int[] stateSums) {

        if (currentBlockIndex == -1) {
            if(IntStream.range(0, maxStateIndex + 2).noneMatch(i -> states[i] == SPRING)) {
                return BigInteger.ONE;
            }
            return BigInteger.ZERO;
        }

        if(maxStateIndex < 0) {
            return BigInteger.ZERO;
        }

        BigInteger cachedValue = cache[maxStateIndex][currentBlockIndex];
        if (cachedValue != null) {
            return cachedValue;
        }

        if(stateSums[maxStateIndex] > checkSums[currentBlockIndex]) {
            return BigInteger.ZERO;
        }

        int springBlockLength = springBlocks[currentBlockIndex];
        BigInteger sum = BigInteger.ZERO;
        for (int blockEndIndex = maxStateIndex; blockEndIndex - springBlockLength + 1 >= 0 && states[blockEndIndex + 1] != SPRING; blockEndIndex--) {
            if (fits(states, blockEndIndex, springBlockLength)) {
                sum = sum.add(calculatePossibleStatesCount(blockEndIndex - springBlockLength - 1, states, currentBlockIndex - 1, springBlocks, cache, checkSums, stateSums));
            }
        }
        cache[maxStateIndex][currentBlockIndex] = sum;
        return sum;
    }

    private static boolean fits(State[] states, int blockEndIndex, int springBlockLength) {
        for(int i = blockEndIndex - springBlockLength + 1; i < blockEndIndex + 1; i++) {
            if(states[i] == NO_SPRING) {
                return false;
            }
        }
        return true;
    }

    private static SpringState parseInput(String s) {
        var split = s.replaceAll("\\.\\.", ".").split(" ");
        var checks = Arrays.stream(split[1].split(",")).mapToInt(Integer::parseInt).toArray();
        var states = (split[0]+".").chars().mapToObj(c -> switch ((char) c) {
            case '#' -> SPRING;
            case '.' -> NO_SPRING;
            case '?' -> UNKNOWN;
            default -> throw new NotImplementedException();
        }).toArray(State[]::new);

        int[] checkSums = new int[checks.length];
        int sum = 0;
        for(int i = 0; i < checks.length; i++) {
            sum += checks[i];
            checkSums[i] = sum;
        }

        int[] stateSums = new int[states.length];
        sum = 0;
        for(int i = 0; i < states.length; i++) {
            sum += states[i] == SPRING ? 1 : 0;
            stateSums[i] = sum;
        }

        return SpringState.builder()
                .states(states)
                .checks(checks)
                .springCheckSums(checkSums)
                .springStatesSums(stateSums)
                .build();
    }

    @Builder
    @Getter
    private static class SpringState {
        State[] states;
        int[] checks;
        int[] springCheckSums;
        int[] springStatesSums;

    }

    enum State {
        NO_SPRING, SPRING, UNKNOWN
    }

}
