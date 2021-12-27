package com.fenrir.filesorter.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateTokenTypeTest {
    @Test
    public void getTypeShouldReturnDatePatternType() {
        DatePatternType patternType = DatePatternType.getType("Y");
        assertEquals(DatePatternType.YEAR, patternType);
    }

    @Test
    public void getNameShouldReturnDatePatternName() {
        DatePatternType patternType = DatePatternType.YEAR;
        assertEquals("Year", patternType.getName());
    }

    @Test
    public void getExampleShouldReturnDatePatternExample() {
        DatePatternType patternType = DatePatternType.YEAR;
        assertEquals("1994", patternType.getExample());
    }

    @Test
    public void getTokenShouldReturnDatePatternToken() {
        DatePatternType patternType = DatePatternType.YEAR;
        assertEquals("Y", patternType.getToken());
    }

    @Test
    public void geTypeShouldReturnNullForInvalidToken() {
        DatePatternType patternType = DatePatternType.getType("Invalid Token");
        assertNull(patternType);
    }

    @Test
    public void getPatternShouldReturnPattern() {
        String pattern = DatePatternType.YEAR.getPattern();
        assertEquals("yyyy", pattern);
    }
}