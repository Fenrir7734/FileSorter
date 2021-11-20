package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.rule.Iterator;
import com.fenrir.filesorter.model.rule.RuleElement;
import com.fenrir.filesorter.model.rule.StringRule;
import com.fenrir.filesorter.model.statement.provider.ProviderDescription;
import com.fenrir.filesorter.model.statement.provider.LiteralProvider;
import com.fenrir.filesorter.model.statement.provider.Provider;
import com.fenrir.filesorter.model.statement.types.ProviderType;
import com.fenrir.filesorter.model.enums.Scope;
import com.fenrir.filesorter.model.enums.DatePatternType;

import java.util.ArrayList;
import java.util.List;

public class SortRuleParser {
    public List<Provider<?>> resolveRule(StringRule rule) throws TokenFormatException {
        try {
            List<Provider<?>> statements = new ArrayList<>();

            Iterator<RuleElement> iterator = rule.getRuleElementsIterator();
            while (iterator.hasNext()) {
                RuleElement element = iterator.next();
                Provider<?> statement = parseElement(element);
                statements.add(statement);
            }

            return statements;
        } catch (TokenFormatException e) {
            throw new TokenFormatException(e.getMessage(), e, rule, e.getToken());
        }
    }

    private Provider<?> parseElement(RuleElement element) throws TokenFormatException {
        if (!element.isToken()) {
            ProviderDescription description = ProviderDescription.ofLiteral(element.element());
            return new LiteralProvider(description);
        }

        String token = element.element();
        ProviderType providerType = ProviderType.getType(token, Scope.SORT);

        if (providerType == null) {
            throw new TokenFormatException("Unknown token", token);
        }

        if (providerType == ProviderType.DATE) {
            ProviderDescription description = ProviderDescription.ofDate(DatePatternType.getType(token).getPattern());
            return providerType.getAsProvider(description);
        }

        return providerType.getAsProvider(null);
    }

}
