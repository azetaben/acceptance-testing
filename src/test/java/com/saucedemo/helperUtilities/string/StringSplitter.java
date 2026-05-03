package com.saucedemo.helperUtilities.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringSplitter {

    public static List<String> splitStringIntoAnArrayWithADelimiterAndReturnIt(String delimiter, String stringToSplit) {
        List<String> stringParts = new ArrayList<>();
        if (stringToSplit.contains(delimiter)) {
            stringParts = Arrays.asList(stringToSplit.split(delimiter));
        } else {
            stringParts.add(stringToSplit);
        }

        return stringParts;
    }
}
