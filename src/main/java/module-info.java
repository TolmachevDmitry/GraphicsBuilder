module com.tolmic.puzzle15 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.tolmic.graphicsbuilder to javafx.fxml;
    exports com.tolmic.graphicsbuilder;
}