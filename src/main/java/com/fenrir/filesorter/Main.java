package com.fenrir.filesorter;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.file.FileStructureMapper;
import com.fenrir.filesorter.rules.RenameRule;
import com.fenrir.filesorter.rules.Rule;
import com.fenrir.filesorter.rules.SortRule;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        /*
        String rule = "%(YYYY)-%(MM)%(/)%(DD)";
        Pattern pattern = Pattern.compile("%\\((.*?)\\)");
        Matcher matcher = pattern.matcher(rule);
        int lastMatchIndex = 0;
        while (matcher.find()) {
            String r;
            if(lastMatchIndex != matcher.start()) {
                r = rule.substring(lastMatchIndex, matcher.start());
                System.out.println(r);
            }
            lastMatchIndex = matcher.end();
            System.out.println(matcher.group(1));
        }

        Rule rule1 = new SortRule("%(0)%(1)%(2)%(3)%(4)");
        System.out.println(rule1.next());
        System.out.println(rule1.next());
         */

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
