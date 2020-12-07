package models;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * This class stores the data to submit log messages to the console
 * in the simulator dashboard
 * @author Team 4
 *
 */
public class LogMessageModel {
	
	private LocalTime time;
	private String message;

	/**
	 * Constructor of LogMessageModel
	 * @param time time to log
	 * @param message message to log
	 */
	public LogMessageModel(LocalTime time, String message) {
		this.time = time;
		this.message = message;
	}

	/**
	 * Getter to retrieve the local time of the log posting
	 * @return time
	 */
	public LocalTime getTime() {
		return time;
	}

	/**
	 * Getter to obtain the log message
	 * @return log message
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return time.format(DateTimeFormatter.ofPattern("hh:mm"))+ " : " +message;
	}
}
