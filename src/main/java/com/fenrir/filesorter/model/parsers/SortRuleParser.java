package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.rules.Rule;
import com.fenrir.filesorter.model.rules.SortRule;
import com.fenrir.filesorter.model.statement.string.*;
import com.fenrir.filesorter.model.tokens.DateTokenType;
import com.fenrir.filesorter.model.tokens.SortTokenType;

import java.util.ArrayList;
import java.util.List;

public class SortRuleParser {
    public List<StringStatement> resolveRule(SortRule rule) throws TokenFormatException {
        try {
            List<StringStatement> statements = new ArrayList<>();

            Rule.RuleElement element;
            while ((element = rule.next()) != null) {
                StringStatement statement = parseElement(element);
                statements.add(statement);
            }

            return statements;
        } catch (TokenFormatException e) {
            throw new TokenFormatException(e.getMessage(), e, rule, e.getToken());
        }
    }

    private StringStatement parseElement(Rule.RuleElement element) throws TokenFormatException {
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

        SortTokenType sortTokenType = SortTokenType.get(token);

        if (sortTokenType == null) {
            throw new TokenFormatException("Unknown token.", element.element());
        }

        return StringStatementFactory.get(null, sortTokenType);
    }

}
