package com.zemoso.order.service.utils;


import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateFormatterTest {

    @Test
    void testFormatToIndiaTimeZone() {
        // Mocking data
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(timeZone);
        String expectedFormattedDate = dateFormat.format(date);

        // Creating the DateFormatter instance
        DateFormatter dateFormatter = new DateFormatter();

        // Calling the method
        String formattedDate = dateFormatter.formatToIndiaTimeZone(date);

        // Assertions
        assertEquals(expectedFormattedDate, formattedDate);
    }


}

