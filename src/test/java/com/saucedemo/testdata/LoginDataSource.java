package com.saucedemo.testdata;

import com.saucedemo.models.ExternalLoginDataRow;

/**
 * Abstraction for loading login credentials and expected outcomes from an external source
 * (JSON/Excel/CSV/etc). Keeps step definitions decoupled from the underlying storage.
 */
public interface LoginDataSource {
    ExternalLoginDataRow findById(String testCaseId);
}

