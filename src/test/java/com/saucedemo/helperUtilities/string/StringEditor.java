package com.saucedemo.helperutilities.string;

public class StringEditor {
    public static String removeWord(String string, String wordToBeRemoved) {


        if (string.contains(wordToBeRemoved)) {


            String tempWord = wordToBeRemoved + " ";
            string = string.replaceAll(tempWord, "");


            tempWord = " " + wordToBeRemoved;
            string = string.replaceAll(tempWord, "");
        }


        return string;
    }
}
