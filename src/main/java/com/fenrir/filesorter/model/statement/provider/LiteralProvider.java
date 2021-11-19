package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;

public class LiteralProvider implements Provider<String> {
    private final ProviderDescription description;

    public LiteralProvider(ProviderDescription description) {
        this.description = description;
    }

    @Override
    public String get(FileData fileData) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        return description.literal();
    }
}
