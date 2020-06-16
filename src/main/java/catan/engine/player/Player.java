package catan.engine.player;

import catan.Catan;
import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.objects.InvalidLocationException;
import catan.engine.board.objects.buildings.City;
import catan.engine.board.objects.buildings.Village;
import catan.engine.moves.Move;
import catan.engine.resources.Purchase;
import catan.engine.resources.ResourceBundle;
import catan.engine.resources.ResourceMetric;

/**
 * Class representing a single player (can be user or opponent)
 * 
 * @author Jacob
 *
 */
public class Player {

	private PlayerColor m_color;
	private ResourceBundle m_resources = new ResourceBundle();

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
	 * 
	 * @param board
	 *            the {@link Board} to get for
	 * @return this {@link Player}'s production {@link ResourceMetric} on the
	 *         specified {@link Board}
	 */
	public ResourceMetric getProductionMetric(Board board) {
		return new ResourceMetric(board, this);
	}

	/**
	 * {@link Player} attempts to buy the specified item
	 * 
	 * @param purchase
	 *            the {@link Purchase} to buy
	 * @return true if the {@link Purchase} was competed successfully
	 */
	public boolean buy(Purchase purchase) {
		return purchase.charge(this);
	}

	/**
	 * Gets the total number of victory points this {@link Player} has
	 * 
	 * @param board
	 *            the {@link Board} to calculate victory points on
	 * @return the number of victory points
	 */
	public int getVictoryPoints(Board board) {
		int sum = 0;

		sum += board.getAllObjectsMatching((object) -> object instanceof City && object.getOwner() == this).length
				* City.VICTORY_POINTS;
		sum += board.getAllObjectsMatching((object) -> object instanceof Village && object.getOwner() == this).length
				* Village.VICTORY_POINTS;

		return sum;
	}

	/**
	 * This {@link Player} takes their turn in the provided {@link Catan} game
	 * 
	 * @param catan
	 *            the specified {@link Catan} game
	 * @throws InvalidLocationException
	 *             if any invalid locations are present on the {@link Board}
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} is not initialized
	 */
	public void takeTurn(Catan catan) throws BoardNotInitializedException, InvalidLocationException {
		Move[] moves = catan.getBoard().getMovesForPlayer(this);
		if (moves.length > 0) {
			moves[(int) (Math.random() * moves.length)].apply();	
		}
	}

}
