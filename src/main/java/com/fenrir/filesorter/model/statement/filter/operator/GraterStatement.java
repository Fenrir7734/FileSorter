package com.fenrir.filesorter.model.statement.filter.operator;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.filter.FilterStatementDescription;
import com.fenrir.filesorter.model.statement.filter.operand.FilterOperandStatement;

import java.io.IOException;
import java.util.function.Predicate;

public class GraterStatement<T extends Comparable<T>> implements FilterOperatorStatement<T> {
    private final FilterOperandStatement<T> operandStatement;
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
                    T operand = operandStatement.execute(fileData);
                    return operand.compareTo(arg) > 0;
                } catch (IOException e) {
                    return false;
                }
            }
        };
    }
}
