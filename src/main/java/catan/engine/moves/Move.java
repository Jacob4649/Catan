package catan.engine.moves;

import catan.engine.board.Board;

/**
 * Interface representing a single {@link Move} which is made by a
 * {@link Player}, chains of {@link Move}s are strung together, constituting a
 * turn
 * 
 * @author Jacob
 *
 */
public interface Move {

	/**
	 * Applies this move
	 */
	public void apply();
	
}
