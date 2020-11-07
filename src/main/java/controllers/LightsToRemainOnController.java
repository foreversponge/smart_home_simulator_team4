package controllers;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.HouseRoomsModel;
import models.RoomModel;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * This class acts as a controller for the lightsToRemainOn.fxml
 * It allows users to select lights they want to remain on when away mode is on
 * @author Team 4
 *
 */
public class LightsToRemainOnController {

    private Stage currentStage;
    private dashBoardController dashboardController;

    private List<String> selectRooms;

    private HouseRoomsModel houseRoomsModel=HouseRoomsModel.getInstance();
    @FXML
    private JFXListView roomsView;
    @FXML
    private ComboBox fromHour;
    @FXML
    private ComboBox fromMinute;
    @FXML
    private ComboBox toHour;
    @FXML
    private ComboBox toMinute;

    public void initialize() throws IOException {
        setRoomsView();
        roomsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setHourComboBox();
        setMinuteComboBox();
    }

    public void setCurrentStage(Stage currentstage) {
        this.currentStage = currentstage;
    }

    public void setCurrentDashboard(dashBoardController dashboardController){
        this.dashboardController = dashboardController;
    }

        private void setRoomsView() {
        for(RoomModel room:houseRoomsModel.getAllRoomsArray() ){
            roomsView.getItems().add(room.getName());
        }
    }

    private void setHourComboBox(){
        DecimalFormat formatter = new DecimalFormat("00");
        for(int i = 0 ; i<24 ; i++){
            String hourFormatted = formatter.format(i);
            fromHour.getItems().add(hourFormatted);
            toHour.getItems().add(hourFormatted);
        }
    }

    private void setMinuteComboBox(){
        DecimalFormat formatter = new DecimalFormat("00");
        for(int i = 0 ; i<60 ; i++){
            String minuteFormatted = formatter.format(i);
            fromMinute.getItems().add(minuteFormatted);
            toMinute.getItems().add(minuteFormatted);
        }
    }

    public void confirmClick(MouseEvent event) {
        List<String> listSelectedRooms = roomsView.getSelectionModel().getSelectedItems();
        if (listSelectedRooms.isEmpty()) {
            currentStage.close();
        } else {
            RoomModel[] allRooms = houseRoomsModel.getAllRoomsArray();
            for (RoomModel room : allRooms) {
                for (String location : listSelectedRooms) {
                    if (room.getName().equalsIgnoreCase(location)) {
                        room.setNumOpenLights(room.getNumLights());
                    }
                }
            }
            currentStage.close();
            dashboardController.displayLayout();
        }
    }

    public void cancelClick(MouseEvent event) {
        currentStage.close();
    }

}
