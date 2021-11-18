package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.ProviderDescription;

import java.io.IOException;

public class FileCategoryProvider implements Provider<String> {

    public FileCategoryProvider(ProviderDescription description) { }

    @Override
    public String get(FileData fileData) throws IOException {
        return fileData.getFileCategory().getName();
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        return get(fileData);
    }
}
