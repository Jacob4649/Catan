package catan.engine.board.objects.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.objects.BoardObject;
import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.engine.board.objects.InvalidLocationException;
import catan.engine.board.objects.NoOwnerException;
import catan.engine.board.objects.Productive;
import catan.engine.board.objects.VertexObject;
import catan.engine.board.tile.EdgeNotInitializedException;
import catan.engine.board.tile.Tile;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.Vertex;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.player.Player;
import catan.engine.resources.ResourceBundle;
import catan.engine.resources.ResourceMetric;
import catan.renderer.panel.BoardPanel;

/**
 * Class representing a single village
 * 
 * @author Jacob
 *
 */
public class Village extends VertexObject implements Productive {

	private static final int VILLAGE_PRODUCTIVITY = 1;

	/**
	 * Creates a {@link Village}
	 * 
	 * @param owner
	 *            the {@link Player} owning the village
	 * @param position
	 *            the {@link Vertex} where the village is positioned
	 * @param ignoreLocation
	 *            if true, don't throw {@link InvalidLocationException} on
	 *            invalid locations
	 * @throws InvalidLocationException
	 *             if initialized in an invalid location
	 */
	public Village(Player owner, Vertex position, boolean ignoreLocation) throws InvalidLocationException {
		super(owner, position, new BufferedImage(
				(int) (0.4 * ((double) BoardPanel.PANEL_HORIZONTAL) / ((double) Board.DEFAULT_BOARD_DIMENSIONS[1])),
				(int) (0.4 * ((double) BoardPanel.PANEL_VERTICAL) / ((double) Board.DEFAULT_BOARD_DIMENSIONS[0])),
				BufferedImage.TYPE_INT_RGB) {
			{
				Graphics2D g2D = createGraphics();
				g2D.setColor(new Color(200, 200, 200));
				g2D.fillRect(0, 0, getWidth(), getHeight());
			}
		}, ignoreLocation);
	}

	/**
	 * Creates a {@link Village}
	 * 
	 * @param owner
	 *            the {@link Player} owning the village
	 * @param position
	 *            the {@link Vertex} where the village is positioned
	 * @throws InvalidLocationException
	 *             if initialized in an invalid location
	 */
	public Village(Player owner, Vertex position) throws InvalidLocationException {
		this(owner, position, false);
	}

	/**
	 * Upgrades this {@link Village} into an {@link City}
	 * 
	 * @return the new {@link City}
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link BoardObject} is not initialized
	 * @throws InvalidLocationException
	 *             if created in an invalid location
	 */
	public City upgrade() throws BoardObjectNotInitializedException, InvalidLocationException {
		getPosition().getBoard().removeObject(this);
		City city = new City(getOwner(), getPosition());
		getPosition().getBoard().addObject(city);
		return city;
	}

	/**
	 * Gets all valid {@link Village} locations on this {@link Board}
	 * 
	 * @param board
	 *            the {@link Board} to check locations on
	 * @param owner
	 *            the hypothetical {@link Player} owning the new {@link Village}
	 * @return array containing all valid {@link Vertex} (vertices)
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 */
	public static Vertex[] getValidLocations(Board board, Player owner) throws BoardNotInitializedException {
		int[][] map = new int[board.getDimensions()[0]][board.getDimensions()[1]];

		board.forAllObjects((object) -> {
			try {
				if (object instanceof Road && ((Road) object).getOwner() == owner) {
					if (map[((Road) object).getPosition().getEndPoints()[0]
							.getPosition()[0]][((Road) object).getPosition().getEndPoints()[0].getPosition()[1]] == 0) {
						map[((Road) object).getPosition().getEndPoints()[0]
								.getPosition()[0]][((Road) object).getPosition().getEndPoints()[0]
										.getPosition()[1]] = 1;
					}
					if (map[((Road) object).getPosition().getEndPoints()[1]
							.getPosition()[0]][((Road) object).getPosition().getEndPoints()[1].getPosition()[1]] == 0) {
						map[((Road) object).getPosition().getEndPoints()[1]
								.getPosition()[0]][((Road) object).getPosition().getEndPoints()[1]
										.getPosition()[1]] = 1;
					}
				}
				if (object instanceof Village || object instanceof City) {
					if (((Vertex) object.getPosition()).getPosition()[0] >= 0
							&& ((Vertex) object.getPosition()).getPosition()[0] < board.getVertexDimensions()[0]
							&& ((Vertex) object.getPosition()).getPosition()[1] >= 0
							&& ((Vertex) object.getPosition()).getPosition()[1] < board.getVertexDimensions()[1]) {
						// if valid vertex
						map[((Vertex) object.getPosition()).getPosition()[0]][((Vertex) object.getPosition())
								.getPosition()[1]] = 2;
					}

					if (((Vertex) object.getPosition()).getPosition()[0] + 1 >= 0
							&& ((Vertex) object.getPosition()).getPosition()[0] + 1 < board.getVertexDimensions()[0]
							&& ((Vertex) object.getPosition()).getPosition()[1] >= 0
							&& ((Vertex) object.getPosition()).getPosition()[1] < board.getVertexDimensions()[1]) {
						// if valid vertex
						map[((Vertex) object.getPosition()).getPosition()[0] + 1][((Vertex) object.getPosition())
								.getPosition()[1]] = 2;
					}

					if (((Vertex) object.getPosition()).getPosition()[0] - 1 >= 0
							&& ((Vertex) object.getPosition()).getPosition()[0] < board.getVertexDimensions()[0]
							&& ((Vertex) object.getPosition()).getPosition()[1] >= 0
							&& ((Vertex) object.getPosition()).getPosition()[1] < board.getVertexDimensions()[1]) {
						// if valid vertex
						map[((Vertex) object.getPosition()).getPosition()[0] - 1][((Vertex) object.getPosition())
								.getPosition()[1]] = 2;
					}

					if (((Vertex) object.getPosition()).getPosition()[0] >= 0
							&& ((Vertex) object.getPosition()).getPosition()[0] < board.getVertexDimensions()[0]
							&& ((Vertex) object.getPosition()).getPosition()[1] + 1 >= 0
							&& ((Vertex) object.getPosition()).getPosition()[1] + 1 < board.getVertexDimensions()[1]) {
						// if valid vertex
						map[((Vertex) object.getPosition()).getPosition()[0]][((Vertex) object.getPosition())
								.getPosition()[1] + 1] = 2;
					}

					if (((Vertex) object.getPosition()).getPosition()[0] >= 0
							&& ((Vertex) object.getPosition()).getPosition()[0] < board.getVertexDimensions()[0]
							&& ((Vertex) object.getPosition()).getPosition()[1] - 1 >= 0
							&& ((Vertex) object.getPosition()).getPosition()[1] - 1 < board.getVertexDimensions()[1]) {
						// if valid vertex
						map[((Vertex) object.getPosition()).getPosition()[0]][((Vertex) object.getPosition())
								.getPosition()[1] - 1] = 2;
					}
				}
			} catch (VertexNotInitializedException | BoardObjectNotInitializedException | BoardNotInitializedException
					| EdgeNotInitializedException e) {
				e.printStackTrace();
				System.exit(0);
			}
		});

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] == 1) {
					vertices.add(board.getVertex(i, j));
				}
			}
		}
		Vertex[] output = new Vertex[vertices.size()];
		output = vertices.toArray(output);
		return output;
	}

	/**
	 * Determines whether the specified position would be a valid location for a
	 * {@link Village}
	 * 
	 * @param vertex
	 *            the specified {@link Vertex}
	 * @param owner
	 *            the {@link Player} who will be constructing this {@link Road}
	 * @return true if valid
	 */
	public static boolean isValidLocation(Vertex vertex, Player owner) {
		return vertex.getBoard().getAllObjectsMatching((object) -> {
			try {
				return (object instanceof Village || object instanceof City)
						&& (((Vertex) object.getPosition()).isAdjacent(vertex)
								|| ((Vertex) object.getPosition()).equals(vertex));
			} catch (VertexNotInitializedException | BoardObjectNotInitializedException e) {
				e.printStackTrace();
				System.exit(0);
				return false;
			}
		}).length == 0 && vertex.getBoard().getAllObjectsMatching((object) -> {
			try {
				return object instanceof Road && ((Road) object).getOwner() == owner
						&& (((Road) object).getPosition().getEndPoints()[0].equals(vertex)
								|| ((Road) object).getPosition().getEndPoints()[1].equals(vertex));
			} catch (EdgeNotInitializedException | BoardObjectNotInitializedException e) {
				e.printStackTrace();
				System.exit(0);
				return false;
			}
		}).length > 0;
	}

	/**
	 * Determines whether this {@link Village} is in a valid location
	 * 
	 * @returns true if this {@link Village} is in a valid location
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
	 * @param frequency
	 *            the int frequency of this turn
	 * @return {@link ResourceBundle} containing all resources this
	 *         {@link Village} produces in a turn
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link BoardObject} has not been initialized
	 * @throws VertexNotInitializedException
	 *             if the {@link Vertex} this {@link Village} is on has not been
	 *             initialized
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 * @throws TileNotInitializedException
	 *             if any {@link Tile}s have not been initialized
	 */
	@Override
	public ResourceBundle getResources(int frequency) throws TileNotInitializedException, BoardNotInitializedException,
			VertexNotInitializedException, BoardObjectNotInitializedException {
		ResourceBundle bundle = new ResourceBundle();
		for (Tile tile : getPosition().getAdjacentTiles()) {
			bundle.add(tile.getResources(VILLAGE_PRODUCTIVITY, frequency));
		}
		return bundle;
	}

	/**
	 * Adds all resources produced by this {@link Village} to the owning
	 * {@link Player}'s {@link ResourceBundle}
	 * 
	 * @param frequency
	 *            the int frequency of this turn
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link Village} has not been initialized
	 * @throws VertexNotInitializedException
	 *             if the {@link Vertex} this {@link Village} is on has not been
	 *             initialized
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 * @throws TileNotInitializedException
	 *             if any {@link Tile}s have not been initialized
	 * @throws NoOwnerException
	 *             if this {@link Village} is not owned by an {@link Player}
	 */
	@Override
	public void giveResourcesToOwner(int frequency) throws TileNotInitializedException, BoardNotInitializedException,
			VertexNotInitializedException, BoardObjectNotInitializedException, NoOwnerException {
		if (getOwner() == null) {
			throw new NoOwnerException(this);
		}
		getOwner().getResources().add(getResources(frequency));
	}

	/**
	 * 
	 * @return {@link ResourceMetric} for this {@link Village}
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link Village} has not been initialized
	 * @throws VertexNotInitializedException
	 *             if the {@link Vertex} this {@link Village} is on has not been
	 *             initialized
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} this {@link Village} is on has not been
	 *             initialized
	 * @throws TileNotInitializedException
	 *             if any {@link Tile}s surrounding this {@link Village} have
	 *             not been initialized
	 */
	@Override
	public ResourceMetric getMetric() throws TileNotInitializedException, BoardNotInitializedException,
			VertexNotInitializedException, BoardObjectNotInitializedException {

		int[] metric = new int[ResourceBundle.RESOURCE_NUMBER];

		for (Tile tile : getPosition().getAdjacentTiles()) {
			if (tile.getTileType().getResource() != ResourceBundle.NULL) {
				metric[tile.getTileType().getResource()] += VILLAGE_PRODUCTIVITY * Tile.getFrequencyProbability(tile.getFrequency());
			}
		}

		return new ResourceMetric(metric);
	}

	/**
	 * 
	 * @param mapDimensions
	 *            the dimensions of the map this {@link Village} is on in tiles
	 *            {row, col}
	 * @param panelDimensions
	 *            the dimensions of the panel this {@link Village} is on in
	 *            pixels {x, y}
	 * @return an int array containing the dimensions of the
	 *         {@link BufferedImage} that should be used to display this
	 *         {@link Village}
	 */
	@Override
	public int[] getImageDimensions(int[] mapDimensions, int[] panelDimensions) {
		return new int[] { (int) (0.4 * ((double) panelDimensions[0] / (double) mapDimensions[1])),
				(int) (0.4 * ((double) panelDimensions[1] / (double) mapDimensions[0])) };
	}

	@Override
	public String toString() {
		try {
			return "Village: (" + getPosition() + ")";
		} catch (BoardObjectNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return "Village: (Not Initialized)";
		}
	}

}
