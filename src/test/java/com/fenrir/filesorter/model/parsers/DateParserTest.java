package com.fenrir.filesorter.model.parsers;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class DateParserTest {
    @Test
    public void resolveDatePatternShouldReturnPatternTransformedToValidPatternUsedByDateTimeFormatterClass() {
        String datePattern = "%Y-%M-%D %H:%m:%s";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("yyyy'-'MM'-'dd' 'HH':'mm':'ss", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForGivenPatternWithLiteralAtTheBeginning() {
        String datePattern = "abc%Y-%M-%D %H:%m:%s";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("'abc'yyyy'-'MM'-'dd' 'HH':'mm':'ss", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForGivenPatternWithLiteralAtTheEnd() {
        String datePattern = "%Y-%M-%D %H:%m:%sabc";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("yyyy'-'MM'-'dd' 'HH':'mm':'ss'abc'", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForGivenPatternWithoutTokens() {
        String datePattern = "abc";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("'abc'", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForGivenPatternWithOnlyTokens() {
        String datePattern = "%Y%M%D%H%m%s";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("yyyyMMddHHmmss", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForEmptyString() {
        String datePattern = "";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForBlankString() {
        String datePattern = "     ";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("'     '", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForEmptyEscapeCharacter() {
        String datePattern = "%";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("'%'", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForEmptyEscapeCharacterAtTheEnd() {
        String datePattern = "%Y-%M-%D %H:%m:%s%";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("yyyy'-'MM'-'dd' 'HH':'mm':'ss'%'", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForEscapedTokenIndicatorPlacedBeforeToken() {
        String datePattern = "%Y-%M-%D %%%H:%m:%s";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("yyyy'-'MM'-'dd' %'HH':'mm':'ss", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForEscapedTokenIndicatorPlacedAfterToken() {
        String datePattern = "%Y-%M-%D %H%%:%m:%s";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("yyyy'-'MM'-'dd' 'HH'%:'mm':'ss", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForInvalidToken() {
        String datePattern = "%p";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("'%p'", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForGivenPatternContainingSingleQuote() {
        String datePattern = "'%Y-%M-%D %H:%m:%s'";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("''yyyy'-'MM'-'dd' 'HH':'mm':'ss''", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForGivenPatternContainingTwoSubsequentSingleQuotes() {
        String datePattern = "'%Y-%M-%D %H'':%m:%s'";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("''yyyy'-'MM'-'dd' 'HH''''':'mm':'ss''", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForGivenPatternContainingSubsequentSameTokens() {
        String datePattern = "%Y%Y%Y";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("[yyyy][yyyy]yyyy", actualPattern);
    }

    @Test
    public void resolveDatePatternShouldReturnValidDateTimeFormatterPatternForGivenPatternContainingSubsequentTokensResolvingIntoSameLetters() {
        String datePattern = "%Q%q%R";
        DateParser parser = new DateParser();
        String actualPattern = parser.resolveDatePattern(datePattern);
        assertEquals("[qqq][q]qqqq", actualPattern);
    }
}