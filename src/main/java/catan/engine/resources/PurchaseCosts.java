package catan.engine.resources;

/**
 * Collection of construction costs for {@link Purchase}s
 * @author Jacob
 *
 */
public class PurchaseCosts {

	public static final ResourceBundle VILLAGE_COST = new ResourceBundle(1, 1, 0, 1, 1);
	public static final ResourceBundle CITY_COST = new ResourceBundle(0, 0, 2, 3);
	public static final ResourceBundle ROAD_COST = new ResourceBundle(1, 1);
	public static final ResourceBundle CARD_COST = new ResourceBundle(0, 0, 1, 1, 1);

	public static final int TRADEEXCHANGE_COST = 3;
	public static final int TRADEEXCHANGE_YIELD = 1;
}
