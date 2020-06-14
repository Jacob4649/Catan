package catan.engine.board.objects.buildings.construction;

import catan.engine.board.objects.BoardObject;
import catan.engine.board.objects.buildings.Village;
import catan.engine.player.Player;
import catan.engine.resources.Purchase;
import catan.engine.resources.ResourceBundle;

/**
 * Class representing a construction request
 * 
 * @author Jacob
 *
 */
public abstract class ConstructionRequisition implements Purchase {

	private BoardObject m_construct;
	private ResourceBundle m_cost;
	protected Player m_player;
	
	/**
	 * Creates a new {@link ConstructionRequisition}
	 * 
	 * @param construct
	 *            the {@link BoardObject} to be constructed
	 * @param cost
	 *            the {@link ResourceBundle} containing the cost of constructing
	 *            this
	 */
	public ConstructionRequisition(BoardObject construct, ResourceBundle cost, Player player) {
		m_construct = construct;
		m_cost = cost;
		m_player = player;
	}
	
	/**
	 * @return the {@link ResourceBundle} containing the cost of this building
	 */
	@Override
	public ResourceBundle getCost() {
		return m_cost;
	}
	
	/**
	 * @return the {@link BoardObject} being built by this {@link ConstructionRequisition}
	 */
	@Override
	public BoardObject getItem() {
		return m_construct;
	}
	
	/**
	 * Completes this {@link ConstructionRequisition}
	 * @return true if completed successfully
	 */
	public boolean complete() {
		return m_player.buy(this);
	}

}
