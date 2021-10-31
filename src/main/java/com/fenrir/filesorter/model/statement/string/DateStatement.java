package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.StatementDescription;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateStatement implements StringStatement {
    private final FileData fileData;
    private final StatementDescription description;

    public DateStatement(FileData fileData, StatementDescription description) {
        this.fileData = fileData;
        this.description = description;
    }

    @Override
    public String execute() throws IOException {
        return getDate();
    }

    private String getDate() {
        String datePattern = description.getDatePattern();
        Calendar calendar = fileData.creationTime();
        return new SimpleDateFormat(datePattern).format(calendar.getTime());
    }
}
