package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.exceptions.SortConfigurationException;
import com.fenrir.filesorter.model.rule.RuleGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Configuration {
    private Path targetRootDir;
    private final ObservableList<Path> sourcePaths = FXCollections.observableArrayList();
    private final ObservableList<Pair<String, RuleGroup>> namedRuleGroup = FXCollections.observableArrayList();

    public void setTargetRootDir(Path targetRootDir) {
        this.targetRootDir = targetRootDir;
    }

    public void addSourcePaths(List<Path> paths) {
        paths.stream()
                .filter(p -> !sourcePaths.contains(p))
                .forEach(sourcePaths::add);
    }

    public void removeSourcePaths(List<Path> paths) {
        sourcePaths.removeAll(paths);
    }

    public void addNamedRuleGroup(String name, RuleGroup group) {
        if (!getRuleGroupsNames().contains(name)) {
            namedRuleGroup.add(new Pair<>(name, group));
        }
    }

    public void removeRuleGroup(Pair<String, RuleGroup> value) {
        namedRuleGroup.remove(value);
    }

    public void validate() throws SortConfigurationException, IOException {
        if (targetRootDir == null) {
            throw new SortConfigurationException("Target directory has not been specified.");
        }
        if (!isTargetDirectoryEmpty()) {
            throw new SortConfigurationException("Target directory should be empty.");
        }
        if (sourcePaths.isEmpty()) {
            throw new SortConfigurationException("Not a single source file or directory has been specified.");
        }
        if (!isAtLeastOneRuleSpecified()) {
            throw new SortConfigurationException("Not a single rule has been specified.");
        }
    }

    private boolean isTargetDirectoryEmpty() throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(targetRootDir)) {
            return !directoryStream.iterator().hasNext();
        }
    }

    private boolean isAtLeastOneRuleSpecified() {
        if (namedRuleGroup.isEmpty()) {
            return false;
        }
        for (RuleGroup ruleGroup: getRuleGroups()) {
            if (isRuleGroupNotEmpty(ruleGroup)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRuleGroupNotEmpty(RuleGroup ruleGroup) {
        return ruleGroup.getRenameRule() != null
                || ruleGroup.getSortRule() != null
                || (ruleGroup.getFilterRules() != null && !ruleGroup.getFilterRules().isEmpty());
    }

    public Path getTargetRootDir() {
        return targetRootDir;
    }

    public ObservableList<Path> getSourcePaths() {
        return sourcePaths;
    }

    public ObservableList<Pair<String, RuleGroup>> getNamedRuleGroups() {
        return namedRuleGroup;
    }

    public List<RuleGroup> getRuleGroups() {
        return namedRuleGroup.stream()
                .map(Pair::getValue)
                .collect(Collectors.toList());
    }

    public List<String> getRuleGroupsNames() {
        return namedRuleGroup.stream()
                .map(Pair::getKey)
                .collect(Collectors.toList());
    }

    public RuleGroup getRuleGroup(String name) {
        return namedRuleGroup.stream()
                .filter(v -> v.getKey().equals(name))
                .map(Pair::getValue)
                .findFirst()
                .get();
    }
}
