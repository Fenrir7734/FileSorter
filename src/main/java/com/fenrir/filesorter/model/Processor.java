package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.file.FileStructureMapper;
import com.fenrir.filesorter.model.parsers.RuleGroupParser;
import com.fenrir.filesorter.model.rule.RuleGroup;
import com.fenrir.filesorter.model.statement.StatementGroup;
import com.fenrir.filesorter.model.statement.predicate.Predicate;
import com.fenrir.filesorter.model.statement.provider.Provider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Processor {
    private final RuleGroupParser ruleParser;
    private final Configuration configuration;
    private List<StatementGroup> statementGroups;
    private List<FileData> fileStructure;

    public Processor(Configuration configuration) throws ExpressionFormatException, IOException {
        this.ruleParser = new RuleGroupParser();
        this.configuration = configuration;
        this.fileStructure = new ArrayList<>();
        parseRuleGroups();
        mapFileStructure();
    }

    private void parseRuleGroups() throws ExpressionFormatException {
        List<RuleGroup> ruleGroups = configuration.getRuleGroups();
        statementGroups = new ArrayList<>();
        for (RuleGroup ruleGroup: ruleGroups) {
            StatementGroup statementGroup = ruleParser.parse(ruleGroup);
            statementGroups.add(statementGroup);
        }
    }

    private void mapFileStructure() throws IOException {
        List<Path> sourceRootDir = configuration.getSourcePaths();
        for (Path path: sourceRootDir) {
            FileStructureMapper mapper = new FileStructureMapper(path);
            fileStructure.addAll(mapper.map());
        }
    }

    public void process() throws IOException {
        fileStructure.forEach(f -> f.setIncluded(false));
        processStatementGroups();
    }

    private void processStatementGroups() throws IOException {
        for (StatementGroup group: statementGroups) {
            List<FileData> filteredFileStructure = filter(group.getFilterStatements());
            createTargetPaths(filteredFileStructure, group.getSortStatement(), group.getRenameStatement());
        }
    }

    private List<FileData> filter(List<Predicate<? extends Comparable<?>>> filterStatements) {
        List<FileData> filteredFileStructure = fileStructure.stream()
                .filter(f -> !f.isIncluded())
                .collect(Collectors.toList());
        for (Predicate<?> predicate: filterStatements) {
            filteredFileStructure = filteredFileStructure.stream()
                    .filter(f -> test(predicate, f))
                    .filter(f -> !f.isDirectory())
                    .collect(Collectors.toList());
        }
        filteredFileStructure.forEach(f -> f.setIncluded(true));
        return filteredFileStructure;
    }

    private boolean test(Predicate<?> predicate, FileData file) {
        try {
            return predicate.test(file);
        } catch (IOException e) {
            // TODO: I will add logger here.
        }
        return false;
    }

    private void createTargetPaths(
            List<FileData> files,
            List<Provider<?>> sortStatement,
            List<Provider<?>> renameStatement)
            throws IOException {

        for (FileData file: files) {
            Path path = buildPathForFile(file, sortStatement);
            Path name = buildFileName(file, renameStatement);
            Path targetPath = path.resolve(name);
            long duplicatesCount = countDuplicate(targetPath);
            file.setTargetPath(targetPath, duplicatesCount);
        }
    }

    private Path buildPathForFile(FileData file, List<Provider<?>> sortStatement) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Provider<?> provider : sortStatement) {
            String s = provider.getAsString(file);
            builder.append(s);
        }
        Path path = Path.of(builder.toString());
        return configuration.getTargetRootDir().resolve(path);
    }

    private Path buildFileName(FileData file, List<Provider<?>> renameStatement) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Provider<?> provider: renameStatement) {
            String s = provider.getAsString(file);
            builder.append(s);
        }
        return Path.of(builder.toString());
    }

    private long countDuplicate(Path targetPath) {
        long count = 0;
        for (FileData file: fileStructure) {
            if (targetPath.equals(file.getTargetPath())) {
                count++;
            }
        }
        return count;
    }

    public List<FileData> getFileStructure() {
        return fileStructure;
    }
}
