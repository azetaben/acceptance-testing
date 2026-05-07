package com.saucedemo.helperutilities.logger;

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
