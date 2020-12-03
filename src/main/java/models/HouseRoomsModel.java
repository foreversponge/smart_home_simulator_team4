package models;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class stores all the available rooms extracted from the uploaded house layout text file
 * @author Team 4
 *
 */
public class HouseRoomsModel {
	private static HouseRoomsModel houseRoomsModel;
	private static RoomModel[] allRoomsArray;
	private static ArrayList<ZoneModel> allZonesArray;

	/**
	 * constructor
	 */
	private HouseRoomsModel() {
	}

	/**
	 * to get instance of the HouseRoomModel
	 * @return
	 */
	public static HouseRoomsModel getInstance(){
		if(houseRoomsModel==null){
			houseRoomsModel = new HouseRoomsModel();
		}
		return houseRoomsModel;
	}

	/**
	 * get the array of all room in the house layout
	 * @return
	 */
	public RoomModel[] getAllRoomsArray() {
		return allRoomsArray;
	}

	/**
	 * This method sets the array that contains all the rooms extracted from the house layout file.
	 * @param allRooms array of rooms
	 */

	public void setAllRooms(RoomModel[] allRooms) {
		allRoomsArray = allRooms;
		for(RoomModel rm : allRoomsArray){
			if(rm.getTemperature()==null){
				rm.setTemperature(new Temperature());
			}
		}
	}

	/**
	 * get the array of all zones
	 * @return
	 */
	public ArrayList<ZoneModel> getAllZonesArray() {
		return allZonesArray;
	}

	/**
	 * set the array of all zones
	 * @param allZonesArray
	 */
	public void setAllZonesArray(ArrayList <ZoneModel> allZonesArray) {
		HouseRoomsModel.allZonesArray = allZonesArray;
	}
}