package com.saucedemo.helperutilities.processfiles;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ProcessFileResource {
    private static final Logger log = LogManager.getLogger(ProcessFileResource.class);
    private JSONObject jsonObject;

    public ProcessFileResource(String filename) throws IOException, JSONException {
        log.info("Initializing ProcessFileResource with filename: " + filename);
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource(filename)).getFile());
        String content = FileUtils.readFileToString(file, "UTF8");
        jsonObject = new JSONObject(content);
        log.info("Successfully loaded JSON data from file: " + filename);
    }

    public JSONObject getJsonObject() {
        log.info("Getting the entire JSON object.");
        return jsonObject;
    }

    public JSONObject getNamedJSONObject(String name) throws JSONException {
        log.info("Getting named JSON object: " + name);
        JSONObject testdata = jsonObject.getJSONObject("testdata");
        JSONObject namedObject = testdata.getJSONObject(name);
        log.info("Retrieved named JSON object: " + namedObject);
        return namedObject;
    }

    public JSONObject changeAttribute(JSONObject original, String key, String value) throws JSONException {
        log.info("Changing attribute: " + key + " to value: " + value + " in JSON object: " + original);
        JSONObject updatedObject = original.put(key, value);
        log.info("Attribute changed successfully. Updated object: " + updatedObject);
        return updatedObject;
    }
}
