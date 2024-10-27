module system.currencyconverterjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires okhttp3;
    requires org.json.chargebee;

    opens system.currencyconverterjavafx to javafx.fxml;
    exports system.currencyconverterjavafx;
}