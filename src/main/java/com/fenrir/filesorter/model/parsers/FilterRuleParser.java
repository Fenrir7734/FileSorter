package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.exceptions.ArgumentFormatException;
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
        try {
            validateRule(rule);

            Rule.RuleElement operand = rule.next();
            Rule.RuleElement operator = rule.next();
            FilterOperandTokenType operandTokenType = FilterOperandTokenType.get(operand.element());
            FilterOperatorTokenType operatorTokenType = FilterOperatorTokenType.get(operator.element());

            return FilterStatementFactory.get(operandTokenType, operatorTokenType, List.of(operator.args()));
        } catch (ArgumentFormatException e) {
            throw new ArgumentFormatException(e.getMessage(), e, rule, e.getToken(), e.getArg());
        }
    }

    private void validateRule(FilterRule rule) throws ExpressionFormatException {
        Rule.RuleElement operand = rule.next();
        Rule.RuleElement operator = rule.next();
        rule.resetIter();

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

        String[] args = operator.args();

        if (args == null || args.length < 1) {
            throw new TokenFormatException("Expected at least one argument.", rule, operator.element());
        }

        if (args.length > 1 && (
                operatorTokenType == FilterOperatorTokenType.GREATER ||
                        operatorTokenType == FilterOperatorTokenType.GREATER_EQUAL ||
                        operatorTokenType == FilterOperatorTokenType.SMALLER ||
                        operatorTokenType == FilterOperatorTokenType.SMALLER_EQUAL
                )) {
            throw new TokenFormatException("Expected only one argument.", rule, operator.element());
        }
    }
}
