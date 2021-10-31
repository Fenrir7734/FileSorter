package com.fenrir.filesorter;

import com.fenrir.filesorter.model.Sorter;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.file.FileStructureMapper;
import com.fenrir.filesorter.model.rules.type.RenameRule;
import com.fenrir.filesorter.model.rules.type.SortRule;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

    }

    private static void test() throws IOException {
        String sortRule = "%(YYYY)-%(MM)%(/)%(0D)";
        String renameRule = "%(0D)-%(MM)-%(YYYY)";
        String source = "/home/fenrir/Documents/Test_environment/screenshot";
        String target = "/home/fenrir/Documents/Test_environment/target/";

        List<FileData> fileDataList = new FileStructureMapper(source).map();
        SortRule sortRule1 = new SortRule(sortRule);
        RenameRule renameRule1 = new RenameRule(renameRule);
        Path targetPath = Path.of(target);

        new Sorter(targetPath, fileDataList, sortRule1, renameRule1).sort();
    }
}
