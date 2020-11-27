package controllers;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.HouseRoomsModel;
import models.RoomModel;

import java.io.IOException;
import java.util.Set;

public class SHHController {
    private static Main mainController;
    @FXML private Label errorLabel;
    @FXML private JFXTextField temperatureInput;
    @FXML private JFXListView zoneRoomList;
    private ObservableList<String> observableZoneList= FXCollections.observableArrayList();
    HouseRoomsModel houseRoomsModel = HouseRoomsModel.getInstance();
    RoomModel [] roomModels = houseRoomsModel.getAllRoomsArray();

    public void setMainController(Main main){
        mainController=main;
    }

    public void updateListView(ObservableList<String> observableZoneRoomList){
        zoneRoomList.setItems(observableZoneRoomList);
    }
    //TO Do check permission before set
    public void handleZoneRoom(ActionEvent event) {
        try {
            mainController.setZoneRoomWindow(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
