package catan.engine.player;

import catan.engine.resources.Purchase;
import catan.engine.resources.ResourceBundle;

/**
 * Class representing a single player (can be user or opponent)
 * 
 * @author Jacob
 *
 */
public class Player {

	private PlayerColor m_color;
	private ResourceBundle m_resources = new ResourceBundle(100, 100, 100, 100, 100);

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
	
	/**
	 * {@link Player} attempts to buy the specified item
	 * @param purchase the {@link Purchase} to buy
	 * @return true if the {@link Purchase} was competed successfully
	 */
	public boolean buy(Purchase purchase) {
		return purchase.charge(this);
	}

}
