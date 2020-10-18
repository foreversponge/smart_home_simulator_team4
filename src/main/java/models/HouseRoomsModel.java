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
    public static void setAllRooms(RoomModel[] allRoomsArray) {
        HouseRoomsModel.allRoomsArray = allRoomsArray;
    }
}