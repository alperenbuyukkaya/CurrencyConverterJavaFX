package system.currencyconverterjavafx;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Main extends Application {


    InterfaceManager mainUI = new InterfaceManager();
    ControllerHandler mainCH = new ControllerHandler(mainUI);
    JSONObject exchangeRates;

    /*public void buttonMethod(RequestHandler mainRequestHandler) throws JSONException {
        double mainRate;
        double currencyAmount;
        String conversionCurrency = (String) referenceFields.;
        String convertedCurrency = (String) convertToBox.getValue();
        try{
            currencyAmount = Double.parseDouble(amountTextField.getText());
        }catch (NumberFormatException e){
            resultText.setText("Please enter a valid amount.");
            return;
        }
        double rate = Double.parseDouble(exchangeRates.get(convertedCurrency).toString()) / Double.parseDouble(exchangeRates.get(conversionCurrency).toString());
        resultText.setText(currencyAmount + " " + conversionCurrency + " equals " + numberFormat.format(rate * currencyAmount) + " " + convertedCurrency + ".");
    }*/

    @Override
    public void start(Stage mainStage) throws IOException {


        RequestHandler mainRequestHandler = new RequestHandler();



        mainUI.mainButton.setOnAction(event -> new Thread(() -> {
            try {
                mainCH.buttonMethod(exchangeRates);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).start());


        mainStage.setTitle("Currency Converter");

        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/dollarsign.png")));
        mainStage.setScene(mainUI.mainScene);
        mainStage.show();
        Thread thread = new Thread(() -> {
            while (true){
                exchangeRates = mainCH.updateValues(mainRequestHandler, mainStage);
                try {
                    TimeUnit.MINUTES.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        /*new Thread(() -> {
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
        }).start();*/
        /*while (exchangeRates == null){
            System.out.println(exchangeRates);
        }*/
        //System.out.println(exchangeRates);
        //mainStage.setScene(mainScene);
    }

    public static void main(String[] args) {
        launch();
    }
}