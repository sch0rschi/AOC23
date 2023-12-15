package org.example;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class Day15Part2 {
    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day15");
        var lines = Files.readString(path).strip();
        String[] instructions = lines.split(",");

        LinkedHashMap<String, Integer>[] boxes = new LinkedHashMap[256];
        for (int i = 0; i < 256; i++) {
            boxes[i] = new LinkedHashMap<>();
        }

        for (var instruction : instructions) {
            String[] split = instruction.split("[-=]");
            int box = HashUtils.hash(split[0], split[0].length());
            if ('=' == instruction.charAt(split[0].length())){
                int update = Integer.parseInt(split[1]);
                boxes[box].put(split[0], update);
            } else {
                boxes[box].remove(split[0]);
            }
            //Arrays.stream(boxes).filter(boxStreamed -> !boxStreamed.isEmpty()).forEach(System.out::println);
            //System.out.println();
        }

        int sum = 0;
        for(int box = 0; box < 256; box++) {
            int lenseIndex = 1;
            for(var lense : boxes[box].values()) {
                int sum1 = (box + 1) * (lenseIndex++) * lense;
                System.out.println(sum1);
                sum += sum1;
            }
        }
        System.out.println(sum);
    }
}
