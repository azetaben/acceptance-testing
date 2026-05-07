package com.saucedemo.transformers;

import com.saucedemo.models.LoginData;

import java.util.Map;

public class LoginDataTransformer {

    public static LoginData loginDataTransformer(Map<String, String> entry) {
        return new LoginData(
                entry.get("username"),
                entry.get("password"),
                entry.get("expectedError")
        );
    }
}
