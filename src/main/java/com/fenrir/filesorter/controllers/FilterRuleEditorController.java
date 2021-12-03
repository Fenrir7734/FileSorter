package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.model.enums.Scope;
import com.fenrir.filesorter.model.rule.FilterRule;
import com.fenrir.filesorter.model.rule.RuleGroup;
import com.fenrir.filesorter.model.statement.types.ActionType;
import com.fenrir.filesorter.model.statement.types.PredicateType;
import com.fenrir.filesorter.model.statement.types.ProviderType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

import java.util.Arrays;

public class FilterRuleEditorController implements Controller {
    @FXML private TextField ruleTextField;
    @FXML private ComboBox<ActionType> actionComboBox;
    @FXML private ComboBox<ProviderType> providerComboBox;
    @FXML private ComboBox<PredicateType> predicateComboBox;
    @FXML private TextField argumentComboBox;

    private final ObservableList<ActionType> actionTypeItems = FXCollections.observableArrayList();
    private final ObservableList<ProviderType> providerTypeItems = FXCollections.observableArrayList();
    private final ObservableList<PredicateType> predicateTypeItems = FXCollections.observableArrayList();

    private final Callback<ListView<ActionType>, ListCell<ActionType>> actionCallback = createActionCellFactory();
    private final Callback<ListView<ProviderType>, ListCell<ProviderType>> providerCallback = createProviderCellFactory();
    private final Callback<ListView<PredicateType>, ListCell<PredicateType>> predicateCallback = createPredicateCellFactory();

    @FXML
    public void initialize() {
        ControllerMediator.getInstance().registerController(this);

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
                .addListener(((observableValue, type, t1) -> populatePredicateList()));

        actionComboBox.getSelectionModel().select(0);
        providerComboBox.getSelectionModel().select(0);
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
        ProviderType providerType = providerComboBox.getSelectionModel().getSelectedItem();
        PredicateType[] types = PredicateType.values();
        predicateTypeItems.addAll(types);
        predicateComboBox.getSelectionModel().select(0);
    }

    private boolean isProviderInFilterScope(ProviderType type) {
        return Arrays.asList(type.getScope()).contains(Scope.FILTER);
    }

    @FXML
    public void confirm() {
        String expression = ruleTextField.getText();
        FilterRule filterRule = new FilterRule(expression);
        ControllerMediator.getInstance().sendReadyFilterRule(filterRule);
        close();
    }

    @FXML
    public void cancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) ruleTextField.getScene().getWindow();
        stage.close();
    }

    public void receiveRule(FilterRule filterRule) {
        if (filterRule != null) {
            ruleTextField.setText(filterRule.getExpression());
        }
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
