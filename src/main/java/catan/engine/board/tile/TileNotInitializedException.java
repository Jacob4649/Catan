package catan.engine.board.tile;

/**
 * {@link Exception} thrown when an uninitialized {@link Tile} is accessed
 * 
 * @author Jacob
 *
 */
public class TileNotInitializedException extends Exception {

	/**
	 * Creates a new {@link TileNotInitializedException}
	 */
	public TileNotInitializedException() {
		super("Tile Not Initialized");
	}

	/**
	 * Creates a new {@link TileNotInitializedException}
	 * @param row the row of the {@link Tile}
	 * @param col the column of the {@link Tile}
	 */
	public TileNotInitializedException(int row, int col) {
		super("Tile At Row: " + row + ", Col: " + col + " Not Initialized");
	}
}
