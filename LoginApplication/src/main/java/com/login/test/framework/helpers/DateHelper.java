package com.login.test.framework.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by s746032 on 07/03/2016.
 */
public class DateHelper {

    public static String DEFAULT_DATE_FORMAT = "dd MMM yyyy";
    public static String FLIGHT_SEGMENT_DATE_FORMAT = "dd MMM yy";
    public static String CALENDAR_DATE_FORMAT = "EEEE MMMM d yyyy";
    public static String DATE_FORMAT = "ddMMMyyyy HH:mm:ss";
    public static String COMBINED_DATE_FORMAT = "ddMMMyyyy";


    public static String addDaysToGivenDate(Date givenDate, int numberOfDays, String dateFormat) {
        SimpleDateFormat formats = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.setTime(givenDate);
        cal.add(Calendar.DATE, numberOfDays);
        return formats.format(cal.getTime());
    }

    public static String systemFutureDate(int numberOfDays, String dateFormat) {
        SimpleDateFormat formats = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, numberOfDays);
        return formats.format(cal.getTime());
    }

    public static String systemPastDate(int numberOfDays, String dateFormat) {
        SimpleDateFormat formats = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -numberOfDays);
        return formats.format(cal.getTime());
    }

    public static long getDayCount(Date dateStart, Date dateEnd) {
        if (dateStart == null) {
            dateStart = new Date();
        }
        long diff = -1;
        try {
            diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
        } catch (Exception ex) {
        }
        return diff;
    }

    public static String currentDate(String format) {
        SimpleDateFormat formats = new SimpleDateFormat(format);
        return formats.format(new Date());
    }

    public static HashMap getDateDiff(LocalDate startDate, LocalDate endDate) throws ParseException {
        HashMap<String, String> dateDiff = new HashMap<>();
        Period diff = Period.between(startDate, endDate);
        if (diff.getYears() > 0) {
            dateDiff.put("Year", String.valueOf(diff.getYears()));
        } else if (diff.getMonths() > 0) {
            dateDiff.put("Months", String.valueOf(diff.getMonths()));
        } else if (diff.getDays() > 0) {
            dateDiff.put("Days", String.valueOf(diff.getDays()));
        }
        return dateDiff;
    }
}
