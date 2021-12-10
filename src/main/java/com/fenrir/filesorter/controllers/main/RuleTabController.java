package com.fenrir.filesorter.controllers.main;


import com.fenrir.filesorter.controllers.ControllerMediator;
import com.fenrir.filesorter.model.Configuration;
import com.fenrir.filesorter.model.rule.FilterRule;
import com.fenrir.filesorter.model.rule.RuleGroup;
import com.fenrir.filesorter.model.rule.StringRule;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class RuleTabController {
    @FXML private TitledPane ruleEditorPane;

    @FXML private ListView<Pair<String, RuleGroup>> ruleGroupListView;
    @FXML private ListView<FilterRule> filterListView;

    @FXML private Button removeRuleGroupButton;
    @FXML private Button moveUpRuleGroupButton;
    @FXML private Button moveDownRuleGroupButton;
    @FXML private Button editFilterRuleButton;
    @FXML private Button removeFilterRuleButton;
    @FXML private Button moveUpFilterRuleButton;
    @FXML private Button moveDownFilterRuleButton;

    @FXML private TextField renameRuleTextField;
    @FXML private TextField sortRuleTextField;

    private Configuration configuration;

    @FXML
    public void initialize() {
        ControllerMediator.getInstance().registerRuleTabController(this);

        hideRuleEditorPane();
        disableFilterRuleButtons();

        initRuleGroupListView();
        initFilterListView();
    }

    private void initRuleGroupListView() {
        ruleGroupListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> onSelectedRuleGroup(oldValue, newValue));
        ruleGroupListView.setCellFactory(v -> createRuleGroupCellFactory());
    }

    private void initFilterListView() {
        filterListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> onSelectedFilterRule(oldValue, newValue));
        filterListView.setCellFactory(v -> createFilterCellFactory());
    }

    private void onSelectedRuleGroup(Pair<String, RuleGroup> oldValue, Pair<String, RuleGroup> newValue) {
        if (oldValue == null && newValue != null) {
            showRuleEditorPane();
        }
        if (oldValue != null && newValue == null) {
            hideRuleEditorPane();
        }
        if (newValue != null) {
            updateRuleEditorPane(newValue);
        }
    }

    private void showRuleEditorPane() {
        ruleEditorPane.setVisible(true);
        removeRuleGroupButton.setDisable(false);
        moveUpRuleGroupButton.setDisable(false);
        moveDownRuleGroupButton.setDisable(false);
    }

    private void hideRuleEditorPane() {
        ruleEditorPane.setVisible(false);
        removeRuleGroupButton.setDisable(true);
        moveUpRuleGroupButton.setDisable(true);
        moveDownRuleGroupButton.setDisable(true);
    }

    private void updateRuleEditorPane(Pair<String, RuleGroup> value) {
        ruleEditorPane.setText(value.getKey());
        RuleGroup group = value.getValue();
        updateRenameRuleTextFieldContent(group.getRenameRule());
        updateSortRuleTextFieldContent(group.getSortRule());
        filterListView.setItems(group.getFilterRules());
    }

    private void updateRenameRuleTextFieldContent(StringRule rule) {
        if (rule != null) {
            renameRuleTextField.setText(rule.getExpression());
        } else {
            renameRuleTextField.setText(null);
        }
    }

    private void updateSortRuleTextFieldContent(StringRule rule) {
        if (rule != null) {
            sortRuleTextField.setText(rule.getExpression());
        } else {
            sortRuleTextField.setText(null);
        }
    }

    private void onSelectedFilterRule(FilterRule oldValue, FilterRule newValue) {
        if (oldValue == null && newValue != null) {
            enableFilterRuleButtons();
        }
        if (oldValue != null && newValue == null) {
            disableFilterRuleButtons();
        }
    }

    private void enableFilterRuleButtons() {
        editFilterRuleButton.setDisable(false);
        removeFilterRuleButton.setDisable(false);
        moveUpFilterRuleButton.setDisable(false);
        moveDownFilterRuleButton.setDisable(false);
    }

    private void disableFilterRuleButtons() {
        editFilterRuleButton.setDisable(true);
        removeFilterRuleButton.setDisable(true);
        moveUpFilterRuleButton.setDisable(true);
        moveDownFilterRuleButton.setDisable(true);
    }

    @FXML
    public void addRuleGroup() {
        String name = generateUniqName();
        RuleGroup group = new RuleGroup();
        configuration.addNamedRuleGroup(name, group);
    }

    private String generateUniqName() {
        List<String> ruleGroupNames = configuration.getRuleGroupsNames();
        String ruleGroupName;

        int i = 1;
        do {
            ruleGroupName = "Rule Group " + (ruleGroupNames.size() + i++);
        } while (ruleGroupNames.contains(ruleGroupName));

        return ruleGroupName;
    }

    @FXML
    public void removeRuleGroup() {
        Pair<String, RuleGroup> namedRuleGroup = ruleGroupListView.getSelectionModel()
                .getSelectedItem();
        configuration.removeRuleGroup(namedRuleGroup);
    }

    @FXML
    public void moveUpRuleGroup() {
        moveSelectedItem(
                ruleGroupListView,
                configuration.getNamedRuleGroups(),
                MoveDirection.UP
        );
    }

    @FXML
    public void moveDownRuleGroup() {
        moveSelectedItem(
                ruleGroupListView,
                configuration.getNamedRuleGroups(),
                MoveDirection.DOWN
        );
    }

    @FXML
    public void editRenameRule() {
        try {
            loadEditorView("/com/fenrir/filesorter/controllers/editor/rename/RenameRuleEditorView.fxml");
            RuleGroup selectedRuleGroup = getSelectedRuleGroup();
            ControllerMediator.getInstance()
                    .sendRenameRuleToEdit(selectedRuleGroup.getRenameRule());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void editSortRule() {
        try {
            loadEditorView("/com/fenrir/filesorter/controllers/editor/sort/SortRuleEditorView.fxml");
            RuleGroup selectedRuleGroup = getSelectedRuleGroup();
            ControllerMediator.getInstance()
                    .sendSortRuleToEdit(selectedRuleGroup.getSortRule());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void addFilterRule() {
        try {
            loadEditorView("/com/fenrir/filesorter/controllers/editor/filter/FilterRuleEditorView.fxml");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void editFilterRule() {
        try {
            FilterRule selectedFilterRule = getSelectedFilterRule();
            if (selectedFilterRule != null) {
                loadEditorView("/com/fenrir/filesorter/controllers/editor/filter/FilterRuleEditorView.fxml");
                ControllerMediator.getInstance().sendFilterRuleToEdit(selectedFilterRule);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadEditorView(String name) throws IOException {
        Parent parent = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource(name))
        );
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void removeFilterRule() {
        RuleGroup selectedRuleGroup = getSelectedRuleGroup();
        FilterRule selectedFilterRule = getSelectedFilterRule();
        selectedRuleGroup.removeFilterRule(selectedFilterRule);
    }

    @FXML
    public void moveUpFilterRule() {
        RuleGroup selectedRuleGroup = getSelectedRuleGroup();
        moveSelectedItem(
                filterListView,
                selectedRuleGroup.getFilterRules(),
                MoveDirection.UP
        );
    }

    @FXML
    public void moveDownFilterRule() {
        RuleGroup selectedRuleGroup = getSelectedRuleGroup();
        moveSelectedItem(
                filterListView,
                selectedRuleGroup.getFilterRules(),
                MoveDirection.DOWN
        );
    }

    public void receiveRenameRule(StringRule rule) {
        RuleGroup selectedRuleGroup = getSelectedRuleGroup();
        selectedRuleGroup.setRenameRule(rule);
        renameRuleTextField.setText(rule.getExpression());
    }

    public void receiveSortRule(StringRule rule) {
        RuleGroup selectedRuleGroup = getSelectedRuleGroup();
        selectedRuleGroup.setSortRule(rule);
        sortRuleTextField.setText(rule.getExpression());
    }

    public void receiveFilterRule(FilterRule rule) {
        RuleGroup selectedRuleGroup = getSelectedRuleGroup();
        FilterRule selectedFilterRule = getSelectedFilterRule();
        List<FilterRule> filterRules = selectedRuleGroup.getFilterRules();
        int indexOfOldFilterRule = filterRules.indexOf(selectedFilterRule);

        if (indexOfOldFilterRule == -1) {
            filterRules.add(rule);
        } else {
            filterRules.set(indexOfOldFilterRule, rule);
        }
    }

    private <T> void moveSelectedItem(ListView<T> listView, ObservableList<T> itemList, MoveDirection direction) {
        T toMove = listView.getSelectionModel().getSelectedItem();
        int indexOfItemToMove = itemList.indexOf(toMove);
        int indexOfItemAfterMoving = direction.move(indexOfItemToMove, itemList.size() - 1);

        if (indexOfItemToMove != indexOfItemAfterMoving) {
            Collections.swap(itemList, indexOfItemToMove, indexOfItemAfterMoving);
            listView.getSelectionModel().clearSelection(indexOfItemToMove);
            listView.getSelectionModel().select(indexOfItemAfterMoving);
        }
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        ruleGroupListView.setItems(configuration.getNamedRuleGroups());
    }

    private RuleGroup getSelectedRuleGroup() {
        return ruleGroupListView.getSelectionModel()
                .getSelectedItem()
                .getValue();
    }

    private FilterRule getSelectedFilterRule() {
        return filterListView.getSelectionModel()
                .getSelectedItem();
    }

    private ListCell<Pair<String, RuleGroup>> createRuleGroupCellFactory() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Pair<String, RuleGroup> rule, boolean empty) {
                super.updateItem(rule, empty);

                if (empty || rule == null) {
                    setText(null);
                } else {
                    setText(rule.getKey());
                }
            }
        };
    }

    private ListCell<FilterRule> createFilterCellFactory() {
        return new ListCell<>() {
            @Override
            protected void updateItem(FilterRule rule, boolean empty) {
                super.updateItem(rule, empty);

                if (empty || rule == null) {
                    setText(null);
                } else {
                    setText(rule.toString());
                }
            }
        };
    }

    enum MoveDirection {
        UP {
            public int move(int index, int upperBound) {
                return index > 0 ? index - 1 : index;
            }
        },
        DOWN {
            public int move(int index, int upperBound) {
                return index < upperBound ? index + 1 : index;
            }
        };

        public abstract int move(int index, int upperBound);
    }
}
