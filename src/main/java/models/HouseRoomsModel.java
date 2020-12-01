package models;


import java.util.Map;
import java.util.Set;

/**
 * This class stores all the available rooms extracted from the uploaded house layout text file
 * @author Team 4
 *
 */
public class HouseRoomsModel {
	private Map<String, Set<String>> zoneRoomMap;
	private static HouseRoomsModel houseRoomsModel;
	private static RoomModel[] allRoomsArray;

	/**
	 * constructor
	 */
	private HouseRoomsModel() {
		zoneRoomMap = null;
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
			rm.setTemperature(new Temperature());
		}
	}

	/**
	 * get the map of zone and room
	 * zoneRoomMap is the map which have key as the zone name and all rooms in the zone store in the set of their room name
	 * @return
	 */
	public Map<String, Set<String>> getZoneRoomMap() {
		return zoneRoomMap;
	}

	/**
	 * setter fo the zone room map
	 * @param zoneRoomMap
	 */
	public void setZoneRoomMap(Map<String, Set<String>> zoneRoomMap) {
		this.zoneRoomMap = zoneRoomMap;
	}

	}