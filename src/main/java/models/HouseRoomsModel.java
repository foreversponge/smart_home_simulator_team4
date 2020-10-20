package models;

/**
 * This class stores all the available rooms extracted from the uploaded house layout text file
 * @author Team 4
 *
 */
public class HouseRoomsModel {

	public static RoomModel[] allRoomsArray;

	public static RoomModel[] getAllRoomsArray() {
		return allRoomsArray;
	}

	/**
	 * This method sets the array that contains all the rooms extracted from the house layout file.
	 * @param allRoomsArray an array of rooms
	 */
	public static void setAllRooms(RoomModel[] allRoomsArray) {
		HouseRoomsModel.allRoomsArray = allRoomsArray;
	}
}