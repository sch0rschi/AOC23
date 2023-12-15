package org.example;

public class HashUtils {

    static int hash(String s) {
        return s.chars().reduce(0, (accu, add) -> {
            accu += add;
            accu *= 17;
            accu %= 256;
            return accu;
        });
    }

    static int hash(String s, int length) {
        return s.chars().limit(length).reduce(0, (accu, add) -> {
            accu += add;
            accu *= 17;
            accu %= 256;
            return accu;
        });
    }
}
