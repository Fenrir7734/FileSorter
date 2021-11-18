package com.fenrir.filesorter.model.statement.filter;

import com.fenrir.filesorter.model.statement.operator.*;

public class FilterStatementFactory {
    public static FilterOperatorStatement<? extends Comparable<?>> get() {
        return null;
    }
    /*
    public static FilterOperatorStatement<? extends Comparable<?>> get(
            FilterOperandTokenType operandType,
            FilterOperatorTokenType operatorType,
            List<String> args) throws ArgumentFormatException {
        try {
            switch (operandType) {
                case CURRENT_FILE_NAME -> {
                    FilterStatementDescription<String> description = new FilterStatementDescription<>(
                            new FileNameOperandStatement(), args
                    );
                    FilterOperatorStatement<? extends Comparable<?>> statement = getGenericComparisonStatement(description, operatorType);
                    return statement != null ? statement : getStringComparisonStatement(description, operatorType);
                }
                case PATH -> {
                    FilterStatementDescription<Path> description = new FilterStatementDescription<>(
                            new PathOperandStatement(), Converter.convertToPaths(args)
                    );
                    return getGenericComparisonStatement(description, operatorType);
                }
                case DATE -> {
                    FilterStatementDescription<ChronoLocalDate> description = new FilterStatementDescription<>(
                            new DateOperandStatement(), Converter.convertToDate(args)
                    );
                    return getGenericComparisonStatement(description, operatorType);
                }
                case DIMENSION -> {
                    FilterStatementDescription<Dimension> description = new FilterStatementDescription<>(
                            new ImageDimensionOperandStatement(), Converter.convertToDimension(args)
                    );
                    return getGenericComparisonStatement(description, operatorType);
                }
                case SIZE -> {
                    FilterStatementDescription<Long> description = new FilterStatementDescription<>(
                            new FileSizeOperandStatement(), Converter.convertToBytes(args)
                    );
                    return getGenericComparisonStatement(description, operatorType);
                }
                case FILE_EXTENSION -> {
                    FilterStatementDescription<String> description = new FilterStatementDescription<>(
                            new FileExtensionOperandStatement(), args
                    );
                    return getGenericComparisonStatement(description, operatorType);
                }
                case FILE_CATEGORY -> {
                    FilterStatementDescription<String> description = new FilterStatementDescription<>(
                            new FileCategoryOperandStatement(), args
                    );
                    return getGenericComparisonStatement(description, operatorType);
                }
                default -> {
                    return null;
                }
            }
        } catch (ArgumentFormatException e) {
            throw new ArgumentFormatException(e.getMessage(), operandType.getToken(), e.getArg());
        }
    }

    private static FilterOperatorStatement<? extends Comparable<?>> getGenericComparisonStatement(
            FilterStatementDescription<?> description,
            FilterOperatorTokenType type) {
        return switch (type) {
            case EQUAL -> new EqualStatement<>(description);
            case NOT_EQUAL -> new NotEqualStatement<>(description);
            case GREATER -> new GraterStatement<>(description);
            case GREATER_EQUAL -> new GreaterEqualStatement<>(description);
            case SMALLER -> new SmallerStatement<>(description);
            case SMALLER_EQUAL -> new SmallerEqualStatement<>(description);
            default -> null;
        };
    }

    private static FilterOperatorStatement<String> getStringComparisonStatement(
            FilterStatementDescription<String> description,
            FilterOperatorTokenType type) {
        return switch (type) {
            case CONTAINS -> new ContainsStatement(description);
            case STARTS_WITH -> new StartsWithStatement(description);
            case ENDS_WITH -> new EndsWithStatement(description);
            default -> null;
        };
    }
     */
}
