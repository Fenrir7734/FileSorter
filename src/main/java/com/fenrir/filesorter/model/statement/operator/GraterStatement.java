package com.fenrir.filesorter.model.statement.operator;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.filter.FilterStatementDescription;
import com.fenrir.filesorter.model.statement.provider.Provider;

import java.io.IOException;
import java.util.function.Predicate;

public class GraterStatement<T extends Comparable<T>> implements FilterOperatorStatement<T> {
    private final Provider<T> operandStatement;
    private final T arg;

    public GraterStatement(FilterStatementDescription<T> description) {
        this.operandStatement = description.operand();
        this.arg = description.args().get(0);
    }

    @Override
    public Predicate<FileData> execute() {
        return new Predicate<FileData>() {
            @Override
            public boolean test(FileData fileData) {
                try {
                    T operand = operandStatement.get(fileData);
                    return operand.compareTo(arg) > 0;
                } catch (IOException e) {
                    return false;
                }
            }
        };
    }
}
