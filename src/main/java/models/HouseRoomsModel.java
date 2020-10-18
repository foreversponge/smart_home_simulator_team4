package models;

public class HouseRoomsModel {
    public static RoomModel[] allRoomsArray;
    public static RoomModel[] getAllRoomsArray() {
        return allRoomsArray;
    }
    public static void setAllRooms(RoomModel[] allRoomsArray) {
        HouseRoomsModel.allRoomsArray = allRoomsArray;
    }
}