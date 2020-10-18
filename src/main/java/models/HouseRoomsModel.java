package models;

public class HouseRoomsModel {

    public static RoomModel[] allRoomsArray;

    /**
     * @return the array that contains all the rooms that were extracted
     * from the house layout file
     */
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