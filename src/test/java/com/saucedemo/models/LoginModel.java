package com.saucedemo.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginModel {
    private String username;
    private String password;

    public LoginModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;

    }

    public String getPassword() {
        return password;

    }

}

