package com.saucedemo.customtype;

import com.saucedemo.domainobjects.LoginDetails;
import io.cucumber.java.DataTableType;

import java.util.Map;

public class CustomDataTableType {

    @DataTableType
    public LoginDetails loginDetailsEntry(Map<String, String> entry){
        return new LoginDetails(
                entry.get("Accepted username"),
                entry.get("Password for all users"));

    }
}
