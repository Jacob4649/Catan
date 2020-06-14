package catan.engine.resources;

import catan.engine.player.Player;

/**
 * Interface for some form of transaction that consumes a {@link Player}'s
 * resources
 * 
 * @author Jacob
 *
 */
public interface Purchase {

	/**
	 * 
	 * @return {@link ResourceBundle} containing the cost of this purchase
	 */
	public ResourceBundle getCost();

	/**
	 * Completes this transaction for a specified {@link Player}, charges them
	 * for the results
	 * 
	 * @param player
	 *            {@link Player} to charge
	 * @return true if successful
	 */
	public boolean charge(Player player);

	/**
	 * 
	 * @return the {@link Object} being purchased
	 */
	public Object getItem();

}
