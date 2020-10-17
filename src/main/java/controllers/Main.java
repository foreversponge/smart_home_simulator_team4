package controllers;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.User;
import java.io.IOException;

public class Main extends Application {

    private Stage currentState;
    private ObservableList<User> userData = FXCollections.observableArrayList();
    private ObservableList<User> tempUserData = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception  {
        currentState = primaryStage;
        try{
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
     * userData is store all the collection of user
     * @return
     */
    public ObservableList<User> getPersonData() {
        return userData;
    }
    public void setPersonData(ObservableList<User> userData) {
        this.userData = userData;
    }
    /**
     * tempUserData is to store when you open the user management so you can add/edit/delete user
     * only when you click save the userData would be update with the exist of tempUserData
     * @return
     */
    public ObservableList<User> getTempPersonData() {
        return tempUserData;
    }
    public void setTempPersonData(ObservableList<User> tempUserData) {
        this.tempUserData = tempUserData;
    }
    /**
     * method to help load the houselayout
     * when we load the fxml file and get the controller
     * so we can set instance of Main to HouseLayoutController
     * @throws IOException
     */
    public void setHouseLayoutWindow() throws IOException {
        currentState.setTitle("Smart Home Simulator");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/houseLayout.fxml"));
        Parent root = fxmlLoader.load();
        HouseLayoutController houselay = fxmlLoader.getController();
        houselay.setMainController(this);
        Scene houseLayoutScene = new Scene(root);
        currentState.setScene(houseLayoutScene);
        currentState.show();
    }
    /**
     * method to load simulator parameter , where you can choose user set time, date and location
     * @throws IOException
     */
    public void setSimulatorParameterWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/simulatorParameter.fxml"));
        Parent root = fxmlLoader.load();
        SimulatorParameterController simParcontroller = fxmlLoader.getController();
        simParcontroller.setMainController(this);
        Scene simScene = new Scene(root);
        currentState.setScene(simScene);
        currentState.show();
    }
    /**
     * method to open the user manager window
     * when you close this window you still on the simulator parameter window
     * @throws IOException
     */
    public void setUserManagerWindow() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/userManager.fxml"));
        Parent root = fxmlLoader.load();
        UserManagerController simParcontroller = fxmlLoader.getController();
        Stage editstage= new Stage();
        simParcontroller.setMaincontroller(this,editstage);
        editstage.initOwner(currentState);
        tempUserData.clear();
        userData.forEach((user)->{
            tempUserData.add(user);
        });
        editstage.initModality(Modality.WINDOW_MODAL);
        Scene userManager = new Scene(root);
        editstage.setScene(userManager);
        editstage.show();
    }
    public void setDashboardWindow() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/dashBoard.fxml"));
        Parent root = fxmlLoader.load();
        dashBoardController dashboardcontroller = fxmlLoader.getController();
        dashboardcontroller.setMainController(this,currentState);
        Scene simScene = new Scene(root);

        currentState.setScene(simScene);
        currentState.show();


    }
}