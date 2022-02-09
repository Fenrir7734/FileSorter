package com.fenrir.filesorter.model.statement.utils;

import com.fenrir.filesorter.model.statement.predicate.PredicateOperands;
import com.fenrir.filesorter.model.statement.provider.*;

import java.nio.file.Path;
import java.util.List;

public class TypeChecker {

    private TypeChecker() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Comparable<T>> boolean isInstanceOfString(PredicateOperands<T> operands) {
        return checkProvider(operands.operand()) && checkArguments(operands.args());
    }

    private static <T extends Comparable<T>> boolean checkProvider(Provider<T> provider) {
        return provider instanceof FileCategoryProvider
                || provider instanceof FileExtensionProvider
                || provider instanceof FileNameProvider
                || provider instanceof FileNameWithExtensionProvider
                || provider instanceof DirectoryNameProvider
                || provider instanceof FileSeparatorProvider
                || provider instanceof CustomTextProvider;
    }

    private static <T extends Comparable<T>> boolean checkArguments(List<T> args) {
        return args.stream().allMatch(a -> a instanceof String);
    }
}
