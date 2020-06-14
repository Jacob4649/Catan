package catan.engine.resources.trading;

import catan.engine.player.Player;
import catan.engine.resources.Purchase;
import catan.engine.resources.PurchaseCosts;
import catan.engine.resources.ResourceBundle;

/**
 * Trades some number of one kind of resource with the game bank for some number
 * of another kind of resource
 * 
 * @author Jacob
 *
 */
public class TradeExchange implements Purchase {

	private int m_input;
	private int m_output;

	/**
	 * Creates a new {@link TradeExchange}
	 * 
	 * @param input
	 *            the input resource
	 * @param output
	 *            the output resource
	 */
	public TradeExchange(int input, int output) {
		m_input = input;
		m_output = output;
	}

	/**
	 * @return an {@link ResourceBundle} containing the cost of this
	 *         {@link TradeExchange}
	 */
	@Override
	public ResourceBundle getCost() {
		int[] input = new int[] { 0, 0, 0, 0, 0 };
		input[m_input] = PurchaseCosts.TRADEEXCHANGE_COST;
		return new ResourceBundle(input);
	}

	/**
	 * Performs this {@link TradeExchange} for a specific {@link Player}
	 * 
	 * @param player
	 *            the {@link Player} to perform for
	 * @return true if successful
	 */
	@Override
	public boolean charge(Player player) {
		if (!player.getResources().greaterOrEqualTo(getCost())) {
			return false;
		}
		player.getResources().subtract(getCost());
		player.getResources().add(getItem());
		return true;
	}

	/**
	 * @return the {@link ResourceBundle} given from this {@link TradeExchange}
	 */
	@Override
	public ResourceBundle getItem() {
		int[] output = new int[] { 0, 0, 0, 0, 0 };
		output[m_output] = PurchaseCosts.TRADEEXCHANGE_YIELD;
		return new ResourceBundle(output);
	}
}
