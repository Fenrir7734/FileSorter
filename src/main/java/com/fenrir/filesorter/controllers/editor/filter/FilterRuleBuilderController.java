package com.fenrir.filesorter.controllers.editor.filter;

import com.fenrir.filesorter.controllers.editor.filter.input.ArgumentInputController;
import com.fenrir.filesorter.controllers.editor.filter.input.ArgumentInputFactory;
import com.fenrir.filesorter.model.enums.Category;
import com.fenrir.filesorter.model.enums.Scope;
import com.fenrir.filesorter.model.rule.FilterRule;
import com.fenrir.filesorter.model.rule.Iterator;
import com.fenrir.filesorter.model.rule.RuleElement;
import com.fenrir.filesorter.model.statement.types.ActionType;
import com.fenrir.filesorter.model.statement.types.PredicateType;
import com.fenrir.filesorter.model.statement.types.ProviderType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FilterRuleBuilderController {
    private final Logger logger = LoggerFactory.getLogger(FilterRuleBuilderController.class);

    @FXML private VBox inputContainer;

    @FXML private ComboBox<ActionType> actionComboBox;
    @FXML private ComboBox<ProviderType> providerComboBox;
    @FXML private ComboBox<PredicateType> predicateComboBox;

    private final ObservableList<ActionType> actionTypeItems = FXCollections.observableArrayList();
    private final ObservableList<ProviderType> providerTypeItems = FXCollections.observableArrayList();
    private final ObservableList<PredicateType> predicateTypeItems = FXCollections.observableArrayList();

    private final Callback<ListView<ActionType>, ListCell<ActionType>> actionCallback = createActionCellFactory();
    private final Callback<ListView<ProviderType>, ListCell<ProviderType>> providerCallback = createProviderCellFactory();
    private final Callback<ListView<PredicateType>, ListCell<PredicateType>> predicateCallback = createPredicateCellFactory();

    private InputControllerMediator inputControllerMediator;

    @FXML
    public void initialize() {
        inputControllerMediator = new InputControllerMediator();
        inputControllerMediator.registerFilterRuleBuilderController(this);

        initActionComboBox();
        initPredicateComboBox();
        initProviderComboBox();

        actionComboBox.getSelectionModel().select(0);
        providerComboBox.getSelectionModel().select(0);
    }

    private void initActionComboBox() {
        actionTypeItems.addAll(ActionType.values());
        actionComboBox.setItems(actionTypeItems);
        actionComboBox.setButtonCell(actionCallback.call(null));
        actionComboBox.setCellFactory(actionCallback);
    }

    private void initPredicateComboBox() {
        predicateComboBox.setItems(predicateTypeItems);
        predicateComboBox.setButtonCell(predicateCallback.call(null));
        predicateComboBox.setCellFactory(predicateCallback);
    }

    private void initProviderComboBox() {
        populateProviderList();
        providerComboBox.setItems(providerTypeItems);
        providerComboBox.setButtonCell(providerCallback.call(null));
        providerComboBox.setCellFactory(providerCallback);
        providerComboBox.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> onProviderTypeChanged(newValue));
    }

    private void populateProviderList() {
        ProviderType[] types = ProviderType.values();
        for (ProviderType type: types) {
            if (isProviderInFilterScope(type)) {
                providerTypeItems.add(type);
            }
        }
    }

    private boolean isProviderInFilterScope(ProviderType type) {
        return Arrays.asList(type.getScope()).contains(Scope.FILTER);
    }

    private void onProviderTypeChanged(ProviderType providerType) {
        populatePredicateList(providerType);
        changeInputFields(providerType);
    }

    private void populatePredicateList(ProviderType providerType) {
        predicateTypeItems.clear();
        Category typeOfSelectedProvider = providerType.getCategory();
        List<PredicateType> predicateTypes = PredicateType.getPredicatesForCategory(typeOfSelectedProvider);
        predicateTypeItems.addAll(predicateTypes);
        predicateComboBox.getSelectionModel().select(0);
    }

    private void changeInputFields(ProviderType providerType) {
        try {
            inputContainer.getChildren().clear();
            inputControllerMediator = new InputControllerMediator();
            inputControllerMediator.registerFilterRuleBuilderController(this);
            loadInputField(providerType);
        } catch (IOException e) {
            logger.error("Error during changing input controller: {}", e.getMessage());
        }
    }

    @FXML
    public void addInputField() {
        try {
            ProviderType selectedProvider = providerComboBox.getSelectionModel().getSelectedItem();
            loadInputField(selectedProvider);
        } catch (IOException e) {
            logger.error("Error during adding input controller: {}", e.getMessage());
        }
    }

    public void addInputFields(int n) {
        for (int j = 0; j < n; j++) {
            addInputField();
        }
    }

    private void loadInputField(ProviderType providerType) throws IOException {
        ArgumentInputController argumentInputController = ArgumentInputFactory.getInputContainer(providerType);
        argumentInputController.setInputControllerMediator(inputControllerMediator);
        inputContainer.getChildren().add(argumentInputController.getInputContainer());
    }

    public void deleteInputField(HBox inputField) {
        inputContainer.getChildren().remove(inputField);
    }

    public String buildExpression() {
        ProviderType providerType = providerComboBox.getSelectionModel().getSelectedItem();
        PredicateType predicateType = predicateComboBox.getSelectionModel().getSelectedItem();
        List<String> argumentList = inputControllerMediator.receiveArguments()
                .stream()
                .filter(arg -> arg != null && !arg.isEmpty())
                .toList();
        String args = String.join(",", argumentList);
        return String.format("%s(%s)%s(%s:%s)", "%", providerType.getToken(), "%", predicateType.getToken(), args);
    }

    public void receiveRule(FilterRule rule) {
        Iterator<RuleElement> iter = rule.getRuleElementsIterator();
        RuleElement provider = iter.next();
        RuleElement predicate = iter.next();
        ProviderType providerType = ProviderType.getType(provider.element(), Scope.FILTER);
        PredicateType predicateType = PredicateType.getType(predicate.element());
        List<String> args = predicate.args();
        providerComboBox.getSelectionModel().select(providerType);
        predicateComboBox.getSelectionModel().select(predicateType);
        addInputFields(args.size() - 1);
        inputControllerMediator.sendArguments(args);
    }

    private Callback<ListView<ActionType>, ListCell<ActionType>> createActionCellFactory() {
        return new Callback<>() {
            @Override
            public ListCell<ActionType> call(ListView<ActionType> o) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(ActionType type, boolean empty) {
                        super.updateItem(type, empty);

                        if (empty || type == null) {
                            setText(null);
                        } else {
                            setText(type.getName());
                        }
                    }
                };
            }
        };
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
                        }
                    }
                };
            }
        };
    }

    private Callback<ListView<PredicateType>, ListCell<PredicateType>> createPredicateCellFactory() {
        return new Callback<>() {
            @Override
            public ListCell<PredicateType> call(ListView<PredicateType> o) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(PredicateType type, boolean empty) {
                        super.updateItem(type, empty);

                        if (empty || type == null) {
                            setText(null);
                        } else {
                            setText(type.getName());
                        }
                    }
                };
            }
        };
    }
}
