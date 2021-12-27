package com.fenrir.filesorter.model.enums;

public enum DatePatternType {
    YEAR("Y", "yyyy", "Year (1994)"),
    YEAR_SHORT("y", "yy", "Year (00-99)"),
    QUARTER_OF_YEAR("Q", "qqq", "Quarter of Year (Q1-Q4)"),
    QUARTER_OF_YEAR_SHORT("q", "q", "Quarter of year (1-3)"),
    QUARTER_OF_YEAR_NAME("R", "qqqq", "Quarter of year (3rd quarter)"),
    MONTH("M", "MM", "Month (1-12)"),
    MONTH_NAME("N", "mmmm", "Month (July)"),
    MONT_NAME_SHORT("n", "mmm", "Month (Jul)"),
    WEEK_OF_MONTH("W", "WW", "Week of month (01-05)"),
    DAY_OF_YEAR("E", "DDD", "Day (001-366)"),
    DAY_OF_MONTH("D", "dd", "Day (01-31)"),
    DAY_OF_WEEK("F", "ee", "Day (01-07)"),
    DAY_OF_WEEK_NAME("G", "EEEE", "Day (Friday)"),
    DAY_OF_WEEK_NAME_SHORT("g", "EEE", "Day (Fri)"),
    HOUR_OF_DAY("H", "HH", "Hour (00-23)"),
    HOUR_OF_DAY_AM_PM("I", "hha", "Hour (0-12AM/PM)"),
    MINUTE_OF_HOUR("m", "mm", "Minute (00-59)"),
    SECOND_OF_MINUTE("s", "ss", "Second (00-59)");

    private final String token;
    private final String pattern;
    private final String name;

    DatePatternType(String token, String pattern, String name) {
        this.token = token;
        this.pattern = pattern;
        this.name = name;
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
