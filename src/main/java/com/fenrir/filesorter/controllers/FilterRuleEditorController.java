package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.controllers.input.ArgumentInputController;
import com.fenrir.filesorter.controllers.input.ArgumentInputFactory;
import com.fenrir.filesorter.model.enums.Scope;
import com.fenrir.filesorter.model.enums.Category;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.parsers.FilterRuleParser;
import com.fenrir.filesorter.model.rule.FilterRule;
import com.fenrir.filesorter.model.rule.Iterator;
import com.fenrir.filesorter.model.rule.RuleElement;
import com.fenrir.filesorter.model.statement.types.ActionType;
import com.fenrir.filesorter.model.statement.types.PredicateType;
import com.fenrir.filesorter.model.statement.types.ProviderType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FilterRuleEditorController implements Controller {
    private final Logger logger = LoggerFactory.getLogger(FilterRuleEditorController.class);

    @FXML private TabPane ruleEditorTabPane;
    @FXML private Tab ruleBuilderTab;
    @FXML private Tab expressionEditorTab;
    @FXML private VBox inputContainer;
    @FXML private CheckBox editExpressionCheckBox;
    @FXML private ComboBox<ActionType> actionComboBox;
    @FXML private ComboBox<ProviderType> providerComboBox;
    @FXML private ComboBox<PredicateType> predicateComboBox;
    @FXML private TextField expressionTextField;

    private final ObservableList<ActionType> actionTypeItems = FXCollections.observableArrayList();
    private final ObservableList<ProviderType> providerTypeItems = FXCollections.observableArrayList();
    private final ObservableList<PredicateType> predicateTypeItems = FXCollections.observableArrayList();

    private final Callback<ListView<ActionType>, ListCell<ActionType>> actionCallback = createActionCellFactory();
    private final Callback<ListView<ProviderType>, ListCell<ProviderType>> providerCallback = createProviderCellFactory();
    private final Callback<ListView<PredicateType>, ListCell<PredicateType>> predicateCallback = createPredicateCellFactory();

    private InputControllerMediator inputControllerMediator = new InputControllerMediator();

    @FXML
    public void initialize() {
        ControllerMediator.getInstance().registerController(this);
        inputControllerMediator.registerFilterRuleEditorController(this);

        actionTypeItems.addAll(ActionType.values());
        actionComboBox.setItems(actionTypeItems);
        actionComboBox.setButtonCell(actionCallback.call(null));
        actionComboBox.setCellFactory(actionCallback);

        predicateComboBox.setItems(predicateTypeItems);
        predicateComboBox.setButtonCell(predicateCallback.call(null));
        predicateComboBox.setCellFactory(predicateCallback);

        populateProviderList();
        providerComboBox.setItems(providerTypeItems);
        providerComboBox.setButtonCell(providerCallback.call(null));
        providerComboBox.setCellFactory(providerCallback);
        providerComboBox.getSelectionModel()
                .selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    populatePredicateList();
                    changeInputFields();
                }));

        ruleEditorTabPane.getSelectionModel()
                .selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> onTabChange(newValue)));
        editExpressionCheckBox.selectedProperty()
                .addListener((observable, oldValue, newValue) -> onEditExpression(newValue));

        actionComboBox.getSelectionModel().select(0);
        providerComboBox.getSelectionModel().select(0);
        buildExpression();
        changeInputFields();
    }

    private void onTabChange(Tab newValue) {
        if (newValue.equals(expressionEditorTab)) {
            expressionTextField.setText(buildExpression());
        }
    }

    private void onEditExpression(boolean newValue) {
        if (newValue) {
            expressionTextField.setDisable(false);
            ruleBuilderTab.setDisable(true);
        } else {
            String expression = buildExpression();
            expressionTextField.setText(expression);
            expressionTextField.setDisable(true);
            ruleBuilderTab.setDisable(false);
        }
    }

    private void populateProviderList() {
        providerTypeItems.clear();
        ProviderType[] types = ProviderType.values();
        for (ProviderType type: types) {
            if (isProviderInFilterScope(type)) {
                providerTypeItems.add(type);
            }
        }
    }

    private void populatePredicateList() {
        predicateTypeItems.clear();
        ProviderType selectedProvider = providerComboBox.getSelectionModel().getSelectedItem();
        Category typeOfSelectedProvider = selectedProvider.getCategory();
        List<PredicateType> predicateTypes = PredicateType.getPredicatesForCategory(typeOfSelectedProvider);
        predicateTypeItems.addAll(predicateTypes);
        predicateComboBox.getSelectionModel().select(0);
    }

    private void changeInputFields() {
        try {
            inputContainer.getChildren().clear();
            inputControllerMediator = new InputControllerMediator();
            ProviderType selectedProvider = providerComboBox.getSelectionModel().getSelectedItem();
            ArgumentInputController argumentInputController = ArgumentInputFactory.getInputContainer(selectedProvider);
            argumentInputController.setInputControllerMediator(inputControllerMediator);
            inputContainer.getChildren().add(argumentInputController.getInputContainer());
        } catch (IOException e) {
            logger.error("Error during changing input controller: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void addInputField() {
        try {
            ProviderType selectedProvider = providerComboBox.getSelectionModel().getSelectedItem();
            ArgumentInputController argumentInputController = ArgumentInputFactory.getInputContainer(selectedProvider);
            argumentInputController.setInputControllerMediator(inputControllerMediator);
            inputContainer.getChildren().add(argumentInputController.getInputContainer());
        } catch (IOException e) {
            logger.error("Error during adding input controller: {}", e.getMessage());
        }
    }

    public void deleteInputField(HBox containerToDelete) {
        inputContainer.getChildren().remove(containerToDelete);
    }

    private boolean isProviderInFilterScope(ProviderType type) {
        return Arrays.asList(type.getScope()).contains(Scope.FILTER);
    }

    @FXML
    public void confirm() {
        try {
            String expression = getExpression();
            System.out.println(expression);
            FilterRule filterRule = new FilterRule(expression);
            FilterRuleParser parser = new FilterRuleParser();
            parser.validateRule(filterRule);
            ControllerMediator.getInstance().sendReadyFilterRule(filterRule);
            close();
        } catch (ExpressionFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }

    private String getExpression() {
        if (editExpressionCheckBox.isSelected()) {
            return expressionTextField.getText();
        } else {
            return buildExpression();
        }
    }

    private String buildExpression() {
        ProviderType providerType = providerComboBox.getSelectionModel().getSelectedItem();
        PredicateType predicateType = predicateComboBox.getSelectionModel().getSelectedItem();
        List<String> listOfArguments = inputControllerMediator.receiveArguments();
        String args = String.join(",", listOfArguments);
        return String.format("%s(%s)%s(%s:%s)", "%", providerType.getToken(), "%", predicateType.getToken(), args);
    }

    @FXML
    public void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) actionComboBox.getScene().getWindow();
        stage.close();
    }

    public void receiveRule(FilterRule filterRule) {

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
