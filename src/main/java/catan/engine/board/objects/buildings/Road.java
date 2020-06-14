package catan.engine.board.objects.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.engine.board.objects.EdgeObject;
import catan.engine.board.objects.InvalidLocationException;
import catan.engine.board.tile.Edge;
import catan.engine.board.tile.EdgeNotInitializedException;
import catan.engine.board.tile.Vertex;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.player.Player;
import catan.renderer.panel.BoardPanel;

/**
 * Class representing a single road
 * 
 * @author Jacob
 *
 */
public class Road extends EdgeObject {

	/**
	 * Creates a new {@link Road}
	 * 
	 * @param owner
	 *            the {@link Player} owning the {@link Road}
	 * @param position
	 *            the {@link Edge} this {@link Road} is on
	 * @param ignoreLocation
	 *            if true, don't throw {@link InvalidLocationException} on
	 *            invalid locations
	 * @throws InvalidLocationException
	 *             if initialized in an invalid location
	 */
	public Road(Player owner, Edge position, boolean ignoreLocation) throws InvalidLocationException {
		super(owner, position, new BufferedImage(
				(int) (0.1 * ((double) BoardPanel.PANEL_HORIZONTAL) / ((double) Board.DEFAULT_BOARD_DIMENSIONS[1])),
				(int) (0.5 * ((double) BoardPanel.PANEL_VERTICAL) / ((double) Board.DEFAULT_BOARD_DIMENSIONS[0])),
				BufferedImage.TYPE_INT_RGB) {
			{
				Graphics2D g2D = createGraphics();
				g2D.setColor(new Color(155, 155, 155));
				g2D.fillRect(0, 0, getWidth(), getHeight());
			}
		}, ignoreLocation);
	}

	/**
	 * Creates a new {@link Road}
	 * 
	 * @param owner
	 *            the {@link Player} owning the {@link Road}
	 * @param position
	 *            the {@link Edge} this {@link Road} is on
	 * @throws InvalidLocationException
	 *             if initialized in an invalid location
	 */
	public Road(Player owner, Edge position) throws InvalidLocationException {
		this(owner, position, false);
	}

	/**
	 * Gets all valid {@link Road} locations on this {@link Board}
	 * 
	 * @param board
	 *            the {@link Board} to check locations on
	 * @param owner
	 *            the hypothetical {@link Player} owning the new {@link Road}
	 * @return array containing all valid {@link Edge}s
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 */
	public static Edge[] getValidLocations(Board board, Player owner) throws BoardNotInitializedException {
		ArrayList<Edge> disallowed = new ArrayList<Edge>();

		board.forAllObjects((object) -> {
			try {
				if (object instanceof Road) {
					disallowed.add(((Road) object).getPosition());
				}
			} catch (BoardObjectNotInitializedException e) {
				e.printStackTrace();
				System.exit(0);
			}
		});

		ArrayList<Edge> edges = new ArrayList<Edge>();

		board.forAllObjects((object) -> {
			try {
				if (object instanceof Road && ((Road) object).getOwner() == owner) {
					ArrayList<Edge> newEdges = new ArrayList<Edge>(
							Arrays.asList(((Road) object).getPosition().getAdjacentEdges()));
					newEdges.removeAll(edges);
					edges.addAll(newEdges);
				}
			} catch (BoardNotInitializedException | VertexNotInitializedException | EdgeNotInitializedException
					| BoardObjectNotInitializedException e) {
				e.printStackTrace();
			}
		});

		edges.removeAll(disallowed);

		Edge[] output = new Edge[edges.size()];
		output = edges.toArray(output);
		return output;
	}

	/**
	 * Determines whether the specified position would be a valid location for a
	 * {@link Road}
	 * 
	 * @param edge
	 *            the specified {@link Edge}
	 * @param owner
	 *            the {@link Player} who would be constructing the {@link Road}
	 * @return true if valid
	 */
	public static boolean isValidLocation(Edge edge, Player owner) {
		try {
			Vertex[] endPoints = edge.getEndPoints();
			for (Vertex vertex : endPoints) {
				if (edge.getBoard().getAllObjectsMatching((object) -> {
					try {
						return object instanceof Road && ((Road) object).getOwner() == owner
								&& (((Road) object).getPosition().getEndPoints()[0].equals(vertex)
										|| ((Road) object).getPosition().getEndPoints()[1].equals(vertex));
					} catch (EdgeNotInitializedException | BoardObjectNotInitializedException e) {
						e.printStackTrace();
						System.exit(0);
						return false;
					}
				}).length > 0 && edge.getBoard().getAllObjectsMatching((object) -> {
					try {
						return object instanceof Road && ((Road) object).getPosition().equals(edge);
					} catch (BoardObjectNotInitializedException e) {
						e.printStackTrace();
						System.exit(0);
						return false;
					}
				}).length == 0) {
					return true;
				}
			}
			return false;
		} catch (EdgeNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return false;
		}
	}

	/**
	 * Determines whether this {@link City} is in a valid location
	 * 
	 * @returns true if this {@link City} is in a valid location
	 */
	@Override
	public boolean validLocation() {
		try {
			return isValidLocation(getPosition(), getOwner());
		} catch (BoardObjectNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return false;
		}
	}

	/**
	 * 
	 * @param mapDimensions
	 *            the dimensions of the map this {@link Road} is on in tiles
	 *            {row, col}
	 * @param panelDimensions
	 *            the dimensions of the panel this {@link Road} is on in pixels
	 *            {x, y}
	 * @return an int array containing the dimensions of the
	 *         {@link BufferedImage} that should be used to display this
	 *         {@link Road}
	 */
	@Override
	public int[] getImageDimensions(int[] mapDimensions, int[] panelDimensions)
			throws BoardObjectNotInitializedException {
		// rotates dimensions if needed
		try {
			if (getPosition().isVertical()) {
				return new int[] { (int) (0.1 * ((double) panelDimensions[0]) / ((double) mapDimensions[1])),
						(int) (0.5 * ((double) panelDimensions[1]) / ((double) mapDimensions[0])) };
			} else {
				// rotate
				return new int[] { (int) (0.5 * ((double) panelDimensions[1]) / ((double) mapDimensions[0])),
						(int) (0.1 * ((double) panelDimensions[0]) / ((double) mapDimensions[1])) };
			}
		} catch (VertexNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return new int[] { 0, 0 };
		}
	}

	@Override
	public String toString() {
		try {
			return "Road: (" + getPosition() + ")";
		} catch (BoardObjectNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return "Road: (Not Initialized)";
		}
	}

}
