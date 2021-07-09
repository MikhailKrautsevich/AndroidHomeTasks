package com.example.criminalintent;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Date;
import java.util.GregorianCalendar;

public class DateFormatter {

    static String getFormattedTime(Date date, Context context) {
        GregorianCalendar calendar = new GregorianCalendar() ;
        calendar.setGregorianChange(date);
        return android.text.format.DateUtils.formatDateTime(context,
                date.getTime(),
                android.text.format.DateUtils.FORMAT_SHOW_TIME);
    }

    static String getFormattedDate(Date date, Context context) {
        GregorianCalendar calendar = new GregorianCalendar() ;
        calendar.setGregorianChange(date);
        return DateUtils.formatDateTime(context,
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_YEAR|DateUtils.FORMAT_SHOW_WEEKDAY);
    }
}
