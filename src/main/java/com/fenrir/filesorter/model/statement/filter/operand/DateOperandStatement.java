package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.util.Calendar;

public class DateOperandStatement implements FilterOperandStatement<ChronoLocalDate> {
    public LocalDate execute(FileData fileData) {
        Calendar calendar = fileData.creationTime();
        return LocalDate.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    }
}
