package controllers;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.*;

import java.lang.reflect.Array;
import java.util.*;

/**
 * this is controller to handle the set up zone and room
 * so the user can add new zone and room to the zone
 */
public class setUpZoneRoomController {
    @FXML private Label errorLabel;
    @FXML private JFXListView zoneList;
    @FXML private JFXListView roomList;
    private Main mainController;
    private ObservableList<String> observableZoneList=FXCollections.observableArrayList();;
    private ObservableList<String> observableRoomList = FXCollections.observableArrayList();
    private ObservableList<RoomModel> observableListAllRoom = FXCollections.observableArrayList();
    private ObservableList<String> observableZoneRoomList= FXCollections.observableArrayList();
    Map<String, Set<String>> zoneRoomMap = new TreeMap<>();
    private Stage currentStage;
    private int id=1;
    private HouseRoomsModel houseRoomsModel = HouseRoomsModel.getInstance();
    private UserModel loggedUser;
    private SHHController shhController;
    private RoomModel [] allRoom ;
    private ArrayList <ZoneModel> allZones = new ArrayList <ZoneModel>();

    /**
     * keep the Main instance
     * CurrentStage to close
     * SHHController to update the zone list on SHH tab of dashbaord
     * @param main
     * @param currentstage
     * @param shhCon
     */
    public void setMaincontroller(Main main, Stage currentstage, SHHController shhCon) {
        this.mainController = main;
        this.currentStage = currentstage;
        this.shhController =shhCon;
        allRoom= houseRoomsModel.getAllRoomsArray();
        for(RoomModel roomModel: allRoom){
            observableRoomList.add(roomModel.getName());
        }
        zoneList.setItems(observableZoneList);
        roomList.setItems(observableRoomList);
        loggedUser=mainController.getLoggedUser();
    }

    /**
     * add the zone first before add the room to zone
     * @param event
     */
    public void addZone(ActionEvent event) {
        int currentId=0;
        for(int i =1 ;i<id;i++){
            if(!observableZoneList.contains("zone"+i)){
                currentId=i;
                break;
            }
        }
        if(currentId !=0){
            observableZoneList.add("zone"+currentId);
        }
        else{
            observableZoneList.add("zone"+id);
            id++;
        }
    }

    /**
     * delete the zone, set the room back the roomlist
     * @param event
     */
    public void deleteZone(ActionEvent event) {
        String selectZone= (String) zoneList.getSelectionModel().getSelectedItem();
        if(selectZone == null) {
            return;
        }
        Set<String> rooms = zoneRoomMap.get(selectZone);
        if(rooms !=null){
            for(String s: rooms){
                observableRoomList.add(s);
            }
        }
        zoneRoomMap.remove(selectZone);
        observableZoneList.remove(selectZone);
    }

    /**
     * add the select room to the select zone
     * zone and room have to be select cannot be null
     * @param event
     */
    public void HandleAddRoomToZone(ActionEvent event) {
        String selectZone= (String) zoneList.getSelectionModel().getSelectedItem();
        String selectRoom =(String) roomList.getSelectionModel().getSelectedItem();
        if(selectRoom == null || selectZone== null){
            errorLabel.setText("* Both zone and room have to be select");
            return;
        }
        Set<String> rooms;
        if(zoneRoomMap.containsKey(selectZone)){
            rooms= zoneRoomMap.get(selectZone);
        }
        else{
            rooms = new HashSet<>();
        }
        rooms.add(selectRoom);
        observableRoomList.remove(selectRoom);
        zoneRoomMap.put(selectZone, rooms);
    }

    /**
     * close the set Zone Room window
     * @param event
     */
    public void HandleCancel(ActionEvent event) {
        currentStage.close();
    }

    /**
     * if the roomlist is not empty mean that the room does not have the zone yet
     * all room have to have a zone
     * one zone can have multiple room
     * update the zoneRoom list on the SHH tab
     * close the current stage
     * @param event
     */
    public void HandleSave(ActionEvent event) {
        if(observableRoomList.stream().count()!=0){
            errorLabel.setText("*each room have to have a zone");
            return;
        }
        zoneRoomMap.forEach((k,v)->{
            v.forEach( room ->{
                int i = getIndexOfRoom(room);
                allRoom[i].setZone(k);
                observableListAllRoom.add(allRoom[i]);
            });
            allZones.add(new ZoneModel(k));
        });
        houseRoomsModel.setAllRooms(allRoom);
        houseRoomsModel.setAllZonesArray(allZones);
        zoneRoomMap.forEach((k,v)->{
            observableZoneRoomList.add(k+":"+v);
        });
        shhController.updateTableView(observableListAllRoom, observableZoneRoomList);
        currentStage.close();
    }

    /**
     * getter index of the specific room in all rom array
     * @param room
     * @return
     */
    public int getIndexOfRoom(String room){
        int index=-1;
        for(int i=0 ;i < allRoom.length;i++){
            if(allRoom[i].getName().equalsIgnoreCase(room)){
                index=i;
            }
        }
        return index;
    }
}