package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Day1Part1 {
    public static void main(String[] args) throws IOException {

        Path path = Paths.get("src/main/resources/day1");
        var lines = Files.readAllLines(path);
        int sum = lines.stream().mapToInt(CalibrationValueUtils::mapDigitStringToCalibrationValue).sum();
        System.out.println(sum);
    }
}