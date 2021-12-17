package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;

public class DateCurrentProvider implements Provider<ChronoLocalDate> {
    private DateTimeFormatter formatter;

    public DateCurrentProvider(ProviderDescription description) {
        if (description != null && description.pattern() != null) {
            formatter = DateTimeFormatter.ofPattern(description.pattern());
        }
    }

    @Override
    public ChronoLocalDate get(FileData fileData) throws IOException {
        throw new UnsupportedOperationException();
    }

    String getAsString(LocalDateTime date) {
        return date.format(formatter);
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        return getAsString(LocalDateTime.now());
    }
}
