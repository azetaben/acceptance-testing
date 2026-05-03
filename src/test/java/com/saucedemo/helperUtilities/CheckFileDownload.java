package com.saucedemo.helperUtilities;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

public class CheckFileDownload {

    private static final Duration DOWNLOAD_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration POLL_INTERVAL = Duration.ofMillis(500);

    public static boolean isFileDownloaded(String downloadPath, String fileName) throws InterruptedException {
        Instant deadline = Instant.now().plus(DOWNLOAD_TIMEOUT);
        File dir = new File(downloadPath);

        while (Instant.now().isBefore(deadline)) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().contains(fileName)) {
                        return true;
                    }
                }
            }
            Thread.sleep(POLL_INTERVAL.toMillis());
        }
        return false;
    }
}