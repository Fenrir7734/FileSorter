package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.file.FilePath;
import com.fenrir.filesorter.model.file.FileStructureMapper;
import com.fenrir.filesorter.model.parsers.RuleGroupParser;
import com.fenrir.filesorter.model.rule.RuleGroup;
import com.fenrir.filesorter.model.statement.StatementGroup;
import com.fenrir.filesorter.model.statement.predicate.Predicate;
import com.fenrir.filesorter.model.statement.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Processor {
    private static final Logger logger = LoggerFactory.getLogger(Processor.class);

    private final Path targetPath;
    private final List<StatementGroup> statementGroups;
    private final List<FileData> fileToProcess;
    private final ArrayDeque<Path> directoriesPaths;
    private final List<FilePath> pathsOfProcessedFiles;
    private final Map<Path, Long> targetPathCount;

    public Processor(List<Path> sourcePaths, Path targetPath, List<RuleGroup> ruleGroups)
            throws ExpressionFormatException, IOException {
        this.targetPath = targetPath;
        this.statementGroups = new ArrayList<>();
        this.fileToProcess = new LinkedList<>();
        this.directoriesPaths = new ArrayDeque<>();
        this.targetPathCount = new HashMap<>();
        parseRuleGroup(ruleGroups);
        mapFileStructure(sourcePaths);
        this.pathsOfProcessedFiles = new ArrayList<>(this.fileToProcess.size());
        process();
    }

    private void parseRuleGroup(List<RuleGroup> ruleGroups) throws ExpressionFormatException {
        logger.info("Parsing provided rule groups...");
        RuleGroupParser parser = new RuleGroupParser();
        for (RuleGroup ruleGroup: ruleGroups) {
            StatementGroup statementGroup = parser.parse(ruleGroup);
            statementGroups.add(statementGroup);
        }
    }

    private void mapFileStructure(List<Path> sourceRootPaths) throws IOException {
        logger.info("Mapping file structure...");
        Set<Path> filePaths = new HashSet<>();

        for (Path rootPath: sourceRootPaths) {
            Deque<Path> mappedPaths = FileStructureMapper.map(rootPath);
            while (!mappedPaths.isEmpty()) {
                Path path = mappedPaths.pollFirst();
                if (path.toFile().isDirectory()) {
                    directoriesPaths.offerLast(path);
                } else {
                    filePaths.add(path);
                }
            }
        }

        for (Path path: filePaths) {
            FileData fileData = new FileData(path);
            fileToProcess.add(fileData);
        }
    }

    private void process() throws IOException {
        for (StatementGroup group: statementGroups) {
            List<FileData> includedFiles = filter(group.getFilterStatements());
            createTargetPaths(includedFiles, group.getSortStatement(), group.getRenameStatement());
        }
    }

    private List<FileData> filter(List<Predicate<? extends Comparable<?>>> filterStatements) {
        List<FileData> includedFiles = new LinkedList<>();
        for (Iterator<FileData> iter = fileToProcess.iterator(); iter.hasNext(); ) {
            FileData file = iter.next();
            boolean included = shouldBeIncluded(file, filterStatements);
            if (included) {
                includedFiles.add(file);
                iter.remove();
                file.setIncluded(true);
            }
        }
        return includedFiles;
    }

    private boolean shouldBeIncluded(FileData fileData, List<Predicate<? extends Comparable<?>>> filterStatements) {
        for (Predicate<?> predicate: filterStatements) {
            if (!test(predicate, fileData)) {
                return false;
            }
        }
        return true;
    }

    private boolean test(Predicate<?> predicate, FileData file) {
        try {
            return predicate.test(file);
        } catch (IOException e) {
            logger.error("File could not be accessed: {}", e.getMessage());
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
            file.resolveTargetPath();
            pathsOfProcessedFiles.add(file.getFilePath());
        }
    }

    private Path buildPathForFile(FileData file, List<Provider<?>> sortStatement) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Provider<?> provider : sortStatement) {
            String s = provider.getAsString(file);
            builder.append(s);
        }
        Path path = Path.of(builder.toString());
        return targetPath.resolve(path);
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
        targetPathCount.putIfAbsent(targetPath, -1L);
        return targetPathCount.compute(targetPath, (k, v) -> v + 1);
    }

    public Deque<Path> getDirectoriesPaths() {
        return directoriesPaths.clone();
    }

    public List<FilePath> getFilePaths() {
        return pathsOfProcessedFiles;
    }
}
