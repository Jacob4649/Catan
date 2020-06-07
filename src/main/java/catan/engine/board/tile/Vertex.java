package catan.engine.board.tile;

import java.util.ArrayList;

import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;

/**
 * Class representing a vertex on the Catan board
 * 
 * @author Jacob
 *
 */
public class Vertex {

	private int m_row = -1;
	private int m_column = -1;
	private Board m_board;

	/**
	 * Creates a new {@link Vertex}
	 * 
	 * @param row
	 *            the row the vertex is on
	 * @param col
	 *            the column the vertex is on
	 * @param board
	 *            the {@link Vertex} is on
	 */
	public Vertex(int row, int col, Board board) {
		m_row = row;
		m_column = col;
		m_board = board;
	}

	/**
	 * 
	 * @return an int array containing the position of this {@link Vertex} {row, col}
	 */
	public int[] getPosition() {
		return new int[] {m_row, m_column};
	}
	
	/**
	 * 
	 * @return an array of {@link Tile}s surrounding this {@link Vertex}
	 * @throws BoardNotInitializedException if {@link Board} provided has not been initialized
	 * @throws TileNotInitializedException if a {@link Tile} has not been initialized
	 */
	public Tile[] getAdjacentTiles() throws TileNotInitializedException, BoardNotInitializedException {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		try {
			tiles.add(m_board.getTileAt(m_row, m_column));
		} catch (ArrayIndexOutOfBoundsException e) {	
		}
		try {
			tiles.add(m_board.getTileAt(m_row - 1, m_column));
		} catch (ArrayIndexOutOfBoundsException e) {	
		}
		try {
			tiles.add(m_board.getTileAt(m_row, m_column - 1));
		} catch (ArrayIndexOutOfBoundsException e) {	
		}
		try {
			tiles.add(m_board.getTileAt(m_row - 1, m_column - 1));
		} catch (ArrayIndexOutOfBoundsException e) {	
		}
		Tile[] output = new Tile[tiles.size()];
		output = tiles.toArray(output);
		return output;
	}

}
