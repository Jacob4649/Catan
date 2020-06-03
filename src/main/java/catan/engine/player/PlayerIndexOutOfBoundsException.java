package catan.engine.player;

/**
 * {@link Exception} thrown when the player index provided to the {@link Catan}
 * constructor exceeds or falls short of the possible bounds
 * 
 * @author Jacob
 *
 */
public class PlayerIndexOutOfBoundsException extends Exception {

	/**
	 * Creates a new {@link PlayerIndexOutOfBoundsException}
	 */
	public PlayerIndexOutOfBoundsException() {
		super("Player Index Is Less Than 0 Or Greater Than Last Player Index");
	}

	/**
	 * Creates a new {@link PlayerIndexOutOfBoundsException}
	 * 
	 * @param index
	 *            the player index
	 * @param max
	 *            the max possible player index
	 */
	public PlayerIndexOutOfBoundsException(int index, int max) {
		super("Player Index Is " + index + ", Must Be Greater Or Equal To 0 Or Lesser Or Equal To " + max);
	}

}
