package models;

import javafx.beans.binding.ObjectExpression;
import javafx.scene.control.ComboBox;

import java.time.LocalDate;
import java.time.LocalTime;


public class UserModel {
    private String name;
	private String role;
	private String currentLocation;	//current location of the user in the house
	private LocalTime time;
    private LocalDate date;
    private ComboBox<String> locationOptions;	//comboBox storing possible user locations (for edit context of simulation)

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

    public UserModel(String name, String role) {
        this.name = name;
        this.role = role;
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
    public ComboBox<String> getLocationOptions() {
        return locationOptions;
    }

    /**
     * Setter to set the ComboBox containing all possible rooms of the house that the user can be placed in
     * @param location ComboBox
     */
    public void setLocationOptions(ComboBox<String> locationComboBox) {
		this.locationOptions = locationComboBox;
	}

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNameAndRole(){ return name+" : "+ role;
    }
    
    public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}
}
