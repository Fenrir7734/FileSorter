package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.util.Calendar;

public class DateAccessedProvider implements Provider<ChronoLocalDate> {
    private final ProviderDescription description;

    public DateAccessedProvider(ProviderDescription description) {
        this.description = description;
    }

    @Override
    public ChronoLocalDate get(FileData fileData) throws IOException {
        Calendar calendar = fileData.lastAccessTime();
        return LocalDate.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        String datePattern = description.pattern();
        Calendar calendar = fileData.lastAccessTime();
        return new SimpleDateFormat(datePattern).format(calendar.getTime());
    }
}
