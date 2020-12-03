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
import java.io.IOException;
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
                    + " \nNight: "+ roomModels[index].getTemperature().getNightTemp());
        }
    }

    /**
     * set the temperature to all room in the same r
     * @param timeToset
     */
    public void setTemperatureZone(String timeToset){
        String selectZone= (String) zoneRoomList.getSelectionModel().getSelectedItem();
        double inputTemperature = Double.parseDouble(InputTemperature.getText());
        String [] zoneRoomArray = selectZone.split(":");
        selectZone=zoneRoomArray[0];
        int index=-1;
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
        roomModels=houseRoomsModel.getAllRoomsArray();
        if(display && index!=-1){
            displayTemperature.setText("Morning: "+roomModels[index].getTemperature().getMorningTemp()
                    +" \tDay: "+roomModels[index].getTemperature().getDayTemp()
                    + " \nNight: "+ roomModels[index].getTemperature().getNightTemp());
        }

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
    public Boolean checkSelectZone(){
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
    public Boolean checkValue(){
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
        switch (temperatureMode.toLowerCase()){
            case "show":
                showTempBtn.setText("hide");
                if(!checkSelectZoneRoom()){
                    return ;
                }
                RoomModel selectRoom = (RoomModel) zoneRoomTableView.getSelectionModel().getSelectedItem();
                display=true;
                int index=getIndexOfRoom(selectRoom.getName());
                displayTemperature.setText("Room-Morning: "+roomModels[index].getTemperature().getMorningTemp()
                        +" \tDay: "+roomModels[index].getTemperature().getDayTemp()
                        + " \nNight: "+ roomModels[index].getTemperature().getNightTemp());
                break;
            case "hide":
                showTempBtn.setText("show");
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
            case "show":
                if(!checkSelectZone()){
                    return ;
                }
                String selectZone= (String) zoneRoomList.getSelectionModel().getSelectedItem();
                String [] zoneRoomArray = selectZone.split(":");
                selectZone=zoneRoomArray[0];
                showZoneTemp.setText("hide");
                display=true;
                RoomModel selectedRoom = null;
                for(RoomModel rm : roomModels){
                    if(rm.getZone().equalsIgnoreCase(selectZone)){
                        selectedRoom = rm;
                    }
                }
                displayTemperature.setText("Zone- Morning: "+selectedRoom.getTemperature().getMorningTemp()
                        +" \tDay: "+selectedRoom.getTemperature().getDayTemp()
                        + " \nNight: "+ selectedRoom.getTemperature().getNightTemp());
                break;
            case "hide":
                showZoneTemp.setText("show");
                displayTemperature.setText("");
                display=false;
                break;
        }
        zoneRoomList.getSelectionModel().clearSelection();
    }
}
