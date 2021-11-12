package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.rule.Iterator;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.rule.RuleElement;
import com.fenrir.filesorter.model.rule.StringRule;
import com.fenrir.filesorter.model.statement.string.*;
import com.fenrir.filesorter.model.tokens.DateTokenType;
import com.fenrir.filesorter.model.tokens.RenameTokenType;

import java.util.ArrayList;
import java.util.List;

public class RenameRuleParser {

    public List<StringStatement> resolveRule(StringRule rule) throws TokenFormatException {
        try {
            List<StringStatement> statements = new ArrayList<>();

            Iterator<RuleElement> iterator = rule.getRuleElementsIterator();
            while (iterator.hasNext()) {
                RuleElement element = iterator.next();
                StringStatement statement = parseElement(element);
                statements.add(statement);
            }

            return statements;
        } catch (TokenFormatException e) {
            throw new TokenFormatException(e.getMessage(), e, rule, e.getToken());
        }
    }

    private StringStatement parseElement(RuleElement element) throws TokenFormatException {
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
            throw new TokenFormatException("Unknown token", token);
        }

        return StringStatementFactory.get(null, renameTokenType);
    }
}
