package com.fenrir.filesorter.controllers.editor.filter;

import com.fenrir.filesorter.controllers.editor.filter.input.ArgumentInputController;
import com.fenrir.filesorter.controllers.editor.filter.input.ArgumentInputFactory;
import com.fenrir.filesorter.model.statement.types.enums.ArgumentNumber;
import com.fenrir.filesorter.model.statement.types.enums.ReturnType;
import com.fenrir.filesorter.model.statement.types.enums.Scope;
import com.fenrir.filesorter.model.rule.Iterator;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.rule.Token;
import com.fenrir.filesorter.model.statement.types.ActionType;
import com.fenrir.filesorter.model.statement.types.PredicateType;
import com.fenrir.filesorter.model.statement.types.ProviderType;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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

    @FXML private Button addInputFieldButton;

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
        inputContainer.getChildren()
                .addListener((ListChangeListener<Node>) change -> onInputContainerChanged());

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
        predicateComboBox.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> onPredicateTypeChanged(newValue));
    }

    private void onPredicateTypeChanged(PredicateType predicateType) {
        if (predicateType != null) {
            if (predicateType.getArgumentNumber() == ArgumentNumber.SINGLE) {
                onSingleArgumentPredicate();
            } else {
                addInputFieldButton.setDisable(false);
            }
        }
    }

    private void onSingleArgumentPredicate() {
        List<Node> inputFields = inputContainer.getChildren();

        // If first condition is met, after altering inputFields, inputContainer's change listener will be triggered.
        // If inputFields size is already equal 1, adding button must be disabled here.
        // If inputFields size is equal 0, adding button should be already enabled, and it should remain enabled.
        if (inputFields.size() > 1) {
            List<Node> inputFieldsToRemove = inputFields.subList(1, inputFields.size());
            inputControllerMediator.removeInputFields(inputFieldsToRemove);
            inputFieldsToRemove.clear();
        } else if (inputFields.size() == 1) {
            addInputFieldButton.setDisable(true);
        }
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
        ReturnType selectedProviderReturnType = providerType.getReturnType();
        List<PredicateType> predicateTypes = PredicateType.getPredicatesForReturnType(selectedProviderReturnType);
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

    private void onInputContainerChanged() {
        List<Node> inputFields = inputContainer.getChildren();
        PredicateType predicateType = predicateComboBox.getSelectionModel().getSelectedItem();
        if (predicateType.getArgumentNumber() == ArgumentNumber.SINGLE) {
            if (inputFields.size() == 0) {
                addInputFieldButton.setDisable(false);
            } else if(inputFields.size() == 1) {
                addInputFieldButton.setDisable(true);
            }
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
        ActionType actionType = actionComboBox.getSelectionModel().getSelectedItem();
        ProviderType providerType = providerComboBox.getSelectionModel().getSelectedItem();
        PredicateType predicateType = predicateComboBox.getSelectionModel().getSelectedItem();
        List<String> argumentList = inputControllerMediator.receiveArguments()
                .stream()
                .filter(arg -> arg != null && !arg.isEmpty())
                .toList();
        String args = String.join(",", argumentList);
        return String.format("%%(%s)%%(%s)%%(%s:%s)",
                actionType.getToken(),
                providerType.getToken(),
                predicateType.getToken(),
                args
        );
    }

    public void setRule(Rule rule) {
        Iterator<Token> iter = rule.getTokenIterator();
        Token action = iter.next();
        Token provider = iter.next();
        Token predicate = iter.next();
        ActionType actionType = ActionType.getType(action.symbol());
        ProviderType providerType = ProviderType.getType(provider.symbol(), Scope.FILTER);
        PredicateType predicateType = PredicateType.getType(predicate.symbol());
        List<String> args = predicate.args();
        actionComboBox.getSelectionModel().select(actionType);
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
