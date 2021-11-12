package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.file.FileStructureMapper;
import com.fenrir.filesorter.model.parsers.RuleGroupParser;
import com.fenrir.filesorter.model.rule.RuleGroup;
import com.fenrir.filesorter.model.statement.StatementGroup;
import com.fenrir.filesorter.model.statement.filter.operator.FilterOperatorStatement;
import com.fenrir.filesorter.model.statement.string.StringStatement;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
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

    private List<FileData> filter(List<FilterOperatorStatement<?>> filterStatements) {
        List<Predicate<FileData>> predicates = filterStatements.stream()
                .map(FilterOperatorStatement::execute)
                .collect(Collectors.toList());
        List<FileData> filteredFileStructure = fileStructure.stream()
                .filter(f -> !f.isIncluded())
                .collect(Collectors.toList());

        for (Predicate<FileData> predicate: predicates) {
            filteredFileStructure = filteredFileStructure.stream()
                    .filter(predicate)
                    .filter(f -> !f.isDirectory())
                    .collect(Collectors.toList());
        }
        filteredFileStructure.forEach(f -> f.setIncluded(true));
        return filteredFileStructure;
    }

    private void createTargetPaths(
            List<FileData> files,
            List<StringStatement> sortStatements,
            List<StringStatement> renameStatements)
            throws IOException {

        for (FileData file: files) {
            Path path = buildPathForFile(file, sortStatements);
            Path name = buildFileName(file, renameStatements);
            Path targetPath = path.resolve(name);
            long duplicatesCount = countDuplicate(targetPath);
            file.setTargetPath(targetPath, duplicatesCount);
        }
    }

    private Path buildPathForFile(FileData file, List<StringStatement> sortStatements) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (StringStatement statement: sortStatements) {
            String s = statement.execute(file);
            builder.append(s);
        }
        Path path = Path.of(builder.toString());
        Path targetPath = targetRootDir.resolve(path);
        return targetPath;
    }

    private Path buildFileName(FileData file, List<StringStatement> renameStatements) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (StringStatement statement: renameStatements) {
            String s = statement.execute(file);
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
