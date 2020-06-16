package catan.engine.board;

import java.util.ArrayList;

import catan.Catan;
import catan.engine.board.objects.BoardObject;
import catan.engine.board.objects.BoardObjectConsumer;
import catan.engine.board.objects.BoardObjectMatcher;
import catan.engine.board.objects.InvalidLocationException;
import catan.engine.board.objects.buildings.City;
import catan.engine.board.objects.buildings.Road;
import catan.engine.board.objects.buildings.Village;
import catan.engine.board.objects.buildings.construction.ConstructRoad;
import catan.engine.board.objects.buildings.construction.ConstructVillage;
import catan.engine.board.objects.buildings.construction.UpgradeVillage;
import catan.engine.board.tile.Edge;
import catan.engine.board.tile.Tile;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.TileType;
import catan.engine.board.tile.Vertex;
import catan.engine.moves.Move;
import catan.engine.moves.PurchaseMove;
import catan.engine.player.Player;
import catan.engine.resources.PurchaseCosts;
import catan.engine.resources.ResourceBundle;
import catan.engine.resources.ResourceMetric;

/**
 * Class representing the board for a game of {@link Catan} Tile positions are
 * stored in matrix notation (row, col) as opposed to x,y
 * 
 * @author Jacob
 *
 */
public class Board {

	public static final int[] DEFAULT_BOARD_DIMENSIONS = new int[] { 6, 6 };
	public static final int[] MINIMUM_BOARD_DIMENSIONS = new int[] { 1, 1 };
	public static final int[] MAXIMUM_BOARD_DIMENSIONS = new int[] { 255, 255 };

	protected Tile[][] m_tileMap;
	protected ArrayList<BoardObject> m_objects = new ArrayList<BoardObject>();
	private BoardObjectConsumer m_objectAddedListener;

	/**
	 * 
	 * @return a random, landlocked {@link Board} of the default size
	 */
	public static Board randomLandBoard() {
		return randomLandBoard(DEFAULT_BOARD_DIMENSIONS);
	}

	/**
	 * 
	 * @param dimensions
	 *            an int array containing the dimensions of the board you would
	 *            like to generate {row, col}
	 * @return a random, landlocked {@link Board}
	 */
	public static Board randomLandBoard(int[] dimensions) {
		int tileNum = dimensions[0] * dimensions[1];
		int[] frequencies = Tile.getFrequencies(tileNum);
		TileType[] types = TileType.getTileTypes(tileNum, TileType.COAST, TileType.OCEAN);
		Tile[][] map = new Tile[dimensions[0]][dimensions[1]];
		for (int row = 0; row < dimensions[0]; row++) {
			for (int col = 0; col < dimensions[1]; col++) {
				int tileIndex = row * dimensions[1] + col;
				map[row][col] = new Tile(frequencies[tileIndex], types[tileIndex]);
			}
		}
		return new Board(map);
	}

	/**
	 * Creates a new {@link Board}
	 * 
	 * @param map
	 *            the {@link Tile}s to populate the {@link Board} with
	 */
	public Board(Tile[][] map) {
		m_tileMap = map;
	}

	/**
	 * 
	 * @return an int array containing the dimensions of the map {rows, cols}
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 */
	public int[] getDimensions() throws BoardNotInitializedException {
		try {
			return new int[] { m_tileMap.length, m_tileMap[0].length };
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new BoardNotInitializedException();
		}
	}

	/**
	 * 
	 * @return an int array containing the dimensions of the {@link Vertex}
	 *         (Vertices) on the map {rows, col}
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 */
	public int[] getVertexDimensions() throws BoardNotInitializedException {
		return new int[] { getDimensions()[0] + 1, getDimensions()[1] + 1 };
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
	public Tile getTileAt(int row, int col) throws TileNotInitializedException {
		if (m_tileMap[row][col] == null) {
			throw new TileNotInitializedException(row, col);
		}
		return m_tileMap[row][col];
	}

	/**
	 * Gets all {@link Tile}s adjacent to the specified one as shown
	 * <p>
	 * 
	 * X = Specified {@link Tile}
	 * 
	 * <br>
	 * 
	 * * = Adjacent {@link Tile}
	 * 
	 * <p>
	 * 
	 * ***
	 * 
	 * <br>
	 * 
	 * *X*
	 * 
	 * <br>
	 * 
	 * ***
	 * 
	 * <br>
	 * 
	 * @param row
	 *            the row of the X {@link Tile}
	 * @param col
	 *            the column of the X {@link Tile}
	 * @return an array containing all * {@link Tile}s
	 * @throws TileNotInitializedException
	 *             if a {@link Tile} has not been initialized
	 */
	public Tile[] getAdjacentTiles(int row, int col) throws TileNotInitializedException {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				try {
					Tile tile = m_tileMap[row + i][col + j];
					if (tile == null) {
						throw new TileNotInitializedException(row, col);
					}
					tiles.add(tile);
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		Tile[] output = new Tile[tiles.size()];
		output = tiles.toArray(output);
		return output;
	}

	/**
	 * Gets all {@link Tile}s on this {@link Board} of the specified
	 * {@link TileType}
	 * 
	 * @param type
	 *            the {@link TileType} to look for
	 * @return an array containing all found {@link Tile}s
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 * @throws TileNotInitializedException
	 *             if a {@link Tile} has not been initialized
	 */
	public Tile[] getTilesOfType(TileType type) throws BoardNotInitializedException, TileNotInitializedException {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		for (int row = 0; row < getDimensions()[0]; row++) {
			for (int col = 0; col < getDimensions()[1]; col++) {
				if (m_tileMap[row][col].getTileType() == type) {
					tiles.add(m_tileMap[row][col]);
				}
			}
		}
		Tile[] output = new Tile[tiles.size()];
		output = tiles.toArray(output);
		return output;
	}

	/**
	 * Gets all {@link Tile}s on this {@link Board} of the specified frequency
	 * 
	 * @param frequency
	 *            the frequency to look for
	 * @return an array containing all found {@link Tile}s
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 * @throws TileNotInitializedException
	 *             if a {@link Tile} has not been initialized
	 */
	public Tile[] getTilesOfFrequency(int frequency) throws BoardNotInitializedException, TileNotInitializedException {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		for (int row = 0; row < getDimensions()[0]; row++) {
			for (int col = 0; col < getDimensions()[1]; col++) {
				if (m_tileMap[row][col].getFrequency() == frequency) {
					tiles.add(m_tileMap[row][col]);
				}
			}
		}
		Tile[] output = new Tile[tiles.size()];
		output = tiles.toArray(output);
		return output;
	}

	/**
	 * Adds a {@link BoardObject} to the {@link Board}
	 * 
	 * @param object
	 *            the {@link BoardObject} to add
	 */
	public void addObject(BoardObject object) {
		m_objects.add(object);
		if (m_objectAddedListener != null) {
			m_objectAddedListener.consume(object);
		}
	}

	/**
	 * Removes a {@link BoardObject} from the {@link Board}
	 * 
	 * @param object
	 *            the {@link BoardObject} to remove
	 */
	public void removeObject(BoardObject object) {
		m_objects.remove(object);
	}

	/**
	 * 
	 * @return an array containing all {@link BoardObject}s on the board
	 */
	public BoardObject[] getObjects() {
		BoardObject[] output = new BoardObject[m_objects.size()];
		output = m_objects.toArray(output);
		return output;
	}

	/**
	 * 
	 * @param row
	 *            the row the {@link Vertex} is on
	 * @param column
	 *            the column the {@link Vertex} is on
	 * @return a {@link Vertex} with the specified row and column for this
	 *         {@link Board}
	 */
	public Vertex getVertex(int row, int column) {
		return new Vertex(row, column, this);
	}

	/**
	 * 
	 * @param point1
	 *            the first point the {@link Edge} is on {row, col}
	 * @param point2
	 *            the second point the {@link Edge} is on {row, col}
	 * @return a {@link Edge} between these two points on this {@link Board}
	 */
	public Edge getEdge(int[] point1, int[] point2) {
		return new Edge(point1, point2, this);
	}

	/**
	 * Gets all {@link BoardObject} matching a set condition
	 * 
	 * @param matcher
	 *            a {@link BoardMatcher} containing the condition
	 * @return array containing all matching {@link BoardObject}s
	 */
	public BoardObject[] getAllObjectsMatching(BoardObjectMatcher matcher) {
		ArrayList<BoardObject> objects = new ArrayList<BoardObject>();
		for (BoardObject object : m_objects) {
			if (matcher.matches(object)) {
				objects.add(object);
			}
		}
		BoardObject[] output = new BoardObject[objects.size()];
		output = objects.toArray(output);
		return output;
	}

	/**
	 * Performs the specified operation on all {@link BoardObject}s
	 * 
	 * @param consumer
	 *            the operation to perform
	 */
	public void forAllObjects(BoardObjectConsumer consumer) {
		for (BoardObject object : m_objects) {
			consumer.consume(object);
		}
	}

	/**
	 * Sets the code to run when an object is added to this {@link Board}
	 * 
	 * @param listener
	 *            the {@link ObjectConsumer} to run
	 */
	public void setObjectAddedListener(BoardObjectConsumer listener) {
		m_objectAddedListener = listener;
	}

	/**
	 * Gets all moves available to a single {@link Player}
	 * 
	 * @param player
	 *            the {@link Player}
	 * @return array containing the {@link Move}s
	 * @throws BoardNotInitializedException
	 *             if this {@link Board} has not been initialized
	 * @throws InvalidLocationException
	 *             if location supplied to any {@link Move} is invalid
	 */
	public Move[] getMovesForPlayer(Player player) throws BoardNotInitializedException, InvalidLocationException {
		ArrayList<Move> moves = new ArrayList<Move>();
		if (player.getResources().greaterOrEqualTo(PurchaseCosts.VILLAGE_COST)) {
			for (Vertex vertex : Village.getValidLocations(this, player)) {
				moves.add(new PurchaseMove(new ConstructVillage(new Village(player, vertex)), player));
			}
		}

		if (player.getResources().greaterOrEqualTo(PurchaseCosts.CITY_COST)) {
			for (BoardObject village : getAllObjectsMatching((object) -> {
				return object instanceof Village && ((Village) object).getOwner() == player;
			})) {
				moves.add(new PurchaseMove(new UpgradeVillage((Village) village), player));
			}
		}

		if (player.getResources().greaterOrEqualTo(PurchaseCosts.ROAD_COST)) {
			for (Edge edge : Road.getValidLocations(this, player)) {
				moves.add(new PurchaseMove(new ConstructRoad(new Road(player, edge)), player));
			}
		}

		Move[] output = new Move[moves.size()];
		output = moves.toArray(output);
		return output;
	}

	/**
	 * 
	 * @param gameStage
	 *            the highest number of victory points any player possesses
	 * @param row
	 *            the row of the {@link Tile}
	 * @param col
	 *            the column of the {@link Tile}
	 * @param metric
	 *            the production metric to assess this {@link Tile} based off of
	 * @return the unitless value of this {@link Tile}
	 * @throws TileNotInitializedException
	 *             if the specified {@link Tile} has not been initialized
	 */
	public int getTileValue(int gameStage, int row, int col, ResourceMetric metric) throws TileNotInitializedException {
		Tile tile = getTileAt(row, col);

		int[] weights = new int[ResourceBundle.RESOURCE_NUMBER];

		// mid game, roads, villages, cities

		for (int i = 0; i < ResourceBundle.RESOURCE_NUMBER; i++) {
			weights[i] = 7;
		}

		if (gameStage < 4) {

			// early game, roads, villages

			weights[ResourceBundle.WOOD] += 1;
			weights[ResourceBundle.CLAY] += 1;
			weights[ResourceBundle.STONE] -= 1;

		} else if (gameStage > 6) {

			// late game, cities, wild cards

			weights[ResourceBundle.WOOD] -= 1;
			weights[ResourceBundle.CLAY] -= 1;

		}
		
		int resource = tile.getTileType().getResource();
		
		if (resource == ResourceBundle.NULL) {
			return 0;
		}
		
		int prodDiff = (gameStage / 2) - metric.getRawMetric()[resource];

		int weighted = weights[resource] * Tile.getFrequencyProbability(tile.getFrequency());
		
		return weighted + prodDiff;
	}

	/**
	 * @param row
	 *            the row of the {@link Tile}
	 * @param col
	 *            the column of the {@link Tile}
	 * @param metric
	 *            the production metric to assess this {@link Tile} based off of
	 * @return the unitless value of this {@link Tile}
	 * @throws TileNotInitializedException
	 *             if the specified {@link Tile} has not been initialized
	 */
	public int getTileValue(int row, int col, ResourceMetric metric) throws TileNotInitializedException {
		return getTileValue(getHighestVictoryPoints(), row, col, metric);
	}

	/**
	 * 
	 * @return the highest number of victory points possessed by any
	 *         {@link Player} on this {@link Board}
	 */
	public int getHighestVictoryPoints() {
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Integer> points = new ArrayList<Integer>();

		for (BoardObject object : getObjects()) {
			if (object.getOwner() != null) {
				int index = -1;
				if (!players.contains(object)) {
					index = players.size();
					players.add(object.getOwner());
					points.add(0);
				}
				if (object instanceof Village) {
					if (index == -1) {
						index = players.indexOf(object.getOwner());
					}
					points.set(index, points.get(index) + Village.VICTORY_POINTS);
				} else if (object instanceof City) {
					if (index == -1) {
						index = players.indexOf(object.getOwner());
					}
					points.set(index, points.get(index) + City.VICTORY_POINTS);
				}
			}
		}

		int max = 0;
		for (Integer i : points) {
			max = Math.max(max, i);
		}
		return max;
	}

	@Override
	public String toString() {
		return "Board";
	}
}
