package catan.engine.board.tile;

/**
 * {@link Exception} thrown when an uninitialized or incorrectly initialized {@link Edge} is accessed
 * 
 * @author Jacob
 *
 */
public class EdgeNotInitializedException extends Exception {

	/**
	 * Creates a new {@link EdgeNotInitializedException}
	 */
	public EdgeNotInitializedException() {
		super("Edge Not Initialized");
	}

	/**
	 * Creates a new {@link EdgeNotInitializedException}
	 * @param vertex1 the first {@link Vertex} associated with this {@link Edge}
	 * @param vertex2 the second {@link Vertex} associated with this {@link Edge}
	 */
	public EdgeNotInitializedException(Vertex vertex1, Vertex vertex2) {
		super("Edge At: " + vertex1 + ", " + vertex2 + " Not Initialized");
	}
	
}
