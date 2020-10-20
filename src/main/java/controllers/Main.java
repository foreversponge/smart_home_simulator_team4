package controllers;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import models.LogMessageModel;
import models.UserModel;
import java.io.IOException;

public class Main extends Application {

    private Stage currentState;
    private ObservableList<UserModel> userModelData = FXCollections.observableArrayList();
    private ObservableList<UserModel> tempUserModelData = FXCollections.observableArrayList();
    private ObservableList<LogMessageModel> logMessageModels = FXCollections.observableArrayList();
    private UserModel loggedUser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        currentState = primaryStage;
        try {
            setHouseLayoutWindow();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * close the current window before open another window
     */
    public void CloseWindow(){
        currentState.close();
    }

    /**
     * getter method of the UserModelData that is ObservableList of the all the User in the application
     * @return
     */
    public ObservableList<UserModel> getUserModelData() {
        return userModelData;
    }

    /**
     * getter method of the TempUserModelData, which is use for User Management window
     * @return
     */
    public ObservableList<UserModel> getTempUserModelData() {
        return tempUserModelData;
    }

    /**
     * getter to get the ObservableList of LogMessages
     * @return
     */
    public ObservableList<LogMessageModel> getLogMessages() {
        return logMessageModels;
    }

    /**
     * close the current window
     */
    public void closeWindow() {
        currentState.close();
    }

    /**
     * getter method to get the Logged User
     * @return
     */
    public UserModel getLoggedUser() {
        return this.loggedUser;
    }
    /**
     * setter method to set the Logged User
     * @return
     */
    public void setLoggedUser(UserModel loggedUser) {
        this.loggedUser = loggedUser;
    }

    /**
     * set house layout window
     * @throws IOException
     */
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

    /**
     * method to open Simulation parameter Window
     * @throws IOException
     */
    public void setSimulationParametersWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/simulationParameters.fxml"));
        Parent root = fxmlLoader.load();
        SimulationParametersController simParController = fxmlLoader.getController();
        simParController.setMainController(this);
        Scene simulationParametersScene = new Scene(root);
        currentState.setScene(simulationParametersScene);
        currentState.show();
    }

    /**
     * method to open the user manager window
     * when you close this window you still on the simulator parameter window
     * @throws IOException
     */
    public void setUserManagerWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/userManager.fxml"));
        Parent root = fxmlLoader.load();
        UserManagerController simParcontroller = fxmlLoader.getController();
        Stage editstage = new Stage();
        simParcontroller.setMaincontroller(this, editstage);
        editstage.initOwner(currentState);
        tempUserModelData.clear();
        userModelData.forEach((userModel)->{
            tempUserModelData.add(userModel);
        });
        editstage.initModality(Modality.WINDOW_MODAL);
        Scene userManager = new Scene(root);
        editstage.setScene(userManager);
        editstage.show();
    }
    
    /**
     * method to load teh DashBoard Window
     * @throws IOException
     */
    public void setDashboardWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/dashBoard.fxml"));
        Parent root = fxmlLoader.load();
        dashBoardController dashboardcontroller = fxmlLoader.getController();
        dashboardcontroller.setMainController(this,currentState);
        Scene simScene = new Scene(root);
        currentState.setScene(simScene);
        currentState.show();
    }
    
    /**
     * method to open the edit Context of Simulation window
     * when closing this window, the user is returned to the dashboard
     * @throws IOException
     */
    public void setEditContextWindow() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/EditContextOfSimulation.fxml"));
        Parent root = fxmlLoader.load();
        EditContextOfSimulationController editContextController = fxmlLoader.getController();
        Stage editContextStage= new Stage();
        editContextController.setMaincontroller(this, editContextStage);
        editContextStage.initOwner(currentState);
        editContextStage.setTitle("Edit Context of Simulation");
        editContextStage.initModality(Modality.WINDOW_MODAL);
        Scene editContextScene = new Scene(root);
        editContextStage.setScene(editContextScene);
        editContextStage.show();
    }
}

