package com.saucedemo.helperUtilities.json;

import com.saucedemo.utils.PathUtil;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class GenericDataProvider {

    @DataProvider(name = "jsonDataProvider")
    public Object[][] jsonDataProvider(Method method) {
        String testMethodName = method.getName();
        String jsonFileName = getJsonFileName(testMethodName);
        String filePath = PathUtil.getTestDataJsonFilePath(jsonFileName);
        List<HashMap<String, String>> data = getJsonDataToMap(filePath);
        Object[][] dataArray = new Object[data.size()][1];

        for (int i = 0; i < data.size(); i++) {
            dataArray[i][0] = data.get(i);
        }
        return dataArray;
    }

    public List<HashMap<String, String>> getJsonDataToMap(String filePath) {
        return getJsonDataToMap(filePath);
    }

    private String getJsonFileName(String testMethodName) {
        return switch (testMethodName) {
            case "registrationTestValidCredentials" -> "createAccountExternal.json";
            case "verifyAccountCreation" -> "createAnAccount.json";
            case "loginTestValidCredentials" -> "login.json";
            // Add more cases for other test methods and their corresponding JSON files
            default ->
                    throw new IllegalArgumentException("No JSON file mapping found for test method: " + testMethodName);
        };
    }
}
