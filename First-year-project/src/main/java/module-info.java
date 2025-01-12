module org.example.firstyearproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires org.apache.commons.compress;
    requires org.apache.commons.text;


    opens org.example.firstyearproject to javafx.fxml;
    exports org.example.firstyearproject;
    exports Helpers;
    opens Helpers to javafx.fxml;
}