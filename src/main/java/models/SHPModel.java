package models;

import com.jfoenix.controls.JFXListView;

import java.util.logging.Level;

/**
 * This class stores the data about the security module (SHP)
 * @author Team 4
 *
 */
public class SHPModel implements Observer {

	private String alertpolice;
	private boolean isAwayModeOn;
	private JFXListView consoleLog;

	/**
	 * Constructor which sets the AwayMode to OFF
	 * Also sets the delay time for the call to authorities to 0
	 */
	public SHPModel() {
		this.isAwayModeOn = false;
		this.alertpolice = "0";
	}

	/**
	 * Getter to see if away mode is ON or OFF 
	 * @return true, if away mode ON, false otherwise
	 */
	public boolean isAwayModeOn() {
		return isAwayModeOn;
	}

	/**
	 * Setter to set the away mode
	 * @param isAwayModeOn	true to turn ON away mode, false to turn OFF away mode.
	 */
	public void setAwayModeOn(boolean isAwayModeOn) {
		this.isAwayModeOn = isAwayModeOn;
	}

	/**
	 * Setter for the delay alert police
	 * @param minutes value of minute
	 */
	public void setAlertpolice(String minutes){
		this.alertpolice = minutes;
	}

	/**
	 * Getter for the delay alert police
	 * @return alertpolice string value
	 */
	public String getAlertpolice(){
		return alertpolice;
	}

	/**
	 * sets the console
	 * @param consoleLog console
	 */
	public void setConsoleLog(JFXListView consoleLog) {
		this.consoleLog = consoleLog;
	}

	/**
	 * Logs to console and to external file when subject notifies observer that someone present in a room
	 * during away mode
	 */
	@Override
	public void update(UserModel user) {
		if (isAwayModeOn && !user.getCurrentLocation().equals("outside")) {
			String message = "!!!AWAY MODE WARNING!!! Person present in " + user.getCurrentLocation();
			consoleLog.getItems().add(message);
			LogToFileModel.log(Level.SEVERE, message);
		}
	}
}
