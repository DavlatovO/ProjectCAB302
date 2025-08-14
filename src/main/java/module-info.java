module com.example.projectcab202 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projectcab202 to javafx.fxml;
    exports com.example.projectcab202;
}