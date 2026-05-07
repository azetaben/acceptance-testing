package com.saucedemo.testdata;

import com.saucedemo.models.ExternalLoginDataRow;


public interface LoginDataSource {
    ExternalLoginDataRow findById(String testCaseId);
}
