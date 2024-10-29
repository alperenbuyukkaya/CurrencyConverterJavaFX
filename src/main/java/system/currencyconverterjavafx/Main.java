package system.currencyconverterjavafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Main extends Application {
    Scene mainScene;
    Scene loadingScene;
    Button mainButton;
    TextField amountTextField;
    Text conversionFromText;
    Text conversionToText;
    Text resultText;
    Text amountText;
    Text loadingText;
    ComboBox conversionBox;
    ComboBox convertToBox;
    JSONObject exchangeRates;
    DecimalFormat numberFormat = new DecimalFormat("0.00");
    public void buttonMethod(RequestHandler mainRequestHandler) throws JSONException {
        double mainRate;
        double currencyAmount;
        String conversionCurrency = (String) conversionBox.getValue();
        String convertedCurrency = (String) convertToBox.getValue();
        try{
            currencyAmount = Double.parseDouble(amountTextField.getText());
        }catch (NumberFormatException e){
            resultText.setText("Please enter a valid amount.");
            return;
        }
        double rate = Double.parseDouble(exchangeRates.get(convertedCurrency).toString()) / Double.parseDouble(exchangeRates.get(conversionCurrency).toString());
        resultText.setText(currencyAmount + " " + conversionCurrency + " equals " + numberFormat.format(rate * currencyAmount) + " " + convertedCurrency + ".");
    }

    @Override
    public void start(Stage mainStage) throws IOException {

        RequestHandler mainRequestHandler = new RequestHandler();

        mainButton = new Button("Convert");

        mainButton.setOnAction(event -> new Thread(() -> {
            try {
                buttonMethod(mainRequestHandler);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).start());

        conversionFromText = new Text("Enter the currency to be converted from:");
        conversionToText = new Text("Enter the currency to be converted to:");
        resultText = new Text("Result");
        amountText = new Text("Amount: ");
        conversionBox = new ComboBox();
        convertToBox = new ComboBox();
        amountTextField = new TextField();
        loadingText = new Text("Loading");

        VBox loadingLayout  = new VBox(loadingText);
        VBox mainLayout = new VBox(amountText, amountTextField, conversionFromText, conversionBox, conversionToText, convertToBox, mainButton, resultText);
        mainLayout.setPadding(new Insets(10,10,10,10));
        mainLayout.setSpacing(5);

        mainStage.setTitle("Currency Converter");
        mainScene = new Scene(mainLayout, 300, 150);
        loadingScene = new Scene(loadingLayout, 300, 150);
        mainScene.setFill(Color.LIGHTGRAY);

        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/dollarsign.png")));
        mainStage.setScene(loadingScene);
        mainStage.show();
        new Thread(() -> {
            while (true){
                try{
                    exchangeRates = mainRequestHandler.getURL("https://api.frankfurter.app/latest?base=USD");
                    exchangeRates.put("USD", 1);
                    conversionBox.getItems().clear();
                    Platform.runLater(() -> {
                        mainStage.setScene(mainScene);
                    });
                    for (Iterator it = exchangeRates.keys(); it.hasNext(); ) {
                        String i = (String) it.next();
                        conversionBox.getItems().add(i);
                        convertToBox.getItems().add(i);
                    }
                }catch (Exception e){
                    System.out.println(e);
                    loadingText.setText("epic fail, trying again");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        loadingText.setText("loading");
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    continue;
                }
                try {
                    TimeUnit.MINUTES.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        /*while (exchangeRates == null){
            System.out.println(exchangeRates);
        }*/
        System.out.println(exchangeRates);
        //mainStage.setScene(mainScene);
    }

    public static void main(String[] args) {
        launch();
    }
}