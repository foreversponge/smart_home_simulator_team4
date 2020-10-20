package models;

import javafx.scene.control.ComboBox;

public class RoomModel {

    private String name;
    private int numWindows;
    private int numDoors;
    private int numLights;
    private String nextRoomName;
    private transient boolean isObjectBlockingWindow;
	private transient ComboBox<String> objectBlockingWindowComboBox;


    /**
     * constructor to create RoomModel
     * @param name
     * @param numWindows
     * @param numDoors
     * @param numLights
     * @param nextRoomName
     */
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
    
    public boolean isObjectBlockingWindow() {
 		return isObjectBlockingWindow;
 	}

 	public void setObjectBlockingWindow(boolean isObjectBlockingWindow) {
 		this.isObjectBlockingWindow = isObjectBlockingWindow;
 	}

 	public ComboBox<String> getObjectBlockingWindowComboBox() {
 		return objectBlockingWindowComboBox;
 	}

 	public void setObjectBlockingWindowComboBox(ComboBox<String> objectBlockingWindowComboBox) {
 		this.objectBlockingWindowComboBox = objectBlockingWindowComboBox;
 	}

    @Override
    public String toString() {
        return "room{" +
                "name='" + name + '\'' +
                ", numWindows=" + numWindows +
                ", numDoors=" + numDoors +
                ", numLights=" + numLights +
                ", nextRoomName='" + nextRoomName + '\'' +
                ", isObjectBlockingWindow=" + isObjectBlockingWindow + 
                '}';
    }
}