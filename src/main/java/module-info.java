module com.example.planets {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;


    opens com.example.planets to javafx.fxml;
    exports com.example.planets;
}