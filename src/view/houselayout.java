package view;

import com.jfoenix.controls.JFXComboBox;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class houselayout extends Application {
    ComboBox<String> comboBox;
    @FXML private JFXComboBox<String> usertype ;
    @Override
    public void start(Stage primaryStage) throws Exception {
        comboBox= new ComboBox<>();
        comboBox.getItems().addAll(
                "Parent",
                "child",
                "guest",
                "stranger"
        );

        Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
