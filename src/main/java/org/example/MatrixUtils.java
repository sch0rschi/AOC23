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
}
