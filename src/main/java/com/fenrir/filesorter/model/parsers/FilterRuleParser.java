package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.exceptions.ArgumentFormatException;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.rule.Iterator;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.rule.Token;
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

    public Predicate<? extends Comparable<?>> resolveRule(Rule rule) throws ExpressionFormatException {
        try {
            validateRule(rule);

            Iterator<Token> iterator = rule.getTokenIterator();
            Token operand = iterator.next();
            Token operator = iterator.next();
            PredicateOperands<? extends Comparable<?>> operands = ProviderType.getType(operand.symbol(), Scope.FILTER)
                    .getAsOperands(operator.args());
            return PredicateType.getType(operator.symbol()).getPredicate(operands);
        } catch (ArgumentFormatException e) {
            throw new ArgumentFormatException(e.getMessage(), e, rule, e.getToken(), e.getArg());
        }
    }

    public void validateRule(Rule rule) throws ExpressionFormatException {
        Iterator<Token> iterator = rule.getTokenIterator();
        Token operand = iterator.next();
        Token operator = iterator.next();
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

    private void validateOperand(Rule rule, Token operand) throws ExpressionFormatException {
        if (operand == null) {
            throw new ExpressionFormatException(
                    "Invalid expression, one of the mandatory tokens is missing.",
                    rule
            );
        }
        ProviderType type = ProviderType.getType(operand.symbol(), Scope.FILTER);

        if (type == null) {
            throw new TokenFormatException("Unknown operand.", rule, operand.symbol());
        }
    }

    private void validateOperator(Rule rule, Token operator) throws ExpressionFormatException {
        if (operator == null) {
            throw new ExpressionFormatException(
                    "Invalid expression, one of the mandatory tokens is missing.",
                    rule
            );
        }
        PredicateType type = PredicateType.getType(operator.symbol());

        if (type == null) {
            throw new TokenFormatException("Unknown operator.", rule, operator.symbol());
        }
        validateArgumentNumber(rule, operator);
    }

    private void validateArgumentNumber(Rule rule, Token operator) throws TokenFormatException {
        List<String> args = operator.args();
        ArgumentNumber argumentNumber = PredicateType.getType(operator.symbol()).getArgumentNumber();

        if (argumentNumber == ArgumentNumber.NONE && !checkIfArgsEmpty(args)) {
            throw new TokenFormatException("Expected zero arguments", rule, operator.symbol());
        }
        if (argumentNumber == ArgumentNumber.SINGLE && !checkIfOnlyOneArg(args)) {
            throw new TokenFormatException("Expected only one argument", rule, operator.symbol());
        }
        if (argumentNumber == ArgumentNumber.MULTIPLE && !checkIfAtLeastOne(args)) {
            throw new TokenFormatException("Expected at least one argument.", rule, operator.symbol());
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
