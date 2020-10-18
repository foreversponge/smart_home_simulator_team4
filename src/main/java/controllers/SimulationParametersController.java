package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.io.IOException;

import javafx.scene.input.MouseEvent;
import models.RoomModel;
import models.HouseRoomsModel;
import models.UserModel;

import javax.naming.Binding;

public class SimulationParametersController {

    @FXML private Button testButton = new Button("Continue");

    @FXML private DatePicker  dateSelected;
    @FXML private TableView<UserModel> allUsers;
    @FXML private TableColumn<UserModel, String> colname;
    @FXML private TableColumn<UserModel, String> colrole;
    @FXML private JFXComboBox roomLocation;
    @FXML private JFXComboBox userSelected;
    @FXML private JFXButton continueButton;

    private Main mainController;

    public void initialize(){
        colname.setCellValueFactory(new PropertyValueFactory<UserModel, String>("name"));
        colrole.setCellValueFactory(new PropertyValueFactory<UserModel, String>("status"));
        ObservableList<String> locationNames= FXCollections.observableArrayList();
//        ObservableList<String> userNames = FXCollections.observableArrayList(); HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        for(RoomModel r : HouseRoomsModel.getAllRoomsArray()){
            locationNames.add(r.getName());
        }
        roomLocation.setItems(locationNames);
//        userSelected.setItems(userNames); HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
    }

    public void setMainController(Main controller ) {
        this.mainController = controller;
        allUsers.setItems(mainController.getPersonData());
    }

    public void handleEditUser(ActionEvent event) {
        try{
            mainController.setUserManagerWindow();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onContinueClick(MouseEvent mouseEvent) {
        UserModel userSelected = allUsers.getSelectionModel().getSelectedItem();
//        if(dateSelected != null && userSelected != null && roomLocation.getValue() != null) { // ADD TIMER HERE
//            mainController.setLoggedUser(userSelected);
//            mainController.getLoggedUser().setDate(dateSelected.getValue());
//            mainController.getLoggedUser().setLocation(roomLocation.getValue().toString());
//            mainController.closeWindow();
//        }
//        try {
//            mainController.setDashboardWindow();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void checkContinue(MouseEvent mouseEvent) {
        testButton.disableProperty().bind(
                roomLocation.valueProperty().isNull()
                        .or(dateSelected.valueProperty().isNull())
                        .or(userSelected.valueProperty().isNull()));
        System.out.println("DATE:"+ (dateSelected.valueProperty()).getValue());
        System.out.println("USER"+(userSelected.valueProperty()).getValue());
        System.out.println("ROOM"+ (roomLocation.valueProperty()).getValue());

    }
}
