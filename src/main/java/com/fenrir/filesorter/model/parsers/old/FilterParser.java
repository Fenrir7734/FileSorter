package com.fenrir.filesorter.model.parsers;

import com.fenrir.filesorter.model.rules.FilterRule;
import com.fenrir.filesorter.model.rules.Rule;
import com.fenrir.filesorter.model.statement.filter.operator.FilterOperatorStatement;
import com.fenrir.filesorter.model.tokens.filter.FilterOperandTokenType;
import com.fenrir.filesorter.model.tokens.filter.FilterOperatorTokenType;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Arrays;
import java.util.List;

public class FilterParser {
    private Rule rule;

    public FilterParser(FilterRule rule) {
        this.rule = rule;
    }

    public FilterOperatorStatement<? extends Comparable<?>> resolveRule() throws IllegalArgumentException {
        Rule.RuleElement operand = rule.next();
        Rule.RuleElement operator = rule.next();

        if (operand == null || operator == null || rule.next() != null) {
            throw new IllegalArgumentException();
        }

        FilterOperandTokenType operandTokenType = FilterOperandTokenType.get(operand.element());
        FilterOperatorTokenType operatorTokenType = FilterOperatorTokenType.get(operator.element());

        if (operandTokenType == null || operatorTokenType == null) {
            throw new IllegalArgumentException();
        }
        /*
        FilterOperandStatement<? extends Comparable<?>> operandStatement = FilterOperandStatementFactory.get(operandTokenType);
        String[] args = operator.args();
        List<FilterStatement<? extends Comparable<?>>> filterStatements = new ArrayList<>();
        switch (operandTokenType) {
            case DATE: {
                List<ChronoLocalDate> dates = getDate(args);
                //FilterStatementDescription<ChronoLocalDate> description = new FilterStatementDescription<ChronoLocalDate>(operandStatement, dates);
                FilterStatementDescription<ChronoLocalDate> description = new FilterStatementDescription<>(FilterOperandStatementFactory.get(operandTokenType), dates);
                filterStatements.add(FilterStatementFactory.get(description, operatorTokenType));
                //FilterStatement filterStatement = FilterStatementFactory.get(description, operatorTokenType);
            }
        }
         */

        return null;
    }

    public List<ChronoLocalDate> getDate(String[] args) {
        ChronoLocalDate[] dates = new LocalDate[args.length];
        for (int i = 0; i < args.length; i++) {
            dates[i] = LocalDate.parse(args[i]);
        }
        return Arrays.asList(dates);
    }



}
