module coilvic {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires java.sql;

    opens coilvic.controllers to javafx.fxml;
    exports coilvic.controllers;
    exports coilvic.objects;
}
