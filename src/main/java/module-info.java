module com.fenrir.filesorter {
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
    requires logback.core;
    requires logback.classic;
    requires org.slf4j;
    requires org.apache.commons.io;
    requires org.apache.commons.imaging;
    requires tika.core;

    opens com.fenrir.filesorter to javafx.fxml;
    exports com.fenrir.filesorter;
    exports com.fenrir.filesorter.controllers;
    opens com.fenrir.filesorter.controllers to javafx.fxml;
    exports com.fenrir.filesorter.controllers.editor.filter.input;
    opens com.fenrir.filesorter.controllers.editor.filter.input to javafx.fxml;
    exports com.fenrir.filesorter.controllers.save;
    opens com.fenrir.filesorter.controllers.save to javafx.fxml;
    exports com.fenrir.filesorter.model;
    opens com.fenrir.filesorter.model to javafx.fxml;
    exports com.fenrir.filesorter.model.parsers;
    opens com.fenrir.filesorter.model.parsers to javafx.fxml;
    exports com.fenrir.filesorter.model.utils;
    opens com.fenrir.filesorter.model.utils to javafx.fxml;
    exports com.fenrir.filesorter.model.file;
    opens com.fenrir.filesorter.model.file to javafx.fxml;
    exports com.fenrir.filesorter.model.statement.types.enums;
    opens com.fenrir.filesorter.model.statement.types.enums to javafx.fxml;
    exports com.fenrir.filesorter.model.file.utils;
    opens com.fenrir.filesorter.model.file.utils to javafx.fxml;
    exports com.fenrir.filesorter.model.rule;
    opens com.fenrir.filesorter.model.rule to javafx.fxml;
    exports com.fenrir.filesorter.model.exceptions;
    opens com.fenrir.filesorter.model.exceptions to javafx.fxml;
    exports com.fenrir.filesorter.model.log;
    exports com.fenrir.filesorter.controllers.editor.filter;
    opens com.fenrir.filesorter.controllers.editor.filter to javafx.fxml;
    exports com.fenrir.filesorter.controllers.editor;
    opens com.fenrir.filesorter.controllers.editor to javafx.fxml;
    exports com.fenrir.filesorter.controllers.main;
    opens com.fenrir.filesorter.controllers.main to javafx.fxml;
    exports com.fenrir.filesorter.controllers.confirm;
    opens com.fenrir.filesorter.controllers.confirm to javafx.fxml;
    exports com.fenrir.filesorter.controllers.editor.string.rename;
    opens com.fenrir.filesorter.controllers.editor.string.rename to javafx.fxml;
    exports com.fenrir.filesorter.controllers.editor.string.sort;
    opens com.fenrir.filesorter.controllers.editor.string.sort to javafx.fxml;
    exports com.fenrir.filesorter.controllers.editor.string;
    opens com.fenrir.filesorter.controllers.editor.string to javafx.fxml;
    exports com.fenrir.filesorter.controllers.editor.string.input;
    opens com.fenrir.filesorter.controllers.editor.string.input to javafx.fxml;
    exports com.fenrir.filesorter.model.statement.types;
    opens com.fenrir.filesorter.model.statement.types to javafx.fxml;
}