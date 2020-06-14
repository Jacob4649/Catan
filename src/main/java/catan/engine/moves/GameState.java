package catan.engine.moves;

import catan.Catan;
import catan.engine.board.Board;
import catan.engine.board.tile.Tile;

/**
 * Class representing the state of the {@link Catan} game at a single point in
 * time
 * 
 * @author Jacob
 *
 */
public class GameState extends Board {

	/**
	 * Creates a new {@link GameState}
	 * @param map
	 */
	public GameState(Tile[][] map) {
		super(map);
	}
	
}
