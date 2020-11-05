package models;

public class SHPModel implements Observer {
	
	private boolean isAwayModeOn;

	public boolean isAwayModeOn() {
		return isAwayModeOn;
	}

	public void setAwayModeOn(boolean isAwayModeOn) {
		this.isAwayModeOn = isAwayModeOn;
	}
	
	@Override
	public void update(UserModel user) {
		if (isAwayModeOn && !user.getCurrentLocation().equals("outside")) {
			System.out.println("Person present in " + user.getCurrentLocation());
		}
		else {
			System.out.println("Hello");
		}
	}
}
