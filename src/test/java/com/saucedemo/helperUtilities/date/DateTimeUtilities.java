package com.saucedemo.helperUtilities.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtilities {

    public static String getCurrentDateInSimpleDateFormat(String dateTemplate) {
        DateFormat dateFormat = new SimpleDateFormat(dateTemplate);
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**** Generates Future Date (Current Date + 5 days) in dd-mm-yyyy format *****/
    public static String getFutureDate() {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.now().plusDays(5);
        String futureDate = localDate.format(dateTimeFormat);
        return futureDate;
    }

    /**** Generates Past Date (Current Date - 5 days) in dd-mm-yyyy format *****/
    public static String getPastDate() {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.now().minusDays(5);
        String pastDate = localDate.format(dateTimeFormat);
        return pastDate;
    }

    public static String getMoreThanOneYearPastDate() {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.now().minusDays(500);
        String pastDate = localDate.format(dateTimeFormat);
        return pastDate;
    }
}
