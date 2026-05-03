package com.saucedemo.helperUtilities.file;

import com.google.common.util.concurrent.Uninterruptibles;
import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import org.testng.Assert;

import java.io.File;
import java.time.Duration;

public class CheckFileDownload {
    public static boolean isFileDownloaded(String downloadPath, String fileName) throws Exception {
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(GlobalVarsHelper.explicitWait));

        File dir = new File(downloadPath);
        File[] dirContents = dir.listFiles();

        for (int i = 0; i < dirContents.length; i++) {
            if (dirContents[i].getName().contains(fileName)) {
                Assert.assertTrue(true, "Unable to download ...");
                return true;
            }
        }
        return false;
    }
}
