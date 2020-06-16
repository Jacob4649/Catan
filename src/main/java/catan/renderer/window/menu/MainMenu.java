package catan.renderer.window.menu;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.SwingConstants;

import catan.Catan;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.objects.InvalidLocationException;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.player.PlayerCountOutOfBoundsException;
import catan.engine.player.PlayerIndexOutOfBoundsException;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

/**
 * Class representing the application's main menu
 * 
 * @author Jacob
 *
 */
public class MainMenu {

	private JFrame m_frame;
	private MapsMenu m_maps;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu window = new MainMenu();
					window.m_frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create a new {@link MainMenu}
	 */
	public MainMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		m_frame = new JFrame();
		m_frame.setBounds(100, 100, 450, 300);
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.getContentPane().setLayout(null);

		JLabel menuLabel = new JLabel("Jacob's Catan");
		menuLabel.setFont(new Font("Tahoma", Font.PLAIN, 32));
		menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
		menuLabel.setBounds(10, 11, 414, 51);
		m_frame.getContentPane().add(menuLabel);

		JButton newGame = new JButton("New Game");
		newGame.setBounds(10, 73, 100, 23);
		
		newGame.addActionListener((performedAction) -> {
			setVisible(false);
			new GameSetupMenu(this);
		});
		
		m_frame.getContentPane().add(newGame);

		JButton quickGame = new JButton("Quickstart Game");
		quickGame.setBounds(120, 73, 194, 23);

		quickGame.addActionListener((performedAction) -> {
			m_frame.setVisible(false);
			new Thread(() -> {
				try {
					Catan.startCatan(this);
				} catch (PlayerCountOutOfBoundsException | PlayerIndexOutOfBoundsException
						| BoardNotInitializedException | TileNotInitializedException | VertexNotInitializedException
						| InvalidLocationException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}).start();
		});

		m_frame.getContentPane().add(quickGame);

		JButton maps = new JButton("Maps");
		maps.setBounds(324, 73, 100, 23);

		maps.addActionListener((performedAction) -> {
			m_maps = new MapsMenu();
		});

		m_frame.getContentPane().add(maps);

		JButton rules = new JButton("Rules");
		rules.setBounds(10, 227, 100, 23);
		m_frame.getContentPane().add(rules);

		JButton quit = new JButton("Quit");
		quit.setBounds(324, 227, 100, 23);

		quit.addActionListener((performedAction) -> {
			System.exit(0);
		});

		m_frame.getContentPane().add(quit);
	}

	/**
	 * Sets the visibility of this window
	 * 
	 * @param visible
	 *            true if visible
	 */
	public void setVisible(boolean visible) {
		m_frame.setVisible(visible);
	}
}