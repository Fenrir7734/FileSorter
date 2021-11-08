package com.fenrir.filesorter.utils;

import com.fenrir.filesorter.model.file.utils.Dimension;

import java.nio.file.Path;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Converter {

    public static List<String> toList(String[] args) {
        return Arrays.stream(args).collect(Collectors.toList());
    }

    public static List<ChronoLocalDate> convertToDate(List<String> args) {
        return args.stream().map(LocalDate::parse).collect(Collectors.toList());
    }

    public static List<Path> convertToPaths(List<String> args) {
        return args.stream().map(Path::of).collect(Collectors.toList());
    }

    public static List<Dimension> convertToDimension(List<String> args) {
        return args.stream().map(Dimension::of).collect(Collectors.toList());
    }

    public static List<Long> convertToBytes(List<String> args) throws IllegalArgumentException {
        List<Long> sizes = new ArrayList<>();

        for (String arg : args) {
            char c = getPostfix(arg);
            long l = Long.parseLong(arg.substring(0, arg.length() - 2));
            long sizeInBytes = getSize(l, c);
            sizes.add(sizeInBytes);
        }
        return sizes;
    }

    private static long getSize(long size, char postfix) {
        switch (postfix) {
            case 'P': size *= 1000;
            case 'T': size *= 1000;
            case 'G': size *= 1000;
            case 'M': size *= 1000;
            case 'k': size *= 1000;
        }
        return size;
    }

    private static char getPostfix(String arg) throws IllegalArgumentException {
        arg = arg.trim();
        char c = arg.charAt(arg.length() - 1);
        if (!checkPostfix(c)) {
            throw new IllegalArgumentException();
        }
        return c;
    }

    private static boolean checkPostfix(char c) {
        return checkIfPostfix(c) || checkIfNumber(c);
    }

    private static boolean checkIfPostfix(char c) {
        CharacterIterator validPostfix = new StringCharacterIterator("kMGTP");
        while (validPostfix.next() != CharacterIterator.DONE) {
            if (validPostfix.current() == c) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkIfNumber(char c) {
        return c >= '0' && c <= '9';
    }
}
