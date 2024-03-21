module coilvic {
    requires javafx.controls;
    requires javafx.fxml;

    opens coilvic to javafx.fxml;
    exports coilvic;
}
