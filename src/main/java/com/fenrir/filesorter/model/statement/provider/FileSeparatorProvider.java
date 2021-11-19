package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.File;
import java.io.IOException;

public class FileSeparatorProvider implements Provider<String> {

    public FileSeparatorProvider(ProviderDescription description) { }

    @Override
    public String get(FileData fileData) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        return File.separator;
    }
}
