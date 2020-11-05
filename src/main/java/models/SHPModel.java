package models;

import com.jfoenix.controls.JFXListView;

/**
 * This class stores the data about the security module (SHP)
 * @author Team 4
 *
 */
public class SHPModel implements Observer {
	
	private boolean isAwayModeOn;
	private JFXListView consoleLog;

	/**
	 * Constructor which sets the AwayMode to OFF
	 */
	public SHPModel() {
		this.isAwayModeOn = false;
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
	 * sets the console
	 * @param consoleLog console
	 */
	public void setConsoleLog(JFXListView consoleLog) {
		this.consoleLog = consoleLog;
	}
	
	@Override
	public void update(UserModel user) {
		if (isAwayModeOn && !user.getCurrentLocation().equals("outside")) {
			consoleLog.getItems().add("!!!AWAY MODE WARNING!!! Person present in " + user.getCurrentLocation());
		}
	}
}
