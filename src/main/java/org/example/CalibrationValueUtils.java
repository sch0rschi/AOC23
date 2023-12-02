package org.example;

public class CalibrationValueUtils {
    static int mapNumberStringToCalibrationValue(String line) {
        String digits = line.replaceAll("zero", "zero0zero");
        digits = digits.replaceAll("one", "one1one");
        digits = digits.replaceAll("two", "two2two");
        digits = digits.replaceAll("three", "three3three");
        digits = digits.replaceAll("four", "four4four");
        digits = digits.replaceAll("five", "five5five");
        digits = digits.replaceAll("six", "six6six");
        digits = digits.replaceAll("seven", "seven7seven");
        digits = digits.replaceAll("eight", "eight8eight");
        digits = digits.replaceAll("nine", "nine9nine");
        digits = digits.replaceAll("\\D", "");
        digits = String.valueOf(digits.charAt(0)) + digits.charAt(digits.length() - 1);
        return Integer.parseInt(digits);
    }


    static int mapDigitStringToCalibrationValue(String line) {
        String digits = line.replaceAll("\\D", "");
        digits = String.valueOf(digits.charAt(0)) + digits.charAt(digits.length() - 1);
        return Integer.parseInt(digits);
    }
}
