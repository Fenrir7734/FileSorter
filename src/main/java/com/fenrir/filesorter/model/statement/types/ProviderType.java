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
    DIMENSION("DIM", "image dimensions", NONE, new Scope[]{SORT, RENAME, FILTER}, Category.NUMBER, Group.PHOTO) {
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
    WIDTH("WID", "image width", NONE, new Scope[]{SORT, RENAME, FILTER}, Category.NUMBER, Group.PHOTO) {
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
    HEIGHT("HEI", "image height", NONE, new Scope[]{SORT, RENAME, FILTER}, Category.NUMBER, Group.PHOTO) {
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
    FILE_EXTENSION("EXT", "file extension", NONE, new Scope[]{SORT, RENAME, FILTER}, Category.STRING, Group.FILE_INFO) {
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
    FILE_CATEGORY("CAT", "file category", NONE, new Scope[]{SORT, RENAME, FILTER}, Category.EXACT_STRING, Group.FILE_INFO) {
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
    DATE("DAT", "file creation date", SINGLE, new Scope[]{SORT, RENAME, FILTER}, Category.DATE, Group.DATES) {
        @Override
        public Provider<?> getAsProvider(List<String> args)
                throws ArgumentFormatException {
            try {
                new SimpleDateFormat(args.get(0));
                ProviderDescription description = ProviderDescription.ofDate(args.get(0));
                return new DateProvider(description);
            } catch (IllegalArgumentException e) {
                throw new ArgumentFormatException("Invalid date format", DATE.getToken(), args.get(0));
            }
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args)
                throws ArgumentFormatException {
            Provider<ChronoLocalDate> operand = new DateProvider(null);
            List<ChronoLocalDate> dates = Converter.convertToDate(args);
            return new PredicateOperands<>(operand, dates);
        }
    },
    DATE_CREATED("DAC", "date created", SINGLE, new Scope[]{SORT, RENAME, FILTER}, Category.DATE, Group.DATES) {
        @Override
        public Provider<?> getAsProvider(List<String> args)
                throws ArgumentFormatException {
            try {
                new SimpleDateFormat(args.get(0));
                ProviderDescription description = ProviderDescription.ofDate(args.get(0));
                return new DateCreatedProvider(description);
            } catch (IllegalArgumentException e) {
                throw new ArgumentFormatException("Invalid date format", DATE.getToken(), args.get(0));
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
    DATE_MODIFIED("DAM", "date modified", SINGLE, new Scope[]{SORT, RENAME, FILTER}, Category.DATE, Group.DATES) {
        @Override
        public Provider<?> getAsProvider(List<String> args)
                throws ArgumentFormatException {
            try {
                new SimpleDateFormat(args.get(0));
                ProviderDescription description = ProviderDescription.ofDate(args.get(0));
                return new DateModifiedProvider(description);
            } catch (IllegalArgumentException e) {
                throw new ArgumentFormatException("Invalid date format", DATE.getToken(), args.get(0));
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
    DATE_ACCESSED("DAA", "date accessed", SINGLE, new Scope[]{SORT, RENAME, FILTER}, Category.DATE, Group.DATES) {
        @Override
        public Provider<?> getAsProvider(List<String> args)
                throws ArgumentFormatException {
            try {
                new SimpleDateFormat(args.get(0));
                ProviderDescription description = ProviderDescription.ofDate(args.get(0));
                return new DateAccessedProvider(description);
            } catch (IllegalArgumentException e) {
                throw new ArgumentFormatException("Invalid date format", DATE.getToken(), args.get(0));
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
    DATE_CURRENT("DAU", "date current", SINGLE, new Scope[]{SORT, RENAME}, Category.DATE, Group.DATES) {
        @Override
        public Provider<?> getAsProvider(List<String> args)
                throws ArgumentFormatException {
            try {
                new SimpleDateFormat(args.get(0));
                ProviderDescription description = ProviderDescription.ofDate(args.get(0));
                return new DateCurrentProvider(description);
            } catch (IllegalArgumentException e) {
                throw new ArgumentFormatException("Invalid date format", DATE.getToken(), args.get(0));
            }
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            throw new UnsupportedOperationException();
        }
    },
    FILE_NAME("FIN", "file name", NONE, new Scope[]{RENAME, FILTER}, Category.STRING, Group.FILE_INFO) {
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
    FILE_NAME_WITH_EXTENSION("FIX", "file name with extension", NONE, new Scope[]{RENAME, FILTER}, Category.STRING, Group.FILE_INFO) {
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
    DIRECTORY_NAME("DIN", "directory name", NONE, new Scope[]{SORT, RENAME, FILTER}, Category.STRING, Group.FILE_INFO) {
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
    SEPARATOR("/", "/", NONE, new Scope[]{SORT}, Category.NONE, Group.NONE) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            return new FileSeparatorProvider(null);
        }

        @Override
        public PredicateOperands<? extends Comparable<?>> getAsOperands(List<String> args) {
            throw new UnsupportedOperationException();
        }
    },
    FILE_PATH("PAT", "file path", NONE, new Scope[]{FILTER}, Category.STRING, Group.FILE_INFO) {
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
    DIRECTORY_PATH("PAD", "directory path", NONE, new Scope[]{FILTER}, Category.STRING, Group.FILE_INFO) {
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
    FILE_SIZE("SIZ", "file size", NONE, new Scope[]{FILTER}, Category.NUMBER, Group.FILE_INFO) {
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
    STRING("STR", "custom name", SINGLE, new Scope[]{SORT, RENAME}, Category.STRING, Group.NONE) {
        @Override
        public Provider<?> getAsProvider(List<String> args) {
            return new LiteralProvider(ProviderDescription.ofLiteral(args.get(0)));
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
    private final Category category;
    private final Group group;

    ProviderType(String token, String name, ArgumentNumber argumentNumber, Scope[] scope, Category category, Group group) {
        this.token = token;
        this.name = name;
        this.argumentNumber = argumentNumber;
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

    public Category getCategory() {
        return category;
    }

    public Group getGroup() {
        return group;
    }
}
