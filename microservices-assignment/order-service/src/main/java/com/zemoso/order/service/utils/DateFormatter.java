package com.zemoso.order.service.utils;


import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class DateFormatter {

    public String formatToIndiaTimeZone(Date date) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

}
