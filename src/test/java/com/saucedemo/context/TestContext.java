package com.saucedemo.context;

import com.saucedemo.domainobjects.Cookies;
import com.saucedemo.domainobjects.LoginDetails;
import org.openqa.selenium.WebDriver;

public class TestContext {
    public WebDriver driver;
    public LoginDetails loginDetails;
    public Cookies cookies;

    public TestContext(){
        cookies = new Cookies();
        cookies.setCookies(new io.restassured.http.Cookies());
    }
}
