package com.fenrir.filesorter.model.statement.filter.operand;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.file.FileUtils;
import com.fenrir.filesorter.model.statement.string.utils.Category;
import com.fenrir.filesorter.model.statement.string.utils.FilesCategory;

import java.io.IOException;

public class FileCategoryOperandStatement implements FilterOperandStatement<String> {
    @Override
    public String execute(FileData fileData) throws IOException {
        return getFileCategory(fileData);
    }

    private String getFileCategory(FileData fileData) throws IOException {
        if (!FileUtils.hasExtension(fileData.getSourcePath())) {
            return Category.OTHERS.getName();
        }
        String extension = fileData.getFileExtension();
        FilesCategory filesCategory = FilesCategory.getInstance();
        Category category = filesCategory.matchCategory(extension);
        return category != null ? category.getName() : Category.OTHERS.getName();
    }
}
