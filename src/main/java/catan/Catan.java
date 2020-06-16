package catan;

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

/**
 * Jacob Klimczak
 * ICS4UO
 * Course Summative - Catan Game
 * 
 * Some components (such as the opponent AI) were based on my summative from last year
 * which can be found at https://github.com/Jacob4649/Chess. These components were only based
 * on last year's code, and were not directly taken from last year's summative.
 * No code was reused.
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

	private Player[] m_players;
	private Board m_board;
	private BoardPanel m_boardPanel;
	private JFrame m_toolBox;
	private int m_playerIndex = -1;
	private int m_turn = 0;

	private boolean m_end = false;

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
	 * @throws BoardNotInitializedException if the {@link Board} has not been initialized
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
			} else {
				getActivePlayer().takeTurn(this);
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

	public static void main(String[] args) throws PlayerCountOutOfBoundsException, PlayerIndexOutOfBoundsException,
			BoardNotInitializedException, BoardObjectNotInitializedException, InvalidLocationException,
			TileNotInitializedException, VertexNotInitializedException {

		Catan catan = new Catan();

		catan.setBoard(Board.randomLandBoard());

		JFrame frame = new JFrame("Catan") {
			@Override
			public void dispose() {
				super.dispose();
				catan.destroyToolBox();
				catan.breakGameLoop();
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
	}
}
