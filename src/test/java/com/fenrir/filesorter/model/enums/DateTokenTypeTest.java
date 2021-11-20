package com.fenrir.filesorter.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateTokenTypeTest {
    @Test
    public void getTypeShouldReturnDatePatternType() {
        DatePatternType patternType = DatePatternType.getType("YYYY");
        assertEquals(DatePatternType.YEAR, patternType);
    }

    @Test
    public void geTypeShouldReturnNullForInvalidToken() {
        DatePatternType patternType = DatePatternType.getType("Invalid Token");
        assertNull(patternType);
    }

    @Test
    public void getPatternShouldReturnPattern() {
        String pattern = DatePatternType.YEAR.getPattern();
        assertEquals("YYYY", pattern);
    }
}