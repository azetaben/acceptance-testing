package com.saucedemo.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {

    private static final Logger log = LogManager.getLogger(PropertyUtils.class);

    public static Properties propertyLoader(String filePath) {
        Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            properties.load(reader);
        } catch (FileNotFoundException e) {
            log.error("Properties file not found at: " + filePath, e);
            throw new RuntimeException("properties file not found at " + filePath, e);
        } catch (IOException e) {
            log.error("Failed to load properties file: " + filePath, e);
            throw new RuntimeException("failed to load properties file " + filePath, e);
        }
        return properties;
    }
}
