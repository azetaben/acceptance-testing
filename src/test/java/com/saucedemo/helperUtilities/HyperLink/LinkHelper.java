package com.saucedemo.helperutilities.hyperlink;


import com.saucedemo.helperutilities.logger.LoggerHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

public class LinkHelper {

    private final Logger log = LoggerHelper.getLogger(LinkHelper.class);

    public String getHyperLink(WebElement element) {
        String link = element.getDomAttribute("href");
        log.info("Element : " + element + " Value : " + link);
        return link;
    }
}
