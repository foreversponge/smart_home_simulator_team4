package models;

import java.time.LocalTime;

public class LogMessageModel {
    private LocalTime time;
    private String message;

    public LogMessageModel(LocalTime time, String message) {
        this.time = time;
        this.message = message;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

}
