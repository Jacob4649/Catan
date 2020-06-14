package catan.engine.board.objects.buildings.construction;

import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.engine.board.objects.InvalidLocationException;
import catan.engine.board.objects.buildings.City;
import catan.engine.board.objects.buildings.Village;
import catan.engine.player.Player;
import catan.engine.resources.PurchaseCosts;

/**
 * Class for upgrading a {@link Village} into a {@link City}. Slight abuse of
 * {@link ConstructionRequisiton}, it really isn't intended to be used in this
 * manner
 * 
 * @author Jacob
 *
 */
public class UpgradeVillage extends ConstructionRequisition {

	/**
	 * Creates a new {@link UpgradeVillage}
	 * 
	 * @param village
	 *            the {@link Village} to upgrade into a {@link City}
	 */
	public UpgradeVillage(Village village) {
		super(village, PurchaseCosts.CITY_COST, village.getOwner());
	}

	/**
	 * Charges the specified {@link Player} and builds a new {@link City}
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
			((Village) getItem()).upgrade();
		} catch (BoardObjectNotInitializedException | InvalidLocationException e) {
			e.printStackTrace();
			System.exit(0);
			return false;
		}
		return true;
	}
}
