package system.currencyconverterjavafx;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class HelloApplication extends Application {

    Button mainButton;
    TextField conversionFromTextField;
    TextField conversionToTextField;
    Text conversionFromText;
    Text conversionToText;
    Text resultText;
    JSONObject exchangeRates;
    DecimalFormat numberFormat = new DecimalFormat("0SD.00");
    public void buttonMethod(RequestHandler mainRequestHandler) throws JSONException {
        double mainRate;
        String conversionCurrency = conversionFromTextField.getText().toUpperCase();
        String convertedCurrency = conversionToTextField.getText().toUpperCase();
        double rate = Double.parseDouble(exchangeRates.get(conversionCurrency).toString()) / Double.parseDouble(exchangeRates.get(convertedCurrency).toString());
        resultText.setText("1 " + conversionCurrency + " equals " + numberFormat.format(rate) + " " + convertedCurrency + ".");
    }

    @Override
    public void start(Stage mainStage) throws IOException {

        RequestHandler mainRequestHandler = new RequestHandler();

        mainButton = new Button("Convert");
        new Thread(() -> {
            while (true){
                try{
                    exchangeRates = mainRequestHandler.getURL("https://api.frankfurter.app/latest?base=USD");
                    exchangeRates.put("USD", 1);
                }catch (Exception e){
                    resultText.setText("epic fail");
                    continue;
                }
                try {
                    TimeUnit.MINUTES.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


        mainButton.setOnAction(event -> new Thread(() -> {
            try {
                buttonMethod(mainRequestHandler);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).start());

        conversionFromTextField = new TextField();
        conversionToTextField = new TextField();

        conversionFromText = new Text("Enter the currency to be converted from:");
        conversionToText = new Text("Enter the currency to be converted to:");
        resultText = new Text("Result");

        VBox mainLayout = new VBox(conversionFromText, conversionFromTextField, conversionToText, conversionToTextField, resultText, mainButton);
        mainLayout.setPadding(new Insets(10,10,10,10));
        mainLayout.setSpacing(5);

        mainStage.setTitle("Currency Converter");
        Scene scene = new Scene(mainLayout, 300, 150);
        scene.setFill(Color.LIGHTGRAY);

        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/dollarsign.png")));
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}