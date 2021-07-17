package com.example.criminalintent;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Date;
import java.util.GregorianCalendar;

public class MyDateUtil {

    public static String getFormattedDate(Date date, Context context) {
        GregorianCalendar calendar = new GregorianCalendar() ;
        calendar.setGregorianChange(date);
        long millis = calendar.getGregorianChange().getTime() ;
        return DateUtils.formatDateTime(context,
                millis,
                DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_YEAR|DateUtils.FORMAT_SHOW_WEEKDAY);
    }
}
