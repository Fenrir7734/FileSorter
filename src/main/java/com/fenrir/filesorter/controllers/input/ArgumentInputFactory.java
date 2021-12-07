package com.fenrir.filesorter.controllers.input;

import com.fenrir.filesorter.model.statement.types.ProviderType;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Objects;

public class ArgumentInputFactory {
    public static HBox getInputContainer(ProviderType providerType) throws IOException {
        return switch (providerType) {
            case FILE_NAME, FILE_EXTENSION -> loadInput("StringInput.fxml");
            case PATH -> loadInput("PathInput.fxml");
            case DIMENSION -> loadInput("DimensionInput.fxml");
            case FILE_CATEGORY -> loadInput("CategoryInput.fxml");
            case DATE -> loadInput("DateInput.fxml");
            case SIZE -> loadInput("SizeInput.fxml");
            default -> null;
        };
    }

    private static HBox loadInput(String name) throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(ArgumentInputFactory.class.getResource(name)));
    }
}
