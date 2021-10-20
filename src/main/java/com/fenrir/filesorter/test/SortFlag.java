package com.fenrir.filesorter.test.flags;

public class SortFlag implements Flag {
    public static final String FILE_EXTENSION = "EXT";
    public static final String FILE_CATEGORY =  "CAT";
    public static final String IMAGE_RESOLUTION = "RES";
    public static final String CURRENT_FILE_NAME = "CUR";

    @Override
    public String get(String str) {
        return switch (str) {
            case FILE_EXTENSION -> FILE_EXTENSION;
            case FILE_CATEGORY -> FILE_CATEGORY;
            case IMAGE_RESOLUTION -> IMAGE_RESOLUTION;
            case CURRENT_FILE_NAME -> CURRENT_FILE_NAME;
            default -> null;
        };
    }
}
