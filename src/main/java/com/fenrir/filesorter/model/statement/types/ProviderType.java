package com.fenrir.filesorter.model.statement.types;


import com.fenrir.filesorter.model.enums.*;
import com.fenrir.filesorter.model.exceptions.ArgumentFormatException;
import com.fenrir.filesorter.model.file.utils.Dimension;
import com.fenrir.filesorter.model.statement.predicate.PredicateOperands;
import com.fenrir.filesorter.model.statement.provider.ProviderDescription;
import com.fenrir.filesorter.model.statement.provider.*;
import com.fenrir.filesorter.model.utils.Converter;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.chrono.ChronoLocalDate;
import java.util.Arrays;
import java.util.List;

import static com.fenrir.filesorter.model.enums.ArgumentNumber.NONE;
import static com.fenrir.filesorter.model.enums.ArgumentNumber.SINGLE;
import static com.fenrir.filesorter.model.enums.Scope.*;

public enum ProviderType {
    FILE_NAME("FIN", "file name", NONE, new Scope[]{RENAME, FILTER}, ReturnType.STRING, Category.FILE_INFO) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            return new FileNameProvider(null);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            Provider<String> operand = new FileNameProvider(null);
            return new PredicateOperands<>(operand, args);
        }
    },
    FILE_NAME_WITH_EXTENSION("FIX", "file name with extension", NONE, new Scope[]{RENAME, FILTER}, ReturnType.STRING, Category.FILE_INFO) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            return new FileNameWithExtensionProvider();
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            Provider<String> operand = new FileNameWithExtensionProvider();
            return new PredicateOperands<>(operand, args);
        }
    },
    DIRECTORY_NAME("DIN", "directory name", NONE, new Scope[]{SORT, RENAME, FILTER}, ReturnType.STRING, Category.FILE_INFO) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            return new DirectoryNameProvider();
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            Provider<String> operand = new DirectoryNameProvider();
            return new PredicateOperands<>(operand, args);
        }
    },
    FILE_PATH("FIP", "file path", NONE, new Scope[]{FILTER}, ReturnType.STRING, Category.FILE_INFO) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            throw new UnsupportedOperationException();
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            Provider<Path> operand = new FilePathProvider(null);
            List<Path> paths = Converter.convertToPaths(args);
            return new PredicateOperands<>(operand, paths);
        }
    },
    DIRECTORY_PATH("DIP", "directory path", NONE, new Scope[]{FILTER}, ReturnType.STRING, Category.FILE_INFO) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            throw new UnsupportedOperationException();
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            Provider<Path> operand = new DirectoryPathProvider();
            List<Path> paths = Converter.convertToPaths(args);
            return new PredicateOperands<>(operand, paths);
        }
    },
    FILE_SIZE("FIS", "file size", NONE, new Scope[]{FILTER}, ReturnType.NUMBER, Category.FILE_INFO) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            throw new UnsupportedOperationException();
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args)
                throws ArgumentFormatException {
            Provider<Long> operand = new FileSizeProvider(null);
            List<Long> sizes = Converter.convertToBytes(args);
            return new PredicateOperands<>(operand, sizes);
        }
    },
    EXTENSION("EXT", "file extension", NONE, new Scope[]{SORT, RENAME, FILTER}, ReturnType.STRING, Category.FILE_INFO) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            return new FileExtensionProvider(null);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            Provider<String> operand = new FileNameProvider(null);
            return new PredicateOperands<>(operand, args);
        }
    },
    CATEGORY("CAT", "file category", NONE, new Scope[]{SORT, RENAME, FILTER}, ReturnType.EXACT_STRING, Category.FILE_INFO) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            return new FileCategoryProvider(null);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            Provider<String> operand = new FileCategoryProvider(null);
            return new PredicateOperands<>(operand, args);
        }
    },
    DIMENSION("DIM", "image dimensions", NONE, new Scope[]{SORT, RENAME, FILTER}, ReturnType.NUMBER, Category.PHOTO) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            return new ImageDimensionProvider(null);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args)
                throws ArgumentFormatException {
            Provider<Dimension> operand = new ImageDimensionProvider(null);
            List<Dimension> dimensions = Converter.convertToDimension(args);
            return new PredicateOperands<>(operand, dimensions);
        }
    },
    WIDTH("WID", "image width", NONE, new Scope[]{SORT, RENAME, FILTER}, ReturnType.NUMBER, Category.PHOTO) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            return new ImageWidthProvider();
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args)
                throws ArgumentFormatException {
            Provider<Integer> operand = new ImageWidthProvider();
            List<Integer> widths = Converter.convertToPositiveInteger(args);
            return new PredicateOperands<>(operand, widths);
        }
    },
    HEIGHT("HEI", "image height", NONE, new Scope[]{SORT, RENAME, FILTER}, ReturnType.NUMBER, Category.PHOTO) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            return new ImageHeightProvider();
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args)
                throws ArgumentFormatException {
            Provider<Integer> operand = new ImageHeightProvider();
            List<Integer> heights = Converter.convertToPositiveInteger(args);
            return new PredicateOperands<>(operand, heights);
        }
    },
    DATE_CREATED("DAC", "date created", SINGLE, new Scope[]{SORT, RENAME, FILTER}, ReturnType.DATE, Category.DATES) {
        @Override
        public Provider<?> getAsProvider(List<String> args)
                throws ArgumentFormatException {
            try {
                new SimpleDateFormat(args.get(0));
                ProviderDescription description = ProviderDescription.ofDate(args.get(0));
                return new DateCreatedProvider(description);
            } catch (IllegalArgumentException e) {
                throw new ArgumentFormatException("Invalid date format", DATE_CREATED.getToken(), args.get(0));
            }
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args)
                throws ArgumentFormatException {
            Provider<ChronoLocalDate> operand = new DateCreatedProvider(null);
            List<ChronoLocalDate> dates = Converter.convertToDate(args);
            return new PredicateOperands<>(operand, dates);
        }
    },
    DATE_MODIFIED("DAM", "date modified", SINGLE, new Scope[]{SORT, RENAME, FILTER}, ReturnType.DATE, Category.DATES) {
        @Override
        public Provider<?> getAsProvider(List<String> args)
                throws ArgumentFormatException {
            try {
                new SimpleDateFormat(args.get(0));
                ProviderDescription description = ProviderDescription.ofDate(args.get(0));
                return new DateModifiedProvider(description);
            } catch (IllegalArgumentException e) {
                throw new ArgumentFormatException("Invalid date format", DATE_MODIFIED.getToken(), args.get(0));
            }
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args)
                throws ArgumentFormatException {
            Provider<ChronoLocalDate> operand = new DateModifiedProvider(null);
            List<ChronoLocalDate> dates = Converter.convertToDate(args);
            return new PredicateOperands<>(operand, dates);
        }
    },
    DATE_ACCESSED("DAA", "date accessed", SINGLE, new Scope[]{SORT, RENAME, FILTER}, ReturnType.DATE, Category.DATES) {
        @Override
        public Provider<?> getAsProvider(List<String> args)
                throws ArgumentFormatException {
            try {
                new SimpleDateFormat(args.get(0));
                ProviderDescription description = ProviderDescription.ofDate(args.get(0));
                return new DateAccessedProvider(description);
            } catch (IllegalArgumentException e) {
                throw new ArgumentFormatException("Invalid date format", DATE_ACCESSED.getToken(), args.get(0));
            }
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args)
                throws ArgumentFormatException {
            Provider<ChronoLocalDate> operand = new DateAccessedProvider(null);
            List<ChronoLocalDate> dates = Converter.convertToDate(args);
            return new PredicateOperands<>(operand, dates);
        }
    },
    DATE_CURRENT("DAU", "date current", SINGLE, new Scope[]{SORT, RENAME}, ReturnType.DATE, Category.DATES) {
        @Override
        public Provider<?> getAsProvider(List<String> args)
                throws ArgumentFormatException {
            try {
                new SimpleDateFormat(args.get(0));
                ProviderDescription description = ProviderDescription.ofDate(args.get(0));
                return new DateCurrentProvider(description);
            } catch (IllegalArgumentException e) {
                throw new ArgumentFormatException("Invalid date format", DATE_CURRENT.getToken(), args.get(0));
            }
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            throw new UnsupportedOperationException();
        }
    },
    CUSTOM_TEXT("TXT", "custom text", SINGLE, new Scope[]{SORT, RENAME}, ReturnType.STRING, Category.NONE) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            return new CustomTextProvider(ProviderDescription.ofLiteral(args.get(0)));
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            throw new UnsupportedOperationException();
        }
    },
    SEPARATOR("/", "/", NONE, new Scope[]{SORT}, ReturnType.NONE, Category.NONE) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            return new FileSeparatorProvider(null);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            throw new UnsupportedOperationException();
        }
    };

    private final String token;
    private final String name;
    private final ArgumentNumber argumentNumber;
    private final Scope[] scope;
    private final ReturnType returnType;
    private final Category group;

    ProviderType(String token, String name, ArgumentNumber argumentNumber, Scope[] scope, ReturnType returnType, Category group) {
        this.token = token;
        this.name = name;
        this.argumentNumber = argumentNumber;
        this.scope = scope;
        this.returnType = returnType;
        this.group = group;
    }

    public static ProviderType getType(String token, Scope scope) {
        ProviderType[] types = ProviderType.values();
        for (ProviderType type: types) {
            if (token.equals(type.getToken()) && checkScope(scope, type)) {
                return type;
            }
        }
        return null;
    }

    private static boolean checkScope(Scope scope, ProviderType type) {
        return Arrays.asList(type.getScope()).contains(scope);
    }

    public abstract Provider<?> getAsProvider(List<String> args) throws ArgumentFormatException;
    public abstract PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) throws ArgumentFormatException;

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public ArgumentNumber getArgumentNumber() {
        return argumentNumber;
    }

    public Scope[] getScope() {
        return scope;
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public Category getGroup() {
        return group;
    }
}
