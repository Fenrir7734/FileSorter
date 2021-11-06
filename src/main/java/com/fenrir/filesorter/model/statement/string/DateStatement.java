package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateStatement implements StringStatement {
    private final StringStatementDescription description;

    public DateStatement(StringStatementDescription description) {
        this.description = description;
    }

    @Override
    public String execute(FileData fileData) throws IOException {
        return getDate(fileData);
    }

    private String getDate(FileData fileData) {
        String datePattern = description.getDatePattern();
        Calendar calendar = fileData.creationTime();
        return new SimpleDateFormat(datePattern).format(calendar.getTime());
    }
}
