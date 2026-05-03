package com.saucedemo.pages;

import com.saucedemo.webdriverutilities.WebDrv;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PageManager {
    private static final Logger log = LogManager.getLogger(PageManager.class);
    private static final PageManager INSTANCE = new PageManager();

    private final Map<Class<?>, Object> pageCache = new ConcurrentHashMap<>();
    private WebDriver cachedDriver;

    private PageManager() {
    }

    public static PageManager getInstance() {
        return INSTANCE;
    }

    public synchronized <T> T getPage(Class<T> pageClass) {
        WebDriver currentDriver = WebDrv.getInstance().getWebDriver();
        if (currentDriver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Navigate/open browser before requesting page objects.");
        }

        if (cachedDriver != currentDriver) {
            log.info("WebDriver instance changed, clearing page cache.");
            clear();
            cachedDriver = currentDriver;
        }

        boolean alreadyCached = pageCache.containsKey(pageClass);
        Object page = pageCache.computeIfAbsent(pageClass, cls -> createPage(currentDriver, pageClass));
        if (!alreadyCached) {
            log.info("Created and cached page object: " + pageClass.getSimpleName());
        } else {
            log.debug("Returning cached page object: " + pageClass.getSimpleName());
        }
        return pageClass.cast(page);
    }

    public synchronized void clear() {
        log.info("Clearing page object cache. Entries cleared: " + pageCache.size());
        pageCache.clear();
        cachedDriver = null;
    }

    private <T> T createPage(WebDriver driver, Class<T> pageClass) {
        log.debug("Initialising page elements for: " + pageClass.getSimpleName());
        return PageFactory.initElements(driver, pageClass);
    }
}
