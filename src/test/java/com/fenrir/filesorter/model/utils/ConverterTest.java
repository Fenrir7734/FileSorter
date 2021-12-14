package com.fenrir.filesorter.model.utils;

import com.fenrir.filesorter.model.exceptions.ArgumentFormatException;
import com.fenrir.filesorter.model.file.utils.Dimension;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConverterTest {

    @Test
    public void convertToPositiveIntegerShouldReturnListOfIntegersForValidInput()
            throws ArgumentFormatException {
        List<String> list = List.of("123", "1920", String.valueOf(Integer.MAX_VALUE));
        List<Integer> actualResult = Converter.convertToPositiveInteger(list);
        List<Integer> expectedResult = List.of(123, 1920, Integer.MAX_VALUE);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void convertToPositiveIntegerShouldReturnListOfIntegersForNumberWithSpaceAtTheBeginning()
            throws ArgumentFormatException {
        List<String> list = List.of("123 ", " 1920", "456");
        List<Integer> actualResult = Converter.convertToPositiveInteger(list);
        List<Integer> expectedResult = List.of(123, 1920, 456);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void convertToPositiveIntegerShouldReturnListOfIntegersForNumberWithSpaceAtTheEnd()
            throws ArgumentFormatException {
        List<String> list = List.of("123 ", "1920 ", "456");
        List<Integer> actualResult = Converter.convertToPositiveInteger(list);
        List<Integer> expectedResult = List.of(123, 1920, 456);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void convertToPositiveIntegerShouldReturnEmptyListForEmptyInputList() throws ArgumentFormatException {
        List<String> list = new ArrayList<>();
        List<Integer> result = Converter.convertToPositiveInteger(list);
        assertTrue(result.isEmpty());
    }

    @Test
    public void convertToPositiveIntegerShouldThrowArgumentFormatExceptionForNegativeNumber() throws ArgumentFormatException {
        List<String> list = List.of("123", "-456", "789");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> Converter.convertToPositiveInteger(list),
                "Incorrect number format."
        );
        assertEquals("-456", exception.getArg());
    }

    @Test
    public void convertToPositiveIntegerShouldThrowArgumentFormatExceptionForNumberWithPlusSignAtTheBeginning() {
        List<String> list = List.of("123", "+456", "789");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> Converter.convertToPositiveInteger(list),
                "Incorrect number format."
        );
        assertEquals("+456", exception.getArg());
    }

    @Test
    public void convertToPositiveIntegerShouldThrowArgumentFormatExceptionForEmptyString() {
        List<String> list = List.of("123", "456", "");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> Converter.convertToPositiveInteger(list),
                "Incorrect number format."
        );
        assertEquals("", exception.getArg());
    }

    @Test
    public void convertToPositiveIntegerShouldThrowArgumentFormatExceptionForBlankString() {
        List<String> list = List.of("123", "   ", "456");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> Converter.convertToPositiveInteger(list),
                "Incorrect number format."
        );
        assertEquals("   ", exception.getArg());
    }

    @Test
    public void convertToPositiveIntegerShouldThrowArgumentFormatExceptionForStringWithSpace() {
        List<String> list = List.of("1 23", "456", "789");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> Converter.convertToPositiveInteger(list),
                "Incorrect number format."
        );
        assertEquals("1 23", exception.getArg());
    }

    @Test
    public void convertToPositiveIntegerShouldTrowArgumentFormatExceptionForNonNumericCharacter() {
        List<String> list = List.of("123a", "456", "789");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> Converter.convertToPositiveInteger(list),
                "Incorrect number format."
        );
        assertEquals("123a", exception.getArg());
    }

    @Test
    public void convertToPositiveIntegerShouldThrowArgumentFormatExceptionForFloatingPointNumber() {
        List<String> list = List.of("123", "45.6", "567");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> Converter.convertToPositiveInteger(list),
                "Incorrect number format."
        );
        assertEquals("45.6", exception.getArg());
    }

    @Test
    public void convertToPositiveIntegerShouldThrowArgumentFormatExceptionForNumberGreaterThanIntegerMaxValue() {
        String greaterThanIntegerMax = String.valueOf(Integer.MAX_VALUE) + "99";
        List<String> list = List.of("123", greaterThanIntegerMax, "456");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> Converter.convertToPositiveInteger(list),
                "Incorrect number format."
        );
        assertEquals(greaterThanIntegerMax, exception.getArg());
    }

    @Test
    public void convertToDateShouldReturnListOfChronoLocalDateForValidInput() throws ArgumentFormatException {
        List<String> list = List.of("2019-02-09", "2020-04-15", "2021-05-25");
        List<ChronoLocalDate> actualResult = Converter.convertToDate(list);
        List<ChronoLocalDate> expectedResult = List.of(
                LocalDate.of(2019, 2, 9),
                LocalDate.of(2020, 4, 15),
                LocalDate.of(2021, 5, 25)
        );
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void convertToDateShouldReturnEmptyListForEmptyInputList() throws ArgumentFormatException {
        List<String> list = new ArrayList<>();
        List<ChronoLocalDate> result = Converter.convertToDate(list);
        assertTrue(result.isEmpty());
    }

    @Test
    public void convertToDateShouldThrowExceptionForInvalidInput() {
        List<String> list = List.of("2019-02-09", "2020:04:15", "2021-05-25");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> Converter.convertToDate(list),
                "Incorrect date format."
        );
        String expectedArgument = "2020:04:15";
        assertEquals(expectedArgument, exception.getArg());
    }

    @Test
    public void convertToPathsShouldReturnListOfPathForValidInput() {
        List<String> list = List.of("/", "/home", "/etc");
        List<Path> actualResult = Converter.convertToPaths(list);
        List<Path> expectedResult = List.of(
                Path.of("/"),
                Path.of("/home"),
                Path.of("/etc")
        );
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void convertToPathShouldReturnEmptyListForEmptyInputList() {
        List<String> list = new ArrayList<>();
        List<Path> result = Converter.convertToPaths(list);
        assertTrue(result.isEmpty());
    }

    @Test
    public void convertToDimensionShouldReturnListOfDimensionForValidInput() throws ArgumentFormatException {
        List<String> list = List.of("320x480", "1280x720", "1920x1080");
        List<Dimension> actualResult = Converter.convertToDimension(list);
        List<Dimension> expectedResult = List.of(
                Dimension.of(320, 480),
                Dimension.of(1280, 720),
                Dimension.of(1920, 1080)
        );
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void convertToDimensionShouldReturnEmptyListForEmptyInputList() throws ArgumentFormatException {
        List<String> list = new ArrayList<>();
        List<Dimension> result = Converter.convertToDimension(list);
        assertTrue(result.isEmpty());
    }

    @Test
    public void convertToDimensionShouldThrowExceptionForInvalidInput() {
        List<String> list = List.of("320x480", "x720", "1920x1080");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> Converter.convertToDimension(list),
                "Incorrect dimension format."
        );
        String expectedArgument = "x720";
        String expectedToken = "DIM";
        assertEquals(expectedArgument, exception.getArg());
        assertEquals(expectedToken, exception.getToken());
    }

    @Test
    public void convertToBytesShouldReturnListOfLongForValidInput() throws ArgumentFormatException {
        List<String> list = List.of("30", "5M", "4G");
        List<Long> actualResult = Converter.convertToBytes(list);
        List<Long> expectedResult = List.of(
                30L,
                5_000_000L,
                4_000_000_000L
        );
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void convertToBytesShouldReturnEmptyListForEmptyInputList() throws ArgumentFormatException {
        List<String> list = new ArrayList<>();
        List<Long> result = Converter.convertToBytes(list);
        assertTrue(result.isEmpty());
    }

    @Test
    public void convertToBytesShouldThrowExceptionForInvalidInputWithInvalidPostfix() {
        List<String> list = List.of("30", "5K", "4G");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> Converter.convertToBytes(list),
                "Incorrect size"
        );
        String expectedArgument = "5K";
        String expectedToken = "FIS";
        assertEquals(expectedArgument, exception.getArg());
        assertEquals(expectedToken, exception.getToken());
    }
    @Test
    public void convertToBytesShouldThrowExceptionForInvalidInputWithInvalidString() {
        List<String> list = List.of("30", "8P", "abc");
        ArgumentFormatException exception = assertThrows(
                ArgumentFormatException.class,
                () -> Converter.convertToBytes(list),
                "Incorrect size"
        );
        String expectedArgument = "abc";
        String expectedToken = "FIS";
        assertEquals(expectedArgument, exception.getArg());
        assertEquals(expectedToken, exception.getToken());
    }
}