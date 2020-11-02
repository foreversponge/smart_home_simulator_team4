package controllers;

import com.jfoenix.controls.JFXBadge;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
	public ImageView window11;
	public ImageView door11;
	public ImageView light11;
	public JFXBadge badgeLight1;
	public JFXBadge badgeDoor1;
	public JFXBadge badgeWindow1;
    public JFXBadge badgeUserNum;
	public ImageView UserNum;
	public ImageView LoggedUser;
	private Main mainController;
	public ImageView window1;
	public ImageView door1;
	public ImageView light1;
	@FXML
	private AnchorPane anchorpaneroom1;
	@FXML private Label room1;

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
		for(RoomModel rm: HouseRoomsModel.getAllRoomsArray()){
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
			user.put(room.getName(),(user.get(room.getName())-1));
		}
		if(user.containsKey(room.getName()) && user.get(room.getName())!=0){
			userInRoom=true;
			badgeUserNum.setText(user.get(room.getName()).toString());
			UserNum.setImage(new Image("file:src/main/resources/images/otherusers.png"));
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
		badgeDoor.setText(String.valueOf(room.getNumDoors()-room.getNumOpenDoor()));
		badgeLight.setText(String.valueOf(room.getNumLights()-room.getNumOpenLights()));
		badgeWindow.setText(String.valueOf(room.getNumWindows()-room.getNumOpenWindows()));
		badgeDoor1.setText(String.valueOf(room.getNumOpenDoor()));
		badgeLight1.setText(String.valueOf(room.getNumOpenLights()));
		badgeWindow1.setText(String.valueOf(room.getNumOpenWindows()));
		window1.setImage(new Image("file:src/main/resources/images/closewindow.png"));
		light1.setImage(new Image("file:src/main/resources/images/lightoff.png"));
		door1.setImage(new Image("file:src/main/resources/images/closedoor.png"));
		window11.setImage(new Image("file:src/main/resources/images/openwindow.png"));
		light11.setImage(new Image("file:src/main/resources/images/lighton.png"));
		door11.setImage(new Image("file:src/main/resources/images/opendoor.png"));
	}
}
