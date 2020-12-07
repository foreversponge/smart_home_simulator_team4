package models;

/**
 * This interface will be implemented by the Subject being observed
 * in the Observer Design Pattern
 * @author Team 4
 *
 */
public interface Subject {

	/**
	 * Register an observer
	 * @param observer observer
	 */
	public void registerObserver(Observer observer);
	
	/**
	 * Unregister an observer
	 * @param observer instance of observer
	 */
	public void unregisterObserver(Observer observer);
	
	/**
	 * Notify observers of a change
	 */
	public void notifyObservers();
}
