package models;

import controllers.Main;
import controllers.RoomController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;


import java.io.IOException;

/**
 * This class stores all the available rooms extracted from the uploaded house layout text file
 * @author Team 4
 *
 */
public class HouseRoomsModel {
	private static HouseRoomsModel houseRoomsModel;
	private static RoomModel[] allRoomsArray;

	private HouseRoomsModel() {
	}
	public static HouseRoomsModel getInstance(){
		if(houseRoomsModel==null){
			houseRoomsModel = new HouseRoomsModel();
		}
		return houseRoomsModel;
	}



	public RoomModel[] getAllRoomsArray() {
		return allRoomsArray;
	}

	/**
	 * This method sets the array that contains all the rooms extracted from the house layout file.
	 * @param allRoomsArray an array of rooms
	 */
	public void setAllRooms(RoomModel[] allRoomsArray) {
		HouseRoomsModel.allRoomsArray = allRoomsArray;
	}
}