package com.fenrir.filesorter.statement3.operand;

import java.nio.file.Path;
import java.util.function.Supplier;

public enum Types {
    CURRENT_FILE_NAME {
        @Override
        public FilterOperandStatement<String> get() {
            return new FileNameOperandStatement();
        }
    },
    PATH {
        @Override
        public FilterOperandStatement<Path> get() {
            return new PathOperandStatement();
        }
    };

    public abstract FilterOperandStatement<?> get();
}
