package com.fenrir.filesorter.model.file.utils;

import com.fenrir.filesorter.model.Sorter;
import com.fenrir.filesorter.model.file.FilePath;

import java.nio.file.Path;
import java.util.Deque;
import java.util.List;

public record Backup(Sorter.Action action, Deque<Path> dirTargetPaths, List<FilePath> filePaths) {

}
