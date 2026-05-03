/**
 * @author rahul.rathore
 * <p>07-Aug-2016
 */
package com.saucedemo.helperUtilities.HyperLink;


import com.saucedemo.helperUtilities.logger.LoggerHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

public class LinkHelper {

    private final Logger log = LoggerHelper.getLogger(LinkHelper.class);

    public String getHyperLink(WebElement element) {
        String link = element.getDomAttribute("hreg");
        log.info("Element : " + element + " Value : " + link);
        return link;
    }
}
