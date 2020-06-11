package catan.engine.board.tile;

/**
 * Exception thrown when a vertex has not been initialized
 * @author Jacob
 *
 */
public class VertexNotInitializedException extends Exception {
	/**
	 * Creates a new {@link VertexNotInitializedException}
	 */
	public VertexNotInitializedException() {
		super("Vertex Not Initialized");
	}

	/**
	 * Creates a new {@link VertexNotInitializedException}
	 * @param row the row of the {@link Vertex}
	 * @param col the column of the {@link Vertex}
	 */
	public VertexNotInitializedException(int row, int col) {
		super("Vertex At Row: " + row + ", Col: " + col + " Not Initialized");
	}
}
