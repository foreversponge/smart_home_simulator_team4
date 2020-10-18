package controllers;

//import helper.Person;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.UserModel;
import models.UserModel;

import java.io.IOException;

public class Main extends Application {

    private Stage currentState;
    private ObservableList<UserModel> userData = FXCollections.observableArrayList();
    private ObservableList<UserModel> tempUserData = FXCollections.observableArrayList();
    private UserModel loggedUser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        currentState = primaryStage;
        try {
            setHouseLayoutWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeWindow() {
        currentState.close();
    }

    public ObservableList<UserModel> getPersonData() {
            return userData;
    }

    public void setPersonData(ObservableList<UserModel> userData) {
        this.userData = userData;
    }

    public ObservableList<UserModel> getTempPersonData() {
        return tempUserData;
    }

    public void setTempPersonData(ObservableList<UserModel> tempUserData) {
        this.tempUserData = tempUserData;
    }

    public UserModel getLoggedUser() {
        return this.loggedUser;
    }

    public void setLoggedUser(UserModel loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setHouseLayoutWindow() throws IOException {
        currentState.setTitle("Smart Home Simulator");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/houseLayout.fxml"));
        Parent root = fxmlLoader.load();
        HouseLayoutController houseLayout = fxmlLoader.getController();
        houseLayout.setMainController(this);
        Scene houseLayoutScene = new Scene(root);
        currentState.setScene(houseLayoutScene);
        currentState.show();
    }

    public void setSimulationParametersWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/simulationParameters.fxml"));
        Parent root = fxmlLoader.load();
        SimulationParametersController simParController = fxmlLoader.getController();
        simParController.setMainController(this);
        Scene simulationParametersScene = new Scene(root);
        currentState.setScene(simulationParametersScene);
        currentState.show();
    }

    public void setUserManagerWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/userManager.fxml"));
        Parent root = fxmlLoader.load();
        UserManagerController simParcontroller = fxmlLoader.getController();
        Stage editstage = new Stage();
        simParcontroller.setMaincontroller(this, editstage);
        editstage.initOwner(currentState);
        tempUserData.clear();
        userData.forEach((user) -> {
            tempUserData.add(user);
        });
        editstage.initModality(Modality.WINDOW_MODAL);
        Scene userManager = new Scene(root);
        editstage.setScene(userManager);
        editstage.show();
    }
}