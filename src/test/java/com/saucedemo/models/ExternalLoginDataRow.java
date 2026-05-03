package com.saucedemo.models;

/**
 * Represents a single row of externally-driven login test data.
 *
 * expectedResult is typically "SUCCESS" or "FAILURE" (case-insensitive).
 * expectedMessage is used when expectedResult indicates failure.
 */
public record ExternalLoginDataRow(
        String testCaseId,
        String username,
        String password,
        String expectedResult,
        String expectedMessage
) {
}

