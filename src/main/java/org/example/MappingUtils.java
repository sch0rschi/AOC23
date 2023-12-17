package org.example;

class MappingUtils {
    static int[] mapDigitsStringToIntegers(String digitString) {
        return digitString.chars().map(Character::getNumericValue).toArray();
    }
}
