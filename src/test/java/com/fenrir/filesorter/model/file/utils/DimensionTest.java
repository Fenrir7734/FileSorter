package com.fenrir.filesorter.model.file.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DimensionTest {

    @Test
    public void createDimensionInstanceFromInt() {
        Dimension dimension = Dimension.of(320, 480);
        assertEquals(320, dimension.getWidth());
        assertEquals(480, dimension.getHeight());
    }

    @Test
    public void createDimensionInstanceFromValidString() {
        Dimension dimension = Dimension.of("320x480");
        assertEquals(320, dimension.getWidth());
        assertEquals(480, dimension.getHeight());
    }

    @Test
    public void createDimensionInstanceFromStringWithInvalidSeparator() {
        assertThrows(IllegalArgumentException.class, () -> Dimension.of("320 480"));
    }

    @Test
    public void createDimensionInstanceFromStringContainingIllegalCharacters() {
        assertThrows(NumberFormatException.class, () -> Dimension.of("3a0x480"));
    }

    @Test
    public void createDimensionInstanceFromStringWithoutOneDimension() {
        assertThrows(IllegalArgumentException.class, () -> Dimension.of("320x"));
    }
}