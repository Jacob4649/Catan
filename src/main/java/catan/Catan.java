package catan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JFrame;

import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.engine.board.objects.buildings.Village;
import catan.engine.player.Player;
import catan.engine.player.PlayerColor;
import catan.engine.player.PlayerCountOutOfBoundsException;
import catan.engine.player.PlayerIndexOutOfBoundsException;
import catan.renderer.panel.BoardPanel;

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

	private Player[] m_players;
	private Board m_board;
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
	 * Sets the {@link Board} for this game
	 * 
	 * @param board
	 *            the {@link Board} to assign
	 */
	public void setBoard(Board board) {
		m_board = board;
	}

	public static void main(String[] args)
			throws PlayerCountOutOfBoundsException, PlayerIndexOutOfBoundsException, BoardNotInitializedException, BoardObjectNotInitializedException {

		// start catan game
		Catan catan = new Catan();

		catan.setBoard(Board.randomLandBoard());

		catan.getBoard().addObject(new Village(catan.getPlayers()[0], catan.getBoard().getVertex(5, 5)));
		
		JFrame frame = new JFrame("Catan");

		frame.getContentPane().add(new BoardPanel(catan.getBoard()));
		frame.pack();

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.setSize(BoardPanel.PANEL_HORIZONTAL + 5, BoardPanel.PANEL_VERTICAL + 25);

		frame.setResizable(false);

		frame.setVisible(true);

		// new BoardBuilderWindow();
	}
}
