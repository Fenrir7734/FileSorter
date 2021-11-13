package utils;

import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.statement.string.StringStatement;

import java.io.IOException;
import java.util.List;

public class StatementUtils {
    public static String build(List<StringStatement> statements, FileData file) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (StringStatement statement: statements) {
            String s = statement.execute(file);
            builder.append(s);
        }
        return builder.toString();
    }
}
