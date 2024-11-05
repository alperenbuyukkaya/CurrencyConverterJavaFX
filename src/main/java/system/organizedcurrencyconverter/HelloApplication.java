package system.organizedcurrencyconverter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loading.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        HelloController controller = fxmlLoader.getController();
        controller.setApplication(this);
        stage.setTitle("Currency Converter");
        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/images/dollarsign.png")));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
    public void changeScene(Parent root){
        primaryStage.setScene(new Scene(root));
    }
}