package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.tokens.RenameTokenType;
import com.fenrir.filesorter.model.tokens.SortTokenType;

public class StringStatementFactory {
    public static StringStatement get(FileData fileData, StringStatementDescription description, RenameTokenType type) {
        return switch (type) {
            case CURRENT_FILE_NAME -> new FileNameStatement(fileData, description);
            case FILE_EXTENSION -> new FileExtensionStatement(fileData, description);
        };
    }

    public static StringStatement get(FileData fileData, StringStatementDescription description, SortTokenType type) {
        return switch (type) {
            case SEPARATOR -> new FileSeparatorStatement(fileData, description);
            case FILE_EXTENSION -> new FileExtensionStatement(fileData, description);
            case IMAGE_RESOLUTION -> new ImageResolutionStatement(fileData, description);
            case FILE_CATEGORY -> new FileCategoryStatement(fileData, description);
        };
    }
}
