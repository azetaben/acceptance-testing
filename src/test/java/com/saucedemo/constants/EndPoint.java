package com.saucedemo.constants;

public enum EndPoint {
    LOGIN("/"),
    INVENTORY("/inventory.html"),
    CART("/cart"),
    CHECKOUT_STEP_ONE("/checkout-step-one"),
    CHECKOUT_STEP_TWO("/checkout-step-two"),
    CHECKOUT_COMPLETE("/checkout-complete");


    public final String url;

    EndPoint(String url) {
        this.url = url;
    }
}
