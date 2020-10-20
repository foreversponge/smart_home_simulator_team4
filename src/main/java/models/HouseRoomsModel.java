package models;

/**
 * this class is create all help keep all the avaible room from the text file that is upload
 * it is static class because it is going to be use only one for all the applicaiton
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