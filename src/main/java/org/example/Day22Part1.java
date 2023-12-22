package org.example;

import lombok.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Day22Part1 {

    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day22");
        var brickStrings = Files.readAllLines(path);

        List<Brick> bricks = brickStrings.stream()
                .map(Day22Part1::parseBlock)
                .sorted(Comparator.comparingInt(Brick::getHighestBlockHeight))
                .toList();

        int maxRow = bricks.stream()
                .mapToInt(brick -> brick.getOccupyingSpace().stream()
                        .mapToInt(Coordinate::getRow)
                        .max().orElse(-1))
                .max().orElse(-1);
        int maxColumn = bricks.stream()
                .mapToInt(brick -> brick.getOccupyingSpace().stream()
                .mapToInt(Coordinate::getColumn)
                .max().orElse(-1))
                .max().orElse(-1);

        Brick[][] brickProjection = new Brick[maxRow + 1][maxColumn + 1];

        Set<Brick> supportingBricks = new HashSet<>();
        for (var brick : bricks) {
            int maxHeight = 0;
            supportingBricks.clear();
            for (var block : brick.getOccupyingSpace()) {
                var potentialSupportingBlock = brickProjection[block.getRow()][block.getColumn()];
                if (potentialSupportingBlock != null) {
                    if (potentialSupportingBlock.getHighestBlockHeight() > maxHeight) {
                        maxHeight = potentialSupportingBlock.getHighestBlockHeight();
                        supportingBricks.clear();
                        supportingBricks.add(potentialSupportingBlock);
                    } else if (potentialSupportingBlock.getHighestBlockHeight() == maxHeight) {
                        supportingBricks.add(potentialSupportingBlock);
                    }
                }
                brickProjection[block.getRow()][block.getColumn()] = brick;
            }
            brick.setHighestBlockHeight(maxHeight + brick.getOwnHeight());
            if (supportingBricks.size() == 1) {
                supportingBricks.forEach(sb -> sb.setBlocked(true));
            }
        }

        long count = bricks.stream().filter(b -> !b.isBlocked()).count();
        System.out.println(count);

    }

    private static Brick parseBlock(String s) {

        int[] split = Arrays.stream(s.split("[~,]")).mapToInt(Integer::parseInt).toArray();

        List<Coordinate> occupyingSpace = new ArrayList<>();
        for (int row = Math.min(split[0], split[3]); row <= Math.max(split[0], split[3]); row++) {
            for (int column = Math.min(split[1], split[4]); column <= Math.max(split[1], split[4]); column++) {
                occupyingSpace.add(new Coordinate(row, column));
            }
        }
        int height = Math.abs(split[5] - split[2]) + 1;
        Brick brick = new Brick(occupyingSpace, height);
        brick.setHighestBlockHeight(Math.max(split[5], split[2]));
        return brick;
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    private static class Brick {
        static AtomicInteger idCounter = new AtomicInteger();
        @EqualsAndHashCode.Include
        final private int id = idCounter.incrementAndGet();
        final private List<Coordinate> occupyingSpace;
        final private int ownHeight;
        @Setter
        private int highestBlockHeight;
        @Setter
        private boolean isBlocked = false;
    }
}
