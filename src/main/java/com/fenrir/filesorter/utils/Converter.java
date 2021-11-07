package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.statement.string.utils.Dimension;
import com.fenrir.filesorter.model.tokens.filter.FilterOperandTokenType;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Converter<T extends Comparable<T>> {

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

    public static List<Long> convertToBytes(List<String> args) {
        return null;
    }
}
