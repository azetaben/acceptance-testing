package com.saucedemo.pages;

import com.saucedemo.helperUtilities.elements.ElementHelper;
import com.saucedemo.helperUtilities.globalVar.GlobalVarsHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.time.Duration;

public class AboutPage extends Page {
    private static final Logger log = LogManager.getLogger(AboutPage.class);
    private final ElementHelper elementHelper = new ElementHelper(driver, Duration.ofSeconds(GlobalVarsHelper.getDefaultExplicitTimeout()));

}
