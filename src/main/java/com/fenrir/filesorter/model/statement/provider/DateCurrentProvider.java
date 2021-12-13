package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;

public class DateCurrentProvider implements Provider<ChronoLocalDate> {
    private final ProviderDescription description;

    public DateCurrentProvider(ProviderDescription description) {
        this.description = description;
    }

    @Override
    public ChronoLocalDate get(FileData fileData) throws IOException {
        throw new UnsupportedOperationException();
    }

    String getAsString(Date date) {
        String datePattern = description.pattern();
        return new SimpleDateFormat(datePattern).format(date);
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        return getAsString(new Date());
    }
}
