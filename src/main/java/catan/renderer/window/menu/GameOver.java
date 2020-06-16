package catan.renderer.window.menu;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import catan.Catan;
import catan.engine.player.Player;

/**
 * Class to display to the user the winner of a game of {@link Catan}
 * 
 * @author Jacob
 *
 */
public class GameOver {

	private JDialog m_frame;
	private Catan m_catan;
	private Player m_winner;

	/**
	 * Creates a new {@link GameOver}.
	 * 
	 * @param catan
	 *            the game of {@link Catan} that was won by the specified
	 *            {@link Player}
	 * @param winner
	 *            the {@link Player} that won
	 */
	public GameOver(Catan catan, Player winner) {
		m_catan = catan;
		m_winner = winner;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		m_frame = new JDialog();
		m_frame.setModalityType(ModalityType.APPLICATION_MODAL);
		m_frame.setBounds(100, 100, 250, 150);
		m_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		m_frame.setResizable(false);
		m_frame.getContentPane().setLayout(new BoxLayout(m_frame.getContentPane(), BoxLayout.Y_AXIS));

		m_frame.getContentPane().add(Box.createVerticalStrut(20));

		JLabel gameOver = new JLabel("Game Over");
		gameOver.setFont(new Font("Tahoma", Font.PLAIN, 32));
		gameOver.setHorizontalAlignment(SwingConstants.CENTER);
		gameOver.setAlignmentX(Component.CENTER_ALIGNMENT);
		m_frame.getContentPane().add(gameOver);

		JLabel winner = new JLabel(m_catan.getPlayer() == m_winner ? "You Win" : "You Lose");
		winner.setAlignmentX(Component.CENTER_ALIGNMENT);
		winner.setHorizontalAlignment(SwingConstants.CENTER);
		m_frame.getContentPane().add(winner);
		
		m_frame.setVisible(true);
	}

}
