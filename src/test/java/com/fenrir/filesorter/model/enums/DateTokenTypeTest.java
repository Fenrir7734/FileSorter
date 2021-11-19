package com.fenrir.filesorter.model.enums;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DateTokenTypeTest {
    Map<String, DateTokenType> tokensToTest = Map.ofEntries(
            Map.entry("YYYY", DateTokenType.YEAR),
            Map.entry("YY", DateTokenType.YEAR_TWO_DIGIT),
            Map.entry("M", DateTokenType.MONTH_IN_YEAR),
            Map.entry("0M", DateTokenType.MONTH_IN_YEAR_0),
            Map.entry("MM", DateTokenType.MONTH_NAME_ABBREVIATION),
            Map.entry("MMM", DateTokenType.MONTH_NAME),
            Map.entry("W", DateTokenType.WEEK_IN_MONTH),
            Map.entry("0W", DateTokenType.WEEK_IN_MONTH_0),
            Map.entry("WW", DateTokenType.WEEK_IN_YEAR),
            Map.entry("0WW", DateTokenType.WEEK_IN_YEAR_0),
            Map.entry("D", DateTokenType.DAY_IN_MONTH),
            Map.entry("0D", DateTokenType.DAY_IN_MONTH_0),
            Map.entry("DD", DateTokenType.DAY_IN_YEAR),
            Map.entry("0DD", DateTokenType.DAY_IN_YEAR_0),
            Map.entry("DDD", DateTokenType.DAY_NAME_ABBREVIATION),
            Map.entry("DDDD", DateTokenType.DAY_NAME),
            Map.entry("HH", DateTokenType.HOUR),
            Map.entry("0HH", DateTokenType.HOUR_0),
            Map.entry("mm", DateTokenType.MINUTE),
            Map.entry("0mm", DateTokenType.MINUTE_0),
            Map.entry("ss", DateTokenType.SECOND),
            Map.entry("0ss", DateTokenType.SECOND_0)
    );
    Map<DateTokenType, String> patternsToTest = Map.ofEntries(
            Map.entry(DateTokenType.YEAR, "YYYY"),
            Map.entry(DateTokenType.YEAR_TWO_DIGIT, "YY"),
            Map.entry(DateTokenType.MONTH_IN_YEAR, "M"),
            Map.entry(DateTokenType.MONTH_IN_YEAR_0, "MM"),
            Map.entry(DateTokenType.MONTH_NAME_ABBREVIATION, "MMM"),
            Map.entry(DateTokenType.MONTH_NAME, "MMMM"),
            Map.entry(DateTokenType.WEEK_IN_MONTH, "W"),
            Map.entry(DateTokenType.WEEK_IN_MONTH_0, "WW"),
            Map.entry(DateTokenType.WEEK_IN_YEAR, "w"),
            Map.entry(DateTokenType.WEEK_IN_YEAR_0, "ww"),
            Map.entry(DateTokenType.DAY_IN_MONTH, "d"),
            Map.entry(DateTokenType.DAY_IN_MONTH_0, "dd"),
            Map.entry(DateTokenType.DAY_IN_YEAR, "D"),
            Map.entry(DateTokenType.DAY_IN_YEAR_0, "DDD"),
            Map.entry(DateTokenType.DAY_NAME_ABBREVIATION, "E"),
            Map.entry(DateTokenType.DAY_NAME, "EEEE"),
            Map.entry(DateTokenType.HOUR, "H"),
            Map.entry(DateTokenType.HOUR_0, "HH"),
            Map.entry(DateTokenType.MINUTE, "m"),
            Map.entry(DateTokenType.MINUTE_0, "mm"),
            Map.entry(DateTokenType.SECOND, "s"),
            Map.entry(DateTokenType.SECOND_0, "ss")
    );

    @Test
    public void testGetType() {
        for (String token: tokensToTest.keySet()) {
            DateTokenType actualType = DateTokenType.getType(token);
            DateTokenType expectedType = tokensToTest.get(token);
            assertEquals(expectedType, actualType);
        }
    }

    @Test
    public void testGetPattern() {
        for (DateTokenType type: patternsToTest.keySet()) {
            String actualPattern = type.getPattern();;
            String expectedPattern = patternsToTest.get(type);
            assertEquals(expectedPattern, actualPattern);
        }
    }
}