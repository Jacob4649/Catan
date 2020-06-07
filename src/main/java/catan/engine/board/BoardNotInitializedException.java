package catan.engine.board;

/**
 * {@link Exception} thrown when an uninitialized {@link Board} is accessed
 * 
 * @author Jacob
 *
 */
public class BoardNotInitializedException extends Exception {

	/**
	 * Creates a new {@link BoardNotInitializedException}
	 */
	public BoardNotInitializedException() {
		super("Board Not Initialized");
	}
	
}
