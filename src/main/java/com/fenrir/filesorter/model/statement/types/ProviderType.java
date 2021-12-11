package com.fenrir.filesorter.model.statement.types;


import com.fenrir.filesorter.model.enums.Category;
import com.fenrir.filesorter.model.enums.Group;
import com.fenrir.filesorter.model.exceptions.ArgumentFormatException;
import com.fenrir.filesorter.model.file.utils.Dimension;
import com.fenrir.filesorter.model.statement.predicate.PredicateOperands;
import com.fenrir.filesorter.model.statement.provider.ProviderDescription;
import com.fenrir.filesorter.model.statement.provider.*;
import com.fenrir.filesorter.model.enums.Scope;
import com.fenrir.filesorter.model.enums.DatePatternType;
import com.fenrir.filesorter.model.utils.Converter;

import java.nio.file.Path;
import java.time.chrono.ChronoLocalDate;
import java.util.Arrays;
import java.util.List;

import static com.fenrir.filesorter.model.enums.Scope.*;

public enum ProviderType {
    DIMENSION("DIM", "dimension", new Scope[]{SORT, RENAME, FILTER}, Category.NUMBER, Group.PHOTO) {
        @Override
        public Provider<?> getAsProvider(ProviderDescription description) {
            return new ImageDimensionProvider(description);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args)
                throws ArgumentFormatException {
            Provider<Dimension> operand = new ImageDimensionProvider(null);
            List<Dimension> dimensions = Converter.convertToDimension(args);
            return new PredicateOperands<>(operand, dimensions);
        }
    },
    FILE_EXTENSION("EXT", "extension", new Scope[]{SORT, RENAME, FILTER}, Category.STRING, Group.FILE_INFO) {
        @Override
        public Provider<?> getAsProvider(ProviderDescription description) {
            return new FileExtensionProvider(description);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            Provider<String> operand = new FileNameProvider(null);
            return new PredicateOperands<>(operand, args);
        }
    },
    FILE_CATEGORY("CAT", "category", new Scope[]{SORT, RENAME, FILTER}, Category.EXACT_STRING, Group.FILE_INFO) {
        @Override
        public Provider<?> getAsProvider(ProviderDescription description) {
            return new FileCategoryProvider(description);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            Provider<String> operand = new FileCategoryProvider(null);
            return new PredicateOperands<>(operand, args);
        }
    },
    FILE_NAME("FIN", "file name", new Scope[]{RENAME, FILTER}, Category.STRING, Group.FILE_INFO) {
        @Override
        public Provider<?> getAsProvider(ProviderDescription description) {
            return new FileNameProvider(description);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            Provider<String> operand = new FileNameProvider(null);
            return new PredicateOperands<>(operand, args);
        }
    },
    SEPARATOR("/", "/", new Scope[]{SORT}, Category.NONE, Group.NONE) {
        @Override
        public Provider<?> getAsProvider(ProviderDescription description) {
            return new FileSeparatorProvider(description);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            throw new UnsupportedOperationException();
        }
    },
    PATH("PAT", "path", new Scope[]{FILTER}, Category.STRING, Group.FILE_INFO) {
        @Override
        public Provider<?> getAsProvider(ProviderDescription description) {
            return new FilePathProvider(description);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            Provider<Path> operand = new FilePathProvider(null);
            List<Path> paths = Converter.convertToPaths(args);
            return new PredicateOperands<>(operand, paths);
        }
    },
    DATE("DAT", "creation date", new Scope[]{FILTER}, Category.DATE, Group.DATES) {
        @Override
        public Provider<?> getAsProvider(ProviderDescription description) {
            return new DateProvider(description);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args)
                throws ArgumentFormatException {
            Provider<ChronoLocalDate> operand = new DateProvider(null);
            List<ChronoLocalDate> dates = Converter.convertToDate(args);
            return new PredicateOperands<>(operand, dates);
        }
    },
    SIZE("SIZ", "size", new Scope[]{FILTER}, Category.NUMBER, Group.FILE_INFO) {
        @Override
        public Provider<?> getAsProvider(ProviderDescription description) {
            return new FileSizeProvider(description);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args)
                throws ArgumentFormatException {
            Provider<Long> operand = new FileSizeProvider(null);
            List<Long> sizes = Converter.convertToBytes(args);
            return new PredicateOperands<>(operand, sizes);
        }
    },
    STRING("STR", "custom name", new Scope[]{SORT, RENAME}, Category.STRING, Group.NONE) {
        @Override
        public Provider<?> getAsProvider(ProviderDescription description) {
            return null;
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) throws ArgumentFormatException {
            return null;
        }
    };

    private final String token;
    private final String name;
    private final Scope[] scope;
    private final Category category;
    private final Group group;

    ProviderType(String token, String name, Scope[] scope, Category category, Group group) {
        this.token = token;
        this.name = name;
        this.scope = scope;
        this.category = category;
        this.group = group;
    }

    public static ProviderType getType(String token, Scope scope) {
        ProviderType[] types = ProviderType.values();
        for (ProviderType type: types) {
            if (token.equals(type.getToken()) && checkScope(scope, type)) {
                return type;
            }
        }
        return checkIfDate(token, scope) ? DATE : null;
    }

    private static boolean checkScope(Scope scope, ProviderType type) {
        return Arrays.asList(type.getScope()).contains(scope);
    }

    public abstract Provider<?> getAsProvider(ProviderDescription description);
    public abstract PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) throws ArgumentFormatException;

    private static boolean checkIfDate(String token, Scope scope) {
        return (scope == RENAME || scope == SORT) && DatePatternType.getType(token) != null;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public Scope[] getScope() {
        return scope;
    }

    public Category getCategory() {
        return category;
    }

    public Group getGroup() {
        return group;
    }
}
