package catan.engine.board.tile;

import catan.Catan;
import catan.engine.board.Board;

/**
 * Class representing an edge between two {@link Tile}s on the {@link Catan}
 * {@link Board}
 * 
 * @author Jacob
 *
 */
public class Edge {

	private Vertex m_vertex1;
	private Vertex m_vertex2;

	/**
	 * Creates a new {@link Edge}
	 * 
	 * @param vertex1
	 *            int array containing the position of the first {@link Vertex}
	 *            {row, col}
	 * @param vertex2
	 *            int array containing the position of the second {@link Vertex}
	 *            {row, col}
	 * @param board
	 *            the {@link Board} both {@link Vertex} (Vertices) are to be
	 *            created on
	 */
	public Edge(int[] vertex1, int[] vertex2, Board board) {
		m_vertex1 = board.getVertex(vertex1[0], vertex1[1]);
		m_vertex2 = board.getVertex(vertex2[0], vertex2[1]);
	}

	/**
	 * Gets the {@link Vertex} (Vertices) at either end of this {@link Edge}
	 * 
	 * @return {@link Vertex} array
	 */
	public Vertex[] getEndPoints() throws EdgeNotInitializedException {
		if (m_vertex1 == null || m_vertex2 == null) {
			throw new EdgeNotInitializedException();
		}
		return new Vertex[] { m_vertex1, m_vertex2 };
	}

	/**
	 * Gets the {@link Board} this {@link Edge} is on
	 * 
	 * @return the {@link Board} this {@link Edge} is on
	 * @throws EdgeNotInitializedException
	 *             if this {@link Edge} has not been initialized
	 */
	public Board getBoard() throws EdgeNotInitializedException {
		if (getEndPoints()[0].getBoard() != getEndPoints()[1].getBoard()) {
			throw new EdgeNotInitializedException();
		}
		return getEndPoints()[0].getBoard();
	}

	/**
	 * 
	 * @return true if this {@link Edge} is vertical
	 * @throws VertexNotInitializedException
	 *             if either {@link Vertex} wasn't initialized correctly
	 */
	public boolean isVertical() throws VertexNotInitializedException {
		return getDelta()[0] == 0;
	}

	/**
	 * 
	 * @return int array containing the distance between the two {@link Vertex}
	 *         (vertices) of this {@link Edge} {x/col, y/row}
	 * @throws VertexNotInitializedException
	 */
	private int[] getDelta() throws VertexNotInitializedException {
		return m_vertex1.getDistanceFrom(m_vertex2);
	}

	@Override
	public String toString() {
		return "Edge Object: (" + m_vertex1 + ", " + m_vertex2 + ")";
	}

}
