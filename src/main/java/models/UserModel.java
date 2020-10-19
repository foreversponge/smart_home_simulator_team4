package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class UserModel {
    private String name;
    private String location;
    private String role;
    private LocalTime time;
    private LocalDate date;

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
        this.location = location;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNameAndRole(){ return name+" : "+ role;
    }

}
