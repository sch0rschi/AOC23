package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.LongStream;

public class Day5Part2 {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("src/main/resources/day5");
        var lines = String.join("\n", Files.readAllLines(path));
        String[] split = lines.split("\n\n");

        Almanac almanac = AlmanacUtils.parseAlmanac(split);

        var minLocation = Long.MAX_VALUE;
        for (int seedIndex = 0; seedIndex < almanac.getSeeds().size(); seedIndex += 2) {
            var start = almanac.getSeeds().get(seedIndex);
            var range = almanac.getSeeds().get(seedIndex + 1);
            System.out.println(start);
            long location = LongStream.range(start, start + range)
                    .parallel()
                    .map(seedl -> AlmanacUtils.getLocation(almanac, seedl))
                    .min()
                    .orElse(-1);
            minLocation = Math.min(minLocation, location);
            System.out.println(start);
        }
        System.out.println(minLocation);
    }
}