package org.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class Day24Part1 {

    private static final double EPSILON = 1e-9;

    public static final long BOX_LOWER = 200000000000000L;
    public static final long BOX_UPPER = 400000000000000L;
    public static final double BOX_LOWER_DOUBLE = (double) BOX_LOWER;
    public static final double BOX_UPPER_DOUBLE = (double) BOX_UPPER;

    @SneakyThrows
    public static void main(String... args) {
        Path path = Paths.get("src/main/resources/day24");
        var trajectoryStrings = Files.readAllLines(path);

        Trajectory[] trajectories = trajectoryStrings.stream().map(Day24Part1::parseLine).toArray(Trajectory[]::new);
        Arrays.stream(trajectories).forEach(Trajectory::setBoxIntersections);
        trajectories = Arrays.stream(trajectories).filter(Trajectory::intersectsBox).toArray(Trajectory[]::new);

        int countCollisions = 0;
        for (int low = 0; low < trajectories.length; low++) {
            for (int high = low + 1; high < trajectories.length; high++) {
                var trajectory1 = trajectories[low];
                var trajectory2 = trajectories[high];
                if (collide(trajectory1, trajectory2)) {
                    countCollisions++;
                }
            }
        }
        System.out.println(countCollisions);
    }

    private static boolean collide(Trajectory trajectory1, Trajectory trajectory2) {
        return doIntersect(trajectory1.getPoint1(), trajectory1.getPoint2(), trajectory2.getPoint1(), trajectory2.getPoint2());
    }

    static boolean doIntersect(Pair<Double, Double> tr1Start, Pair<Double, Double> tr1End, Pair<Double, Double> tr2Start, Pair<Double, Double> tr2End) {

        if (Math.max(tr1Start.getLeft(), tr1End.getLeft()) < Math.min(tr2Start.getLeft(), tr2End.getLeft() + EPSILON)
                || Math.min(tr1Start.getLeft(), tr1End.getLeft()) + EPSILON > Math.max(tr2Start.getLeft(), tr2End.getLeft())
                || Math.max(tr1Start.getRight(), tr1End.getRight()) < Math.min(tr2Start.getRight(), tr2End.getRight() + EPSILON)
                || Math.min(tr1Start.getRight(), tr1End.getRight()) + EPSILON > Math.max(tr2Start.getRight(), tr2End.getRight())) {
            return false;
        }

        // Calculate cross products
        double o1 = orientation(tr1Start, tr1End, tr2Start);
        double o2 = orientation(tr1Start, tr1End, tr2End);
        double o3 = orientation(tr2Start, tr2End, tr1Start);
        double o4 = orientation(tr2Start, tr2End, tr1End);

        // Check relative orientations
        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // Check for collinearity
        if (o1 == 0 && onSegment(tr1Start, tr2Start, tr1End)) return true;
        if (o2 == 0 && onSegment(tr1Start, tr2End, tr1End)) return true;
        if (o3 == 0 && onSegment(tr2Start, tr1Start, tr2End)) return true;
        if (o4 == 0 && onSegment(tr2Start, tr1End, tr2End)) return true;

        return false;
    }

    static double orientation(Pair<Double, Double> p, Pair<Double, Double> q, Pair<Double, Double> r) {
        double val = (q.getRight() - p.getRight()) * (r.getLeft() - q.getLeft()) - (q.getLeft() - p.getLeft()) * (r.getRight() - q.getRight());
        if (val == 0) return 0;  // Collinear
        return (val > 0) ? 1 : 2; // Clockwise or counterclockwise
    }

    static boolean onSegment(Pair<Double, Double> p, Pair<Double, Double> q, Pair<Double, Double> r) {
        return q.getLeft() <= Math.max(p.getLeft(), r.getLeft()) && q.getLeft() >= Math.min(p.getLeft(), r.getLeft()) &&
                q.getRight() <= Math.max(p.getRight(), r.getRight()) && q.getRight() >= Math.min(p.getRight(), r.getRight());
    }

    private static boolean areParallel(Trajectory trajectory1, Trajectory trajectory2) {
        return trajectory1.getDeltaX() * trajectory2.getDeltaY() == trajectory1.getDeltaY() * trajectory2.getDeltaX();
    }

    private static Trajectory parseLine(String s) {
        String[] split = s.split(" *[,@] *");
        long[] trajectoryValues = Arrays.stream(split).mapToLong(Long::parseLong).toArray();
        return new Trajectory(trajectoryValues[0], trajectoryValues[1], trajectoryValues[3], trajectoryValues[4]);
    }

    @RequiredArgsConstructor
    @ToString
    @Getter
    static class Trajectory {

        final private long X;
        final private long Y;
        final private long deltaX;
        final private long deltaY;

        private Pair<Double, Double> point1;
        private Pair<Double, Double> point2;

        boolean intersectsBox() {
            return getPoint1() != null && getPoint2() != null;
        }

        void setBoxIntersections() {
            var topBoxIntersection = intersectHorizontalBoxLine(this, BOX_UPPER);
            var leftBoxIntersection = intersectVerticalBoxLine(this, BOX_LOWER);
            var bottomBoxIntersection = intersectHorizontalBoxLine(this, BOX_LOWER);
            var rightBoxIntersection = intersectVerticalBoxLine(this, BOX_UPPER);

            long count = Stream.of(topBoxIntersection, leftBoxIntersection, bottomBoxIntersection, rightBoxIntersection)
                    .filter(Objects::nonNull).count();

            if (count == 0) {
                return;
            } else if (count == 1) {
                point1 = Pair.of((double) getX(), (double) getY());
                if (topBoxIntersection != null) {
                    point2 = Pair.of(topBoxIntersection, BOX_UPPER_DOUBLE);
                } else if (leftBoxIntersection != null) {
                    point2 = Pair.of(BOX_LOWER_DOUBLE, leftBoxIntersection);
                } else if (bottomBoxIntersection != null) {
                    point2 = Pair.of(bottomBoxIntersection, BOX_LOWER_DOUBLE);
                } else if (rightBoxIntersection != null) {
                    point2 = Pair.of(BOX_UPPER_DOUBLE, rightBoxIntersection);
                }
            } else if (count == 2) {
                boolean firstSet = false;
                if (topBoxIntersection != null) {
                    var point = Pair.of(topBoxIntersection, BOX_UPPER_DOUBLE);
                    if (firstSet) {
                        point2 = point;
                    } else {
                        point1 = point;
                    }
                    firstSet = true;
                }
                if (leftBoxIntersection != null) {
                    var point = Pair.of(BOX_LOWER_DOUBLE, leftBoxIntersection);
                    if (firstSet) {
                        point2 = point;
                    } else {
                        point1 = point;
                    }
                    firstSet = true;
                }
                if (bottomBoxIntersection != null) {
                    var point = Pair.of(bottomBoxIntersection, BOX_LOWER_DOUBLE);
                    if (firstSet) {
                        point2 = point;
                    } else {
                        point1 = point;
                    }
                    firstSet = true;
                }
                if (rightBoxIntersection != null) {
                    var point = Pair.of(BOX_UPPER_DOUBLE, rightBoxIntersection);
                    if (firstSet) {
                        point2 = point;
                    } else {
                        point1 = point;
                    }
                }
            } else {
                throw new IllegalStateException();
            }
        }

        private Double intersectHorizontalBoxLine(Trajectory trajectory, long l) {
            long neededDelta = l - trajectory.getY();
            double stepsNeeded = (double) neededDelta / trajectory.getDeltaY();
            if (stepsNeeded < 0) {
                return null;
            }
            double intersection = trajectory.getX() + stepsNeeded * trajectory.getDeltaX();
            return intersection + EPSILON >= BOX_LOWER_DOUBLE && intersection - EPSILON <= BOX_UPPER_DOUBLE ? intersection : null;
        }

        private Double intersectVerticalBoxLine(Trajectory trajectory, long l) {
            long neededDelta = l - trajectory.getX();
            double stepsNeeded = (double) neededDelta / trajectory.getDeltaX();
            if (stepsNeeded < 0) {
                return null;
            }
            double intersection = trajectory.getY() + stepsNeeded * trajectory.getDeltaY();
            return intersection + EPSILON >= BOX_LOWER_DOUBLE && intersection - EPSILON <= BOX_UPPER_DOUBLE ? intersection : null;
        }
    }
}
