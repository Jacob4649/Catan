package catan.engine.board.objects.buildings.construction;

import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.engine.board.objects.buildings.Road;
import catan.engine.board.tile.EdgeNotInitializedException;
import catan.engine.player.Player;
import catan.engine.resources.PurchaseCosts;

/**
 * Class for building a new {@link Road}
 * 
 * @author Jacob
 *
 */
public class ConstructRoad extends ConstructionRequisition {
	
	/**
	 * Creates a new {@link ConstructRoad}
	 * 
	 * @param road
	 *            the {@link Road} to construct
	 */
	public ConstructRoad(Road road) {
		super(road, PurchaseCosts.ROAD_COST, road.getOwner());
	}

	/**
	 * Charges the specified {@link Player} and builds a new {@link Road}
	 * 
	 * @param player
	 *            the {@link Player} to charge
	 * @return true if successful
	 */
	@Override
	public boolean charge(Player player) {
		if (!player.getResources().greaterOrEqualTo(getCost())) {
			return false;
		}
		player.getResources().subtract(getCost());
		try {
			((Road) getItem()).getPosition().getBoard().addObject(getItem());
		} catch (BoardObjectNotInitializedException | EdgeNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return false;
		}
		return true;
	}
}
