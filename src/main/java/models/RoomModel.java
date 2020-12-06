package models;

import javafx.scene.control.ComboBox;

/**
 * This class stores the data of each room in the house
 * @author Team 4
 *
 */
public class RoomModel {

	private transient String zone;
	private String mode;
	private String name;
	private transient boolean heating;
	private transient boolean ac;
	private int numWindows;
	private int numDoors;
	private int numLights;
	private String nextRoomName;
	private int numOpenDoor;
	private int numOpenLights;
	private int numOpenWindows;
	private transient boolean isObjectBlockingWindow;
	private transient ComboBox<String> objectBlockingWindowComboBox;
	private transient Temperature temperature; // each room would create with instance of Temperature with default temperature
	private transient double currentTemperature;
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
		this.temperature = new Temperature();
		this.zone=null;
		this.heating=false;
		this.ac=false;
	}

	/**
	  * getter to get the mode of light in the room
	 * @return
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * setter to set the mode of light in the room
	 * @param mode
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * Getter to obtain the name of the room
	 * @return name of the room
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter to set the name of the room
	 * @param name of the room
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter to obtain the number of windows in the room
	 * @return the number of windows
	 */
	public int getNumWindows() {
		return numWindows;
	}

	/**
	 * Setter to set the number of windows in the room
	 * @param numWindows number of windows
	 */
	public void setNumWindows(int numWindows) {
		this.numWindows = numWindows;
	}

	/**
	 * Getter to get the number of doors in the room
	 * @return number of doors
	 */
	public int getNumDoors() {
		return numDoors;
	}

	/**
	 * Setter to set the number of doors in the room
	 * @param numDoors number of doors
	 */
	public void setNumDoors(int numDoors) {
		this.numDoors = numDoors;
	}

	/**
	 * Getter to get the number of lights in the room
	 * @return number of lights
	 */
	public int getNumLights() {
		return numLights;
	}

	/**
	 * Setter to set the number of lights in the room
	 * @param numLights number of lights
	 */
	public void setNumLights(int numLights) {
		this.numLights = numLights;
	}

	/**
	 * Getter to get the room name of the adjacent room
	 * @return room name of the adjacent room
	 */
	public String getNextRoomName() {
		return nextRoomName;
	}

	/**
	 * Setter to set the room name of the adjacent room
	 * @param nextRoomName room name of the adjacent room
	 */
	public void setNextRoomName(String nextRoomName) {
		this.nextRoomName = nextRoomName;
	}

	/**
	 * Getter to know if an object is blocking the window
	 * @return true if an object is blocking the window, false otherwise
	 */
	public boolean isObjectBlockingWindow() {
		return isObjectBlockingWindow;
	}

	/**
	 * Setter to set if an object is blocking the window
	 * @param isObjectBlockingWindow
	 */
	public void setObjectBlockingWindow(boolean isObjectBlockingWindow) {
		this.isObjectBlockingWindow = isObjectBlockingWindow;
	}

	/**
	 * Getter to get the comboBox with the options to block window movement
	 * @return comboBox with block window options
	 */
	public ComboBox<String> getObjectBlockingWindowComboBox() {
		return objectBlockingWindowComboBox;
	}

	/**
	 * Setter to set the comboBox with the options to block window movement
	 * @param objectBlockingWindowComboBox comboBox with block window options
	 */
	public void setObjectBlockingWindowComboBox(ComboBox<String> objectBlockingWindowComboBox) {
		this.objectBlockingWindowComboBox = objectBlockingWindowComboBox;
	}

	/**
	 * getter to get the number of open door
	 * @return
	 */
	public int getNumOpenDoor() {
		return numOpenDoor;
	}

	/**
	 * setter to set the number of open door
	 * @param numOpenDoor
	 */
	public void setNumOpenDoor(int numOpenDoor) {
		this.numOpenDoor = numOpenDoor;
	}

	/**
	 * getter to get the number of open light
	 * @return
	 */
	public int getNumOpenLights() {
		return numOpenLights;
	}
	/**
	 * setter to set the number of open lights
	 * @param numOpenLights
	 */
	public void setNumOpenLights(int numOpenLights) {
		this.numOpenLights = numOpenLights;
	}
	/**
	 * getter to get the number of open widows
	 * @return
	 */
	public int getNumOpenWindows() {
		return numOpenWindows;
	}
	/**
	 * setter to set the number of open windows
	 * @param numOpenWindows
	 */
	public void setNumOpenWindows(int numOpenWindows) {
		this.numOpenWindows = numOpenWindows;
	}

	/**
	 * getter of the Temperature instance
	 * @return
	 */
	public Temperature getTemperature() {
		return temperature;
	}

	/**
	 * setter of the Temperature instance
	 * @param temperature
	 */
	public void setTemperature(Temperature temperature) {
		this.temperature = temperature;
	}

	/**
	 * getter of the zone of the room
	 * @return
	 */
	public String getZone() {
		return zone;
	}

	/**
	 * setter of the zone of the room
	 */
	public void setZone(String zone) {
		this.zone = zone;
	}

	/**
	 * getter if the heating is on or off
	 * @return
	 */
	public boolean isHeating() {
		return heating;
	}

	/**
	 * setter of heating
	 * @param heating
	 */
	public void setHeating(boolean heating) {
		this.heating = heating;
	}

	/**
	 * getter of AC, if AC is on or off
	 * @return
	 */
	public boolean isAc() {
		return ac;
	}

	/**
	 * setter of AC
	 * @param ac
	 */
	public void setAc(boolean ac) {
		this.ac = ac;
	}

	/**
	 * getter the current temperature of the room
	 * @return
	 */
	public double getCurrentTemperature() {
		return currentTemperature;
	}

	/**
	 * setter the current temperature of the room
	 * @param currentTemperature
	 */
	public void setCurrentTemperature(double currentTemperature) {
		this.currentTemperature = currentTemperature;
	}

	@Override
	public String toString() {
		return "RoomModel{" +
				"zone='" + zone + '\'' +
				", mode='" + mode + '\'' +
				", name='" + name + '\'' +
				", numWindows=" + numWindows +
				", numDoors=" + numDoors +
				", numLights=" + numLights +
				", nextRoomName='" + nextRoomName + '\'' +
				", numOpenDoor=" + numOpenDoor +
				", numOpenLights=" + numOpenLights +
				", numOpenWindows=" + numOpenWindows +
				", isObjectBlockingWindow=" + isObjectBlockingWindow +
				", objectBlockingWindowComboBox=" + objectBlockingWindowComboBox +
				", temperature=" + temperature +
				'}';
	}
}