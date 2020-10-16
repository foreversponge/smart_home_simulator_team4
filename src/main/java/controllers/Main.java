package controllers;

//import helper.Person;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
        import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception  {
        primaryStage.setTitle("Smart Home Simulator");
        Parent root = FXMLLoader.load(getClass().getResource("/views/houseLayout.fxml"));
        Scene houseLayoutScene = new Scene(root);
        primaryStage.setScene(houseLayoutScene);
        primaryStage.show();
    }
}