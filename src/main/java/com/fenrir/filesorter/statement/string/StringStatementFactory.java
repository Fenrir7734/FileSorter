package com.fenrir.filesorter.statement.string;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.statement.StatementDescription;
import com.fenrir.filesorter.tokens.RenameTokenType;
import com.fenrir.filesorter.tokens.SortTokenType;

public class StringStatementFactory {
    public static StringStatement get(FileData fileData, StatementDescription description, RenameTokenType type) {
        return switch (type) {
            case CURRENT_FILE_NAME -> new FileNameStatement(fileData, description);
            case FILE_EXTENSION -> new FileExtensionStatement(fileData, description);
        };
    }

    public static StringStatement get(FileData fileData, StatementDescription description, SortTokenType type) {
        return switch (type) {
            case SEPARATOR -> new FileSeparatorStatement(fileData, description);
            case FILE_EXTENSION -> new FileExtensionStatement(fileData, description);
            case IMAGE_RESOLUTION -> new ImageResolutionStatement(fileData, description);
            case FILE_CATEGORY -> new FileCategoryStatement(fileData, description);
        };
    }
}
