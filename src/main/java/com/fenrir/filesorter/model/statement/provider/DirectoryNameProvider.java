package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class DirectoryNameProvider implements Provider<String>{
    @Override
    public String get(FileData fileData) throws IOException {
        return fileData.getSourceParentDirectoryName();
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        return get(fileData);
    }
}
