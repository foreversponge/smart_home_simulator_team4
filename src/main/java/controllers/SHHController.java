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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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
                mainController.getDashBoardController().addToConsoleLog(selectRoom.getName() + " morning temperature set to " + inputTemperature);
                break;
            case "day":
                roomModels[index].getTemperature().setDayTemp(inputTemperature);
                mainController.getDashBoardController().addToConsoleLog(selectRoom.getName() + " day temperature set to " + inputTemperature);
                break;
            case "night":
                roomModels[index].getTemperature().setNightTemp(inputTemperature);
                mainController.getDashBoardController().addToConsoleLog(selectRoom.getName() + " night temperature set to " + inputTemperature);
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
                        mainController.getDashBoardController().addToConsoleLog(allZones.get(i).getZoneName() + " morning temperature set to " + inputTemperature);
                        break;
                    case "day":
                        allZones.get(i).getTemperature().setDayTemp(inputTemperature);
                        mainController.getDashBoardController().addToConsoleLog(allZones.get(i).getZoneName() + " day temperature set to " + inputTemperature);
                        break;
                    case "night":
                        allZones.get(i).getTemperature().setNightTemp(inputTemperature);
                        mainController.getDashBoardController().addToConsoleLog(allZones.get(i).getZoneName() + " night temperature set to " + inputTemperature);
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
        monitorTemp();
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
    public boolean checkSelectZoneRoom(){
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
        mainController.getDashBoardController().addToConsoleLog("Default away temperature set to " + inputTemperature);
        InputTemperature.clear();
    }

    /**
     * When this method is called, it means that the temperature of a room or outside changed
     * then when check if we are in summer, compare rooms with outside temp to see if we open the windows
     * then we check if the windows are blocked, if they are send a message
     * Also checks to see if rooms temperatures goes below 0 and sends message if so
     */
    public void monitorTemp() {
        LocalDate localDate = mainController.getLoggedUser().getDate();
        int outTemp = mainController.getDashBoardController().outsideTemp;
        RoomModel[] allRoom = houseRoomsModel.getAllRoomsArray();
        int month = localDate.getMonthValue();
        int month_start = getSummerStart();
        int month_end = getSummerEnd();
        if (month >= month_start && month <= month_end) {
            for (RoomModel rm : allRoom) {
                double roomTemp = getCurrentRoomTemp(rm);
                if (roomTemp > outTemp) {
                    if (!rm.isObjectBlockingWindow()) {
                        int numberWindows = rm.getNumWindows();
                        rm.setNumOpenWindows(numberWindows);
                    } else {
                        mainController.getDashBoardController().addToConsoleLog(" "+rm.getName()+": Object blocking window");
                    }
                }
                else{
                   rm.setNumOpenWindows(0);
                }
            }
            mainController.getDashBoardController().displayLayout();
        }
        for (RoomModel rm : allRoom) {
            double roomTemp = getCurrentRoomTemp(rm);
            if (roomTemp <= 0) {
                mainController.getDashBoardController().addToConsoleLog(" "+rm.getName()+": Warning! Possible Burst Pipe ");
            }
        }
    }

    /**
     *
     * @param rm
     * @return Gets the current room temperature depending of the time during the day
     */
    public double getCurrentRoomTemp(RoomModel rm){
        LocalTime time = mainController.getLoggedUser().getTime();
        int hour = time.getHour();
        double currentTemp = 0.0;

        if(hour>6 && hour<12){
            currentTemp = rm.getTemperature().getMorningTemp();
        }
        if(hour>12 && hour<18){
            currentTemp = rm.getTemperature().getDayTemp();
        }
        if((hour>18 && hour<24) || (hour>0 && hour<6)){
            currentTemp = rm.getTemperature().getNightTemp();
        }
        return currentTemp;
    }

    /**
     *
     * @return the start of the summer as an integer
     */
    public int getSummerStart(){
        int summer_start = 0;

        String month_start = mainController.getLoggedUser().getSeasonStart();


        switch (month_start){
            case "January":
                summer_start=1;
                break;
            case "February":
                summer_start=2;
                break;
            case "March":
                summer_start=3;
                break;
            case "April":
                summer_start=4;
                break;
            case "May":
                summer_start=5;
                break;
            case "June":
                summer_start=6;
                break;
            case "July":
                summer_start=7;
                break;
            case "August":
                summer_start=8;
                break;
            case "September":
                summer_start=9;
                break;
            case "October":
                summer_start=10;
                break;
            case "November":
                summer_start=11;
                break;
            case "December":
                summer_start=12;
                break;
        }

        return summer_start;
    }

    /**
     *
     * @return returns the end of the summer as an integer
     */
    public int getSummerEnd(){
        int summer_end = 0;
        String month_end = mainController.getLoggedUser().getSeasonEnd();

        switch (month_end){
            case "January":
                summer_end=1;
                break;
            case "February":
                summer_end=2;
                break;
            case "March":
                summer_end=3;
                break;
            case "April":
                summer_end=4;
                break;
            case "May":
                summer_end=5;
                break;
            case "June":
                summer_end=6;
                break;
            case "July":
                summer_end=7;
                break;
            case "August":
                summer_end=8;
                break;
            case "September":
                summer_end=9;
                break;
            case "October":
                summer_end=10;
                break;
            case "November":
                summer_end=11;
                break;
            case "December":
                summer_end=12;
                break;
        }
        return summer_end;
    }
}