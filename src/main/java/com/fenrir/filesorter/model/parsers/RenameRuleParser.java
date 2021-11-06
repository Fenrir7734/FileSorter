package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.rules.RenameRule;
import com.fenrir.filesorter.model.rules.Rule;
import com.fenrir.filesorter.model.rules.SortRule;
import com.fenrir.filesorter.model.statement.string.*;
import com.fenrir.filesorter.model.tokens.DateTokenType;
import com.fenrir.filesorter.model.tokens.RenameTokenType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RenameRuleParser {

    public List<StringStatement> resolveRule(RenameRule rule) throws IOException {
        List<StringStatement> statements = new ArrayList<>();

        Rule.RuleElement element;
        while ((element = rule.next()) != null) {
            StringStatement statement = parseElement(element);
            statements.add(statement);
        }

        return statements;
    }

    private StringStatement parseElement(Rule.RuleElement element) throws IllegalArgumentException {
        if (!element.isToken()) {
            StringStatementDescription description = new StringStatementDescription(null, element.element());
            return new LiteralStatement(description);
        }

        String token = element.element();
        DateTokenType dateTokenType = DateTokenType.get(token);

        if (dateTokenType != null) {
            StringStatementDescription description = new StringStatementDescription(dateTokenType.getPattern(), null);
            return new DateStatement(description);
        }

        RenameTokenType renameTokenType = RenameTokenType.get(token);

        if (renameTokenType == null) {
            throw new IllegalArgumentException();
        }

        return StringStatementFactory.get(null, renameTokenType);
    }
}
