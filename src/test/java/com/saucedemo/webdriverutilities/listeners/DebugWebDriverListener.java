package com.saucedemo.webdriverutilities.listeners;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DebugWebDriverListener implements WebDriverListener {
    private static final Logger log = LogManager.getLogger(DebugWebDriverListener.class);

    private final List<String> events = Collections.synchronizedList(new ArrayList<>());

    public List<String> getEventsSnapshot() {
        synchronized (events) {
            return new ArrayList<>(events);
        }
    }

    @Override
    public void beforeGet(WebDriver driver, String url) {
        record("beforeGet:" + url);
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        record("afterGet:" + url);
    }

    @Override
    public void beforeClick(WebElement element) {
        record("beforeClick:" + safeElement(element));
    }

    @Override
    public void afterClick(WebElement element) {
        record("afterClick:" + safeElement(element));
    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        Throwable root = e.getTargetException() != null ? e.getTargetException() : e;
        record("onError:" + method.getName() + ":" + root.getClass().getSimpleName());
    }

    private void record(String message) {
        events.add(message);
        log.info("[S4-LISTENER] " + message);
    }

    private String safeElement(WebElement element) {
        try {
            return element.getTagName();
        } catch (Exception ignored) {
            return "unknown-element";
        }
    }
}
