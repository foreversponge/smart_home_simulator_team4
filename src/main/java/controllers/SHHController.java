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

/**
 * responsible for the SHHTab
 * it is the controller of the SHHTab
 * dashboard would load this tab and view in their setMainController
 */
public class SHHController {
    private static Main mainController;
    @FXML private Label errorLabel;
    @FXML private JFXTextField temperatureInput;
    @FXML private JFXListView zoneRoomList;
    private ObservableList<String> observableZoneList= FXCollections.observableArrayList();
    HouseRoomsModel houseRoomsModel = HouseRoomsModel.getInstance();
    RoomModel [] roomModels = houseRoomsModel.getAllRoomsArray();

    /**
     * keep an instance of the mainController
     * @param main
     */
    public void setMainController(Main main){
        mainController=main;
    }

    /**
     * update the zone room view after finish adding zone and room
     * @param observableZoneRoomList
     */
    public void updateListView(ObservableList<String> observableZoneRoomList){
        zoneRoomList.setItems(observableZoneRoomList);
    }

    /**
     * open the new set up zone room window
     * @param event
     */
    public void handleZoneRoom(ActionEvent event) {
        // check permission before open this
        try {
            mainController.setZoneRoomWindow(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * set the morning temperature to the selected zone and room
     * each room have an instance of the Temperature so change the new value with the existing one
     * @param event
     */
    public void setMorningTemperature(ActionEvent event) {
        temperatureInput.clear();
        if(!checkSelectZone() || !checkValue()){
            return ;
        }
        String selectZone= (String) zoneRoomList.getSelectionModel().getSelectedItem();
        double inputTemperature = Double.parseDouble(temperatureInput.getText());
        String [] zoneRoomArray = selectZone.split(":");
        Set<String> roomInZone = houseRoomsModel.getZoneRoomMap().get(zoneRoomArray[0]);
        for(String s: roomInZone){
            for(RoomModel rm : roomModels){
                if(s.equalsIgnoreCase(rm.getName())){
                    rm.getTemperature().setMorningTemp(inputTemperature);
                }
            }
        }
    }

    /**
     * set the day temperature to the select zone and room
     * each room have an instance of the Temperature so change the new value with the existing one
     * @param event
     */
    public void setDayTemperature(ActionEvent event) {
        temperatureInput.clear();
        if(!checkSelectZone() || !checkValue()){
            return ;
        }
        String selectZone= (String) zoneRoomList.getSelectionModel().getSelectedItem();
        double inputTemperature = Double.parseDouble(temperatureInput.getText());
        String [] zoneRoomArray = selectZone.split(":");
        Set<String> roomInZone = houseRoomsModel.getZoneRoomMap().get(zoneRoomArray[0]);
        for(String s: roomInZone){
            for(RoomModel rm : roomModels){
                if(s.equalsIgnoreCase(rm.getName())){
                    rm.getTemperature().setMorningTemp(inputTemperature);
                }
            }
        }
    }

    /**
     * set the night temperature to the zone and room
     * each room have an instance of the Temperature so change the new value with the existing one
     * @param event
     */
    public void setNightTemperature(ActionEvent event) {
        temperatureInput.clear();
        if(!checkSelectZone() || !checkValue()){
            return ;
        }
        String selectZone= (String) zoneRoomList.getSelectionModel().getSelectedItem();
        double inputTemperature = Double.parseDouble(temperatureInput.getText());
        String [] zoneRoomArray = selectZone.split(":");
        Set<String> roomInZone = houseRoomsModel.getZoneRoomMap().get(zoneRoomArray[0]);
        for(String s: roomInZone){
            for(RoomModel rm : roomModels){
                if(s.equalsIgnoreCase(rm.getName())){
                    rm.getTemperature().setMorningTemp(inputTemperature);
                }
            }
        }
    }

    /**
     * check if the zone is select or not
     * if it is not select display error log return false
     * else return true
     * @return
     */
    public Boolean checkSelectZone(){
        String selectZone= (String) zoneRoomList.getSelectionModel().getSelectedItem();
        if(selectZone == null){
            errorLabel.setText("* please select the zone");
            return false;
        }
        return true;
    }

    /**
     * check the value of the desired temperature is a number or not
     * if it is not a number or cannot cast to double display error log and return fasle
     * else return true
     * @return
     */
    public Boolean checkValue(){
        String tempValue = temperatureInput.getText();
        boolean isNumber=false;
        double inputTemperature;
        try{
            inputTemperature = Double.parseDouble(tempValue);
            isNumber =true;
        }
        catch (NumberFormatException e){
            errorLabel.setText("* input temperature have to be number");
        }
        return isNumber;
    }
}
