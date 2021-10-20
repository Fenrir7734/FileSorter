package com.fenrir.filesorter.test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilePathContainer {

    private List<PathTuple> paths;

    public FilePathContainer() {
        this.paths = new ArrayList<>();
    }

    public void addPaths(Path oldPath, Path newPath) {
        long count =paths.stream()
                .map(PathTuple::getNewPath)
                .map(Path::toString)
                .filter(p -> p.equals(newPath.toString()))
                .count();
        System.out.println(oldPath + " -> " + newPath + " -> " + count);
        paths.add(new PathTuple(oldPath, newPath, count));
    }

    public List<PathTuple> getPaths() {
        return paths;
    }

    public record PathTuple(Path oldPath, Path newPath, long count) {
        public Path getResolvedPath() {
            return count > 0 ? Path.of(newPath + " (" + count + ")") : newPath;
        }

        public Path getNewPath() {
            return newPath;
        }

        public Path getOldPath() {
            return oldPath;
        }

        public long getCount() {
            return count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PathTuple pathTuple = (PathTuple) o;
            return Objects.equals(oldPath, pathTuple.oldPath) && Objects.equals(newPath, pathTuple.newPath);
        }

        @Override
        public int hashCode() {
            return Objects.hash(oldPath, newPath);
        }
    }
}
