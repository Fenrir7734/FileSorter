package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.rules.FilterRule;
import com.fenrir.filesorter.model.rules.Rule;
import com.fenrir.filesorter.model.statement.filter.operator.FilterOperatorStatement;
import com.fenrir.filesorter.model.statement.filter.FilterStatementFactory;
import com.fenrir.filesorter.model.tokens.filter.FilterOperandTokenType;
import com.fenrir.filesorter.model.tokens.filter.FilterOperatorTokenType;

import java.util.List;

public class FilterRuleParser {

    public FilterOperatorStatement<? extends Comparable<?>> resolveRule(FilterRule rule) throws ExpressionFormatException {
        Rule.RuleElement operand = rule.next();
        Rule.RuleElement operator = rule.next();

        if (operand == null || operator == null || rule.next() != null) {
            throw new ExpressionFormatException("Incorrect expression. Some elements are missing.", rule);
        }

        FilterOperandTokenType operandTokenType = FilterOperandTokenType.get(operand.element());
        FilterOperatorTokenType operatorTokenType = FilterOperatorTokenType.get(operator.element());

        if (operandTokenType == null) {
            throw new TokenFormatException("Unknown operand.", rule, operand.element());
        }
        if (operatorTokenType == null) {
            throw new TokenFormatException("Unknown operator.", rule, operator.element());
        }

        return FilterStatementFactory.get(operandTokenType, operatorTokenType, List.of(operator.args()));
    }
}
