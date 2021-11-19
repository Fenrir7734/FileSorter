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
    private Path sourceRootDir;
    private Path targetRootDir;
    private List<RuleGroup> ruleGroups;
    private List<StatementGroup> statementGroups;
    private List<FileData> fileStructure;

    public Processor(Path sourceRootDir, Path targetRootDir, List<RuleGroup> ruleGroup) throws ExpressionFormatException, IOException {
        this.ruleParser = new RuleGroupParser();
        this.sourceRootDir = sourceRootDir;
        this.targetRootDir = targetRootDir;
        this.ruleGroups = ruleGroup;
        parseRuleGroups();
        mapFileStructure();
    }

    private void parseRuleGroups() throws ExpressionFormatException {
        statementGroups = new ArrayList<>();
        for (RuleGroup ruleGroup: ruleGroups) {
            StatementGroup statementGroup = ruleParser.parse(ruleGroup);
            statementGroups.add(statementGroup);
        }
    }

    private void mapFileStructure() throws IOException {
        FileStructureMapper mapper = new FileStructureMapper(sourceRootDir);
        fileStructure = mapper.map();
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
        for (Predicate predicate: filterStatements) {
            filteredFileStructure = filteredFileStructure.stream()
                    .filter(f -> {
                        try {
                            return predicate.test(f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return false;
                    })
                    .filter(f -> !f.isDirectory())
                    .collect(Collectors.toList());
        }
        filteredFileStructure.forEach(f -> f.setIncluded(true));
        return filteredFileStructure;
    }

    private void createTargetPaths(
            List<FileData> files,
            List<Provider<?>> sortStatements,
            List<Provider<?>> renameStatements)
            throws IOException {

        for (FileData file: files) {
            Path path = buildPathForFile(file, sortStatements);
            Path name = buildFileName(file, renameStatements);
            Path targetPath = path.resolve(name);
            long duplicatesCount = countDuplicate(targetPath);
            file.setTargetPath(targetPath, duplicatesCount);
        }
    }

    private Path buildPathForFile(FileData file, List<Provider<?>> sortStatements) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Provider<?> sortStatement : sortStatements) {
            String s = sortStatement.getAsString(file);
            builder.append(s);
        }
        Path path = Path.of(builder.toString());
        return targetRootDir.resolve(path);
    }

    private Path buildFileName(FileData file, List<Provider<?>> renameStatements) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Provider<?>  statement: renameStatements) {
            String s = statement.getAsString(file);
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
