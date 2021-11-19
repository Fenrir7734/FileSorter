package com.fenrir.filesorter.model.statement.types;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.statement.predicate.PredicateOperands;
import com.fenrir.filesorter.model.statement.predicate.*;
import com.fenrir.filesorter.model.enums.ArgumentNumber;

import static com.fenrir.filesorter.model.enums.ArgumentNumber.MULTIPLE;
import static com.fenrir.filesorter.model.enums.ArgumentNumber.SINGLE;

public enum PredicateType {
    EQUAL("==", MULTIPLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands) {
            return new EqualPredicate<>(operands);
        }
    },
    NOT_EQUAL("!=", MULTIPLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands) {
            return new NotEqualPredicate<>(operands);
        }
    },
    GREATER(">", SINGLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands) {
            return new GraterPredicate<>(operands);
        }
    },
    GREATER_EQUAL(">=", SINGLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands) {
            return new GreaterEqualPredicate<>(operands);
        }
    },
    SMALLER("<", SINGLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands) {
            return new SmallerPredicate<>(operands);
        }
    },
    SMALLER_EQUAL("<=", SINGLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands) {
            return new SmallerEqualPredicate<>(operands);
        }
    },
    CONTAINS("CON", MULTIPLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands)
                throws ExpressionFormatException {
            return new ContainsPredicate<>(operands);
        }
    },
    STARTS_WITH("SW", MULTIPLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands)
                throws ExpressionFormatException {
            return new StartsWithPredicate<>(operands);
        }
    },
    ENDS_WITH("EW", MULTIPLE) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands)
                throws ExpressionFormatException {
            return new EndsWithPredicate<>(operands);
        }
    };

    private final String token;
    private final ArgumentNumber argumentNumber;

    PredicateType(String token, ArgumentNumber argumentNumber) {
        this.token = token;
        this.argumentNumber = argumentNumber;
    }

    public abstract Predicate<? extends Comparable<?>> getPredicate(PredicateOperands<? extends Comparable<?>> operands)
            throws ExpressionFormatException;

    public static PredicateType getType(String token) {
        PredicateType[] types = PredicateType.values();
        for (PredicateType type: types) {
            if (token.equals(type.getToken())) {
                return type;
            }
        }
        return null;
    }

    public String getToken() {
        return token;
    }

    public ArgumentNumber getArgumentNumber() {
        return argumentNumber;
    }
}
