package com.fenrir.filesorter.model.enums;

public enum DatePatternType {
    YEAR("Y", "yyyy", "Year", "1994"),
    YEAR_SHORT("y", "yy", "Year", "00-99"),
    QUARTER_OF_YEAR("Q", "qqq", "Quarter of year", "Q1-Q4"),
    QUARTER_OF_YEAR_SHORT("q", "q", "Quarter of Year", "1-3"),
    QUARTER_OF_YEAR_NAME("R", "qqqq", "Quarter of Year", "3rd quarter"),
    MONTH("M", "MM", "Month", "1-12"),
    MONTH_NAME("N", "MMMM", "Month", "July"),
    MONT_NAME_SHORT("n", "MMM", "Month", "Jul"),
    WEEK_OF_MONTH("W", "W", "Week of month", "01-05"),
    DAY_OF_YEAR("E", "DDD", "Day of year", "001-366"),
    DAY_OF_MONTH("D", "dd", "Day of month", "01-31"),
    DAY_OF_WEEK("F", "ee", "Day of week", "01-07"),
    DAY_OF_WEEK_NAME("G", "EEEE", "Day of week", "Friday"),
    DAY_OF_WEEK_NAME_SHORT("g", "EEE", "Day od week", "Fri"),
    HOUR_OF_DAY("H", "HH", "Hour", "00-23"),
    HOUR_OF_DAY_AM_PM("I", "hha", "Hour", "0-12AM/PM"),
    MINUTE_OF_HOUR("m", "mm", "Minute", "00-59"),
    SECOND_OF_MINUTE("s", "ss", "Second", "00-59");

    private final String token;
    private final String pattern;
    private final String name;
    private final String example;

    DatePatternType(String token, String pattern, String name, String example) {
        this.token = token;
        this.pattern = pattern;
        this.name = name;
        this.example = example;
    }

    public String getToken() {
        return token;
    }

    public String getPattern() {
        return pattern;
    }

    public String getName() {
        return name;
    }

    public String getExample() {
        return example;
    }

    public static DatePatternType getType(String token) {
        DatePatternType[] types = DatePatternType.values();
        for (DatePatternType type: types) {
            if (token.equals(type.getToken())) {
                return type;
            }
        }
        return null;
    }
}
