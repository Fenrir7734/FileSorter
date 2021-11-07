package com.fenrir.filesorter.model.statement.filter.operator;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.filter.FilterStatementDescription;
import com.fenrir.filesorter.model.statement.filter.operand.FilterOperandStatement;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class SmallerEqualStatement<T extends Comparable<T>> implements FilterOperatorStatement<T> {
    private final FilterOperandStatement<T> operandStatement;
    private final List<T> args;

    public SmallerEqualStatement(FilterStatementDescription<T> description) {
        this.operandStatement = description.getOperand();
        this.args = description.getArgs();
    }

    @Override
    public Predicate<FileData> execute() {
        return new Predicate<FileData>() {
            @Override
            public boolean test(FileData fileData) {
                try {
                    T operand = operandStatement.execute(fileData);
                    return operand.compareTo(args.get(0)) <= 0;
                } catch (IOException e) {
                    return false;
                }
            }
        };
    }
}
