package catan.engine.player;

/**
 * Class representing a single player (can be user or opponent)
 * 
 * @author Jacob
 *
 */
public class Player {

	private PlayerColor m_color;

	/**
	 * Creates a player with the specified color
	 * @param color the color of the player
	 */
	public Player(PlayerColor color) {
		m_color = color;
	}

	/**
	 * 
	 * @return the {@link PlayerColor} of this {@link Player}
	 */
	public PlayerColor getColor() {
		return m_color;
	}

}
