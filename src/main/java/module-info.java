module org.sysimc {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.sysimc to javafx.fxml;
    exports org.sysimc;
    exports org.sysimc.controller;
    opens org.sysimc.model to javafx.base;

    opens org.sysimc.controller to javafx.fxml;
}