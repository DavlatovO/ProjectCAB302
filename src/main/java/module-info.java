module com.example.projectcab302 {
    requires javafx.controls;
    requires javafx.fxml;


    requires java.sql;
    requires org.json;

    requires com.google.auth;
    requires com.google.auth.oauth2;


    requires proto.google.cloud.translate.v3;
    requires java.net.http;
    requires com.google.gson;

    opens com.example.projectcab302 to javafx.fxml;
    exports com.example.projectcab302.Controller;
    exports com.example.projectcab302;
    opens com.example.projectcab302.Controller to javafx.fxml;
}