package catan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JFrame;

import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.engine.board.objects.InvalidLocationException;
import catan.engine.board.objects.buildings.Road;
import catan.engine.player.Player;
import catan.engine.player.PlayerColor;
import catan.engine.player.PlayerCountOutOfBoundsException;
import catan.engine.player.PlayerIndexOutOfBoundsException;
import catan.renderer.panel.BoardPanel;
import catan.renderer.window.boardbuilder.BoardBuilderWindow;
import catan.renderer.window.construction.ConstructionToolBox;

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
	private ConstructionToolBox m_toolBox;
	private int m_playerIndex = -1;

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

	public static void main(String[] args) throws PlayerCountOutOfBoundsException, PlayerIndexOutOfBoundsException,
			BoardNotInitializedException, BoardObjectNotInitializedException, InvalidLocationException {

		Catan catan = new Catan();

		catan.setBoard(Board.randomLandBoard());

		catan.getBoard().addObject(
				new Road(catan.getPlayer(), catan.getBoard().getEdge(new int[] { 1, 2 }, new int[] { 1, 1 }), true));
		catan.getBoard().addObject(
				new Road(catan.getPlayer(), catan.getBoard().getEdge(new int[] { 1, 2 }, new int[] { 2, 2 })));

		JFrame frame = new JFrame("Catan") {
			@Override
			public void dispose() {
				super.dispose();
				catan.destroyToolBox();
			}
		};

		catan.setBoardPanel(new BoardPanel(catan.getBoard()));

		frame.getContentPane().add(catan.getBoardPanel());

		frame.pack();

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.setSize(BoardPanel.PANEL_HORIZONTAL + 5, BoardPanel.PANEL_VERTICAL + 25);

		frame.setResizable(false);

		frame.setVisible(true);

		catan.createToolBox();

		// new BoardBuilderWindow();

		// new ResourceMetricWindow(new ResourceMetric(new int[] {1, 1, 5, 7,
		// 6}), "Player Metric");
	}
}
