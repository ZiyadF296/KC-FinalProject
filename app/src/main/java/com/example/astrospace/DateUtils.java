package com.example.astrospace;

public class DateUtils {
    public static String dayOfWeekFromInt(int day, boolean shortName) {
        switch (day) {
            case 1:
                return shortName ? "Mon" : "Monday";
            case 2:
                return shortName ? "Tue" : "Tuesday";
            case 3:
                return shortName ? "Wed" : "Wednesday";
            case 4:
                return shortName ? "Thu" : "Thursday";
            case 5:
                return shortName ? "Fri" : "Friday";
            case 6:
                return shortName ? "Sat" : "Saturday";
            case 7:
                return shortName ? "Sun" : "Sunday";
            default:
                return "Invalid day";
        }
    }

    public static String monthFromInt(int month, boolean shortName) {
        switch (month) {
            case 1:
                return shortName ? "Jan" : "January";
            case 2:
                return shortName ? "Feb" : "February";
            case 3:
                return shortName ? "Mar" : "March";
            case 4:
                return shortName ? "Apr" : "April";
            case 5:
                return "May";
            case 6:
                return shortName ? "Jun" : "June";
            case 7:
                return shortName ? "Jul" : "July";
            case 8:
                return shortName ? "Aug" : "August";
            case 9:
                return shortName ? "Sep" : "September";
            case 10:
                return shortName ? "Oct" : "October";
            case 11:
                return shortName ? "Nov" : "November";
            case 12:
                return shortName ? "Dec" : "December";
            default:
                return "Invalid month";
        }
    }
}
