package utils;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.provider.ProviderDescription;
import com.fenrir.filesorter.model.statement.provider.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatementUtilsTest {
    @TempDir
    Path tempDir;
    FileData file;

    @BeforeEach
    public void init() throws IOException {
        Path path = FileUtils.createFile(tempDir, "testfile.txt");
        file = new FileData(path);
    }

    @Test
    public void shouldReturnCorrectStringForValidInput() throws IOException {
        List<Provider<?>> statements = List.of(
                new FileNameProvider(null),
                new FileSeparatorProvider(null),
                new FileNameProvider(null),
                new LiteralProvider(ProviderDescription.ofLiteral("ABC")),
                new LiteralProvider(ProviderDescription.ofLiteral(".")),
                new FileExtensionProvider(null)
        );
        String actualResult = StatementUtils.build(statements, file);
        String expectedResult = "testfile/testfileABC.txt";
        assertEquals(expectedResult, actualResult);
    }
}