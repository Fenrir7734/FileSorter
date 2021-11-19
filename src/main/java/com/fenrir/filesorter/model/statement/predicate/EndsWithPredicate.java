package com.fenrir.filesorter.model.statement.predicate;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.filter.FilterStatementDescription;
import com.fenrir.filesorter.model.statement.provider.Provider;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class EndsWithStatement implements FilterOperatorStatement<String> {
    private final Provider<String> operandStatement;
    private final List<String> args;

    public EndsWithStatement(FilterStatementDescription<String> description) {
        this.operandStatement = description.operand();
        this.args = description.args();
    }
    @Override
    public Predicate<FileData> execute() {
        return new Predicate<FileData>() {
            @Override
            public boolean test(FileData fileData) {
                try {
                    String operand = operandStatement.get(fileData);
                    for (String arg: args) {
                        if (operand.endsWith(arg)) {
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
