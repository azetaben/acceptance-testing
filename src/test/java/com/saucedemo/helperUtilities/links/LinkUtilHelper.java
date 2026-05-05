package com.saucedemo.helperUtilities.links;

import java.net.HttpURLConnection;
import java.net.URL;

public class LinkUtilHelper {

    public static int getResponseCode(String link) {
        URL url;
        HttpURLConnection con = null;
        int responsecode = 0;
        try {
            url = new URL(link);
            con = (HttpURLConnection) url.openConnection();
            responsecode = con.getResponseCode();
        } catch (Exception e) {
            // skip
        } finally {
            if (null != con) con.disconnect();
        }
        return responsecode;
    }
}
