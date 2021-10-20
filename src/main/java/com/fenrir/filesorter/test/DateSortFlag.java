package com.fenrir.filesorter.test.flags;

import com.fenrir.filesorter.test.Flag;

public class DateSortFlag implements Flag {
    public static final String YEAR = "YYYY";
    public static final String YEAR_TWO_DIGIT = "YY";
    public static final String MONTH_NUMBER = "M";
    public static final String MONTH_NUMBER_0 = "0M";
    public static final String MONTH_NAME_ABBREVIATION = "MM";
    public static final String MONTH_NAME = "MMM";
    public static final String WEEK_NUMBER_IN_MONTH = "W";
    public static final String WEEK_NUMBER_IN_MONTH_0 = "0W";
    public static final String WEEK_NUMBER_IN_YEAR = "WW";
    public static final String WEEK_NUMBER_IN_YEAR_0 = "0WW";
    public static final String DAY_NUMBER_IN_MONTH = "D";
    public static final String DAY_NUMBER_IN_MONTH_0 = "0D";
    public static final String DAY_OF_THE_YEAR = "DD";
    public static final String DAY_OF_THE_YEAR_0 = "0DD";
    public static final String DAY_NAME_ABBREVIATION = "DDD";
    public static final String DAY_NAME = "DDDD";
    public static final String HOUR = "HH";
    public static final String HOUR_0 = "0HH";
    public static final String MINUTE = "mm";
    public static final String MINUTE_0 = "0mm";
    public static final String SECOND = "ss";
    public static final String SECOND_0 = "0ss";

    @Override
    public String get(String str) {
        return null;
    }
}
