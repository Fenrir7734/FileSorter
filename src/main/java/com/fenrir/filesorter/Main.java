package com.fenrir.filesorter;

import com.fenrir.filesorter.model.Processor;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.file.FileStructureMapper;
import com.fenrir.filesorter.model.rule.*;
import com.fenrir.filesorter.model.statement.StatementGroup;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        test();
    }

    private static void test2() {
        Object o1 = new String("string");
        Object o2 = new String("string");
        System.out.println(o1.equals(o2));
    }

    private static void test() throws Exception {
        String sortRule = "%(YYYY)-%(MM)%(/)%(0D)";
        String renameRule = "%(0D)-%(MM)-%(YYYY)";
        String source = "/home/fenrir/Documents/Test_environment/screenshot";
        String target = "/home/fenrir/Documents/Test_environment/target/";

        List<FileData> fileDataList = new FileStructureMapper(Path.of(source)).map();
        StringRule sortRule1 = new StringRule(sortRule);
        StringRule renameRule1 = new StringRule(renameRule);
        Path targetPath = Path.of(target);

        FilterRule filterRule1 = new FilterRule("%(DAT)%(>:2021-09-04)");
        //FilterRule filterRule2 = new FilterRule("%(DAT)%(>=:2022-02-20)");
        List<FilterRule> filterRules = new ArrayList<>();
        filterRules.add(filterRule1);
        //filterRules.add(filterRule2);
        RuleGroup group = new RuleGroup(renameRule1, sortRule1, filterRules);
        List<RuleGroup> ruleGroups = new ArrayList<>();
        ruleGroups.add(group);
        Processor processor = new Processor(Path.of(source), Path.of(target), ruleGroups);
        processor.process();
        processor.getFileStructure().stream().map(FileData::resolveTargetPath).forEach(System.out::println);
        //new Sorter(targetPath, fileDataList, filterRules, sortRule1, renameRule1);
    }

    private static void test3() throws IOException {
        //FilterOperandStatement operandStatement = FilterOperandStatementFactory.get(FilterOperandTokenType.DATE);
        List<ChronoLocalDate> args = new ArrayList<>();
        args.add(LocalDate.of(1922, 2, 2));
        args.add(LocalDate.now());
        //FilterStatementDescription<ChronoLocalDate> description = new FilterStatementDescription<ChronoLocalDate>(operandStatement, args);
        //FilterStatement statement = FilterStatementFactory.get(description, FilterOperatorTokenType.EQUAL);
        //System.out.println(statement.execute(LocalDate.of(1999, 11, 2)).test(LocalDate.of(1922, 2, 2)));
    }

    private static void test4() {
        StatementGroup group = new StatementGroup();
    }
}
