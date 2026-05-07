package com.saucedemo.helperutilities.file;

import com.saucedemo.constants.FrameworkConstants;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CheckFileDownload {

    private static final Logger log = LogManager.getLogger(CheckFileDownload.class);

    private static final Duration DEFAULT_TIMEOUT =
            Duration.ofSeconds(FrameworkConstants.getDownloadTimeoutSeconds());

    private static final Set<String> PARTIAL_EXTENSIONS = Set.of(
            ".crdownload",
            ".part",
            ".tmp",
            ".download"
    );

    public static boolean isFileDownloaded(String downloadPath, String fileName) {
        return isFileDownloaded(downloadPath, fileName, DEFAULT_TIMEOUT);
    }

    public static boolean isFileDownloaded(String downloadPath, String fileName, Duration timeout) {
        if (downloadPath == null || downloadPath.isBlank()) {
            throw new IllegalArgumentException("downloadPath must not be null or blank");
        }
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("fileName must not be null or blank");
        }
        if (timeout == null || timeout.isNegative() || timeout.isZero()) {
            throw new IllegalArgumentException("timeout must be a positive duration");
        }

        Path dir = Path.of(downloadPath);
        if (!Files.isDirectory(dir)) {
            log.warn("Download directory does not exist: " + downloadPath);
            return false;
        }

        if (scanForCompletedFile(dir, fileName)) {
            log.info("File already present: " + fileName);
            return true;
        }

        log.info("Watching for '" + fileName + "' in: " + downloadPath + " (timeout=" + timeout.getSeconds() + "s)");

        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            dir.register(watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            long remainingNanos = timeout.toNanos();
            long start = System.nanoTime();

            while (remainingNanos > 0) {
                WatchKey key = watcher.poll(remainingNanos, TimeUnit.NANOSECONDS);
                if (key == null) {
                    break;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                        if (scanForCompletedFile(dir, fileName)) {
                            key.reset();
                            return true;
                        }
                        continue;
                    }
                    String changed = dir.resolve((Path) event.context()).getFileName().toString();
                    if (isCompleteMatch(changed, fileName)) {
                        log.info("Download complete: " + changed);
                        key.reset();
                        return true;
                    }
                }

                key.reset();
                remainingNanos = timeout.toNanos() - (System.nanoTime() - start);
            }

        } catch (IOException e) {
            log.warn("WatchService unavailable, falling back to directory scan: " + e.getMessage());
            return scanForCompletedFile(dir, fileName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Download wait interrupted while watching for: " + fileName);
            return false;
        }

        log.warn("Timed out after " + timeout.getSeconds() + "s waiting for: " + fileName);
        return false;
    }

    private static boolean scanForCompletedFile(Path dir, String fileName) {
        try (var stream = Files.list(dir)) {
            return stream
                    .map(p -> p.getFileName().toString())
                    .anyMatch(name -> isCompleteMatch(name, fileName));
        } catch (IOException e) {
            log.warn("Directory scan failed: " + e.getMessage());
            return false;
        }
    }

    private static boolean isCompleteMatch(String name, String fileName) {
        if (!name.contains(fileName)) {
            return false;
        }
        for (String ext : PARTIAL_EXTENSIONS) {
            if (name.endsWith(ext)) {
                return false;
            }
        }
        return true;
    }
}
