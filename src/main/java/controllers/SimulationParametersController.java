package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

import javafx.scene.input.MouseEvent;
import models.RoomModel;
import models.HouseRoomsModel;
import models.UserModel;

import javax.naming.Binding;

public class SimulationParametersController {

    @FXML private DatePicker  dateSelected;

    @FXML private TableView<UserModel> allUsers;
    @FXML private TableColumn<UserModel, String> colname;
    @FXML private TableColumn<UserModel, String> colrole;

    @FXML private JFXComboBox roomLocation;
    @FXML private JFXComboBox userSelected;
    @FXML private JFXButton continueButton;

    private Main mainController;

    /**
     * Initalize the values of the table that contains the user names and roles
     * & the room names extracted from the house layout file
     */
    public void initialize(){
        colname.setCellValueFactory(new PropertyValueFactory<UserModel, String>("name"));
        colrole.setCellValueFactory(new PropertyValueFactory<UserModel, String>("status"));
        ObservableList<String> locationNames= FXCollections.observableArrayList();
        for(RoomModel r : HouseRoomsModel.getAllRoomsArray()){
            locationNames.add(r.getName());
        }
        roomLocation.setItems(locationNames);
    }

    /**
     * This method sets the controller provided in the parameter to replace the
     * main controller so that it can get access to all the users from the user model.
     * @param controller controller that will replace the main controller
     */
    public void setMainController(Main controller ) {
        this.mainController = controller;
        allUsers.setItems(mainController.getPersonData());
    }

    /**
     * This method handles when there is a mouse click on the edit label that will open the
     * user management window
     * @param event on mouse click
     */
    public void handleEditUser(ActionEvent event) {
        try{
            mainController.setUserManagerWindow();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method handles when the continue button is clicked. It will verify that all the necessary
     * simulation parameters are set (date, time, user and location).
     * @param mouseEvent on mouse click
     */
    public void onContinueClick(MouseEvent mouseEvent) {
        UserModel userSelected = allUsers.getSelectionModel().getSelectedItem();
        if(dateSelected != null && userSelected != null && roomLocation.getValue() != null) { // ADD TIMER HERE
            mainController.setLoggedUser(userSelected);
            mainController.getLoggedUser().setDate(dateSelected.getValue());
            mainController.getLoggedUser().setCurrentLocation(roomLocation.getValue().toString());
            mainController.closeWindow();
            try {
                mainController.setDashboardWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Selections");
            alert.setHeaderText("Missing Selections.");
            alert.setContentText("You must select a date, user and location to proceed. \nPlease try again.");
            alert.showAndWait();
        }
    }
}
