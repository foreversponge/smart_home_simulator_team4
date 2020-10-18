package models;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.scene.control.ComboBox;

public class User {
    private String name;
	private String status;
	private String currentLocation;	//current location of the user in the house
	private LocalTime time;
    private LocalDate date;
    private ComboBox<String> locationComboBox;	//comboBox storing possible user locations (for edit context of simulation)

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter to obtain the ComboBox containing all possible rooms of the house
     * @return ComboBox
     */
    public ComboBox<String> getLocation() {
        return locationComboBox;
    }

    /**
     * Setter to set the ComboBox containing all possible rooms of the house that the user can be placed in
     * @param location ComboBox
     */
    public void setLocation(ComboBox<String> location) {
		this.locationComboBox = location;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}
}
