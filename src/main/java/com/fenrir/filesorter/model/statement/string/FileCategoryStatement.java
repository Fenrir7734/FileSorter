package com.fenrir.filesorter.model.statement.string;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.file.FileUtils;
import com.fenrir.filesorter.model.statement.StatementDescription;
import com.fenrir.filesorter.model.statement.string.utils.Category;
import com.fenrir.filesorter.model.statement.string.utils.FilesCategory;

import java.io.IOException;

public class FileCategoryStatement implements StringStatement {
    private final FileData fileData;

    public FileCategoryStatement(FileData fileData, StatementDescription description) {
        this.fileData = fileData;
    }

    @Override
    public String execute() throws IOException {
        return getFileCategory();
    }

    private String getFileCategory() throws IOException {
        if (!FileUtils.hasExtension(fileData.getSourcePath())) {
            return Category.OTHERS.getName();
        }
        String extension = fileData.getFileExtension();
        FilesCategory filesCategory = FilesCategory.getInstance();
        Category category = filesCategory.matchCategory(extension);
        return category != null ? category.getName() : Category.OTHERS.getName();
    }
}
