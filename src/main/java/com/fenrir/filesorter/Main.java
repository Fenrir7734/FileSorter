package com.fenrir.filesorter;

import com.fenrir.filesorter.model.Configuration;
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
                Objects.requireNonNull(getClass().getResource("controllers/main/MainView.fxml"))
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
}
