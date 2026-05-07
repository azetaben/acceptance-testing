package com.saucedemo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LighthouseUtil {
    private static final Logger log = LogManager.getLogger(LighthouseUtil.class);

    public static double parsePerformanceScore(String jsonOutput) {
        Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(jsonOutput, JsonObject.class);

        return jsonObject.getAsJsonObject("categories")
                .getAsJsonObject("performance")
                .get("score")
                .getAsDouble();
    }

    public static void runLighthouseAudit(String url) {
        try {

            ProcessBuilder processBuilder = new ProcessBuilder("lighthouse", url);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();


            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(String.valueOf(line));
            }


            int exitCode = process.waitFor();
            log.info(String.valueOf("Lighthouse audit completed with exit code: " + exitCode));
        } catch (IOException | InterruptedException e) {
            log.warn("Exception: " + e.getMessage(), e);
        }
    }
}
