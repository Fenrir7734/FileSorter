package com.fenrir.filesorter.controllers.main;


import com.fenrir.filesorter.controllers.ControllerMediator;
import com.fenrir.filesorter.model.Configuration;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.rule.RuleGroup;
import com.fenrir.filesorter.model.rule.SavedRuleGroup;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.json.JSONException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class RuleTabController {
    @FXML private TitledPane ruleEditorPane;

    @FXML private ListView<Pair<String, RuleGroup>> ruleGroupListView;
    @FXML private ListView<Rule> filterListView;

    @FXML private Button removeRuleGroupButton;
    @FXML private Button moveUpRuleGroupButton;
    @FXML private Button moveDownRuleGroupButton;
    @FXML private Button saveRuleGroupButton;
    @FXML private Button loadRuleGroupButton;
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
        saveRuleGroupButton.setDisable(false);
    }

    private void hideRuleEditorPane() {
        ruleEditorPane.setVisible(false);
        removeRuleGroupButton.setDisable(true);
        moveUpRuleGroupButton.setDisable(true);
        moveDownRuleGroupButton.setDisable(true);
        saveRuleGroupButton.setDisable(true);
    }

    private void updateRuleEditorPane(Pair<String, RuleGroup> value) {
        ruleEditorPane.setText(value.getKey());
        RuleGroup group = value.getValue();
        updateRenameRuleTextFieldContent(group.getRenameRule());
        updateSortRuleTextFieldContent(group.getSortRule());
        filterListView.setItems(group.getFilterRules());
    }

    private void updateRenameRuleTextFieldContent(Rule rule) {
        if (rule != null) {
            renameRuleTextField.setText(rule.getExpression());
        } else {
            renameRuleTextField.setText(null);
        }
    }

    private void updateSortRuleTextFieldContent(Rule rule) {
        if (rule != null) {
            sortRuleTextField.setText(rule.getExpression());
        } else {
            sortRuleTextField.setText(null);
        }
    }

    private void onSelectedFilterRule(Rule oldValue, Rule newValue) {
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
        String name = generateUniqName("Rule Group");
        RuleGroup group = new RuleGroup();
        configuration.addNamedRuleGroup(name, group);
    }

    private String generateUniqName(String name) {
        List<String> ruleGroupNames = configuration.getRuleGroupsNames();
        String ruleGroupName = name;

        int i = 1;
        while (ruleGroupNames.contains(ruleGroupName)) {
            ruleGroupName = name + " (" + i++ + ")";
        }

        return ruleGroupName;
    }

    @FXML
    public void removeRuleGroup() {
        Pair<String, RuleGroup> namedRuleGroup = ruleGroupListView.getSelectionModel()
                .getSelectedItem();
        configuration.removeRuleGroup(namedRuleGroup);
    }

    @FXML
    public void saveRuleGroup() {
        try {
            loadView("/com/fenrir/filesorter/controllers/save/SaveRuleGroupView.fxml");
            String selectedRuleGroupName = getSelectedRuleGroupName();
            RuleGroup selectedRuleGroup = getSelectedRuleGroup();
            ControllerMediator.getInstance()
                    .sendRuleGroupForSave(selectedRuleGroupName, selectedRuleGroup);
        } catch (IOException | JSONException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load saved rule groups. " + e.getMessage());
            alert.showAndWait();
            boolean generate = askIfGenerateNewFile();
            if (generate) {
                generateNewRuleGroupFile();
            }
        }
    }

    @FXML
    public void loadRuleGroup() {
        try {
            loadView("/com/fenrir/filesorter/controllers/save/LoadRuleGroupView.fxml");
        } catch (IOException | JSONException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load saved rule groups. " + e.getMessage());
            alert.showAndWait();
            boolean generate = askIfGenerateNewFile();
            if (generate) {
                generateNewRuleGroupFile();
            }
        }
    }

    private boolean askIfGenerateNewFile() {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Generate valid file? All saved data will be lost.",
                ButtonType.YES,
                ButtonType.CANCEL
        );
        alert.showAndWait();
        return alert.getResult() != null && alert.getResult() == ButtonType.YES;
    }

    private void generateNewRuleGroupFile() {
        try {
            SavedRuleGroup.generateRuleGroupFile();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create new file.");
            alert.showAndWait();
        }
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
            loadView("/com/fenrir/filesorter/controllers/editor/string/rename/RenameRuleEditorView.fxml");
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
            loadView("/com/fenrir/filesorter/controllers/editor/string/sort/SortRuleEditorView.fxml");
            RuleGroup selectedRuleGroup = getSelectedRuleGroup();
            ControllerMediator.getInstance()
                    .sendSortRuleToEdit(selectedRuleGroup.getSortRule());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void addFilterRule() {
        try {
            filterListView.getSelectionModel().clearSelection();
            loadView("/com/fenrir/filesorter/controllers/editor/filter/FilterRuleEditorView.fxml");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void editFilterRule() {
        try {
            Rule selectedFilterRule = getSelectedFilterRule();
            if (selectedFilterRule != null) {
                loadView("/com/fenrir/filesorter/controllers/editor/filter/FilterRuleEditorView.fxml");
                ControllerMediator.getInstance().sendFilterRuleToEdit(selectedFilterRule);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadView(String name) throws IOException {
        Parent parent = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource(name))
        );
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void removeFilterRule() {
        RuleGroup selectedRuleGroup = getSelectedRuleGroup();
        Rule selectedFilterRule = getSelectedFilterRule();
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

    public void receiveRenameRule(Rule rule) {
        RuleGroup selectedRuleGroup = getSelectedRuleGroup();
        selectedRuleGroup.setRenameRule(rule);
        renameRuleTextField.setText(rule.getExpression());
    }

    public void receiveSortRule(Rule rule) {
        RuleGroup selectedRuleGroup = getSelectedRuleGroup();
        selectedRuleGroup.setSortRule(rule);
        sortRuleTextField.setText(rule.getExpression());
    }

    public void receiveFilterRule(Rule rule) {
        RuleGroup selectedRuleGroup = getSelectedRuleGroup();
        Rule selectedFilterRule = getSelectedFilterRule();
        List<Rule> filterRules = selectedRuleGroup.getFilterRules();
        int indexOfOldFilterRule = filterRules.indexOf(selectedFilterRule);

        if (indexOfOldFilterRule == -1) {
            filterRules.add(rule);
        } else {
            filterRules.set(indexOfOldFilterRule, rule);
        }
    }

    public void receiveRuleGroup(String name, RuleGroup ruleGroup) {
        name = generateUniqName(name);
        configuration.addNamedRuleGroup(name, ruleGroup);
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

    private String getSelectedRuleGroupName() {
        return ruleGroupListView.getSelectionModel()
                .getSelectedItem()
                .getKey();
    }

    private Rule getSelectedFilterRule() {
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

    private ListCell<Rule> createFilterCellFactory() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Rule rule, boolean empty) {
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
