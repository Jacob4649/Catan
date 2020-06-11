package catan.engine.board.objects;

/**
 * Class for when a board object has not been initialized
 * 
 * @author Jacob
 *
 */
public class BoardObjectNotInitializedException extends Exception {

	/**
	 * Creates a new {@link BoardObjectNotInitializedException}
	 */
	public BoardObjectNotInitializedException() {
		super("Board Object Not Initialized");
	}

	/**
	 * Creates a new {@link BoardObjectNotInitializedException}
	 * 
	 * @param object
	 *            the {@link BoardObject} that threw the exception
	 */
	public BoardObjectNotInitializedException(BoardObject object) {
		super("Board Object: " + object + " Not Initialized");
	}

}
