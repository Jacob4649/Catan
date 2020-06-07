package catan.renderer.panel;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import catan.Catan;
import catan.engine.board.Board;
import catan.renderer.Colors;

/**
 * Class for displaying a {@link Catan} board
 * 
 * @author Jacob
 *
 */
public class BoardPanel extends JPanel {

	public static final int PANEL_HORIZONTAL = 800;
	public static final int PANEL_VERTICAL = 600;

	Board m_board;

	/**
	 * Creates a {@link BoardPanel} that will show the specified {@link Catan}
	 * board
	 * 
	 * @param board
	 */
	public BoardPanel(Board board) {
		m_board = board;

		setBorder(BorderFactory.createLineBorder(Colors.BOARD_BORDER_COLOR));
		setBackground(Colors.BOARD_EMPTY_COLOR);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// paint tiles
		
		// paint grid
		
		// paint objects
		
		// paint ui details

		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PANEL_HORIZONTAL, PANEL_VERTICAL);
	}

}
