package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.exceptions.SortConfigurationException;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.rule.RuleGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Configuration {
    private Path targetRootDir;
    private final ObservableList<Path> sourcePaths = FXCollections.observableArrayList();
    private final ObservableList<Pair<String, RuleGroup>> namedRuleGroup = FXCollections.observableArrayList();
    private Sorter.Action sortAction;

    public void setTargetRootDir(Path targetRootDir) {
        this.targetRootDir = targetRootDir;
    }

    public void addSourcePaths(List<Path> paths) {
        paths.stream()
                .map(FileData::normalizeFilePath)
                .filter(Objects::nonNull)
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
        if (!FileUtils.isEmptyDirectory(targetRootDir.toFile())) {
            throw new SortConfigurationException("Target directory should be empty.");
        }
        if (sourcePaths.isEmpty()) {
            throw new SortConfigurationException("Not a single source file or directory has been specified.");
        }
        if (!isAtLeastOneRuleSpecified()) {
            throw new SortConfigurationException("Not a single rule has been specified.");
        }
        if (sortAction == null) {
            throw new SortConfigurationException("Sort action has not been specified");
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

    public Sorter.Action getSortAction() {
        return sortAction;
    }

    public void setSortAction(Sorter.Action sortAction) {
        this.sortAction = sortAction;
    }
}
