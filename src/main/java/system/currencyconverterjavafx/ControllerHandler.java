package system.currencyconverterjavafx;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ControllerHandler extends Thread {

    InterfaceManager mainUI;
    public ControllerHandler(InterfaceManager manager) {
        this.mainUI = manager;
    }

    public void buttonMethod(JSONObject exchangeRates) throws JSONException {
        double currencyAmount;
        String conversionCurrency = (String) this.mainUI.conversionBox.getValue();
        String convertedCurrency = (String) this.mainUI.convertToBox.getValue();
        try{
            currencyAmount = Double.parseDouble(this.mainUI.amountTextField.getText());
        }catch (NumberFormatException e){
            this.mainUI.resultText.setText("Please enter a valid amount.");
            return;
        }
        double rate = Double.parseDouble(exchangeRates.get(convertedCurrency).toString()) / Double.parseDouble(exchangeRates.get(conversionCurrency).toString());
        this.mainUI.resultText.setText(currencyAmount + " " + conversionCurrency + " equals " + this.mainUI.numberFormat.format(rate * currencyAmount) + " " + convertedCurrency + ".");
    }

    public JSONObject updateValues(RequestHandler mainRequestHandler, Stage mainStage){
        JSONObject returnRates;
        while (true){
            try{
                returnRates = mainRequestHandler.getURL("https://api.frankfurter.app/latest?base=USD");
                returnRates.put("USD", 1);
                mainUI.conversionBox.getItems().clear();
                Platform.runLater(() -> {
                    mainStage.setScene(mainUI.mainScene);
                });
                for (Iterator it = returnRates.keys(); it.hasNext(); ) {
                    String i = (String) it.next();
                    mainUI.conversionBox.getItems().add(i);
                    mainUI.convertToBox.getItems().add(i);
                }
                System.out.println("worked xd");
                if (returnRates != null)
                {
                    break;
                }
            }catch (Exception e){
                /*System.out.println(e);
                mainUI.loadingText.setText("epic fail, trying again");
                try {
                    TimeUnit.SECONDS.sleep(1);
                    mainUI.loadingText.setText("loading");
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                    }
                continue;*/
                System.out.println(e);
                }
            }
        return returnRates;
    }
}
