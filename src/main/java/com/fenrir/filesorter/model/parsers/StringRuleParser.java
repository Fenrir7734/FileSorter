package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.enums.ArgumentNumber;
import com.fenrir.filesorter.model.enums.Scope;
import com.fenrir.filesorter.model.exceptions.ArgumentFormatException;
import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.rule.Iterator;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.rule.Token;
import com.fenrir.filesorter.model.statement.provider.Provider;
import com.fenrir.filesorter.model.statement.types.ProviderType;

import java.util.ArrayList;
import java.util.List;

public abstract class StringRuleParser {
    private final Scope scope;

    public StringRuleParser(Scope scope) {
        this.scope = scope;
    }

    public List<Provider<?>> resolveRule(Rule rule) throws TokenFormatException {
        try {
            List<Provider<?>> statements = new ArrayList<>();

            Iterator<Token> iterator = rule.getTokenIterator();
            while (iterator.hasNext()) {
                Token token = iterator.next();
                Provider<?> statement = parseToken(token);
                statements.add(statement);
            }

            return statements;
        } catch (ArgumentFormatException e) {
            throw new ArgumentFormatException(e.getMessage(), e, rule, e.getToken(), e.getArg());
        } catch (TokenFormatException e) {
            throw new TokenFormatException(e.getMessage(), e, rule, e.getToken());
        }
    }

    private Provider<?> parseToken(Token token) throws TokenFormatException {
        validateToken(token);
        String symbol = token.symbol();
        ProviderType providerType = ProviderType.getType(symbol, scope);

        return providerType.getAsProvider(token.args());
    }

    private void validateToken(Token token) throws TokenFormatException {
        ProviderType providerType = ProviderType.getType(token.symbol(), scope);

        if (providerType == null) {
            throw new TokenFormatException("Unknown token", token.symbol());
        }
        validateArgumentNumber(token, providerType.getArgumentNumber());
    }

    private void validateArgumentNumber(Token token, ArgumentNumber number) throws TokenFormatException {
        List<String> args = token.args();

        if (number == ArgumentNumber.NONE && !checkIfArgsEmpty(args)) {
            throw new TokenFormatException("Expected zero arguments", token.symbol());
        }
        if (number == ArgumentNumber.SINGLE && !checkIfOnlyOneArg(args)) {
            throw new TokenFormatException("Expected one argument", token.symbol());
        }
        if (number == ArgumentNumber.MULTIPLE && !checkIfAtLeastOne(args)) {
            throw new TokenFormatException("Expected at least one argument.", token.symbol());
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
