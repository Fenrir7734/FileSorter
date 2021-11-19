package com.fenrir.filesorter.model.statement.provider;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LiteralProviderTest {
    @Test
    public void getShouldThrowUnsupportedOperationException() throws IOException {
        ProviderDescription description = ProviderDescription.ofLiteral("abc");
        Provider<String> provider = new LiteralProvider(description);
        assertThrows(
                UnsupportedOperationException.class,
                () -> provider.get(null)
        );
    }

    @Test
    public void getAsStringShouldReturnLiteral() throws IOException {
        ProviderDescription description = ProviderDescription.ofLiteral("abc");
        Provider<String> provider = new LiteralProvider(description);
        String actualLiteral = provider.getAsString(null);
        assertEquals("abc", actualLiteral);
    }
}