package models;

public class RoomModel {
    private String name;
    private int numWindows;
    private int numDoors;
    private int numLights;
    private String nextRoomName;

    public RoomModel(String name, int numWindows, int numDoors, int numLights, String nextRoomName) {
        this.name = name;
        this.numWindows = numWindows;
        this.numDoors = numDoors;
        this.numLights = numLights;
        this.nextRoomName = nextRoomName;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumWindows() {
        return numWindows;
    }

    public void setNumWindows(int numWindows) {
        this.numWindows = numWindows;
    }

    public int getNumDoors() {
        return numDoors;
    }

    public void setNumDoors(int numDoors) {
        this.numDoors = numDoors;
    }

    public int getNumLights() {
        return numLights;
    }

    public void setNumLights(int numLights) {
        this.numLights = numLights;
    }

    public String getNextRoomName() {
        return nextRoomName;
    }

    public void setNextRoomName(String nextRoomName) {
        this.nextRoomName = nextRoomName;
    }

    @Override
    public String toString() {
        return "room{" +
                "name='" + name + '\'' +
                ", numWindows=" + numWindows +
                ", numDoors=" + numDoors +
                ", numLights=" + numLights +
                ", nextRoomName='" + nextRoomName + '\'' +
                '}';
    }
}