package controllers;

import com.jfoenix.controls.JFXBadge;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Tooltip;

import models.HouseRoomsModel;
import models.RoomModel;
import models.UserModel;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for loading the various rooms
 * obtained from the house layout file
 * @author Team 4
 *
 */
public class RoomController {

	public JFXBadge badgeWindow;
	public JFXBadge badgeDoor;
	public JFXBadge badgeLight;
    public JFXBadge badgeUserNum;
	public ImageView UserNum;
	public ImageView LoggedUser;
	private Main mainController;
	public ImageView window1;
	public ImageView door1;
	public ImageView light1;
	public ImageView doorLock;
	public ImageView windowBlocked;
	@FXML private Label room1;
	private HouseRoomsModel houseRoomsModel= HouseRoomsModel.getInstance();
	private RoomModel room;

	/**
	 * keep an instance of the Main
	 * @param maincontroller
	 */
	public void setMainController(Main maincontroller) {
		this.mainController = maincontroller;
	}

	/**
	 * get the Map of room location and number of user in the room include the logged user
	 * @return
	 */
	private Map<String, Integer> extractUserInRoom(){
		Map<String, Integer> userInRoom = new HashMap<>();
		ObservableList<UserModel> allUser= mainController.getUserModelData();
		for(RoomModel rm: houseRoomsModel.getAllRoomsArray()){
			userInRoom.put(rm.getName(),0);
		}
		for(UserModel u : allUser){
			if(u.getCurrentLocation()!=null && !u.getCurrentLocation().equalsIgnoreCase("outside")){
				userInRoom.put(u.getCurrentLocation(),(userInRoom.get(u.getCurrentLocation())+1));
			}
		}
		return userInRoom;
	}

	/**
	 * Receives information about a room to display in the house layout tab
	 * @param room
	 */
	public void setData(RoomModel room){
		boolean userInRoom= false;
		String loggedLoc= mainController.getLoggedUser().getCurrentLocation();
		Map<String, Integer> user= extractUserInRoom();
		this.room = room;
		if(room.getName().equalsIgnoreCase(loggedLoc)){
			userInRoom=true;
			LoggedUser.setImage(new Image("file:src/main/resources/images/loggedUser.png"));
			Tooltip loggedUserTooltip = new Tooltip("Logged User");
			Tooltip.install(LoggedUser, loggedUserTooltip);
			user.put(room.getName(),(user.get(room.getName())-1));
		}
		if(user.containsKey(room.getName()) && user.get(room.getName())!=0){
			userInRoom=true;
			badgeUserNum.setText(user.get(room.getName()).toString());
			UserNum.setImage(new Image("file:src/main/resources/images/otherusers.png"));
			Tooltip otherUsersTooltip = new Tooltip("Other Users");
			Tooltip.install(UserNum, otherUsersTooltip);
		}
		if(room.getMode()!=null && room.getMode().equalsIgnoreCase("auto")){
			if(userInRoom){
				room.setNumOpenLights(room.getNumLights());
			}
			else{
				room.setNumOpenLights(0);
			}
		}
		room1.setText(room.getName());
		badgeDoor.setText(String.valueOf(room.getNumDoors()));
		badgeLight.setText(String.valueOf(room.getNumLights()));
		badgeWindow.setText(String.valueOf(room.getNumWindows()));

		if(mainController.getShpModel().isAwayModeOn()) {
			if (room.getName().equalsIgnoreCase("House Entrance") || room.getName().equalsIgnoreCase("Garage") || room.getName().equalsIgnoreCase("Backyard")) {
				doorLock.setImage(new Image("file:src/main/resources/images/locked.png"));
				Tooltip doorLockedTooltip = new Tooltip("Door is locked");
				Tooltip.install(doorLock, doorLockedTooltip);
			}
		}
		else {
			doorLock.setImage(null);
		}

		if (room.isObjectBlockingWindow()) {
			windowBlocked.setImage(new Image(("file:src/main/resources/images/windowBlocked.png")));
			Tooltip windowBlockedTooltip = new Tooltip("Object blocking window!");
			Tooltip.install(windowBlocked, windowBlockedTooltip);
		}
		else {
			windowBlocked.setImage(null);
		}

		if (room.getNumOpenWindows() == 0) {
			window1.setImage(new Image("file:src/main/resources/images/closewindow.png"));
			Tooltip windowTooltip = new Tooltip("Windows");
			Tooltip.install(window1, windowTooltip);
		}
		else {
			window1.setImage(new Image("file:src/main/resources/images/openwindow.png"));
		}

		if (room.getNumOpenDoor() == 0) {
			door1.setImage(new Image("file:src/main/resources/images/closedoor.png"));
			Tooltip doorTooltip = new Tooltip("Doors");
			Tooltip.install(door1, doorTooltip);
		}
		else {
			door1.setImage(new Image("file:src/main/resources/images/opendoor.png"));
		}

		if (room.getNumOpenLights() == 0) {
			light1.setImage(new Image("file:src/main/resources/images/lightoff.png"));
			Tooltip lightTooltip = new Tooltip("Lights");
			Tooltip.install(light1, lightTooltip);
		}
		else {
			light1.setImage(new Image("file:src/main/resources/images/lighton.png"));
		}
	}
}
