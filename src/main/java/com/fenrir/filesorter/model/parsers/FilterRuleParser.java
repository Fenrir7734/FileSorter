package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.exceptions.ArgumentFormatException;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.rule.FilterRule;
import com.fenrir.filesorter.model.rule.Iterator;
import com.fenrir.filesorter.model.rule.RuleElement;
import com.fenrir.filesorter.model.statement.predicate.PredicateOperands;
import com.fenrir.filesorter.model.statement.predicate.Predicate;
import com.fenrir.filesorter.model.statement.types.PredicateType;
import com.fenrir.filesorter.model.statement.types.ProviderType;
import com.fenrir.filesorter.model.enums.Scope;
import com.fenrir.filesorter.model.enums.ArgumentNumber;

import java.util.List;

/**
 * TODO: 1.Sprawdzenie kolejności tokenów 2. Jeżeli któregoś tokenu brakuje powinien informować którego
 */
public class FilterRuleParser {

    public Predicate<? extends Comparable<?>> resolveRule(FilterRule rule) throws ExpressionFormatException {
        try {
            validateRule(rule);

            Iterator<RuleElement> iterator = rule.getRuleElementsIterator();
            RuleElement operand = iterator.next();
            RuleElement operator = iterator.next();
            PredicateOperands<? extends Comparable<?>> operands = ProviderType.getType(operand.element(), Scope.FILTER)
                    .getAsOperands(operator.args());
            return PredicateType.getType(operator.element()).getPredicate(operands);
        } catch (ArgumentFormatException e) {
            throw new ArgumentFormatException(e.getMessage(), e, rule, e.getToken(), e.getArg());
        }
    }

    public void validateRule(FilterRule rule) throws ExpressionFormatException {
        Iterator<RuleElement> iterator = rule.getRuleElementsIterator();
        RuleElement operand = iterator.next();
        RuleElement operator = iterator.next();
        if (iterator.hasNext()) {
            throw new ExpressionFormatException(
                    "Filter expression should contain only operand and operator token.",
                    rule
            );
        }
        iterator.reset();

        validateOperand(rule, operand);
        validateOperator(rule, operator);
    }

    private void validateOperand(FilterRule rule, RuleElement operand) throws ExpressionFormatException {
        if (operand == null) {
            throw new ExpressionFormatException(
                    "Invalid expression, one of the mandatory tokens is missing.",
                    rule
            );
        }
        ProviderType type = ProviderType.getType(operand.element(), Scope.FILTER);

        if (type == null) {
            throw new TokenFormatException("Unknown operand.", rule, operand.element());
        }
    }

    private void validateOperator(FilterRule rule, RuleElement operator) throws ExpressionFormatException {
        if (operator == null) {
            throw new ExpressionFormatException(
                    "Invalid expression, one of the mandatory tokens is missing.",
                    rule
            );
        }
        PredicateType type = PredicateType.getType(operator.element());

        if (type == null) {
            throw new TokenFormatException("Unknown operator.", rule, operator.element());
        }
        validateArgumentNumber(rule, operator);
    }

    private void validateArgumentNumber(FilterRule rule, RuleElement operator) throws TokenFormatException {
        List<String> args = operator.args();
        ArgumentNumber argumentNumber = PredicateType.getType(operator.element()).getArgumentNumber();

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

    private boolean checkIfArgsEmpty(List<String> args) {
        return args == null || args.size() < 1;
    }

    private boolean checkIfOnlyOneArg(List<String> args) {
        return !checkIfArgsEmpty(args) && args.size() < 2;
    }

    private boolean checkIfAtLeastOne(List<String> args) {
        return !checkIfArgsEmpty(args);
    }
}
