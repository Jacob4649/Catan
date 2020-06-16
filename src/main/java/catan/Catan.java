package catan;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JFrame;

import catan.engine.Dice;
import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.engine.board.objects.InvalidLocationException;
import catan.engine.board.objects.NoOwnerException;
import catan.engine.board.objects.Productive;
import catan.engine.board.objects.VertexObject;
import catan.engine.board.objects.buildings.City;
import catan.engine.board.objects.buildings.Road;
import catan.engine.board.objects.buildings.Village;
import catan.engine.board.tile.Edge;
import catan.engine.board.tile.Tile;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.Vertex;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.player.Player;
import catan.engine.player.PlayerColor;
import catan.engine.player.PlayerCountOutOfBoundsException;
import catan.engine.player.PlayerIndexOutOfBoundsException;
import catan.engine.resources.ResourceMetric;
import catan.renderer.panel.BoardPanel;
import catan.renderer.window.construction.ConstructionToolBox;
import catan.renderer.window.construction.InitialConstructionToolBox;
import catan.renderer.window.menu.GameOver;
import catan.renderer.window.menu.MainMenu;

/**
 * Jacob Klimczak
 * ICS4UO
 * Course Summative - Catan Game
 * 
 * All code is original.
 * Catan is a turn based, strategy where players 
 * compete for control of a newly discovered island. 
 * Points are won by building villages and/or cities. 
 * Cities are 2 points while villages are 1. 
 * The first player to accumulate 10 points wins the game.
 * Resources are needed to build villages and upgrade 
 * them into cities. These resources are taken from the territory surrounding 
 * your existing cities/villages. Every turn, a two dice are rolled. If the
 * sum of the numbers on the dice is equivalent to the number on a tile. 
 * Any villages on vertices contacting that tile will get 
 * one resource of the type that tile produces. 
 * Cities will get two resources. Resources may also be traded for.
 * If you have 3 of one resource, you may exchange all 3 of 
 * them for 1 of another resource. All 3 resources must be of the same type.
 * You may only build villages on roads. 
 * You may also not build a village adjacent to an existing village.
 * Roads may only be constructed with one point contacting an existing road.
 * Each player starts with 2 villages and 2 roads, and 7 random resources.
 * 
 * Games are played on maps composed of tiles. 
 * These maps are either randomly generated,
 * or loaded in. The game comes with a built in map editor, 
 * as well as several pre-made maps for you to play on.
 * 
 * Pressing the 'Quickstart Game' button in the menu begins a 
 * game with default settings - a 6x6 random map, and 4 players.
 * If 'New Game' if pressed instead, the game setup screen will open.
 * Here players can specify how many players, the type of map (random or pre-loaded),
 * the map dimensions (only if random), or which map to use (if loading in a map).
 * 
 * The game is played with one player and between 1 and 4 AI opponents.
 * The AI opponents do not collude and are competing against each other
 * as much as the player is competing against them.
 * 
 */

/**
 * Represents a game of catan
 * 
 * @author Jacob
 *
 */

public class Catan {

	public static final int MIN_PLAYERS = 2;
	public static final int MAX_PLAYERS = 5;
	public static final int NORMAL_PLAYERS = 4;

	public static final int STARTING_RESOURCES = 7;

	public static final int MAX_VICTORY_POINTS = 10;

	private Player[] m_players;
	private Board m_board;
	private BoardPanel m_boardPanel;
	private JFrame m_toolBox;
	private int m_playerIndex = -1;
	private int m_turn = 0;

	private boolean m_end = false;

	private MainMenu m_menu;

	/**
	 * Creates a new game of {@link Catan} with the specified players
	 * 
	 * @param playerIndex
	 *            the index of the user's player
	 * @param players
	 *            the players
	 * @throws PlayerCountOutOfBoundsException
	 *             when the player count is not within engine constraints
	 * @throws PlayerIndexOutOfBoundsException
	 *             when the player index is invalid
	 */
	public Catan(int playerIndex, Player... players)
			throws PlayerCountOutOfBoundsException, PlayerIndexOutOfBoundsException {
		if (players.length > MAX_PLAYERS) {
			throw new PlayerCountOutOfBoundsException(players.length);
		}

		if (playerIndex > players.length - 1 || playerIndex < 0) {
			throw new PlayerIndexOutOfBoundsException(playerIndex, players.length);
		}

		m_players = players;
		m_playerIndex = playerIndex;

		// add starting resources
		for (Player player : m_players) {
			player.getResources().addRandom(STARTING_RESOURCES);
		}

		determineTurnOrder();
	}

	/**
	 * Creates a game of {@link Catan} with the specified number of players
	 * 
	 * @param playerNumber
	 *            the number of players
	 * @throws PlayerIndexOutOfBoundsException
	 *             if player number is not within engine constraints
	 * @throws PlayerCountOutOfBoundsException
	 *             when player number is not within engine constraints
	 */
	public Catan(int playerNumber) throws PlayerCountOutOfBoundsException, PlayerIndexOutOfBoundsException {
		this(0, (Player[]) (new ArrayList<Player>() {
			{
				ArrayList<PlayerColor> colorList = new ArrayList<PlayerColor>(Arrays.asList(PlayerColor.values()));
				Collections.shuffle(colorList);
				for (int i = 0; i < playerNumber; i++) {
					add(new Player(colorList.get(i)));
				}
			}
		}).toArray(new Player[playerNumber]));
	}

	/**
	 * Creates a new game of {@link Catan} with the default number of players
	 * 
	 * @throws PlayerIndexOutOfBoundsException
	 *             if default player number is 0
	 * @throws PlayerCountOutOfBoundsException
	 *             if default player number is not within engine constraints
	 */
	public Catan() throws PlayerCountOutOfBoundsException, PlayerIndexOutOfBoundsException {
		this(NORMAL_PLAYERS);
	}

	/**
	 * 
	 * @return array containing all players
	 */
	public Player[] getPlayers() {
		return m_players;
	}

	/**
	 * 
	 * @return the user's {@link Player}
	 */
	public Player getPlayer() {
		return m_players[m_playerIndex];
	}

	/**
	 * Sets up the players array, randomizes turn order according to simulated
	 * dice rolls
	 */
	public void determineTurnOrder() {
		// assign each player random dice roll
		int[] rolls = new int[m_players.length];
		for (int i = 0; i < rolls.length; i++) {
			rolls[i] = Dice.singleRoll();
		}

		// sort rolls and players using simple insertion sort
		for (int i = 1; i < rolls.length; i++) {
			int temp = rolls[i];
			Player tempPlayer = m_players[i];

			int j = i;
			while (j > 0 && rolls[j - 1] < temp) {
				rolls[j] = rolls[j - 1];
				if (m_players[j - 1] == getPlayer()) {
					m_playerIndex = j;
				}
				m_players[j] = m_players[j - 1];
				j--;
			}
			rolls[j] = temp;
			if (tempPlayer == getPlayer()) {
				m_playerIndex = j;
			}
			m_players[j] = tempPlayer;
		}
	}

	/**
	 * Ends the current {@link Player}'s turn
	 */
	public void nextTurn() {
		m_turn += 1;
		if (m_turn >= m_players.length) {
			m_turn = 0;
		}
	}

	/**
	 * Ends the current {@link Player}'s turn, resumes game loop
	 */
	public synchronized void syncNextTurn() {
		nextTurn();
		notify();
	}

	/**
	 * 
	 * @return the {@link Player} whose turn it currently is
	 */
	public Player getActivePlayer() {
		return m_players[m_turn];
	}

	/**
	 * 
	 * @return the {@link Board} for this game
	 * @throws BoardNotInitializedException
	 *             if the board has not been initialized
	 */
	public Board getBoard() throws BoardNotInitializedException {
		if (m_board == null) {
			throw new BoardNotInitializedException();
		}
		return m_board;
	}

	/**
	 * 
	 * @return the {@link BoardPanel} for this game
	 */
	public BoardPanel getBoardPanel() {
		return m_boardPanel;
	}

	/**
	 * Sets the {@link BoardPanel} for this game
	 * 
	 * @param panel
	 *            the {@link BoardPanel} to assign
	 */
	public void setBoardPanel(BoardPanel panel) {
		m_boardPanel = panel;
	}

	/**
	 * Sets the {@link Board} for this game
	 * 
	 * @param board
	 *            the {@link Board} to assign
	 */
	public void setBoard(Board board) {
		m_board = board;
	}

	/**
	 * Creates a new {@link ConstructionToolBox} for this game
	 */
	public void createToolBox() {
		m_toolBox = new ConstructionToolBox(this);
	}

	/**
	 * Destroys the {@link ConstructionToolBox} for this game
	 */
	public void destroyToolBox() {
		if (m_toolBox != null) {
			m_toolBox.dispose();
		}
		m_toolBox = null;
	}

	/**
	 * Distributes the produced resources for this turn
	 * 
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 */
	public void distributeTurnResources() throws BoardNotInitializedException {
		int frequency = Dice.doubleRoll();
		getBoard().forAllObjects((object) -> {
			if (object instanceof Productive) {
				try {
					((Productive) object).giveResourcesToOwner(frequency);
				} catch (TileNotInitializedException | BoardNotInitializedException | VertexNotInitializedException
						| BoardObjectNotInitializedException | NoOwnerException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Runs the main loop for this {@link Catan}
	 * 
	 * @throws InvalidLocationException
	 *             if an {@link InvalidLocationException} is thrown within the
	 *             game loop
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} is not initialized
	 */
	public synchronized void gameLoop() throws BoardNotInitializedException, InvalidLocationException {
		while (!m_end) {
			distributeTurnResources();
			if (getActivePlayer() == getPlayer()) {
				createToolBox();
				while (getActivePlayer() == getPlayer() && !m_end) {
					try {
						// wait until notified user is done
						wait();
					} catch (InterruptedException e) {
					}
				}
				destroyToolBox();
				if (getPlayer().getVictoryPoints(getBoard()) >= MAX_VICTORY_POINTS) {
					m_end = true;
					new GameOver(this, getPlayer());
				}
			} else {
				getActivePlayer().takeTurn(this);
				if (getActivePlayer().getVictoryPoints(getBoard()) >= MAX_VICTORY_POINTS) {
					m_end = true;
					new GameOver(this, getActivePlayer());
				}
				nextTurn();
			}
		}
	}

	/**
	 * exits the game
	 */
	public synchronized void breakGameLoop() {
		notifyGame();
		m_end = true;
	}

	/**
	 * Notifies the game thread
	 */
	public synchronized void notifyGame() {
		notify();
	}

	/**
	 * Sets up this game
	 * 
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} hasn't been initialized
	 * @throws VertexNotInitializedException
	 *             if any {@link Vertex} can't be initialized
	 * @throws TileNotInitializedException
	 *             if any {@link Tile} has not been initialized
	 * @throws InvalidLocationException
	 *             if anything is created in an invalid location
	 */
	public synchronized void setUp() throws BoardNotInitializedException, TileNotInitializedException,
			VertexNotInitializedException, InvalidLocationException {
		for (Player player : getPlayers()) {
			if (player == getPlayer()) {
				m_toolBox = new InitialConstructionToolBox(this);
				while (getBoard().getAllObjectsMatching(
						(object) -> object instanceof Village && object.getOwner() == getPlayer()).length < 2
						&& !m_end) {
					try {
						// wait until notified user is done
						wait();
					} catch (InterruptedException e) {
					}
				}
				destroyToolBox();
			} else {
				for (int i = 0; i < 2; i++) {
					Vertex highest = null;
					int highScore = 0;
					Vertex secondHighest = null;
					int secondHighScore = 0;
					ResourceMetric production = player.getProductionMetric(getBoard());
					int gameStage = getBoard().getHighestVictoryPoints();
					for (int row = 0; row < getBoard().getVertexDimensions()[0]; row++) {
						for (int col = 0; col < getBoard().getVertexDimensions()[1]; col++) {
							Vertex vertex = getBoard().getVertex(row, col);
							int val = vertex.getVertexValue(gameStage, production);
							if (getBoard().getAllObjectsMatching((object) -> {
								try {
									return (object instanceof Village || object instanceof City)
											&& (((VertexObject) object).getPosition().equals(vertex)
													|| ((VertexObject) object).getPosition().isAdjacent(vertex));
								} catch (BoardObjectNotInitializedException | VertexNotInitializedException e) {
									e.printStackTrace();
									System.exit(0);
									return false;
								}
							}).length > 0) {
								continue;
							}
							if (highest == null || secondHighest == null) {
								highest = vertex;
								secondHighest = vertex;
								highScore = val;
								secondHighest = vertex;
							} else if (val > highScore) {
								secondHighest = highest;
								secondHighScore = highScore;
								highest = vertex;
								highScore = val;
							} else if (val > secondHighScore) {
								secondHighest = vertex;
								secondHighScore = val;
							}
						}
					}

					int[] direction = new int[2];
					int[] delta = highest.getDistanceFrom(secondHighest);
					if (Math.abs(delta[0]) > Math.abs(delta[1])) {
						direction[1] = Integer.signum(delta[0]) * 1;
					} else {
						direction[0] = Integer.signum(delta[1]) * 1;
					}
					int[] secondPosition = new int[] { highest.getPosition()[0] + direction[0],
							highest.getPosition()[1] + direction[1] };

					Edge edge = new Edge(highest.getPosition(), secondPosition, getBoard());

					getBoard().addObject(new Road(player, edge, true));
					getBoard().addObject(new Village(player, highest));
				}
			}
		}
	}

	/**
	 * Starts a new game of {@link Catan}
	 * 
	 * @param playerCount
	 *            the number of players
	 * @param board
	 *            the {@link Board} to play on, null for a random {@link Board}
	 * @param menu
	 *            the calling {@link MainMenu}
	 * @throws PlayerIndexOutOfBoundsException
	 *             if an invalid player index is supplied
	 * @throws PlayerCountOutOfBoundsException
	 *             if an invalid playerCount is supplied
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} is not initialized
	 * @throws InvalidLocationException
	 *             if an {@link InvalidLocationException} is thrown within the
	 *             game
	 * @throws VertexNotInitializedException
	 *             if any uninitialized {@link Vertex} (vertices) are accessed
	 * @throws TileNotInitializedException
	 *             if any uninitialized {@link Tile}s are accessed
	 */
	public static void startCatan(int playerCount, Board board, MainMenu menu)
			throws PlayerCountOutOfBoundsException, PlayerIndexOutOfBoundsException, BoardNotInitializedException,
			TileNotInitializedException, VertexNotInitializedException, InvalidLocationException {
		Catan catan;
		if (playerCount > 0) {
			catan = new Catan(playerCount);
		} else {
			catan = new Catan();
		}

		catan.setBoard(board != null ? board : Board.randomLandBoard());

		JFrame frame = new JFrame("Catan") {
			@Override
			public void dispose() {
				super.dispose();
				catan.destroyToolBox();
				catan.breakGameLoop();
				menu.setVisible(true);
			}
		};

		catan.setBoardPanel(new BoardPanel(catan.getBoard()));

		frame.getContentPane().add(catan.getBoardPanel());

		frame.pack();

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.setSize(BoardPanel.PANEL_HORIZONTAL + 5, BoardPanel.PANEL_VERTICAL + 25);

		frame.setResizable(false);

		frame.setVisible(true);

		catan.setUp();

		catan.gameLoop();

		frame.dispose();

		menu.setVisible(true);
	}

	/**
	 * Starts a new game of {@link Catan}
	 * 
	 * @param menu
	 *            the calling {@link MainMenu}
	 * @throws PlayerIndexOutOfBoundsException
	 *             if an invalid player index is supplied
	 * @throws PlayerCountOutOfBoundsException
	 *             if an invalid playerCount is supplied
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} is not initialized
	 * @throws InvalidLocationException
	 *             if an {@link InvalidLocationException} is thrown within the
	 *             game
	 * @throws VertexNotInitializedException
	 *             if any uninitialized {@link Vertex} (vertices) are accessed
	 * @throws TileNotInitializedException
	 *             if any uninitialized {@link Tile}s are accessed
	 */
	public static void startCatan(MainMenu menu)
			throws PlayerCountOutOfBoundsException, PlayerIndexOutOfBoundsException, BoardNotInitializedException,
			TileNotInitializedException, VertexNotInitializedException, InvalidLocationException {
		startCatan(-1, null, menu);
	}

	/**
	 * Launch the game
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainMenu();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
