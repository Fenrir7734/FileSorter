package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;

public class DateAccessedProvider implements Provider<ChronoLocalDate> {
    private DateTimeFormatter formatter;

    public DateAccessedProvider(ProviderDescription description) {
        if (description != null && description.pattern() != null) {
            formatter = DateTimeFormatter.ofPattern(description.pattern());
        }
    }

    @Override
    public ChronoLocalDate get(FileData fileData) throws IOException {
        return fileData.lastAccessTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        return fileData.lastAccessTime()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(formatter);
    }
}
