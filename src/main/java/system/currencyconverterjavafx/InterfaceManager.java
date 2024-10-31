package system.currencyconverterjavafx;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.HashMap;

public class InterfaceManager {
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
    VBox loadingLayout;
    VBox mainLayout;
    HashMap<String, Object> objectHashMap;
    DecimalFormat numberFormat = new DecimalFormat("0.00");

    public InterfaceManager() {
        this.conversionFromText = new Text("Enter the currency to be converted from:");
        this.conversionToText = new Text("Enter the currency to be converted to:");
        this.resultText = new Text("Result");
        this.amountText = new Text("Amount: ");
        this.conversionBox = new ComboBox();
        this.convertToBox = new ComboBox();
        this.amountTextField = new TextField();
        this.loadingText = new Text("Loading");
        this.loadingLayout  = new VBox(loadingText);
        mainButton = new Button("Convert");
        this.mainLayout = new VBox(
                amountText, amountTextField,
                conversionFromText, conversionBox, conversionToText, convertToBox,
                mainButton, resultText);
        this.objectHashMap = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            objectHashMap.put(field.getName(), field);
        }


        this.mainLayout.setPadding(new Insets(10,10,10,10));
        this.mainLayout.setSpacing(5);
        this.mainScene = new Scene(mainLayout, 300, 150);
        this.loadingScene = new Scene(loadingLayout, 300, 150);
        this.mainScene.setFill(Color.LIGHTGRAY);
    }

    /*public String getValue(String inputName){
        if (objectHashMap.containsKey(inputName)){
            try{
                Object object = objectHashMap.get(inputName);
                switch (String.valueOf(object.getClass())){
                    case "Text":
                        Text text = (Text) object;
                        return text.getText();
                    case "TextField":
                        TextField textField = (TextField) object;
                        return textField.getText();
                    case "ComboBox":
                        ComboBox comboBox = (ComboBox) object;
                        return (String) comboBox.getValue();
                }
            }catch (Exception e){
                throw(e);
            }
        }
        return null;
    }
    Archived for now.       */
}
