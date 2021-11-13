package com.fenrir.filesorter.model.statement.filter.operator;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.filter.FilterStatementDescription;
import com.fenrir.filesorter.model.statement.filter.operand.FilterOperandStatement;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class StartsWithStatement implements FilterOperatorStatement<String> {
    private final FilterOperandStatement<String> operandStatement;
    private final List<String> args;

    public StartsWithStatement(FilterStatementDescription<String> description) {
        this.operandStatement = description.operand();
        this.args = description.args();
    }
    @Override
    public Predicate<FileData> execute() {
        return new Predicate<FileData>() {
            @Override
            public boolean test(FileData fileData) {
                try {
                    String operand = operandStatement.execute(fileData);
                    for (String arg: args) {
                        if (operand.startsWith(arg)) {
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
