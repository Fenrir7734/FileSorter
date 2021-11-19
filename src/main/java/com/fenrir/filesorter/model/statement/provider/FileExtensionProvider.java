package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class FileExtensionProvider implements Provider<String> {

    public FileExtensionProvider(ProviderDescription description) { }

    @Override
    public String get(FileData fileData) throws IOException {
        String extension = fileData.getFileExtension();
        return extension != null ? extension : "";
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        return get(fileData);
    }
}
