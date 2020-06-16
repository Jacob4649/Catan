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
	 * 
	 * @param source
	 *            the {@link ResourceBundle} paying the cost
	 * @param cost
	 *            the cost {@link ResourceBundle}
	 * @return true if source bundle can afford cost bundle once trade is
	 *         factored in
	 */
	public static TradeInfo getTradeStats(ResourceBundle source, ResourceBundle cost) {
		if (source.greaterOrEqualTo(cost)) {
			TradeInfo info = new TradeInfo();
			info.m_tradeAffordable = true;
			return info;
		}

		int[] tradeable = new int[] {
				PurchaseCosts.TRADEEXCHANGE_YIELD * (source.getWood() / PurchaseCosts.TRADEEXCHANGE_COST),
				PurchaseCosts.TRADEEXCHANGE_YIELD * (source.getClay() / PurchaseCosts.TRADEEXCHANGE_COST),
				PurchaseCosts.TRADEEXCHANGE_YIELD * (source.getStone() / PurchaseCosts.TRADEEXCHANGE_COST),
				PurchaseCosts.TRADEEXCHANGE_YIELD * (source.getGrain() / PurchaseCosts.TRADEEXCHANGE_COST),
				PurchaseCosts.TRADEEXCHANGE_YIELD * (source.getSheep() / PurchaseCosts.TRADEEXCHANGE_COST) };

		int[][] costs = new int[][] { { ResourceBundle.WOOD, cost.getWood() }, { ResourceBundle.CLAY, cost.getClay() },
				{ ResourceBundle.STONE, cost.getStone() }, { ResourceBundle.GRAIN, cost.getGrain() },
				{ ResourceBundle.SHEEP, cost.getSheep() } };

		// sort costs from least to greatest with insertion sort
		for (int i = 1; i < costs.length; i++) {
			int[] temp = costs[i];

			int j = i;
			while (j > 0 && costs[j - 1][1] > temp[1]) {
				costs[j] = costs[j - 1];
				j--;
			}
			costs[j] = temp;
		}

		int[] missing = new int[] { (cost.getWood() > source.getWood() ? cost.getWood() - source.getWood() : 0),
				(cost.getClay() > source.getClay() ? cost.getClay() - source.getClay() : 0),
				(cost.getStone() > source.getStone() ? cost.getStone() - source.getStone() : 0),
				(cost.getGrain() > source.getGrain() ? cost.getGrain() - source.getGrain() : 0),
				(cost.getSheep() > source.getSheep() ? cost.getSheep() - source.getSheep() : 0) };

		int totalMissing = 0;

		for (int i : missing) {
			totalMissing += i;
		}

		int[] trades = new int[costs.length];

		for (int i = 0; i < costs.length; i++) {
			int resource = costs[i][0];
			if (missing[resource] > 0) {
				continue;
			}
			while (totalMissing > 0) {
				if (cost.getRawBundle()[resource] > source.getRawBundle()[resource]
						- (trades[resource] + 1) * PurchaseCosts.TRADEEXCHANGE_COST) {
					// trading more will make you unable to afford resource
					break;
				}
				trades[resource]++;
				totalMissing--;
			}
		}
		
		if (totalMissing > 0) {
			TradeInfo info = new TradeInfo();
			info.m_tradeAffordable = false;
			return info;
		} else {
			TradeInfo info = new TradeInfo();
			info.m_tradeAffordable = true;

			int missingIndex = 0;
			for (int resource = 0; resource < trades.length; resource++) {
				for (int i = 0; i < trades[resource]; i++) {
					if (missing[missingIndex] > 0) {
						info.m_trades.add(new TradeExchange(resource, missingIndex));
						missing[missingIndex]--;
					} else {
						missingIndex++;
						i--;
					}
				}
			}

			return info;
		}
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

	@Override
	public String toString() {
		return "TradeExchange: (" + PurchaseCosts.TRADEEXCHANGE_COST + "x " + ResourceBundle.toString(m_input) + " For "
				+ PurchaseCosts.TRADEEXCHANGE_YIELD + "x " + ResourceBundle.toString(m_output) + ")";
	}
}
