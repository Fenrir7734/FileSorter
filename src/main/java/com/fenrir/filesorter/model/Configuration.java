package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.rule.RuleGroup;
import javafx.util.Pair;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Configuration {
    private Path targetPath;
    private final Set<Path> sourcePaths = new HashSet<>();
    private final Map<String, RuleGroup> namedRuleGroup = new HashMap<>();

    public void setTargetPath(Path targetPath) {
        this.targetPath = targetPath;
    }

    public List<Path> addSourcePaths(List<Path> paths) {
        return paths.stream()
                .filter(sourcePaths::add)
                .collect(Collectors.toList());
    }

    public List<Path> removeSourcePaths(List<Path> paths) {
        return paths.stream()
                .filter(sourcePaths::remove)
                .collect(Collectors.toList());
    }

    public void addNamedRuleGroup(String name, RuleGroup group) {
        namedRuleGroup.put(name, group);
    }

    public void removeRuleGroup(String name) {

    }

    public Path getTargetPath() {
        return targetPath;
    }

    public Set<Path> getSourcePaths() {
        return sourcePaths;
    }

    public Map<String, RuleGroup> getNamedRuleGroups() {
        return namedRuleGroup;
    }

    public List<RuleGroup> getRuleGroups() {
        return new ArrayList<>(namedRuleGroup.values());
    }
}
