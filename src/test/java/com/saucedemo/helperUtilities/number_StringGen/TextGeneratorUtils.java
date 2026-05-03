package com.saucedemo.helperUtilities.number_StringGen;

import java.util.Collections;
import java.util.Random;

public class TextGeneratorUtils {

    private static final Random random = new Random();
    private static final char[] symbols;
    private static final char[] numbers;
    private static final int MAX_SIZE = 15;
    private static final int NUM_SIZE = 3;
    static String repeatableChar = "o";
    static int charCount = 0;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch) tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch) tmp.append(ch);
        symbols = tmp.toString().toCharArray();
    }

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch) tmp.append(ch);
        numbers = tmp.toString().toCharArray();
    }

    public static String generateStringWithLength(int characterCount) {
        charCount = characterCount;
        return generateString();
    }

    private static String generateString() {
        String generatedString = repeatableChar.replace(repeatableChar, String.join("", Collections.nCopies(charCount, repeatableChar)));
        return generatedString;
    }

    public static CharSequence generateStringWithLengthAndCase(int numberOfChars, String caseType) {
        charCount = numberOfChars;
        if (caseType.equals("upper")) {
            repeatableChar = "O";
        }
        return generateString();
    }

    public static String getRandomString() {
        char[] buf = new char[MAX_SIZE];
        for (int idx = 0; idx < buf.length; ++idx) buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }
}
