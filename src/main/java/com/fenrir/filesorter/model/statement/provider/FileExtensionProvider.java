package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class FileExtensionProvider implements Provider<String> {

    public FileExtensionProvider(ProviderDescription description) { }

    @Override
    public String get(FileData fileData) throws IOException {
        return fileData.getFileExtension();
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        String extension = get(fileData);
        return extension.isBlank() ? "Unknown" : extension;
    }
}
