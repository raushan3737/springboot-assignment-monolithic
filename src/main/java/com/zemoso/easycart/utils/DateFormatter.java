package com.zemoso.easycart.utils;


import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Configuration
public class DateFormatter {

    public String formatToIndiaTimeZone(Date date) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

}
