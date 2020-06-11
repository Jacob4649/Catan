package catan.engine.player;

import catan.engine.resources.ResourceBundle;

/**
 * Class representing a single player (can be user or opponent)
 * 
 * @author Jacob
 *
 */
public class Player {

	private PlayerColor m_color;
	private ResourceBundle m_resources = new ResourceBundle();

	/**
	 * Creates a player with the specified color
	 * 
	 * @param color
	 *            the color of the player
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

	/**
	 * 
	 * @return the {@link ResourceBundle} containing this {@link Player}'s
	 *         resources
	 */
	public ResourceBundle getResources() {
		return m_resources;
	}

}
