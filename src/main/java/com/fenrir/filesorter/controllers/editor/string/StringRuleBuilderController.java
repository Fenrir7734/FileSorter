package com.fenrir.filesorter.controllers.editor.string;

import com.fenrir.filesorter.controllers.editor.string.input.StringArgumentInputController;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.statement.types.ProviderType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class StringRuleBuilderController {
    @FXML
    private ListView<ProviderType> providerListView;
    @FXML private ListView<ProviderArgPair> selectedProvidersListView;

    private final ObservableList<ProviderType> providerTypeItems = FXCollections.observableArrayList();
    private final ObservableList<ProviderArgPair> selectedProviderTypeItems = FXCollections.observableArrayList();

    private final Callback<ListView<ProviderType>, ListCell<ProviderType>> providerCallback = createProviderCellFactory();
    private final Callback<ListView<ProviderArgPair>, ListCell<ProviderArgPair>> selectedProviderCallback =
            createSelectedProviderCellFactory();

    private final InputControllerMediator inputControllerMediator = new InputControllerMediator();

    @FXML
    public void initialize() {
        inputControllerMediator.registerRenameRuleBuilderController(this);
        initProviderListView();
        initSelectedProviderListView();
    }

    private void initProviderListView() {
        List<ProviderType> providerTypes = getProviderList();
        providerTypeItems.addAll(providerTypes);
        providerListView.setItems(providerTypeItems);
        providerListView.setCellFactory(providerCallback);
    }

    protected abstract List<ProviderType> getProviderList();

    private void initSelectedProviderListView() {
        selectedProvidersListView.setItems(selectedProviderTypeItems);
        selectedProvidersListView.setCellFactory(selectedProviderCallback);
    }

    private void onProviderClicked(MouseEvent event, ProviderType type) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            if (type == ProviderType.DATE_CREATED
                    || type == ProviderType.DATE_ACCESSED
                    || type == ProviderType.DATE_MODIFIED
                    || type == ProviderType.DATE_CURRENT) {

            } else if (type == ProviderType.CUSTOM_TEXT) {
                openTextInput();
            } else {
                selectedProviderTypeItems.add(new ProviderArgPair(type, null));
            }
        }
    }

    private void openTextInput() {
        try {
            loadView("/com/fenrir/filesorter/controllers/editor/string/input/TextInputView.fxml");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadView(String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(getClass().getResource(name))
        );
        Parent parent = loader.load();
        StringArgumentInputController controller = loader.getController();
        controller.setInputControllerMediator(inputControllerMediator);
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void receiveArgument(String arg, ProviderType type) {
        if (arg != null && !arg.isBlank()) {
            ProviderArgPair pair = new ProviderArgPair(type, arg);
            selectedProviderTypeItems.add(pair);
        }
    }

    private void onDeleteButtonClick(ProviderArgPair pair) {
        selectedProviderTypeItems.remove(pair);
    }

    public String buildExpression() {
        StringBuilder expressionBuilder = new StringBuilder();
        for (ProviderArgPair providerArgPair: selectedProviderTypeItems) {
            String token = String.format("%%(%s%s)",
                    providerArgPair.providerType.getToken(),
                    providerArgPair.getArgs().map(v ->":" + v).orElse("")
            );
            expressionBuilder.append(token);
        }
        return expressionBuilder.toString();
    }

    public void setRule(Rule rule) {
        List<ProviderArgPair> providerArgPairs = parseRule(rule);
        selectedProviderTypeItems.addAll(providerArgPairs);
    }

    protected abstract List<ProviderArgPair> parseRule(Rule rule);

    private Callback<ListView<ProviderType>, ListCell<ProviderType>> createProviderCellFactory() {
        return new Callback<>() {
            @Override
            public ListCell<ProviderType> call(ListView<ProviderType> o) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(ProviderType type, boolean empty) {
                        super.updateItem(type, empty);

                        if (empty || type == null) {
                            setText(null);
                        } else {
                            setText(type.getName());
                            setOnMouseClicked(mouseEvent -> onProviderClicked(mouseEvent, type));
                        }
                    }
                };
            }
        };
    }

    private Callback<ListView<ProviderArgPair>, ListCell<ProviderArgPair>> createSelectedProviderCellFactory() {
        return new Callback<>() {
            @Override
            public ListCell<ProviderArgPair> call(ListView<ProviderArgPair> providerTypeOptionalPair) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(ProviderArgPair pair, boolean empty) {
                        super.updateItem(pair, empty);

                        if (empty || pair == null) {
                            setGraphic(null);
                        } else {
                            HBox container = createContainerForSelectedProvider(pair);
                            setGraphic(container);
                        }
                    }
                };
            }
        };
    }

    private HBox createContainerForSelectedProvider(ProviderArgPair pair) {
        Button deleteButton = createDeleteButton();
        deleteButton.setOnAction(actionEvent -> onDeleteButtonClick(pair));
        Label providerNameLabel = new Label(pair.providerType.getName());
        HBox container = createContainer();
        container.getChildren().addAll(providerNameLabel, deleteButton);
        return container;
    }

    private Button createDeleteButton() {
        Button deleteButton = new Button("\u00D7");
        deleteButton.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: red;" +
                "-fx-padding: 0 0 0 0;" +
                "-fx-cursor: hand"
        );
        return deleteButton;
    }

    private HBox createContainer() {
        HBox container = new HBox();
        container.setStyle(
                "-fx-max-height: 20px;" +
                "-fx-pref-height: 20px;" +
                "-fx-padding: 0 10px 0 10px;" +
                "-fx-background-radius: 20px;" +
                "-fx-background-color: #23c3cf;" +
                "-fx-border-radius: 20px;" +
                "-fx-spacing: 5px"
        );
        return container;
    }

    public static class ProviderArgPair {
        private final ProviderType providerType;
        private final String args;

        public ProviderArgPair(ProviderType providerType, String args) {
            this.providerType = providerType;
            this.args = args;
        }

        public ProviderType getProviderType() {
            return providerType;
        }

        public Optional<String> getArgs() {
            return Optional.ofNullable(args);
        }
    }
}
