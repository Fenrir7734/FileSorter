package com.fenrir.filesorter.model;

public class Sorter {
    /*
    private final SortRule sortRule;
    private final RenameRule renameRule;
    private final List<FilterRule> filterRules;
    private final List<FileData> filesToSort;
    private final Path targetDir;
    private final List<? extends FilterOperatorStatement<?>> filterStatements = new ArrayList<>();

    public Sorter(Path target, List<FileData> filesToSort, List<FilterRule> filterRules, SortRule sortRule, RenameRule renameRule) {
        this.targetDir = target;
        this.filesToSort = filesToSort;
        this.filterRules = filterRules;
        this.sortRule = sortRule;
        this.renameRule = renameRule;
    }
    */
    /*
    public void sort() throws IOException {
        for (FileData file: filesToSort) {
            if (!file.isDirectory()) {
                createTargetPath(file);
            }
        }
        copyFromSourceToTarget();
    }

    private void createTargetPath(FileData file) throws IOException {
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

    private void copyFromSourceToTarget() throws IOException {
        for (FileData file: filesToSort) {
            if (!file.isDirectory()) {
                copyFile(file);
            }
        }
    }

    private void copyFile(FileData file) throws IOException {
        Path sourcePath = file.getSourcePath();
        Path targetPath = file.resolveTargetPath();
        Path dirPath = targetPath.getParent();

        if (Files.notExists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Files.copy(sourcePath, targetPath, COPY_ATTRIBUTES, NOFOLLOW_LINKS);
    }

     */
}
