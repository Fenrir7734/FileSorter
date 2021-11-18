package com.fenrir.filesorter.model.statement.operator;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.filter.FilterStatementDescription;
import com.fenrir.filesorter.model.statement.provider.Provider;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class NotEqualStatement<T extends Comparable<T>> implements FilterOperatorStatement<T> {
    private final Provider<T> operandStatement;
    private final List<T> args;

    public NotEqualStatement(FilterStatementDescription<T> description) {
        this.operandStatement = description.operand();
        this.args = description.args();
    }

    @Override
    public Predicate<FileData> execute() {
        return new Predicate<FileData>() {
            @Override
            public boolean test(FileData fileData) {
                try {
                    T operand = operandStatement.get(fileData);
                    for (T arg: args) {
                        if (operand.compareTo(arg) == 0) {
                            return false;
                        }
                    }
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
        };
    }
}
