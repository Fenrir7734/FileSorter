package com.fenrir.filesorter.rules.parsers;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.rules.Rule;
import com.fenrir.filesorter.rules.SortRule;
import com.fenrir.filesorter.statement.StatementDescription;
import com.fenrir.filesorter.statement.string.DateStatement;
import com.fenrir.filesorter.statement.string.StringStatement;
import com.fenrir.filesorter.statement.string.StringStatementFactory;
import com.fenrir.filesorter.tokens.DateTokenType;
import com.fenrir.filesorter.tokens.SortTokenType;

import java.io.IOException;
import java.nio.file.Path;

public class SortRuleParser {
    private Rule rule;
    private FileData fileData;

    public SortRuleParser(SortRule rule, FileData fileData) {
        this.rule = rule;
        this.fileData = fileData;
    }

    public Path resolveRule() throws IOException {
        StringBuilder resolvedRule = new StringBuilder();

        Rule.RuleElement element;
        while ((element = rule.next()) != null) {
            if (element.isToken()) {
                StringStatement statement = parseToken(element.element());
                resolvedRule.append(statement.execute());
            } else {
                resolvedRule.append(element.element());
            }
        }

        this.rule.resetIter();
        return Path.of(resolvedRule.toString());
    }

    private StringStatement parseToken(String token) throws IllegalArgumentException, IOException {
        DateTokenType dateTokenType = DateTokenType.get(token);

        if (dateTokenType != null) {
            StatementDescription description = new StatementDescription(dateTokenType.getPattern());
            return new DateStatement(fileData, description);
        }

        SortTokenType sortTokenType = SortTokenType.get(token);

        if (sortTokenType == null) {
            throw new IllegalArgumentException();
        }

        return StringStatementFactory.get(fileData, null, sortTokenType);
    }

}
