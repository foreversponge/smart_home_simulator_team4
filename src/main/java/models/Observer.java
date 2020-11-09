package models;

/**
 * This interface is utilized to implement the Observer Design pattern
 * @author Team 4
 *
 */
public interface Observer {

	/**
	 * When notified, the observer will be updated
	 * @param user
	 */
	public void update(UserModel user);
}
