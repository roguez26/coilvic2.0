module coilvic {
    requires javafx.controls;
    requires javafx.fxml;

    opens coilvic.controllers to javafx.fxml;
    exports coilvic.controllers;
}
