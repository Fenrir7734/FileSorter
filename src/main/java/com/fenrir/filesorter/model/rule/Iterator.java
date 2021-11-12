package com.fenrir.filesorter.model.rule;

public interface Iterator<T> {
    boolean hasNext();
    T next();
    void reset();
}
