package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.HouseRoomsModel;
import models.RoomModel;
import models.ZoneModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * responsible for the SHHTab
 * it is the controller of the SHHTab
 * dashboard would load this tab and view in their setMainController
 */
public class SHHController {
    @FXML private JFXButton showZoneTemp;
    @FXML private JFXListView zoneRoomList;
    @FXML private JFXTextField InputTemperature;
    @FXML private Label displayTemperature;
    @FXML private TableColumn zoneColumn;
    @FXML private TableColumn roomColumn;
    @FXML private TableView zoneRoomTableView;
    @FXML private JFXButton showTempBtn;
    private static Main mainController;
    @FXML private Label errorLabel;
    private ObservableList<RoomModel> observableZoneList= FXCollections.observableArrayList();
    private ObservableList<String> observableZoneRoomList= FXCollections.observableArrayList();
    HouseRoomsModel houseRoomsModel = HouseRoomsModel.getInstance();
    RoomModel [] roomModels = houseRoomsModel.getAllRoomsArray();
    boolean display=false;

    /**
     * keep an instance of the mainController
     * @param main
     */
    public void setMainController(Main main){
        mainController=main;
    }

    /**
     * help to set the column of the table view with the attribute from the table list
     */
    public void initialize(){
        zoneColumn.setCellValueFactory(new PropertyValueFactory<RoomModel, String>("zone"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<RoomModel, String>("name"));
    }

    /**
     * update the zone room view after finish adding zone and room
     * @param observableZoneRoomList
     */
    public void updateTableView(ObservableList<RoomModel> observableZoneList, ObservableList<String> observableZoneRoomList){
        zoneRoomTableView.setItems(observableZoneList);
        zoneRoomList.setItems(observableZoneRoomList);
    }

    /**
     * open the new set up zone room window
     * @param event
     */
    public void handleZoneRoom(ActionEvent event) {
        String loggedUser = mainController.getLoggedUser().getRole();
        if(!loggedUser.equalsIgnoreCase("parent")){
            errorLabel.setText("only parent can set zone and room");
            mainController.getDashBoardController().addToConsoleLog("only parent can set zone and room");
            return;
        }
        try {
            mainController.setZoneRoomWindow(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * set temperature of the room
     */
    public void setTemperatureRoom(String timeToset){
        RoomModel selectRoom = (RoomModel) zoneRoomTableView.getSelectionModel().getSelectedItem();
        double inputTemperature = Double.parseDouble(InputTemperature.getText());
        int index = getIndexOfRoom(selectRoom.getName());
        switch (timeToset){
            case "morning":
                roomModels[index].getTemperature().setMorningTemp(inputTemperature);
                break;
            case "day":
                roomModels[index].getTemperature().setDayTemp(inputTemperature);
                break;
            case "night":
                roomModels[index].getTemperature().setNightTemp(inputTemperature);
                break;
        }
        houseRoomsModel.setAllRooms(roomModels);
        roomModels=houseRoomsModel.getAllRoomsArray();
        if(display){
            displayTemperature.setText("Morning: "+roomModels[index].getTemperature().getMorningTemp()
                    +" \tDay: "+roomModels[index].getTemperature().getDayTemp()
                    + " \tNight: "+ roomModels[index].getTemperature().getNightTemp());
        }
    }

    /**
     * set the temperature of a zone, that will also change the temperature of the corresponding rooms
     * @param timeToset
     */
    public void setTemperatureZone(String timeToset){
        String selectZone= (String) zoneRoomList.getSelectionModel().getSelectedItem();
        double inputTemperature = Double.parseDouble(InputTemperature.getText());
        String [] zoneRoomArray = selectZone.split(":");
        selectZone=zoneRoomArray[0];

        ArrayList<ZoneModel> allZones = houseRoomsModel.getAllZonesArray();
        for (int i = 0; i < allZones.size(); i++) {
            if(allZones.get(i).getZoneName().equalsIgnoreCase(selectZone)) {
                switch (timeToset) {
                    case "morning":
                        allZones.get(i).getTemperature().setMorningTemp(inputTemperature);
                        break;
                    case "day":
                        allZones.get(i).getTemperature().setDayTemp(inputTemperature);
                        break;
                    case "night":
                        allZones.get(i).getTemperature().setNightTemp(inputTemperature);
                        break;
                }
            }
        }

        for(RoomModel rm : roomModels){
            if(rm.getZone().equalsIgnoreCase(selectZone)){
                switch (timeToset){
                    case "morning":
                        rm.getTemperature().setMorningTemp(inputTemperature);
                        break;
                    case "day":
                        rm.getTemperature().setDayTemp(inputTemperature);
                        break;
                    case "night":
                        rm.getTemperature().setNightTemp(inputTemperature);
                        break;
                }
            }
        }
        houseRoomsModel.setAllRooms(roomModels);
        houseRoomsModel.setAllZonesArray(allZones);
        roomModels=houseRoomsModel.getAllRoomsArray();
    }


    /**
     * set the morning temperature to the selected zone and room
     * each room have an instance of the Temperature so change the new value with the existing one
     * @param event
     */
    public void setMorningTemperature(ActionEvent event) {
        if(!checkValue()){
            return ;
        }
        if(!checkSelectZoneRoom() && !checkSelectZone()){
            return;
        }
        if(checkSelectZoneRoom() && !checkSelectZone()){
            setTemperatureRoom("morning");

        }
        else if (checkSelectZone() && !checkSelectZoneRoom()) {
            setTemperatureZone("morning");
        }
        else{
            setTemperatureRoom("morning");
            setTemperatureZone("morning");
        }
        InputTemperature.clear();
        zoneRoomTableView.getSelectionModel().clearSelection();
        zoneRoomList.getSelectionModel().clearSelection();
    }

    /**
     * set the day temperature to the select zone and room
     * each room have an instance of the Temperature so change the new value with the existing one
     * @param event
     */
    public void setDayTemperature(ActionEvent event) {
        if(!checkValue()){
            return ;
        }
        if(!checkSelectZoneRoom() && !checkSelectZone()){
            return;
        }
        if(checkSelectZoneRoom() && !checkSelectZone() && checkValue()){
            setTemperatureRoom("day");

        }
        else if (!checkSelectZoneRoom() && checkSelectZone() && checkValue()) {
            setTemperatureZone("day");
        }
        else if (checkSelectZoneRoom() && checkSelectZone() && checkValue()) {
            setTemperatureRoom("day");
            setTemperatureZone("day");
        }
        else
            return;
        InputTemperature.clear();
        zoneRoomTableView.getSelectionModel().clearSelection();
        zoneRoomList.getSelectionModel().clearSelection();

    }

    /**
     * set the night temperature to the zone and room
     * each room have an instance of the Temperature so change the new value with the existing one
     * @param event
     */
    public void setNightTemperature(ActionEvent event) {
        if(!checkValue()){
            return ;
        }
        if(!checkSelectZoneRoom() && !checkSelectZone()){
            return;
        }
        if(checkSelectZoneRoom() && !checkSelectZone()){
            setTemperatureRoom("night");

        }
        else if (checkSelectZone() && !checkSelectZoneRoom()) {
            setTemperatureZone("night");
        }
        else{
            setTemperatureRoom("night");
            setTemperatureZone("night");
        }
        InputTemperature.clear();
        zoneRoomTableView.getSelectionModel().clearSelection();
        zoneRoomList.getSelectionModel().clearSelection();
    }

    /**
     * check if the zone is select or not
     * if it is not select display error log return false
     * else return true
     * @return
     */
    public Boolean checkSelectZoneRoom(){
        RoomModel selectRoom = (RoomModel) zoneRoomTableView.getSelectionModel().getSelectedItem();
        if(selectRoom == null){
            return false;
        }
        return true;
    }

    /**
     * check the zone and room
     * @return
     */
    public boolean checkSelectZone(){
        String selectZone= (String) zoneRoomList.getSelectionModel().getSelectedItem();
        if(selectZone == null){
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
    public boolean checkValue(){
        String tempValue = InputTemperature.getText();
        boolean isNumber=false;
        double inputTemperature;
        try{
            inputTemperature = Double.parseDouble(tempValue);
            isNumber =true;
        }
        catch (NumberFormatException e){
            InputTemperature.clear();
            errorLabel.setText("* input temperature have to be number");
        }
        return isNumber;
    }

    /**
     * toggle to show the temperature of the room
     * @param event
     */
    public void toggleShowTemperature(ActionEvent event) {
        String temperatureMode = showTempBtn.getText();
        double zoneTemp = 0;
        ZoneModel targetZone = new ZoneModel("");

        switch (temperatureMode.toLowerCase()){
            case "show room temperature":
                showTempBtn.setText("Hide Room Temperature");
                if(!checkSelectZoneRoom()){
                    return ;
                }
                RoomModel selectRoom = (RoomModel) zoneRoomTableView.getSelectionModel().getSelectedItem();
                display=true;
                int index=getIndexOfRoom(selectRoom.getName());
                String zoneName = selectRoom.getZone();
                ArrayList<ZoneModel> allZones = houseRoomsModel.getAllZonesArray();

                for (int i = 0 ; i < allZones.size() ; i++) {
                    if (allZones.get(i).getZoneName().equalsIgnoreCase(zoneName)) {
                        targetZone = allZones.get(i);
                        break;
                    }
                }

                if(mainController.getShpModel().isAwayModeOn()) {
                    displayTemperature.setText(selectRoom.getName() + " - Morning: "+roomModels[index].getTemperature().getDefaultTemp()
                            +" \tDay: "+roomModels[index].getTemperature().getDefaultTemp()
                            + " \tNight: "+ roomModels[index].getTemperature().getDefaultTemp());
                }

                else {
                    if(roomModels[index].getTemperature().getMorningTemp() != targetZone.getTemperature().getMorningTemp()
                            || roomModels[index].getTemperature().getDayTemp() != targetZone.getTemperature().getDayTemp()
                            || roomModels[index].getTemperature().getNightTemp() != targetZone.getTemperature().getNightTemp()) {
                        displayTemperature.setText(selectRoom.getName() + " - Morning: "+roomModels[index].getTemperature().getMorningTemp()
                                +" \tDay: "+roomModels[index].getTemperature().getDayTemp()
                                + " \tNight: "+ roomModels[index].getTemperature().getNightTemp() + " (!!OVERRIDEN!!)");
                    }
                    else {
                        displayTemperature.setText(selectRoom.getName() + " - Morning: "+roomModels[index].getTemperature().getMorningTemp()
                                +" \tDay: "+roomModels[index].getTemperature().getDayTemp()
                                + " \tNight: "+ roomModels[index].getTemperature().getNightTemp());
                    }
                }

                break;
            case "hide room temperature":
                showTempBtn.setText("Show Room Temperature");
                displayTemperature.setText("");
                display=false;
                break;
        }
        zoneRoomTableView.getSelectionModel().clearSelection();
    }

    /**
     * getter index of the specific room in all rom array
     * @param room
     * @return
     */
    public int getIndexOfRoom(String room){
        int index=-1;
        for(int i=0 ;i < roomModels.length;i++){
            if(roomModels[i].getName().equalsIgnoreCase(room)){
                index=i;
            }
        }
        return index;
    }

    /**
     * show zone temperature
     * @param event
     */
    public void toggleShowZoneTemperature(ActionEvent event) {
        String temperatureMode = showZoneTemp.getText();
        switch (temperatureMode.toLowerCase()){
            case "show zone temperature":
                if(!checkSelectZone()){
                    return ;
                }
                String selectZone= (String) zoneRoomList.getSelectionModel().getSelectedItem();
                int index = -1;
                ArrayList<ZoneModel> allZones = houseRoomsModel.getAllZonesArray();
                String [] zoneRoomArray = selectZone.split(":");
                selectZone=zoneRoomArray[0];
                for (int i = 0; i < allZones.size();  i++) {
                    if(allZones.get(i).getZoneName().equalsIgnoreCase(selectZone)) {
                        index = i;
                    }
                }
                showZoneTemp.setText("Hide Zone Temperature");
                display=true;
                if(mainController.getShpModel().isAwayModeOn()) {
                    displayTemperature.setText(selectZone + " - Morning: "+allZones.get(index).getTemperature().getDefaultTemp()
                            +" \tDay: "+ allZones.get(index).getTemperature().getDefaultTemp()
                            + " \tNight: "+ allZones.get(index).getTemperature().getDefaultTemp());
                }
                else {
                    displayTemperature.setText(selectZone + " - Morning: "+allZones.get(index).getTemperature().getMorningTemp()
                            +" \tDay: "+ allZones.get(index).getTemperature().getDayTemp()
                            + " \tNight: "+ allZones.get(index).getTemperature().getNightTemp());
                }
                break;
            case "hide zone temperature":
                showZoneTemp.setText("Show Zone Temperature");
                displayTemperature.setText("");
                display=false;
                break;
        }
        zoneRoomList.getSelectionModel().clearSelection();
    }

    /**
     * This method will set the temperatures of all rooms to the default away temperature.
     * @param actionEvent
     */
    public void setAwayTemp(ActionEvent actionEvent) {
        if (!checkValue()){
            return;
        }
        double inputTemperature = Double.parseDouble(InputTemperature.getText());

        ArrayList <ZoneModel> allZones = houseRoomsModel.getAllZonesArray();
        for (int i = 0; i < roomModels.length ; i++) {
            roomModels[i].getTemperature().setDefaultTemp(inputTemperature);
        }
        for(int i = 0; i <allZones.size() ; i++){
            allZones.get(i).getTemperature().setDefaultTemp(inputTemperature);
        }
        InputTemperature.clear();
    }
}