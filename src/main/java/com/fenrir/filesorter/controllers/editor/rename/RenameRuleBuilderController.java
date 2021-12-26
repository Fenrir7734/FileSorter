package com.fenrir.filesorter.controllers.editor.rename;

import com.fenrir.filesorter.model.enums.Scope;
import com.fenrir.filesorter.model.rule.Iterator;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.rule.Token;
import com.fenrir.filesorter.model.statement.types.ProviderType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Pair;
import org.apache.tika.utils.StringUtils;

import java.util.*;

public class RenameRuleBuilderController {
    @FXML private ListView<ProviderType> providerListView;
    @FXML private ListView<ProviderArgPair> selectedProvidersListView;

    private final ObservableList<ProviderType> providerTypeItems = FXCollections.observableArrayList();
    private final ObservableList<ProviderArgPair> selectedProviderTypeItems = FXCollections.observableArrayList();

    private final Callback<ListView<ProviderType>, ListCell<ProviderType>> providerCallback = createProviderCellFactory();
    private final Callback<ListView<ProviderArgPair>, ListCell<ProviderArgPair>> selectedProviderCallback = createSelectedProviderCellFactory();

    @FXML
    public void initialize() {
        initProviderListView();
        initSelectedProviderListView();
    }

    private void initProviderListView() {
        populateProviderList();
        providerListView.setItems(providerTypeItems);
        providerListView.setCellFactory(providerCallback);
    }

    private void populateProviderList() {
        ProviderType[] types = ProviderType.values();
        for (ProviderType type: types) {
            if (isProviderInRenameScope(type)) {
                providerTypeItems.add(type);
            }
        }
    }

    private boolean isProviderInRenameScope(ProviderType type) {
        return Arrays.asList(type.getScope()).contains(Scope.RENAME);
    }

    private void initSelectedProviderListView() {
        selectedProvidersListView.setItems(selectedProviderTypeItems);
        selectedProvidersListView.setCellFactory(selectedProviderCallback);
    }

    private void onProviderClicked(MouseEvent event, ProviderType type) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            String args = null;
            if (type == ProviderType.DATE_CREATED
                    || type == ProviderType.DATE_ACCESSED
                    || type == ProviderType.DATE_MODIFIED
                    || type == ProviderType.DATE_CURRENT) {

            }
            if (type == ProviderType.STRING) {

            }
            selectedProviderTypeItems.add(new ProviderArgPair(type, null));
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
                    providerArgPair.getArgs()
            );
            expressionBuilder.append(token);
        }
        return expressionBuilder.toString();
    }

    public void setRule(Rule rule) {
        Iterator<Token> iterator = rule.getTokenIterator();
        while (iterator.hasNext()) {
            Token token = iterator.next();
            ProviderType providerType = ProviderType.getType(token.symbol(), Scope.RENAME);
            String args = token.args() != null ? String.join(",", token.args()) : null;
            ProviderArgPair pair = new ProviderArgPair(providerType, args);
            selectedProviderTypeItems.add(pair);
        }
    }

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

    private static class ProviderArgPair {
        private final ProviderType providerType;
        private final Optional<String> args;

        public ProviderArgPair(ProviderType providerType, String args) {
            this.providerType = providerType;
            this.args = Optional.ofNullable(args);
        }

        public ProviderType getProviderType() {
            return providerType;
        }

        public String getArgs() {
            return args.orElse("");
        }
    }
}
