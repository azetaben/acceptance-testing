package com.saucedemo.helperUtilities.excel;


import com.saucedemo.helperUtilities.logger.LoggerHelper;
import com.saucedemo.utils.PathUtil;
import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

public class DataProviders {
    private static final Logger log = LoggerHelper.getLogger(DataProviders.class);

    public DataProviders() {

    }

    @DataProvider(name = "LoginData")
    public Object[][] getLoginData(Method method) throws IOException {
        log.info("Getting Login Data from Excel");
        return getDataFromExcel("login", method);
    }

    @DataProvider(name = "RegistrationData")
    public Object[][] getRegistrationData(Method method) throws IOException {
        log.info("Getting Registration Data from Excel");
        return getDataFromExcel("registration", method);
    }

    private Object[][] getDataFromExcel(String sheetName, Method method) throws IOException {
        log.info("Getting data from Excel for sheet: " + sheetName);

        String filePath = PathUtil.getExcelFilePath("testData.xlsx");
        ExcelHelper excelHelper = new ExcelHelper(filePath, sheetName);
        List<Map<String, String>> dataList = excelHelper.getData();

        // Determine the number of columns based on the method's parameters
        Parameter[] parameters = method.getParameters();
        int parameterCount = parameters.length;
        log.info("Parameter count for method " + method.getName() + ": " + parameterCount);

        Object[][] data = new Object[dataList.size()][parameterCount];
        log.info("Number of rows in data: " + dataList.size());


        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> row = dataList.get(i);
            int j = 0;
            log.info("Processing row: " + (i + 1));

            // Iterate through the method's parameters and populate the data array
            for (Parameter parameter : parameters) {
                String parameterName = parameter.getName();
                log.info("Processing parameter: " + parameterName);
                // Check if the parameter is annotated with @Parameters
                Parameters parametersAnnotation = method.getAnnotation(Parameters.class);
                if (parametersAnnotation != null) {
                    String[] parameterNames = parametersAnnotation.value();
                    if (j < parameterNames.length) {
                        parameterName = parameterNames[j];
                        log.info("Parameter name updated from @Parameters: " + parameterName);

                    }
                }
                if (row.containsKey(parameterName)) {
                    data[i][j] = row.get(parameterName);
                    log.info("Data found for parameter '" + parameterName + "': " + data[i][j]);

                } else if (parameterName.equals("expectedResult")) {
                    data[i][j] = excelHelper.getExpectedResult(i + 1);
                    log.info("Data found for parameter 'expectedResult': " + data[i][j] + " in row " + (i + 1));
                } else if (parameterName.equals("testCaseDescription")) {
                    data[i][j] = excelHelper.getTestCaseDescription(i + 1);
                    log.info("Data found for parameter 'testCaseDescription': " + data[i][j] + " in row " + (i + 1));
                } else {
                    // Handle cases where the parameter name doesn't match a column
                    log.warn("Warning: No data found for parameter '" + parameterName + "' in row " + (i + 1));
                    data[i][j] = ""; // Or handle it differently (e.g., null, default value)
                }
                j++;
            }
        }
        log.info("Data retrieval completed for sheet: " + sheetName);
        return data;
    }
}
