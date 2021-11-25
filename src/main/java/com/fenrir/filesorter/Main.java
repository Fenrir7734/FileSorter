package com.fenrir.filesorter;

import com.fenrir.filesorter.model.Processor;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.rule.*;

import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        test();
    }

    private static void test() throws Exception {
        String sortExpression = "%(YYYY)-%(MM)%(/)%(0D)";
        String renameExpression = "%(0D)-%(MM)-%(YYYY)";
        String filterExpression = "%(DAT)%(>:2021-09-04)";
        String source = "/home/fenrir/Documents/Test_environment/screenshot";
        String target = "/home/fenrir/Documents/Test_environment/target/";

        StringRule sortRule = new StringRule(sortExpression);
        StringRule renameRule = new StringRule(renameExpression);
        FilterRule filterRule = new FilterRule(filterExpression);
        List<FilterRule> filterRules = List.of(filterRule);

        RuleGroup group = new RuleGroup(renameRule, sortRule, filterRules);
        List<RuleGroup> ruleGroups = List.of(group);
        Processor processor = new Processor(Path.of(source), Path.of(target), ruleGroups);
        processor.process();
        processor.getFileStructure().stream().map(FileData::resolveTargetPath).forEach(System.out::println);
    }
}
