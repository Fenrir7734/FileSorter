package com.fenrir.filesorter.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

public class Sorter {
    private String dirRule;
    private String fileRule;

    private Path source;
    private Path target;
    private List<Path> fileStructure;
    private FilePathContainer filePathContainer;

    public Sorter(String dirRule, String fileRule, String source, String target) throws Exception {
        //resolvePattern(dirRule);
        this.dirRule = dirRule;
        this.fileRule = fileRule;
        this.source = Path.of(source);
        this.target = Path.of(target);
        this.filePathContainer = new FilePathContainer();
        walkFileStructure();
        sort();
    }

    private void walkFileStructure() {
        try (Stream<Path> walk = Files.walk(source)) {
            fileStructure = walk.collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void sort() throws IOException {
        for (Path file: fileStructure) {
            if (!Files.isDirectory(file)) {
                Path dirPath = resolveDirRule(dirRule, file);
                String fileName = resolveFileRule(fileRule, file);
                if (Files.notExists(dirPath)) {
                    Files.createDirectories(dirPath);
                }
                Path filePath = dirPath.resolve(fileName);
                filePathContainer.addPaths(file, filePath);
            }
        }

        for (FilePathContainer.PathTuple tuple: filePathContainer.getPaths()) {
            Files.copy(tuple.getOldPath(), tuple.getResolvedPath(), COPY_ATTRIBUTES, NOFOLLOW_LINKS);
        }
    }

    private Path resolveDirRule(String dirRule, Path file) throws IOException {
        Pattern pattern = Pattern.compile("%\\((.*?)\\)");
        Matcher matcher = pattern.matcher(dirRule);
        while (matcher.find()) {
            dirRule = dirRule.replace(matcher.group(), parseFlag(matcher.group(1), file));
        }
        return target.resolve(Path.of(dirRule));
    }

    private String resolveFileRule(String fileRule, Path file) throws IOException {
        Pattern pattern = Pattern.compile("%\\((.*?)\\)");
        Matcher matcher = pattern.matcher(fileRule);
        while (matcher.find()) {
            fileRule = fileRule.replace(matcher.group(), parseFlag(matcher.group(1), file));
        }
        return fileRule;
    }

    private String parseFlag(String flag, Path path) throws IOException {
        if(flag.equals("/")) {
            return File.separator;
        }

        BasicFileAttributes attr;
        try {
            attr = Files.readAttributes(path, PosixFileAttributes.class);
        } catch (IOException e) {
            System.err.println(e);
            throw e;
        }
        return switch (flag) {
            case "YYYY" -> String.valueOf(fileTimeToDate(attr.creationTime()).get(Calendar.YEAR));
            case "MM" -> String.valueOf(fileTimeToDate(attr.creationTime()).get(Calendar.MONTH) + 1);
            case "DD" -> String.valueOf(fileTimeToDate(attr.creationTime()).get(Calendar.DAY_OF_MONTH));
            default -> null;
        };
    }

    private Calendar fileTimeToDate(FileTime time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time.toMillis());
        return c;
    }

        /*
    private List<String> resolvePattern(String rule) throws Exception {
        for (int i = 0; i < rule.length(); i++) {
            if (rule.charAt(i) == '%') {
                String flag = extractFlag(rule, i);
                i += flag.length() + 2;
                System.out.println("flag: " + flag);
            } else {
                String literal = extractLiteral(rule, i);
                i += literal.length() - 1;
                System.out.println("literał: " + literal);
            }
        }
        return null;
    }

    private String extractFlag(String rule, int start) throws Exception {
        if(rule.length() > start + 1) {
            start++;
            if(rule.charAt(start) == '(') {
                start++;
                int end = start;
                while ((rule.charAt(end) != ')' && rule.charAt(end) != '%')) {
                    end++;
                }
                return rule.substring(start, end);
            }
        }
        throw new Exception("Bleee");
    }

    private String extractLiteral(String rule, int start) {
        int end = start;
        while (rule.length() > end && rule.charAt(end) != '%') {
            end++;
        }
        return rule.substring(start, end);
    }
     */
}
