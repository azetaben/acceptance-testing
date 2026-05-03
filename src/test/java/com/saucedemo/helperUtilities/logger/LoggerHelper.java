package com.saucedemo.helperUtilities.logger;

import org.apache.log4j.Logger;

import java.net.URL;

public class LoggerHelper {

    private static volatile boolean configured = false;

    private LoggerHelper() {
    }

    public static Logger getLogger(Class<?> cls) {
        configureIfNeeded();
        return Logger.getLogger(cls);
    }

    public static void configureIfNeeded() {
        if (configured) {
            return;
        }
        synchronized (LoggerHelper.class) {
            if (configured) {
                return;
            }

            // Log4j2 auto-loads log4j2.properties from classpath. If a custom location is
            // already provided via JVM arg, keep it. Otherwise set a classpath-discovered one.
            if (System.getProperty("log4j.configurationFile") == null) {
                URL log4j2Config = Thread.currentThread().getContextClassLoader().getResource("log4j2.properties");
                if (log4j2Config != null) {
                    System.setProperty("log4j.configurationFile", log4j2Config.toString());
                }
            }

            configured = true;
        }
    }
}
