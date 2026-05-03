package com.saucedemo.helperUtilities.number_StringGen;

import java.util.Random;

public class NumberGeneratorUtils {

    public static String generateNumberWithLength(int numberOfChars) {
        Random random = new Random();
        String number = "";
        for (int i = 0; i < numberOfChars; i++) {
            number += Integer.toString(random.nextInt(9));
        }
        return number;
    }
}
