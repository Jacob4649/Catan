package catan.engine.moves;

import catan.engine.player.Player;
import catan.engine.resources.Purchase;

/**
 * Class representing a {@link Purchase} as a {@link Move}
 * @author Jacob
 *
 */
public class PurchaseMove implements Move {

	private Purchase m_purchase;
	private Player m_player;
	
	/**
	 * Creates a new {@link PurchaseMove}
	 * @param purchase the {@link Purchase} to use
	 * @param player this {@link Player} to use
	 */
	public PurchaseMove(Purchase purchase, Player player) {
		m_purchase = purchase;
		m_player = player;
	}

	/**
	 * 
	 * @return the {@link Purchase} associated with this {@link Move}
	 */
	public Purchase getPurchase() {
		return m_purchase;
	}

	/**
	 * Applies this {@link Purchase}
	 */
	@Override
	public void apply() {
		m_player.buy(m_purchase);
	}
	
	@Override
	public String toString() {
		return "Purchase Move: (" + m_purchase + ")";
	}
}
