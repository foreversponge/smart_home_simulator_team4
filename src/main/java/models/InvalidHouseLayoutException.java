package models;

/**
 * This class is a custom Exception class for invalid House Layout uploads
 * It will be thrown if the user uploads an incompatible house layout
 * @author Team 4
 *
 */
public class InvalidHouseLayoutException extends Exception {

	/**
	 * Default constructor containing error message
	 */
	public InvalidHouseLayoutException() {
		super("The file uploaded does not contain a valid house layout");
	}

	/**
	 * Constructor which allows for custom message
	 * @param message error message the user wishes to display
	 */
	public InvalidHouseLayoutException(String message) {
		super(message);
	}
}
