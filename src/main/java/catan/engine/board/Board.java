package catan.engine.board;

import catan.engine.board.tile.Tile;

/**
 * Class representing the board for a game of {@link Catan} Tile positions are
 * stored in matrix notation (row, col) as opposed to x,y
 * 
 * @author Jacob
 *
 */
public class Board {

	private Tile[][] m_tileMap;

	/**
	 * Converts from index on `m_tileMap` to position on game map
	 * 
	 * @param index
	 *            array containing int indices
	 * @return array containing int positions
	 */
	public int[] indexToPosition(int[] index) {

		int longestRow = 0; // size of the longest row on the map

		for (Tile[] row : m_tileMap) {
			longestRow = Math.max(longestRow, row.length);
		}

		/* How the math works:
		 *
		 * index of the middle element, or greater middle element in the row
		 * int middleElement = m_tileMap[index[0]].length / 2;
		 *
		 * int middleDistance = index[0] - middleElement;
		 *
		 * int longestMiddle = longestRow / 2;
		 *
		 * return new int[] {index[0], longestMiddle + middleDistance}; */
		
		return new int[] {index[0], (longestRow / 2) + (index[0] - (m_tileMap[index[0]].length / 2))};
	}
}
