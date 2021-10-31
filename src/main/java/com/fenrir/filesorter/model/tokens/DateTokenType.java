package com.fenrir.filesorter.model.tokens;

public enum DateTokenType {
    YEAR("YYYY"), YEAR_TWO_DIGIT("YY"),

    MONTH_IN_YEAR("M"), MONTH_IN_YEAR_0("MM"), MONTH_NAME_ABBREVIATION("MMM"),
    MONTH_NAME("MMMM"),

    WEEK_IN_MONTH("W"), WEEK_IN_MONTH_0("WW"), WEEK_IN_YEAR("w"), WEEK_IN_YEAR_0("ww"),

    DAY_IN_MONTH("d"), DAY_IN_MONTH_0("dd"), DAY_IN_YEAR("D"), DAY_IN_YEAR_0("DDD"),
    DAY_NAME_ABBREVIATION("E"), DAY_NAME("EEEE"),

    HOUR("H"), HOUR_0("HH"), MINUTE("m"), MINUTE_0("mm"), SECOND("s"),
    SECOND_0("ss");

    private final String pattern;

    DateTokenType(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public static DateTokenType get(String pattern) {
        return switch (pattern) {
            case "YYYY" -> YEAR;
            case "YY" -> YEAR_TWO_DIGIT;
            case "M" -> MONTH_IN_YEAR;
            case "0M" -> MONTH_IN_YEAR_0;
            case "MM" -> MONTH_NAME_ABBREVIATION;
            case "MMM" -> MONTH_NAME;
            case "W" -> WEEK_IN_MONTH;
            case "0W" -> WEEK_IN_MONTH_0;
            case "WW" -> WEEK_IN_YEAR;
            case "0WW" -> WEEK_IN_YEAR_0;
            case "D" -> DAY_IN_MONTH;
            case "0D" -> DAY_IN_MONTH_0;
            case "DD" -> DAY_IN_YEAR;
            case "0DD" -> DAY_IN_YEAR_0;
            case "DDD" -> DAY_NAME_ABBREVIATION;
            case "DDDD" -> DAY_NAME;
            case "HH" -> HOUR;
            case "0HH" -> HOUR_0;
            case "mm" -> MINUTE;
            case "0mm" -> MINUTE_0;
            case "ss" -> SECOND;
            case "0ss" -> SECOND_0;
            default -> null;
        };
    }
}
