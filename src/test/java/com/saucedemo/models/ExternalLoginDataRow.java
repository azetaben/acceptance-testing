package com.saucedemo.models;


public record ExternalLoginDataRow(
        String testCaseId,
        String username,
        String password,
        String expectedResult,
        String expectedMessage
    ){
}
