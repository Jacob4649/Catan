package catan.engine.board.objects.buildings.construction;

import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.engine.board.objects.buildings.Village;
import catan.engine.player.Player;
import catan.engine.resources.PurchaseCosts;

/**
 * Class for constructing an {@link Village}
 * 
 * @author Jacob
 *
 */
public class ConstructVillage extends ConstructionRequisition {

	/**
	 * Creates a new {@link ConstructVillage}
	 * 
	 * @param village
	 *            the {@link Village} to construct
	 */
	public ConstructVillage(Village village) {
		super(village, PurchaseCosts.VILLAGE_COST, village.getOwner());
	}

	/**
	 * Charges the specified {@link Player} and builds a new {@link Village}
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
			((Village) getItem()).getPosition().getBoard().addObject(getItem());
		} catch (BoardObjectNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return false;
		}
		return true;
	}

}
