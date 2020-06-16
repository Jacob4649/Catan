package catan.renderer.window.menu;

import java.awt.Component;
import java.awt.Font;
import java.awt.Dialog.ModalityType;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * Class for displaying the game rules
 * 
 * @author Jacob
 *
 */
public class Rules {

	private JDialog m_frame;

	private static final String RULES = "Catan is a turn based, strategy where players compete "
			+ "for control of a newly discovered island. Points are won by building villages "
			+ "and/or cities. Cities are 2 points while villages are 1. The first player "
			+ "to accumulate 10 points wins the game. Resources are needed to build villages "
			+ "and upgrade them into cities. These resources are taken from the "
			+ "territory surrounding your existing cities/villages. Every turn, "
			+ "a two dice are rolled. If the sum of the numbers on the dice is equivalent "
			+ "to the number on a tile. Any villages on vertices contacting that tile "
			+ "will get 1 resource of the type that tile produces. Cities will get 2 resources. "
			+ "Resources may also be traded for. If you have 3 of one resource, "
			+ "you may exchange all 3 of them for 1 of another resource. All 3 "
			+ "resources must be of the same type. You may only build villages on roads. "
			+ "You may also not build a village adjacent to an existing village. Roads may "
			+ "only be constructed with one point contacting an existing road. "
			+ "Each player starts with 2 villages and 2 roads, and 7 random resources.\n\n"
			+ "Games are played on maps composed of tiles. These maps are either randomly "
			+ "generated, or loaded in. The game comes with a built in map editor, "
			+ "as well as several pre-made maps for you to play on.\n\n"
			+ "Pressing the 'Quickstart Game' button in the menu begins a game with "
			+ "default settings - a 6x6 random map, and 4 players. If 'New Game' "
			+ "if pressed instead, the game setup screen will open. Here players "
			+ "can specify how many players, the type of map (random or pre-loaded), the "
			+ "map dimensions (only if random), or which map to use (if loading in a map).\n\n"
			+ "The game is played with one player and between 1 and 4 AI opponents. "
			+ "The AI opponents do not collude and are competing against each other "
			+ "as much as the player is competing against them.\n\n"
			+ "The password for the game is 'Catan123' (no apostrophes).";

	/**
	 * Creates a new {@link Rules}
	 */
	public Rules() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		m_frame = new JDialog();
		m_frame.setBounds(100, 100, 450, 300);
		m_frame.setModalityType(ModalityType.APPLICATION_MODAL);
		m_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		m_frame.getContentPane().setLayout(new BoxLayout(m_frame.getContentPane(), BoxLayout.Y_AXIS));
		m_frame.setResizable(false);

		m_frame.getContentPane().add(new JLabel("Rules") {
			{
				setFont(new Font("Tahoma", Font.BOLD, 18));
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
			}
		});

		m_frame.getContentPane().add(Box.createVerticalStrut(20));

		m_frame.getContentPane().add(new JScrollPane(new JTextArea(RULES) {
			{
				setEditable(false);
				setCursor(null);
				setOpaque(false);
				setFocusable(false);
				setFont(new Font("Tahoma", Font.PLAIN, 11));
				setWrapStyleWord(true);
				setLineWrap(true);
				setBorder(new EmptyBorder(5, 5, 5, 5));
				setAlignmentY(JLabel.CENTER_ALIGNMENT);
			}
		}));

		m_frame.setVisible(true);
	}

}
