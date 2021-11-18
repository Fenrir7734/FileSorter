package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.ProviderDescription;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.util.Calendar;

public class DateProvider implements Provider<ChronoLocalDate> {
    private ProviderDescription description;

    public DateProvider(ProviderDescription description) {
        this.description = description;
    }

    public LocalDate get(FileData fileData) {
        Calendar calendar = fileData.creationTime();
        return LocalDate.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        String datePattern = description.pattern();
        Calendar calendar = fileData.creationTime();
        return new SimpleDateFormat(datePattern).format(calendar.getTime());
    }
}
