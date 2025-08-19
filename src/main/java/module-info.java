module com.example.projectcab302 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projectcab302 to javafx.fxml;
    exports com.example.projectcab302;
}