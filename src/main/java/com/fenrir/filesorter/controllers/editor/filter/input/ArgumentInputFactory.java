package com.fenrir.filesorter.controllers.editor.filter.input;

import com.fenrir.filesorter.model.statement.types.ProviderType;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

public class ArgumentInputFactory {
    public static ArgumentInputController getInputContainer(ProviderType providerType) throws IOException {
        return switch (providerType) {
            case FILE_NAME, FILE_EXTENSION -> loadInput("StringInput.fxml");
            case FILE_PATH -> loadInput("PathInput.fxml");
            case DIMENSION -> loadInput("DimensionInput.fxml");
            case FILE_CATEGORY -> loadInput("CategoryInput.fxml");
            case DATE -> loadInput("DateInput.fxml");
            case FILE_SIZE -> loadInput("SizeInput.fxml");
            default -> null;
        };
    }

    private static ArgumentInputController loadInput(String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(ArgumentInputFactory.class.getResource(name)));
        loader.load();
        return loader.getController();
    }
}
