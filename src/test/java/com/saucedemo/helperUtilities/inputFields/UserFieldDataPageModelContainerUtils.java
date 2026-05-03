package com.saucedemo.helperUtilities.inputFields;

import java.util.HashMap;
import java.util.Map;

public class UserFieldDataPageModelContainerUtils {

    private static Map<String, Map<String, String>> pageFieldData = new HashMap<>();

    public static void registerFieldDataModelForPage(String pageName, Map<String, String> fieldValues) {
        pageFieldData.put(pageName, fieldValues);
    }

    public static Map<String, String> getFieldDataFromPage(String pageName) {
        return pageFieldData.get(pageName);
    }
}
