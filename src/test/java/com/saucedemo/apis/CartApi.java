package com.saucedemo.apis;

import com.saucedemo.constants.EndPoint;
import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.response.Response;

public class CartApi {
    private Cookies cookies;

    public CartApi(Cookies cookies) {
        this.cookies = cookies;
    }

    public Cookies getCookies() {
        return cookies;
    }

    public Response addToCart(String productName) {
        Response response = RestAssured.given()
                .spec(SpecBuilder.getRequestSpec())
                .cookies(cookies)
                .formParam("product", productName)
                .post(EndPoint.CART.url);
        this.cookies = response.getDetailedCookies();
        return response;
    }
}
