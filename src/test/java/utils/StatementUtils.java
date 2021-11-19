package utils;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.provider.Provider;

import java.io.IOException;
import java.util.List;

public class StatementUtils {
    public static String build(List<Provider<?>> statements, FileData file) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Provider<?> statement: statements) {
            String s = statement.getAsString(file);
            builder.append(s);
        }
        return builder.toString();
    }
}
