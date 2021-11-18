package com.fenrir.filesorter.model.statement.provider;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.StatementDescription;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.util.Calendar;

public class DateOperandStatement implements FilterOperandStatement<ChronoLocalDate> {
    private StatementDescription<String> description;

    public DateOperandStatement(StatementDescription<String> description) {
        this.description = description;
    }

    public LocalDate get(FileData fileData) {
        Calendar calendar = fileData.creationTime();
        return LocalDate.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public String getAsString(FileData fileData) throws IOException {
        String datePattern = description.getPattern();
        Calendar calendar = fileData.creationTime();
        return new SimpleDateFormat(datePattern).format(calendar.getTime());
    }
}
