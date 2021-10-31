module com.example.filesortere {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.json;
    requires java.desktop;

    opens com.fenrir.filesorter to javafx.fxml;
    exports com.fenrir.filesorter;
    opens com.fenrir.filesorter.test.parsers to javafx.fxml;
    exports com.fenrir.filesorter.test;
    opens com.fenrir.filesorter.test to javafx.fxml;
    exports com.fenrir.filesorter.model.tokens;
    opens com.fenrir.filesorter.model.tokens to javafx.fxml;
    exports com.fenrir.filesorter.model;
    opens com.fenrir.filesorter.model to javafx.fxml;
}