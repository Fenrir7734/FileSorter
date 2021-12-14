package com.fenrir.filesorter.controllers.editor.rename;

import com.fenrir.filesorter.model.enums.Category;
import com.fenrir.filesorter.model.statement.types.ProviderType;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenameRuleBuilderController {
    @FXML private TreeView<ProviderType> tokenTreeView;
    @FXML private ListView<ProviderType> selectedTokensListView;
    @FXML private TreeItem<ProviderType> rootItem;

    @FXML
    public void initialize() {

    }

    public void initTokenTreeView() {
        ProviderType[] providerTypes = ProviderType.values();
        Map<Category, List<ProviderType>> providerTypesByGroup = new HashMap<>();
        for (ProviderType type: providerTypes) {
            providerTypesByGroup.computeIfAbsent(type.getGroup(), k -> new ArrayList<>()).add(type);
        }
        List<ProviderType> providerTypesWithoutGroup = providerTypesByGroup.getOrDefault(Category.NONE, new ArrayList<>());
        providerTypesByGroup.remove(Category.NONE);
    }
}
