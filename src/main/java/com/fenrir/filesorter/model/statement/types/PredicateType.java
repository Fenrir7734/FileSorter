package com.fenrir.filesorter.model.statement.types;

import com.fenrir.filesorter.model.enums.Category;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.statement.predicate.PredicateOperands;
import com.fenrir.filesorter.model.statement.predicate.*;
import com.fenrir.filesorter.model.enums.ArgumentNumber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.fenrir.filesorter.model.enums.ArgumentNumber.MULTIPLE;
import static com.fenrir.filesorter.model.enums.ArgumentNumber.SINGLE;
import static com.fenrir.filesorter.model.enums.Category.*;

public enum PredicateType {
    EQUAL("==", "equal",  MULTIPLE, new Category[]{NUMBER, STRING, EXACT_STRING, DATE}) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(
                PredicateOperands<? extends Comparable<?>> operands, boolean isInverted) {
            return new EqualPredicate<>(operands, isInverted);
        }
    },
    NOT_EQUAL("!=", "not equal", MULTIPLE, new Category[]{NUMBER, STRING, EXACT_STRING, DATE}) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(
                PredicateOperands<? extends Comparable<?>> operands, boolean isInverted) {
            return new NotEqualPredicate<>(operands, isInverted);
        }
    },
    GREATER(">", "greater than", SINGLE, new Category[]{NUMBER, DATE}) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(
                PredicateOperands<? extends Comparable<?>> operands, boolean isInverted) {
            return new GreaterPredicate<>(operands, isInverted);
        }
    },
    GREATER_EQUAL(">=", "greater or equal than", SINGLE, new Category[]{NUMBER, DATE}) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(
                PredicateOperands<? extends Comparable<?>> operands, boolean isInverted) {
            return new GreaterEqualPredicate<>(operands, isInverted);
        }
    },
    SMALLER("<", "smaller than", SINGLE, new Category[]{NUMBER, DATE}) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(
                PredicateOperands<? extends Comparable<?>> operands, boolean isInverted) {
            return new SmallerPredicate<>(operands, isInverted);
        }
    },
    SMALLER_EQUAL("<=", "smaller or equal than", SINGLE, new Category[]{NUMBER, DATE}) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(
                PredicateOperands<? extends Comparable<?>> operands, boolean isInverted) {
            return new SmallerEqualPredicate<>(operands, isInverted);
        }
    },
    CONTAINS("CON", "contains", MULTIPLE, new Category[]{STRING}) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(
                PredicateOperands<? extends Comparable<?>> operands, boolean isInverted) throws ExpressionFormatException {
            return new ContainsPredicate<>(operands, isInverted);
        }
    },
    NOT_CONTAINS("NCO", "not contains", MULTIPLE, new Category[]{STRING}) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(
                PredicateOperands<? extends Comparable<?>> operands, boolean isInverted) throws ExpressionFormatException {
            return new NotContainsPredicate<>(operands, isInverted);
        }
    },
    STARTS_WITH("SW", "starts with", MULTIPLE, new Category[]{STRING}) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(
                PredicateOperands<? extends Comparable<?>> operands, boolean isInverted) throws ExpressionFormatException {
            return new StartsWithPredicate<>(operands, isInverted);
        }
    },
    ENDS_WITH("EW", "ends with", MULTIPLE, new Category[]{STRING}) {
        @Override
        public Predicate<? extends Comparable<?>> getPredicate(
                PredicateOperands<? extends Comparable<?>> operands, boolean isInverted) throws ExpressionFormatException {
            return new EndsWithPredicate<>(operands, isInverted);
        }
    };

    private final String token;
    private final String name;
    private final ArgumentNumber argumentNumber;
    private final Category[] forCategory;

    PredicateType(String token, String name, ArgumentNumber argumentNumber, Category[] forCategory) {
        this.token = token;
        this.name = name;
        this.argumentNumber = argumentNumber;
        this.forCategory = forCategory;
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

    public static List<PredicateType> getPredicatesForCategory(Category category) {
        PredicateType[] predicateTypes = PredicateType.values();
        List<PredicateType> predicatesTypesForCategory = new ArrayList<>();
        for (PredicateType predicateType: predicateTypes) {
            List<Category> categories = Arrays.asList(predicateType.forCategory);
            if (categories.contains(category)) {
                predicatesTypesForCategory.add(predicateType);
            }
        }
        return predicatesTypesForCategory;
    }

    public abstract Predicate<? extends Comparable<?>> getPredicate(
            PredicateOperands<? extends Comparable<?>> operands, boolean isInverted) throws ExpressionFormatException;

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public ArgumentNumber getArgumentNumber() {
        return argumentNumber;
    }

    public Category[] getCategories() {
        return forCategory;
    }
}
