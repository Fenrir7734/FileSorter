package com.fenrir.filesorter.model.utils;

import com.fenrir.filesorter.model.exceptions.ArgumentFormatException;
import com.fenrir.filesorter.model.file.utils.Dimension;
import com.fenrir.filesorter.model.statement.types.ProviderType;
import org.apache.tika.utils.StringUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Converter {

    private Converter() {
        throw new UnsupportedOperationException();
    }

    public static List<Integer> convertToPositiveInteger(List<String> args) throws ArgumentFormatException {
        List<Integer> integers = new ArrayList<>();
        for (String arg: args) {
            if (isPositiveInteger(arg)) {
                integers.add(Integer.parseInt(arg.trim()));
            } else {
                throw new ArgumentFormatException("Incorrect number format.", arg);
            }
        }
        return integers;
    }

    private static boolean isPositiveInteger(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        if (!str.matches("\\d+")) {
            return false;
        }
        String max = String.valueOf(Integer.MAX_VALUE);
        BigInteger integerMaxValue = new BigInteger(max);
        BigInteger convertedNumber = new BigInteger(str);
        return convertedNumber.compareTo(integerMaxValue) <= 0;
    }

    public static List<ChronoLocalDate> convertToDate(List<String> args) throws ArgumentFormatException {
        List<ChronoLocalDate> dates = new ArrayList<>();
        for (String arg: args) {
            ChronoLocalDate date = convertToDate(arg);
            dates.add(date);
        }
        return dates;
    }

    private static ChronoLocalDate convertToDate(String arg) throws ArgumentFormatException {
        try {
            return LocalDate.parse(arg);
        } catch (DateTimeParseException e) {
            throw new ArgumentFormatException(
                    "Incorrect date format.",
                    e,
                    ProviderType.DATE.getToken(),
                    arg
            );
        }
    }

    public static List<Path> convertToPaths(List<String> args) {
        return args.stream().map(Path::of).collect(Collectors.toList());
    }

    public static List<Dimension> convertToDimension(List<String> args) throws ArgumentFormatException {
        List<Dimension> dimensions = new ArrayList<>();
        for (String arg: args) {
            Dimension dimension = convertToDimension(arg);
            dimensions.add(dimension);
        }
        return dimensions;
    }

    private static Dimension convertToDimension(String arg) throws ArgumentFormatException {
        try {
            return Dimension.of(arg);
        } catch (IllegalArgumentException e) {
            throw new ArgumentFormatException(
                    "Incorrect dimension format.",
                    e,
                    ProviderType.DIMENSION.getToken(),
                    arg
            );
        }
    }

    public static List<Long> convertToBytes(List<String> args) throws ArgumentFormatException {
        List<Long> sizes = new ArrayList<>();

        for (String arg : args) {
            char postfix = getPostfix(arg);
            arg = checkIfNumber(postfix) ? arg : arg.substring(0, arg.length() - 1);
            long sizeInBytes = getSize(Long.parseLong(arg), postfix);
            sizes.add(sizeInBytes);
        }
        return sizes;
    }

    private static long getSize(long size, char postfix) {
        switch (postfix) {
            case 'T': size *= 1000;
            case 'G': size *= 1000;
            case 'M': size *= 1000;
            case 'k': size *= 1000;
        }
        return size;
    }

    private static char getPostfix(String arg) throws ArgumentFormatException {
        arg = arg.trim();
        char c = arg.charAt(arg.length() - 1);
        if (!checkPostfix(c)) {
            throw new ArgumentFormatException(
                    "Incorrect size",
                    ProviderType.FILE_SIZE.getToken(),
                    arg
            );
        }
        return c;
    }

    private static boolean checkPostfix(char c) {
        return checkIfPostfix(c) || checkIfNumber(c);
    }

    private static boolean checkIfPostfix(char c) {
        CharacterIterator validPostfix = new StringCharacterIterator("kMGTP");
        do {
            if (validPostfix.current() == c) {
                return true;
            }
        } while (validPostfix.next() != CharacterIterator.DONE);
        return false;
    }

    private static boolean checkIfNumber(char c) {
        return c >= '0' && c <= '9';
    }
}
