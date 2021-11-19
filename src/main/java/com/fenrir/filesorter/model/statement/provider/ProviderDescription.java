package com.fenrir.filesorter.model.statement.provider;

public record ProviderDescription(String pattern, String literal) {

    public static ProviderDescription ofDate(String pattern) {
        return new ProviderDescription(pattern, null);
    }

    public static ProviderDescription ofLiteral(String literal) {
        return new ProviderDescription(null, literal);
    }
}
