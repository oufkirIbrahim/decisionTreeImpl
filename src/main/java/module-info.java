module com.fsoteam.ml.decisiontreeimpl {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires de.jensd.fx.glyphs.fontawesome;
    
    exports com.fsoteam.ml.decisiontreeimpl;
    opens com.fsoteam.ml.decisiontreeimpl to javafx.fxml;
    exports com.fsoteam.ml.decisiontreeimpl.ui;
    opens com.fsoteam.ml.decisiontreeimpl.ui to javafx.fxml;
    opens com.fsoteam.ml.decisiontreeimpl.model to javafx.base;
}