package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.ProviderDescription;

import java.io.IOException;

public class FileSizeProvider implements Provider<Long> {

    public FileSizeProvider(ProviderDescription description) { }

    @Override
    public Long get(FileData fileData) throws IOException {
        return fileData.getFileSize();
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        throw new UnsupportedOperationException();
    }
}
