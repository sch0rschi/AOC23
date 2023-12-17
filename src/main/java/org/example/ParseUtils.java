package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

class ParseUtils {
    static int[][] parseFileToDigitMatrix(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        return Files.readAllLines(path).stream().map(MappingUtils::mapDigitsStringToIntegers).toArray(int[][]::new);
    }

    static int[][] parseFileToCharMatrix(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        var lines = Files.readAllLines(path);
        return lines.stream().map(String::chars).map(IntStream::toArray).toArray(int[][]::new);
    }
}
