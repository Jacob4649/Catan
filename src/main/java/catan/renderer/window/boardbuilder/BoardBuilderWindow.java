package catan.renderer.window.boardbuilder;

import javax.swing.JFrame;

import catan.engine.board.BoardBuilder;
import catan.engine.board.SelectedPositionListener;
import catan.renderer.panel.BoardPanel;

/**
 * Class for building a board
 * @author Jacob
 *
 */
public class BoardBuilderWindow extends JFrame {

	private BoardBuilder m_builder;
	private BoardPanel m_panel;
	private BoardBuilderToolBox m_toolBox;
	
	/**
	 * Creates a {@link BoardBuilderWindow}
	 */
	public BoardBuilderWindow() {
		super("Board Builder");
		
		m_builder = new BoardBuilder();
		m_panel = new BoardPanel(m_builder);
		
		getContentPane().add(m_panel);
		pack();
		
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        
        setSize(BoardPanel.PANEL_HORIZONTAL + 5, BoardPanel.PANEL_VERTICAL + 25);
        
        setResizable(false);
		
        setVisible(true);
        
        m_toolBox = new BoardBuilderToolBox(this);
	}
	
	public BoardBuilder getBoardBuilder() {
		return m_builder;
	}
	
	/**
	 * Sets the {@link SelectedPositionListener} for this window's associated {@link BoardBuilder}
	 * @param listener the listener to assign
	 */
	public void setOnTileSelectedListener(SelectedPositionListener listener) {
		m_panel.setOnTileSelectedListener(listener);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		m_toolBox.dispose();
	}
	
}
