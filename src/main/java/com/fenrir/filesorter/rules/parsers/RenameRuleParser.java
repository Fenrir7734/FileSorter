package com.fenrir.filesorter.rules.parsers;

import com.fenrir.filesorter.file.FileData;
import com.fenrir.filesorter.rules.RenameRule;
import com.fenrir.filesorter.rules.Rule;
import com.fenrir.filesorter.statement.StatementDescription;
import com.fenrir.filesorter.statement.string.DateStatement;
import com.fenrir.filesorter.statement.string.StringStatement;
import com.fenrir.filesorter.statement.string.StringStatementFactory;
import com.fenrir.filesorter.tokens.DateTokenType;
import com.fenrir.filesorter.tokens.RenameTokenType;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RenameRuleParser {
    private Rule rule;
    private FileData fileData;

    public RenameRuleParser(RenameRule rule, FileData fileData) {
        this.rule = rule;
        this.fileData = fileData;
    }

    public String resolveRule() throws IOException {
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
        return resolvedRule.toString();
    }

    private StringStatement parseToken(String token) throws IllegalArgumentException {
        DateTokenType dateTokenType = DateTokenType.get(token);

        if (dateTokenType != null) {
            StatementDescription description = new StatementDescription(dateTokenType.getPattern());
            return new DateStatement(fileData, description);
        }

        RenameTokenType renameTokenType = RenameTokenType.get(token);

        if (renameTokenType == null) {
            throw new IllegalArgumentException();
        }

        return StringStatementFactory.get(fileData, null, renameTokenType);
    }
}
