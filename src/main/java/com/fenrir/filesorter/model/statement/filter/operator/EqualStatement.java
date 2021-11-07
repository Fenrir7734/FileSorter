package com.fenrir.filesorter.model.statement.filter.operator;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.filter.FilterStatementDescription;
import com.fenrir.filesorter.model.statement.filter.operand.FilterOperandStatement;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class EqualStatement<T extends Comparable<T>> implements FilterOperatorStatement<T> {
    private final FilterOperandStatement<T> operandStatement;
    private final List<T> args;

    public EqualStatement(FilterStatementDescription<T> description) {
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
                    for (T arg: args) {
                        if (operand.compareTo(arg) == 0) {
                            return true;
                        }
                    }
                    return false;
                } catch (IOException e) {
                    return false;
                }
            }
        };
    }
}
