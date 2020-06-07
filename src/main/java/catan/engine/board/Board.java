package catan.engine.board;

import java.util.ArrayList;

import catan.Catan;
import catan.engine.board.objects.BoardObject;
import catan.engine.board.tile.Tile;
import catan.engine.board.tile.TileNotInitializedException;

/**
 * Class representing the board for a game of {@link Catan} Tile positions are
 * stored in matrix notation (row, col) as opposed to x,y
 * 
 * @author Jacob
 *
 */
public class Board {

	private Tile[][] m_tileMap;
	private ArrayList<BoardObject> m_objects;

	/**
	 * Creates a new {@link Board}
	 * @param map the {@link Tile}s to populate the {@link Board} with
	 */
	public Board(Tile[][] map) {
		m_tileMap = map;
	}
	
	/**
	 * 
	 * @return an int array containing the dimensions of the map {rows, cols}
	 * @throws BoardNotInitializedException
	 *             if the board has not been initialized
	 */
	public int[] getDimensions() throws BoardNotInitializedException {
		try {
			return new int[] { m_tileMap.length, m_tileMap[0].length };
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new BoardNotInitializedException();
		}
	}

	/**
	 * Gets the {@link Tile} at the specified position
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the column
	 * @return the {@link Tile}
	 * @throws TileNotInitializedException
	 *             if the tile has not been initialized
	 */
	public Tile getTileAt(int row, int col) throws TileNotInitializedException, BoardNotInitializedException {
		if (m_tileMap[row][col] == null) {
			throw new TileNotInitializedException(row, col);
		}
		return m_tileMap[row][col];
	}

}
