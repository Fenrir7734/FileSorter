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
    exports com.fenrir.filesorter.model;
    opens com.fenrir.filesorter.model to javafx.fxml;
    exports com.fenrir.filesorter.model.parsers;
    opens com.fenrir.filesorter.model.parsers to javafx.fxml;
    exports com.fenrir.filesorter.model.utils;
    opens com.fenrir.filesorter.model.utils to javafx.fxml;
    exports com.fenrir.filesorter.model.file;
    opens com.fenrir.filesorter.model.file to javafx.fxml;
    exports com.fenrir.filesorter.model.enums;
    opens com.fenrir.filesorter.model.enums to javafx.fxml;
}