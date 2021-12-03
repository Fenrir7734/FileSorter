package com.fenrir.filesorter.controllers;

import com.fenrir.filesorter.model.Configuration;
import com.fenrir.filesorter.model.Processor;
import com.fenrir.filesorter.model.Sorter;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.exceptions.SortConfigurationException;
import com.fenrir.filesorter.model.exceptions.TokenFormatException;
import com.fenrir.filesorter.model.log.LogAppender;
import com.fenrir.filesorter.model.rule.FilterRule;
import com.fenrir.filesorter.model.rule.RuleGroup;
import com.fenrir.filesorter.model.rule.StringRule;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML private TabPane tabPane;
    @FXML private TitledPane ruleEditorPane;

    @FXML private TextField targetPathTextField;

    @FXML private ListView<Path> sourcesListView;
    @FXML private ListView<Pair<String, RuleGroup>> ruleGroupListView;
    @FXML private ListView<FilterRule> filterListView;

    @FXML private TextField renameRuleTextField;
    @FXML private TextField sortRuleTextField;

    @FXML private Button removeRuleGroupButton;
    @FXML private Button moveUpRuleGroupButton;
    @FXML private Button moveDownRuleGroupButton;
    @FXML private Button editFilterRuleButton;
    @FXML private Button removeFilterRuleButton;
    @FXML private Button moveUpFilterRuleButton;
    @FXML private Button moveDownFilterRuleButton;

    @FXML private TextArea logTextArea;
    @FXML private ProgressBar progressBar;

    private final Configuration configuration = new Configuration();

    @FXML
    public void initialize() {
        ControllerMediator.getInstance().registerController(this);
        LogAppender.setPrinter(new GUILogPrinter(logTextArea));

        hideRuleEditorPane();
        disableFilterRuleButtons();

        sourcesListView.setItems(configuration.getSourcePaths());
        sourcesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ruleGroupListView.setItems(configuration.getNamedRuleGroups());
        ruleGroupListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> onSelectedRuleGroup(oldValue, newValue));
        ruleGroupListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Pair<String, RuleGroup> rule, boolean empty) {
                super.updateItem(rule, empty);

                if (empty || rule == null) {
                    setText(null);
                } else {
                    setText(rule.getKey());
                }
            }
        });

        filterListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> onSelectedFilterRule(oldValue, newValue));
        filterListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(FilterRule rule, boolean empty) {
                super.updateItem(rule, empty);

                if (empty || rule == null) {
                    setText(null);
                } else {
                    setText(rule.toString());
                }
            }
        });
        initForTest();
    }

    private void initForTest() {
        configuration.setTargetRootDir(Path.of("/home/fenrir/Documents/Test_environment/wall_sorted_test"));
        //configuration.addSourcePaths(List.of(Path.of("/home/fenrir/Documents/Test_environment/wall")));
        configuration.addSourcePaths(List.of(
                Path.of("/home/fenrir/Documents/Test_environment/wall/"),
                Path.of("/home/fenrir/Documents/Test_environment/wall2"),
                Path.of("/home/fenrir/Documents/Test_environment/wall3"),
                Path.of("/home/fenrir/Documents/Test_environment/wallpapers")
        ));
        RuleGroup group = new RuleGroup();
        group.setRenameRule(new StringRule("%(FIN)"));
        group.setSortRule(new StringRule("%(DIM)"));
        configuration.addNamedRuleGroup("1", group);

    }

    private void onSelectedRuleGroup(Pair<String, RuleGroup> oldValue, Pair<String, RuleGroup> newValue) {
        if (oldValue == null && newValue != null) {
            showRuleEditorPane();
        }
        if (oldValue != null && newValue == null) {
            hideRuleEditorPane();
        }
        if (newValue != null) {
            ruleEditorPane.setText(newValue.getKey());
            RuleGroup group = newValue.getValue();
            updateRenameRuleTextFieldContent(group.getRenameRule());
            updateSortRuleTextFieldContent(group.getSortRule());
            filterListView.setItems(group.getFilterRules());
        }
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

    private void onSelectedFilterRule(FilterRule oldValue, FilterRule newValue) {
        if (oldValue == null && newValue != null) {
            enableFilterRuleButtons();
        }
        if (oldValue != null && newValue == null) {
            disableFilterRuleButtons();
        }
    }

    private void disableFilterRuleButtons() {
        editFilterRuleButton.setDisable(true);
        removeFilterRuleButton.setDisable(true);
        moveUpFilterRuleButton.setDisable(true);
        moveDownFilterRuleButton.setDisable(true);
    }

    private void enableFilterRuleButtons() {
        editFilterRuleButton.setDisable(false);
        removeFilterRuleButton.setDisable(false);
        moveUpFilterRuleButton.setDisable(false);
        moveDownFilterRuleButton.setDisable(false);
    }

    @FXML
    public void sort() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    configuration.validate();
                    Processor processor = new Processor(configuration);
                    Sorter sorter = new Sorter(processor);
                    sorter.sort();
                } catch (TokenFormatException e) {
                    logger.error("{} Rule: {} Token: {}", e.getMessage(), e.getRule(), e.getToken());
                } catch (ExpressionFormatException e) {
                    logger.error("{} Rule: {}", e.getMessage(), e.getRule());
                } catch (SortConfigurationException | IOException e) {
                    logger.error("{}", e.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML
    public void choiceTargetDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(tabPane.getScene().getWindow());

        if (selectedDirectory != null) {
            configuration.setTargetRootDir(selectedDirectory.toPath());
            targetPathTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void addFileToSource() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(tabPane.getScene().getWindow());

        if (selectedFile != null) {
            addPathToSource(selectedFile.toArray(new File[0]));
        }
    }

    @FXML
    public void addDirectoryToSource() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(tabPane.getScene().getWindow());

        if (selectedDirectory != null) {
            addPathToSource(selectedDirectory);
        }
    }

    private void addPathToSource(File... files) {
        List<Path> toAdd = Arrays.stream(files)
                .map(File::toPath)
                .collect(Collectors.toList());
        configuration.addSourcePaths(toAdd);
    }

    @FXML
    public void removeFromSource() {
        List<Path> toRemove = sourcesListView.getSelectionModel()
                .getSelectedItems();
        configuration.removeSourcePaths(toRemove);
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
    public void moveRuleGroupUp() {
        moveSelectedItem(ruleGroupListView, configuration.getNamedRuleGroups(), MoveDirection.UP);
    }

    @FXML
    public void moveRuleGroupDown() {
        moveSelectedItem(ruleGroupListView, configuration.getNamedRuleGroups(), MoveDirection.DOWN);
    }

    @FXML
    public void editRenameRule() {
        try {
            loadEditorView("RenameRuleEditorView.fxml");
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
            loadEditorView("SortRuleEditorView.fxml");
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
            loadEditorView("FilterRuleEditorView.fxml");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void editFilterRule() {
        try {
            FilterRule selectedFilterRule = filterListView.getSelectionModel().getSelectedItem();
            if (selectedFilterRule != null) {
                loadEditorView("FilterRuleEditorView.fxml");
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
    public void moveFilterRuleUp() {
        RuleGroup selectedRuleGroup = getSelectedRuleGroup();
        moveSelectedItem(filterListView, selectedRuleGroup.getFilterRules(), MoveDirection.UP);
    }

    @FXML
    public void moveFilterRuleDown() {
        RuleGroup selectedRuleGroup = getSelectedRuleGroup();
        moveSelectedItem(filterListView, selectedRuleGroup.getFilterRules(), MoveDirection.DOWN);
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

    private RuleGroup getSelectedRuleGroup() {
        return ruleGroupListView.getSelectionModel()
                .getSelectedItem()
                .getValue();
    }

    private FilterRule getSelectedFilterRule() {
        return filterListView.getSelectionModel()
                .getSelectedItem();
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
