package com.fenrir.filesorter.statement.string;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.file.FileUtils;
import com.fenrir.filesorter.statement.StatementDescription;
import com.fenrir.filesorter.statement.utils.Category;
import com.fenrir.filesorter.statement.utils.FilesCategory;

import java.io.IOException;

public class FileCategoryStatement implements StringStatement {
    private final FileData fileData;

    public FileCategoryStatement(FileData fileData, StatementDescription description) {
        this.fileData = fileData;
    }

    @Override
    public String execute(FileData file) throws IOException {
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
