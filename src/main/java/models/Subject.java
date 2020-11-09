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
	 * @param o observer
	 */
	public void registerObserver(Observer observer);
	
	/**
	 * Unregister an observer
	 * @param observer
	 */
	public void unregisterObserver(Observer observer);
	
	/**
	 * Notify observers of a change
	 */
	public void notifyObservers();
}
