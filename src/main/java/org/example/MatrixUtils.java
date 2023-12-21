package org.example;

import java.lang.reflect.Array;
import java.util.List;

public class MatrixUtils {

    static <T>T[][] mapToArrays(List<List<T>> lists) {
        int height = lists.size();
        int width = lists.get(0).size();
        T[][] Ts = (T[][]) Array.newInstance(lists.get(0).get(0).getClass(), height, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Ts[y][x] = lists.get(y).get(x);
            }
        }
        return Ts;
    }
    
    static <T>T[][] transpose(T[][] array) {
        int rows = array.length;
        int cols = array[0].length;

        T[][] transposedArray = (T[][]) Array.newInstance(array[0][0].getClass(), cols, rows);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposedArray[j][i] = array[i][j];
            }
        }

        return transposedArray;
    }

    static <T>void transpose(T[][] from, T[][] to) {
        int rows = from.length;
        int cols = from[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                to[j][i] = from[i][j];
            }
        }
    }

    static <T>T[][] rotateClockwise(T[][] array) {
        int rows = array.length;
        int cols = array[0].length;


        T[][] transposedArray = (T[][]) Array.newInstance(array[0][0].getClass(), cols, rows);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposedArray[j][cols - i - 1] = array[i][j];
            }
        }

        return transposedArray;
    }

    static <T>void rotateClockwise(T[][] from, T[][] to) {
        int rows = from.length;
        int cols = from[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                to[j][cols - i - 1] = from[i][j];
            }
        }
    }

    static void rotateClockwise(int[][] from, int[][] to) {
        int cols = from[0].length;
        for (int i = 0; i < from.length; i++) {
            for (int j = 0; j < cols; j++) {
                to[j][cols - i - 1] = from[i][j];
            }
        }
    }

    static void rotateClockwise(byte[][] from, byte[][] to) {
        int cols = from[0].length;
        for (int i = 0; i < from.length; i++) {
            for (int j = 0; j < cols; j++) {
                to[j][cols - i - 1] = from[i][j];
            }
        }
    }

    static <T>void rotateCounterClockwise(T[][] from, T[][] to) {
        int rows = from.length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < from[0].length; j++) {
                to[rows - j - 1][i] = from[i][j];
            }
        }
    }

    static void rotateCounterClockwise(int[][] from, int[][] to) {
        int rows = from.length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < from[0].length; j++) {
                to[rows - j - 1][i] = from[i][j];
            }
        }
    }

    static void rotateCounterClockwise(byte[][] from, byte[][] to) {
        int rows = from.length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < from[0].length; j++) {
                to[rows - j - 1][i] = from[i][j];
            }
        }
    }

    static <T>void printPattern(T[][] columns) {
        for (T[] rows : columns) {
            for (T element : rows) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    static void printPattern(int[][] columns) {
        for (var rows : columns) {
            for (var element : rows) {
                System.out.print((char)element + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
