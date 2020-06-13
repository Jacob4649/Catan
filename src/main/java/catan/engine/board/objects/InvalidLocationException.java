package catan.engine.board.objects;

/**
 * Exception raised when an {@link BoardObject} is initialized in an invalid
 * position
 * 
 * @author Jacob
 *
 */
public class InvalidLocationException extends Exception {

	/**
	 * Creates a new {@link InvalidLocationException}
	 */
	public InvalidLocationException() {
		super("Board Object Created In Invalid Location");
	}
	
	/**
	 * Creates a new {@link InvalidLocationException}
	 * @param object the {@link BoardObject} causing the exception to be raised
	 * @throws BoardObjectNotInitializedException if the {@link BoardObject}'s position has not been initialized
	 */
	public InvalidLocationException(BoardObject object) throws BoardObjectNotInitializedException {
		super(object + " Created In Invalid Location: " + object.getPosition());
	}
	
}
