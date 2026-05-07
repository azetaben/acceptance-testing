package com.saucedemo.helperutilities.webelement;

import java.util.List;

public interface WebElementOrderChecker {

    public boolean areWebElementsOrderedLikeSpecifiedListByElementId(List<String> expectedOrder);
}
