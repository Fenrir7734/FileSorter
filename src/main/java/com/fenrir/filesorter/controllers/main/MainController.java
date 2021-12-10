package com.fenrir.filesorter.controllers.main;

import com.fenrir.filesorter.model.Configuration;
import javafx.fxml.FXML;

public class MainController {
    @FXML private SortTabController sortTabController;
    @FXML private SourceTabController sourceTabController;
    @FXML private RuleTabController ruleTabController;

    private final Configuration configuration = new Configuration();

    @FXML
    public void initialize() {
        sortTabController.setConfiguration(configuration);
        sourceTabController.setConfiguration(configuration);
        ruleTabController.setConfiguration(configuration);
    }
}
