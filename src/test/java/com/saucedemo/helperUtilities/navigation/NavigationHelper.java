package com.saucedemo.helperUtilities.navigation;

import com.saucedemo.helperUtilities.logger.LoggerHelper;
import com.saucedemo.interfaces.IwebComponent;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.net.URL;

public class NavigationHelper implements IwebComponent {
    private static WebDriver driver;

    private final Logger log = LoggerHelper.getLogger(NavigationHelper.class);

    public NavigationHelper() {
        log.debug("NavigationHelper : " + driver.hashCode());
    }

    public void navigateTo(String url) {
        log.info(url);
        driver.get(url);
    }

    public void navigateTo(URL url) {
        log.info(url.getPath());
        driver.get(url.getPath());
    }

    public String getTitle() {
        String title = driver.getTitle();
        log.info(title);
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        log.info(url);
        return driver.getCurrentUrl();
    }
}
