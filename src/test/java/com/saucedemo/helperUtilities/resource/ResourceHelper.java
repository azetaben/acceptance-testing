package com.saucedemo.helperutilities.resource;

import com.saucedemo.utils.PathUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceHelper {
    private static final Logger log = LogManager.getLogger(ResourceHelper.class);

    public static String getResourcePath(String path) {
        return PathUtil.getAbsolutePath(path);
    }

    public static String getBaseResourcePath() {
        return com.saucedemo.helperutilities.resource.ResourceHelper.class.getClass().getResource("/").getPath();
    }

    public static InputStream getResourcePathInputStream(String resource) throws FileNotFoundException {
        return new FileInputStream(com.saucedemo.helperutilities.resource.ResourceHelper.getResourcePath(resource));
    }
}
