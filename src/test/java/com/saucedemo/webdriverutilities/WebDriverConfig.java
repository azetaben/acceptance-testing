package com.saucedemo.webdriverutilities;

import com.saucedemo.configreader.FrameworkConfig;
import com.saucedemo.utils.PathUtil;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;


public final class WebDriverConfig {

    private static final FrameworkConfig CONFIG = FrameworkConfig.getInstance();

    private WebDriverConfig() {
    }

    public static ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", buildChromePreferences());
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments(
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--window-size=1920,1080",
                "--start-maximized",
                "--disable-extensions",
                "--disable-gpu",
                "--disable-notifications",
                "--disable-popup-blocking",
                "--ignore-certificate-errors",
                "--remote-allow-origins=*"
        );
        if (isHeadless()) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    public static FirefoxOptions buildFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1920", "--height=1080");
        if (isHeadless()) {
            options.addArguments("--headless");
        }
        return options;
    }

    public static EdgeOptions buildEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments(
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--window-size=1920,1080",
                "--start-maximized",
                "--disable-notifications",
                "--remote-allow-origins=*"
        );
        if (isHeadless()) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    private static Map<String, Object> buildChromePreferences() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", PathUtil.getDownloadsDir());
        prefs.put("download.prompt_for_download", false);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("safebrowsing.enabled", false);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        return prefs;
    }


    private static boolean isHeadless() {
        String sysProp = System.getProperty("headless");
        if (sysProp != null) return Boolean.parseBoolean(sysProp.trim());
        return CONFIG.getBoolean("headless", false);
    }
}
