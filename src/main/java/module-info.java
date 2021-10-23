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

    opens com.fenrir.filesorter to javafx.fxml;
    exports com.fenrir.filesorter;
    exports com.fenrir.filesorter.test.parsers;
    opens com.fenrir.filesorter.test.parsers to javafx.fxml;
    exports com.fenrir.filesorter.test;
    opens com.fenrir.filesorter.test to javafx.fxml;
    exports com.fenrir.filesorter.flags;
    opens com.fenrir.filesorter.flags to javafx.fxml;
}