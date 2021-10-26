package com.fenrir.filesorter;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.rules.RenameRule;
import com.fenrir.filesorter.rules.SortRule;
import com.fenrir.filesorter.rules.parsers.RenameRuleParser;
import com.fenrir.filesorter.rules.parsers.SortRuleParser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Sorter {
    private SortRule sortRule;
    private RenameRule renameRule;
    private List<FileData> filesToSort;
    private Path targetDir;

    public Sorter(Path target, List<FileData> filesToSort, SortRule sortRule, RenameRule renameRule) {
        this.targetDir = target;
        this.filesToSort = filesToSort;
        this.sortRule = sortRule;
        this.renameRule = renameRule;
    }

    public void sort() throws IOException {
        for (FileData file: filesToSort) {
            if (!file.isDirectory()) {
                createTargetPath(file);
            }
        }
        copyFromSourceToTarget();
    }

    private void createTargetPath(FileData file) {
        Path dirPath = new SortRuleParser(sortRule, file).resolveRule();
        String fileName = new RenameRuleParser(renameRule, file).resolveRule();
        Path targetPath = targetDir.resolve(dirPath).resolve(fileName);
        long count = countDuplicate(targetPath);
        file.setTargetPath(targetPath, count);
    }

    private long countDuplicate(Path targetPath) {
        return filesToSort.stream()
                .filter(file -> !file.isDirectory())
                .map(FileData::getTargetPath)
                .filter(Objects::nonNull)
                .map(Path::toString)
                .filter(s -> s.equals(targetPath.toString()))
                .count();
    }

    private void copyFromSourceToTarget() {

    }
}
