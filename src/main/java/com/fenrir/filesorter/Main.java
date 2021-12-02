package com.fenrir.filesorter;

import com.fenrir.filesorter.model.Processor;
import com.fenrir.filesorter.model.file.FileData;
import com.fenrir.filesorter.model.rule.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("Starting");
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("controllers/MainView.fxml"))
        );
        Scene scene = new Scene(root);
        stage.setMinWidth(650);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    private static void test() throws Exception {
        String sortExpression = "%(YYYY)-%(MM)%(/)%(0D)";
        String renameExpression = "%(0D)-%(MM)-%(YYYY)";
        String filterExpression = "%(DAT)%(>:2021-09-04)";
        String source = "/home/fenrir/Documents/Test_environment/screenshot";
        String target = "/home/fenrir/Documents/Test_environment/target/";

        StringRule sortRule = new StringRule(sortExpression);
        StringRule renameRule = new StringRule(renameExpression);
        FilterRule filterRule = new FilterRule(filterExpression);

        RuleGroup group = new RuleGroup();
        group.setRenameRule(renameRule);
        group.setSortRule(sortRule);
        group.addFilterRule(filterRule);
        List<RuleGroup> ruleGroups = List.of(group);
        //Processor processor = new Processor(Path.of(source), Path.of(target), ruleGroups);
        //processor.process();
        //processor.getFileStructure().stream().map(FileData::resolveTargetPath).forEach(System.out::println);
    }
}
