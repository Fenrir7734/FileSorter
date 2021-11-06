package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.tokens.RenameTokenType;
import com.fenrir.filesorter.model.tokens.SortTokenType;

public class StringStatementFactory {
    public static StringStatement get(StringStatementDescription description, RenameTokenType type) {
        return switch (type) {
            case CURRENT_FILE_NAME -> new FileNameStatement(description);
            case FILE_EXTENSION -> new FileExtensionStatement(description);
        };
    }

    public static StringStatement get(StringStatementDescription description, SortTokenType type) {
        return switch (type) {
            case SEPARATOR -> new FileSeparatorStatement(description);
            case FILE_EXTENSION -> new FileExtensionStatement(description);
            case IMAGE_RESOLUTION -> new ImageResolutionStatement(description);
            case FILE_CATEGORY -> new FileCategoryStatement(description);
        };
    }
}
