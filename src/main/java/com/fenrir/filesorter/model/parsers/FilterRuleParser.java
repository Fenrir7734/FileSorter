package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.exceptions.ArgumentFormatException;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.rules.FilterRule;
import com.fenrir.filesorter.model.rules.Rule;
import com.fenrir.filesorter.model.statement.filter.operator.FilterOperatorStatement;
import com.fenrir.filesorter.model.statement.filter.FilterStatementFactory;
import com.fenrir.filesorter.model.tokens.filter.ArgumentNumber;
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

        validateOperand(rule, operand);
        validateOperator(rule, operator);
    }

    private void validateOperand(FilterRule rule, Rule.RuleElement operand) throws ExpressionFormatException {
        if (operand == null) {
            throw new ExpressionFormatException("Incorrect expression, operand is missing.", rule);
        }
        FilterOperandTokenType operandTokenType = FilterOperandTokenType.get(operand.element());

        if (operandTokenType == null) {
            throw new TokenFormatException("Unknown operand.", rule, operand.element());
        }
    }

    private void validateOperator(FilterRule rule, Rule.RuleElement operator) throws ExpressionFormatException {
        if (operator == null) {
            throw new ExpressionFormatException("Incorrect expression, operand is missing.", rule);
        }
        FilterOperatorTokenType operatorTokenType = FilterOperatorTokenType.get(operator.element());

        if (operatorTokenType == null) {
            throw new TokenFormatException("Unknown operator.", rule, operator.element());
        }
        validateArgumentNumber(rule, operator);
    }

    private void validateArgumentNumber(FilterRule rule, Rule.RuleElement operator) throws TokenFormatException {
        String[] args = operator.args();
        ArgumentNumber argumentNumber = FilterOperatorTokenType.get(operator.element()).getArgumentNumber();

        if (argumentNumber == ArgumentNumber.NONE && !checkIfArgsEmpty(args)) {
            throw new TokenFormatException("Expected zero arguments", rule, operator.element());
        }
        if (argumentNumber == ArgumentNumber.SINGLE && !checkIfOnlyOneArg(args)) {
            throw new TokenFormatException("Expected only one argument", rule, operator.element());
        }
        if (argumentNumber == ArgumentNumber.MULTIPLE && !checkIfAtLeastOne(args)) {
            throw new TokenFormatException("Expected at least one argument.", rule, operator.element());
        }
    }

    private boolean checkIfArgsEmpty(String[] args) {
        return args == null || args.length < 1;
    }

    private boolean checkIfOnlyOneArg(String[] args) {
        return !checkIfArgsEmpty(args) && args.length < 2;
    }

    private boolean checkIfAtLeastOne(String[] args) {
        return !checkIfArgsEmpty(args);
    }
}
