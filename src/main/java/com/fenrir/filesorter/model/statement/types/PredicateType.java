package com.fenrir.filesorter.model.statement.types;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.statement.predicate.PredicateOperands;
import com.fenrir.filesorter.model.statement.predicate.*;
import com.fenrir.filesorter.model.enums.ArgumentNumber;

import static com.fenrir.filesorter.model.enums.ArgumentNumber.MULTIPLE;
import static com.fenrir.filesorter.model.enums.ArgumentNumber.SINGLE;

public enum PredicateType {
    EQUAL("==", "equal",  MULTIPLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands) {
            return new EqualPredicate<>(operands);
        }
    },
    NOT_EQUAL("!=", "not equal", MULTIPLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands) {
            return new NotEqualPredicate<>(operands);
        }
    },
    GREATER(">", "greater than", SINGLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands) {
            return new GreaterPredicate<>(operands);
        }
    },
    GREATER_EQUAL(">=", "greater or equal than", SINGLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands) {
            return new GreaterEqualPredicate<>(operands);
        }
    },
    SMALLER("<", "smaller than", SINGLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands) {
            return new SmallerPredicate<>(operands);
        }
    },
    SMALLER_EQUAL("<=", "smaller or equal than", SINGLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands) {
            return new SmallerEqualPredicate<>(operands);
        }
    },
    CONTAINS("CON", "contains", MULTIPLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands)
                throws ExpressionFormatException {
            return new ContainsPredicate<>(operands);
        }
    },
    STARTS_WITH("SW", "starts with", MULTIPLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands)
                throws ExpressionFormatException {
            return new StartsWithPredicate<>(operands);
        }
    },
    ENDS_WITH("EW", "ends with", MULTIPLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands)
                throws ExpressionFormatException {
            return new EndsWithPredicate<>(operands);
        }
    };

    private final String token;
    private final String name;
    private final ArgumentNumber argumentNumber;

    PredicateType(String token, String name, ArgumentNumber argumentNumber) {
        this.token = token;
        this.name = name;
        this.argumentNumber = argumentNumber;
    }

    public static PredicateType getType(String token) {
        PredicateType[] types = PredicateType.values();
        for (PredicateType type: types) {
            if (token.equals(type.getToken())) {
                return type;
            }
        }
        return null;
    }

    public abstract Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands)
            throws ExpressionFormatException;

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public ArgumentNumber getArgumentNumber() {
        return argumentNumber;
    }
}
