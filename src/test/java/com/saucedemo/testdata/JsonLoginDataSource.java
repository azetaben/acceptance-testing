package com.saucedemo.testdata;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saucedemo.models.ExternalLoginDataRow;
import com.saucedemo.utils.PathUtil;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class JsonLoginDataSource implements LoginDataSource {
    private final String jsonFilePath;
    private volatile Map<String, ExternalLoginDataRow> rowsById;

    public JsonLoginDataSource(String jsonFilePath) {
        this.jsonFilePath = jsonFilePath;
    }

    public JsonLoginDataSource() {
        this(PathUtil.getTestDataJsonFilePath("login_external_data.json"));
    }

    private static ExternalLoginDataRow mapRow(Map<String, String> row) {
        return new ExternalLoginDataRow(
                normalize(value(row, "testCaseId")),
                normalize(value(row, "username")),
                normalize(value(row, "password")),
                normalize(value(row, "expectedResult")),
                normalize(value(row, "expectedMessage"))
        );
    }

    private static String value(Map<String, String> row, String expectedKey) {
        if (row == null || expectedKey == null) return "";
        return row.entrySet().stream()
                .filter(e -> e.getKey() != null)
                .filter(e -> e.getKey().trim().equalsIgnoreCase(expectedKey))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("");
    }

    private static String normalize(String v) {
        return v == null ? "" : v.trim();
    }

    @Override
    public ExternalLoginDataRow findById(String testCaseId) {
        if (testCaseId == null || testCaseId.isBlank()) {
            throw new IllegalArgumentException("testCaseId must not be blank");
        }
        String key = testCaseId.trim().toUpperCase(Locale.ROOT);
        ExternalLoginDataRow row = loadRows().get(key);
        if (row == null) {
            throw new IllegalArgumentException("No JSON login data row found for testCaseId: " + testCaseId + " in file: " + jsonFilePath);
        }
        return row;
    }

    private Map<String, ExternalLoginDataRow> loadRows() {
        if (rowsById != null) {
            return rowsById;
        }
        synchronized (this) {
            if (rowsById != null) {
                return rowsById;
            }
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String, String>> rows = objectMapper.readValue(
                        new File(jsonFilePath),
                        new TypeReference<>() {
                        }
                );

                Map<String, ExternalLoginDataRow> loaded = new LinkedHashMap<>();
                for (Map<String, String> row : rows) {
                    ExternalLoginDataRow mapped = mapRow(row);
                    if (mapped.testCaseId() == null || mapped.testCaseId().isBlank()) {
                        continue;
                    }
                    loaded.put(mapped.testCaseId().trim().toUpperCase(Locale.ROOT), mapped);
                }
                rowsById = loaded;
                return rowsById;
            } catch (IOException e) {
                throw new RuntimeException("Unable to load JSON login data file: " + jsonFilePath, e);
            }
        }
    }
}
