package com.saucedemo.configReader.DataProviders;

import com.saucedemo.utils.PathUtil;
import org.testng.annotations.DataProvider;

import java.io.IOException;

public class DataProviders {

    // DataProvider 1
    @DataProvider(name = "LoginData")
    public String[][] getData() throws IOException {
        String path = PathUtil.getTestDataExcelFilePath("testData.xlsx");


        ExcelUtility xlutil = new ExcelUtility(path);

        int totalrows = xlutil.getRowCount("login");
        int totalcols = xlutil.getCellCount("login", 1);

        String logindata[][] = new String[totalrows]
                [totalcols];

        for (int i = 1; i <= totalrows; i++) {
            for (int j = 0; j < totalcols; j++) // 0    i is rows j is col
            {
                logindata[i - 1][j] = xlutil.getCellData("login", i, j); // 1,0
            }
        }
        return logindata;
    }

    // DataProvider 2
    @DataProvider(name = "RegistrationData")
    public String[][] getRegistrationData() throws IOException {
        String path = PathUtil.getTestDataExcelFilePath("testData.xlsx");

        ExcelUtility xlutil = new ExcelUtility(path);

        int totalrows = xlutil.getRowCount("registration");
        int totalcols = xlutil.getCellCount("registration", 1);

        String logindata[][] = new String[totalrows]
                [totalcols];

        for (int i = 1; i <= totalrows; i++) {
            for (int j = 0; j < totalcols; j++) {
                logindata[i - 1][j] = xlutil.getCellData("registration", i, j);
            }
        }
        return logindata;
    }

    // DataProvider 3


    // DataProvider 4
}
