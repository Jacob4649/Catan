package catan.engine.board.tile;

import java.util.ArrayList;
import java.util.Arrays;

import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;

/**
 * Class representing a vertex on the {@link Catan} {@link Board}
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
	 * @return an int array containing the position of this {@link Vertex} {row,
	 *         col}
	 * @throws VertexNotInitializedException
	 *             if this {@link Vertex} has not been initialized
	 */
	public int[] getPosition() throws VertexNotInitializedException {
		if (m_row == -1 || m_column == -1) {
			throw new VertexNotInitializedException();
		}
		return new int[] { m_row, m_column };
	}

	/**
	 * 
	 * @return an array of {@link Tile}s surrounding this {@link Vertex}
	 * @throws BoardNotInitializedException
	 *             if {@link Board} provided has not been initialized
	 * @throws TileNotInitializedException
	 *             if a {@link Tile} has not been initialized
	 * @throws VertexNotInitializedException
	 *             if this {@link Vertex} has not been initialized
	 */
	public Tile[] getAdjacentTiles()
			throws TileNotInitializedException, BoardNotInitializedException, VertexNotInitializedException {
		if (m_row == -1 || m_column == -1) {
			throw new VertexNotInitializedException();
		}
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

	/**
	 * 
	 * @return array of all {@link Vertex} (vertices) adjacent to this one
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 * @throws VertexNotInitializedException
	 *             if this {@link Vertex} has not been initialized
	 */
	public Vertex[] getAdjacentVertices() throws BoardNotInitializedException, VertexNotInitializedException {
		if (m_row == -1 || m_column == -1) {
			throw new VertexNotInitializedException();
		}
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		if (m_row + 1 >= 0 && m_row + 1 < m_board.getVertexDimensions()[0] && m_column >= 0
				&& m_column < m_board.getVertexDimensions()[1]) {
			// if valid vertex
			vertices.add(m_board.getVertex(m_row + 1, m_column));
		}

		if (m_row - 1 >= 0 && m_row - 1 < m_board.getVertexDimensions()[0] && m_column >= 0
				&& m_column < m_board.getVertexDimensions()[1]) {
			// if valid vertex
			vertices.add(m_board.getVertex(m_row - 1, m_column));
		}

		if (m_row >= 0 && m_row < m_board.getVertexDimensions()[0] && m_column + 1 >= 0
				&& m_column + 1 < m_board.getVertexDimensions()[1]) {
			// if valid vertex
			vertices.add(m_board.getVertex(m_row, m_column + 1));
		}

		if (m_row >= 0 && m_row < m_board.getVertexDimensions()[0] && m_column - 1 >= 0
				&& m_column - 1 < m_board.getVertexDimensions()[1]) {
			// if valid vertex
			vertices.add(m_board.getVertex(m_row, m_column - 1));
		}

		Vertex[] output = new Vertex[vertices.size()];
		output = vertices.toArray(output);
		return output;
	}

	/**
	 * Determines whether this {@link Vertex} is adjacent to the specified
	 * {@link Vertex}
	 * 
	 * @param vertex
	 *            the {@link Vertex} to determine for
	 * @return true if adjacent
	 */
	public boolean isAdjacent(Vertex vertex) throws VertexNotInitializedException {
		return Math.abs(getPosition()[0] - vertex.getPosition()[0])
				+ Math.abs(getPosition()[1] - vertex.getPosition()[1]) == 1;
	}

	/**
	 * 
	 * @return the {@link Board} this {@link Vertex} is on
	 */
	public Board getBoard() {
		return m_board;
	}

	/**
	 * 
	 * @param vertex
	 *            the {@link Vertex} to get distance from
	 * @return int array containing a vector going from this {@link Vertex} to
	 *         the specified {@link Vertex} {x/col, y/row}
	 * @throws VertexNotInitializedException
	 *             if either {@link Vertex} has not been initialized
	 */
	public int[] getDistanceFrom(Vertex vertex) throws VertexNotInitializedException {
		return new int[] { vertex.getPosition()[1] - getPosition()[1], vertex.getPosition()[0] - getPosition()[0] };
	}

	@Override
	public String toString() {
		return "Vertex: (" + m_row + ", " + m_column + ", " + m_board + ")";
	}

	@Override
	public boolean equals(Object object) {
		try {
			return object instanceof Vertex && ((Vertex) object).getBoard() == getBoard()
					&& Arrays.equals(((Vertex) object).getPosition(), getPosition());
		} catch (VertexNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return false;
		}
	}

}
