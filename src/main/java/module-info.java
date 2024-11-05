module system.organizedcurrencyconverter {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires okhttp3;
    requires org.json.chargebee;

    opens system.organizedcurrencyconverter to javafx.fxml;
    exports system.organizedcurrencyconverter;
}