package catan.engine.board.tile;

import java.util.ArrayList;
import java.util.Collections;

import catan.Catan;
import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;

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
	 * @return array of all {@link Edge}s adjacent to this one
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} hasn't been initialized
	 * @throws VertexNotInitializedException
	 *             if any {@link Vertex} hasn't been initialized
	 * @throws EdgeNotInitializedException
	 *             if the {@link Edge} hasn't been initialized
	 */
	public Edge[] getAdjacentEdges()
			throws BoardNotInitializedException, VertexNotInitializedException, EdgeNotInitializedException {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Vertex vertex : getEndPoints()[0].getAdjacentVertices()) {
			edges.add(new Edge(getEndPoints()[0].getPosition(), vertex.getPosition(), getBoard()));
		}
		for (Vertex vertex : getEndPoints()[1].getAdjacentVertices()) {
			edges.add(new Edge(getEndPoints()[1].getPosition(), vertex.getPosition(), getBoard()));
		}
		edges.removeAll(Collections.singleton(this));
		Edge[] output = new Edge[edges.size()];
		output = edges.toArray(output);
		return output;
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
	public boolean equals(Object object) {
		try {
			return object instanceof Edge && ((((Edge) object).getEndPoints()[0].equals(getEndPoints()[0])
					&& ((Edge) object).getEndPoints()[1].equals(getEndPoints()[1]))
					|| (((Edge) object).getEndPoints()[0].equals(getEndPoints()[1])
							&& ((Edge) object).getEndPoints()[1].equals(getEndPoints()[0])));
		} catch (EdgeNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return false;
		}
	}

	@Override
	public String toString() {
		return "Edge: (" + m_vertex1 + ", " + m_vertex2 + ")";
	}

}
