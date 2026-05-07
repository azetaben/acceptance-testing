package com.saucedemo.testdata;

import java.util.Locale;

public final class LoginDataSources {
    private static final LoginDataSource JSON = new JsonLoginDataSource();
    private static final LoginDataSource EXCEL = new ExcelLoginDataSource();

    private LoginDataSources() {
    }

    public static LoginDataSource byName(String source) {
        String normalized = source == null ? "" : source.trim().toLowerCase(Locale.ROOT);
        return switch (normalized) {
            case "json" -> JSON;
            case "excel", "xlsx" -> EXCEL;
            default -> throw new IllegalArgumentException("Unsupported source: " + source + ". Use 'json' or 'excel'.");
        };
    }
}
