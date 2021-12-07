package com.fenrir.filesorter.controllers.editor;

public interface EditorController {
    String getExpression();
    void lockTab();
    void unlockTab();
    void confirm();
    void close();
}
