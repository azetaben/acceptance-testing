package com.saucedemo.helperUtilities.string;

public class StringEditor {
    public static String removeWord(String string, String wordToBeRemoved) {

        // Check if the word is present in string
        // If found, remove it using removeAll()
        if (string.contains(wordToBeRemoved)) {

            // To cover the case
            // if the word is at the
            // beginning of the string
            // or anywhere in the middle
            String tempWord = wordToBeRemoved + " ";
            string = string.replaceAll(tempWord, "");

            // To cover the edge case
            // if the word is at the
            // end of the string
            tempWord = " " + wordToBeRemoved;
            string = string.replaceAll(tempWord, "");
        }

        // Return the resultant string
        return string;
    }
}
