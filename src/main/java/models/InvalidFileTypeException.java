package models;

/**
 * This class is a custom Exception class for invalid File type uploads
 * It will be thrown if the user uploads an incompatible file type
 * @author Team 4
 *
 */
public class InvalidFileTypeException extends Exception {

	/**
	 * Default constructor containing error message
	 */
	public InvalidFileTypeException() {
		super("Invalid File Type");
	}

	/**
	 * Constructor which indicates the valid file type to the user
	 * @param fileType specifies the file type that the user must upload
	 */
	public InvalidFileTypeException(String fileType) {
		super("The uploaded file is invalid! You must upload a " + fileType + " file");
	}
}
