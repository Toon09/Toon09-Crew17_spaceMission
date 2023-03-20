module com.example.planets {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.planets to javafx.fxml;
    exports com.example.planets;
}