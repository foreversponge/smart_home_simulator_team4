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

    /**
     * Initializes the list of rooms and sets up the ComboBox items related to the time frame
     * @throws IOException
     */
    public void initialize() throws IOException {
        setRoomsView();
        roomsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setHourComboBox();
        setMinuteComboBox();
    }

    /**
     * Keeps an instance of the current stage to allow for stage transitions
     * @param currentStage is the stage that is currently displayed
     */
    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    /**
     * Keeps an instance of the current dashboard to allow commands to be sent
     * @param dashboardController is the dashboard that is currently displayed
     */
    public void setCurrentDashboard(dashBoardController dashboardController){
        this.dashboardController = dashboardController;
    }

    /**
     * Method that initializes the list of rooms that is going to be displayed to the user
     */
    private void setRoomsView() {
        for(RoomModel room:houseRoomsModel.getAllRoomsArray() ){
            roomsView.getItems().add(room.getName());
        }
    }

    /**
     * Method that sets up the ComboBoxes responsible for representing hours
     */
    private void setHourComboBox(){
        DecimalFormat formatter = new DecimalFormat("00");
        for(int i = 0 ; i<24 ; i++){
            String hourFormatted = formatter.format(i);
            fromHour.getItems().add(hourFormatted);
            toHour.getItems().add(hourFormatted);
        }
    }

    /**
     * Method that sets up the ComboBoxes responsible for representing minutes
     */
    private void setMinuteComboBox(){
        DecimalFormat formatter = new DecimalFormat("00");
        for(int i = 0 ; i<60 ; i++){
            String minuteFormatted = formatter.format(i);
            fromMinute.getItems().add(minuteFormatted);
            toMinute.getItems().add(minuteFormatted);
        }
    }

    /**
     * When user clicks the confirm button, the lights to remain on stage will close
     * and the house layout will be updated
     * @param event
     */
    public void confirmClick(MouseEvent event) {
        List<String> listSelectedRooms = roomsView.getSelectionModel().getSelectedItems();
        if (listSelectedRooms.isEmpty()) {
            currentStage.close();
        }
        else {
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

    /**
     * When user clicks the Cancel button, the lights to remain on stage will close
     * @param event
     */
    public void cancelClick(MouseEvent event) {
        currentStage.close();
    }
}
