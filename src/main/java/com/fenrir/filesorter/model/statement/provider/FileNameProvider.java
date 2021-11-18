package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.ProviderDescription;

import java.io.IOException;

public class FileNameProvider implements Provider<String> {

    public FileNameProvider(ProviderDescription description) { }

    @Override
    public String get(FileData fileData) {
        return fileData.getFileNameWithoutExtension();
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        return get(fileData);
    }
}
