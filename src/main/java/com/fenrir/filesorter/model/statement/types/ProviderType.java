package com.fenrir.filesorter.model.statement.types;


import com.fenrir.filesorter.model.statement.filter.FilterStatementDescription;
import com.fenrir.filesorter.model.statement.filter.operand.*;
import com.fenrir.filesorter.statement3.string.FileSeparatorStatement;

import java.util.Arrays;
import java.util.List;

import static com.fenrir.filesorter.model.statement.types.Scope.*;

public enum SupplierType {
    CURRENT_FILE_NAME("CUR", new Scope[]{SORT, RENAME, FILTER}) {
        @Override
        public FilterOperandStatement<?> get() {
            return new FileNameOperandStatement();
        }

        @Override
        public FilterStatementDescription<? extends Comparable<?>> get(List<String> args) {
            return null;
        }
    },
    DIMENSION("DIM", new Scope[]{SORT, RENAME, FILTER}) {
        @Override
        public FilterOperandStatement<?> get() {
            return new ImageDimensionOperandStatement();
        }

        @Override
        public FilterStatementDescription<? extends Comparable<?>> get(List<String> args) {
            return null;
        }
    },
    FILE_EXTENSION("EXT", new Scope[]{SORT, RENAME, FILTER}) {
        @Override
        public FilterOperandStatement<?> get() {
            return new FileExtensionOperandStatement();
        }

        @Override
        public FilterStatementDescription<? extends Comparable<?>> get(List<String> args) {
            return null;
        }
    },
    FILE_CATEGORY("CAT", new Scope[]{SORT, RENAME, FILTER}) {
        @Override
        public FilterOperandStatement<?> get() {
            return new FileCategoryOperandStatement();
        }

        @Override
        public FilterStatementDescription<? extends Comparable<?>> get(List<String> args) {
            return null;
        }
    },
    SEPARATOR("/", new Scope[]{SORT}) {
        @Override
        public FilterOperandStatement<?> get() {
            return null;
        }

        @Override
        public FilterStatementDescription<? extends Comparable<?>> get(List<String> args) {
            return null;
        }
    },
    PATH("PAT", new Scope[]{FILTER}) {
        @Override
        public FilterOperandStatement<?> get() {
            return null;
        }

        @Override
        public FilterStatementDescription<? extends Comparable<?>> get(List<String> args) {
            return null;
        }
    },
    DATE("DAT", new Scope[]{FILTER}) {
        @Override
        public FilterOperandStatement<?> get() {
            return null;
        }

        @Override
        public FilterStatementDescription<? extends Comparable<?>> get(List<String> args) {
            return null;
        }
    },
    SIZE("SIZ", new Scope[]{FILTER}) {
        @Override
        public FilterOperandStatement<?> get() {
            return null;
        }

        @Override
        public FilterStatementDescription<? extends Comparable<?>> get(List<String> args) {
            return null;
        }
    };

    private final String token;
    private final Scope[] scope;

    SupplierType(String token, Scope[] scope) {
        this.token = token;
        this.scope = scope;
    }

    public abstract FilterOperandStatement<?> get();
    public abstract FilterStatementDescription<? extends Comparable<?>> get(List<String> args);

    public static SupplierType get(String token, Scope scope) {
        SupplierType[] types = SupplierType.values();
        for (SupplierType type: types) {
            if (token.equals(type.getToken()) && checkScope(scope, type)) {
                return type;
            }
        }
        return null;
    }

    public static boolean checkScope(Scope scope, SupplierType type) {
        return Arrays.asList(type.getScope()).contains(scope);
    }

    public String getToken() {
        return token;
    }

    public Scope[] getScope() {
        return scope;
    }
}
