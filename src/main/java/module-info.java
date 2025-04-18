module com.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // For MySQL connectivity

    // Export the root package for Main.java to javafx.graphics
    exports com.library to javafx.graphics;

    // Export and open the controller package for FXML loading
    exports com.library.controller to javafx.fxml;
    opens com.library.controller to javafx.fxml;

    // Export and open the model package for JavaFX bindings
    exports com.library.model;
    opens com.library.model to javafx.base;
}