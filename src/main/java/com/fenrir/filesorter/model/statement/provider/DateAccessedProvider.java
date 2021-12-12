package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;
import java.time.chrono.ChronoLocalDate;

public class DateAccessedProvider implements Provider<ChronoLocalDate> {
    private final ProviderDescription description;

    public DateAccessedProvider(ProviderDescription description) {
        this.description = description;
    }

    @Override
    public ChronoLocalDate get(FileData fileData) throws IOException {
        return null;
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        return null;
    }
}
