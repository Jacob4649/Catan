package catan.engine.player;

import catan.Catan;

/**
 * {@link Exception} thrown when the player count exceeds the maximum specified
 * by the game engine
 * 
 * @author Jacob
 *
 */
public class PlayerCountOutOfBoundsException extends Exception {

	/**
	 * Creates a new {@link PlayerCountOutOfBoundsException}
	 */
	public PlayerCountOutOfBoundsException() {
		super("Player Count Is Less Than " + Catan.MIN_PLAYERS + " Or Greater Than " + Catan.MAX_PLAYERS);
	}

	/**
	 * Creates a new {@link PlayerCountOutOfBoundsException}
	 * @param count the player count
	 */
	public PlayerCountOutOfBoundsException(int count) {
		super("Player Count Is " + count + " Must Be Greater Than Or Equal To " + Catan.MIN_PLAYERS
				+ " And Lesser Or Equal To " + Catan.MAX_PLAYERS);
	}

}
