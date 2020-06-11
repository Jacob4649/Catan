package catan.renderer.panel;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import catan.Catan;
import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.SelectedBoardObjectListener;
import catan.engine.board.SelectedPositionListener;
import catan.engine.board.objects.BoardObject;
import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.engine.board.tile.Tile;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.TileType;
import catan.engine.board.tile.Vertex;
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

	private static final double VERTEX_INDICATOR_RELATIVE_SIZE = 0.4;

	private boolean m_mouseLock = false;
	private boolean m_preSelectionIndicator = false;
	private boolean m_selectVertex = false;
	private int[] m_selected;
	private BoardObject m_selectedObject;
	private SelectedPositionListener m_selectedTileListener;
	private SelectedPositionListener m_selectedVertexListener;
	private SelectedBoardObjectListener m_selectedObjectListener;

	private Board m_board;

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

		//Deselect keybinding
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                "deselect");
		getActionMap().put("deselect", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_selected = null;
			}
		});
		
		//mouse listener
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				if (!m_mouseLock) {
					m_mouseLock = true;
					new Thread(() -> {

						m_selected = null;
						m_selectedObject = null;

						// check selected objects
						for (BoardObject object : m_board.getObjects()) {
							try {
								int[] firstCorner = object.getImagePosition(m_board.getDimensions(),
										new int[] { getWidth(), getHeight() });
								int[] dimensions = object.getImageDimensions(m_board.getDimensions(),
										new int[] { getWidth(), getHeight() });
								if (event.getX() > firstCorner[0] && event.getX() < firstCorner[0] + dimensions[0]
										&& event.getY() > firstCorner[1]
										&& event.getY() < firstCorner[1] + dimensions[1]) {
									m_selectedObject = object;
									if (m_selectedObjectListener != null) {
										m_selectedObjectListener.onSelect(m_selectedObject);
									}
									m_mouseLock = false;
									return;
								}
							} catch (BoardObjectNotInitializedException | BoardNotInitializedException e) {
								e.printStackTrace();
								System.exit(0);
							}
						}

						// check selected tiles or vertices

						try {
							if (m_selectVertex) {
								m_selected = pixelToVertex(event.getX(), event.getY());
								if (m_selectedVertexListener != null) {
									m_selectedVertexListener.onSelect(m_selected[0], m_selected[1]);
								}
							} else {
								m_selected = pixelToPos(event.getX(), event.getY());
								if (m_selectedTileListener != null) {
									m_selectedTileListener.onSelect(m_selected[0], m_selected[1]);
								}	
							}
						} catch (BoardNotInitializedException | TileNotInitializedException e) {
							e.printStackTrace();
							System.exit(0);
						}

						m_mouseLock = false;
					}).start();
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2D = (Graphics2D) g.create();
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		try {
			// check if resize needed
			if (TileType.getWithValue(0).getImage().getTileWidth() != Tile
					.getTilePixelDimensions(m_board.getDimensions(), new int[] { getWidth(), getHeight() })[0]) {
				resize();
			}

			// paint tiles
			for (int row = 0; row < m_board.getDimensions()[0]; row++) {
				for (int col = 0; col < m_board.getDimensions()[1]; col++) {
					try {
						g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
						g2D.drawImage(m_board.getTileAt(row, col).getImage(), colToInt(col), rowToInt(row), null);
					} catch (TileNotInitializedException e) {
					}
				}
			}

			// paint grid
			g2D.setColor(Colors.BOARD_GRID_COLOR);
			g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
			g2D.setStroke(new BasicStroke(5f));

			// horizontal lines
			for (int i = 0; i <= m_board.getDimensions()[0]; i++) {
				int height = (int) (((float) i / (float) m_board.getDimensions()[0]) * getHeight());
				g2D.drawLine(0, height, getWidth(), height);
			}

			// vertical lines
			for (int i = 0; i <= m_board.getDimensions()[1]; i++) {
				int width = (int) (((float) i / (float) m_board.getDimensions()[1]) * getWidth());
				g2D.drawLine(width, 0, width, getHeight());
			}

			// paint objects
			for (BoardObject object : m_board.getObjects()) {
				try {
					int[] position = object.getImagePosition(m_board.getDimensions(),
							new int[] { getWidth(), getHeight() });
					g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
					g2D.drawImage(object.getImage(), position[0], position[1], null);
				} catch (BoardObjectNotInitializedException e) {
				}
			}

			// paint selected tile, vertex, or object
			if (m_selected != null) {
				g2D.setColor(Colors.BOARD_SELECTED_COLOR);
				g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
				g2D.setStroke(new BasicStroke(5f));

				int[] tileDimensions = Tile.getTilePixelDimensions(m_board.getDimensions(),
						new int[] { getWidth(), getHeight() });
				if (m_selectVertex) {
					g2D.drawRect(
							(int) ((((double) m_selected[1] / (double) m_board.getDimensions()[1]) * (double) getWidth())
									- (VERTEX_INDICATOR_RELATIVE_SIZE * ((double) (tileDimensions[0]) / 2d))),
							(int) ((((double) m_selected[0] / (double) m_board.getDimensions()[0]) * (double) getHeight())
									- (VERTEX_INDICATOR_RELATIVE_SIZE * ((double) (tileDimensions[1])) / 2d)),
							(int) (VERTEX_INDICATOR_RELATIVE_SIZE * ((double) (tileDimensions[0]))),
							(int) (VERTEX_INDICATOR_RELATIVE_SIZE * ((double) (tileDimensions[1]))));
				} else {
					g2D.drawRect((int) (((float) m_selected[1] / (float) m_board.getDimensions()[1]) * getWidth()),
							(int) (((float) m_selected[0] / (float) m_board.getDimensions()[0]) * getHeight()),
							tileDimensions[0], tileDimensions[1]);	
				}
			}

			if (m_selectedObject != null) {
				try {
					g2D.setColor(Colors.BOARD_SELECTED_COLOR);
					g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
					g2D.setStroke(new BasicStroke(5f));

					int[] objectPosition = m_selectedObject.getImagePosition(m_board.getDimensions(),
							new int[] { getWidth(), getHeight() });
					int[] objectDimensions = m_selectedObject.getImageDimensions(m_board.getDimensions(),
							new int[] { getWidth(), getHeight() });

					g2D.drawRect(objectPosition[0], objectPosition[1], objectDimensions[0], objectDimensions[1]);
				} catch (BoardObjectNotInitializedException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}

			// paint preselection indicator
			if (m_preSelectionIndicator) {
				g2D.setColor(Colors.BOARD_PRE_SELECTED_COLOR);
				g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
				g2D.setStroke(new BasicStroke(5f));
				int[] tileDimensions = Tile.getTilePixelDimensions(m_board.getDimensions(),
						new int[] { getWidth(), getHeight() });
				Point p = MouseInfo.getPointerInfo().getLocation();
				SwingUtilities.convertPointFromScreen(p, this);
				if (m_selectVertex) {
					// vertex mode
					int[] mapDimensions = m_board.getDimensions();
					int[] dimensions = m_board.getVertexDimensions();
					int[] vertexPos = pixelToVertex((int) p.getX(), (int) p.getY());
					// check bounds
					if (vertexPos[0] >= 0 && vertexPos[0] < dimensions[0] && vertexPos[1] >= 0
							&& vertexPos[1] < dimensions[1]) {
						g2D.drawRect(
								(int) ((((double) vertexPos[1] / (double) mapDimensions[1]) * (double) getWidth())
										- (VERTEX_INDICATOR_RELATIVE_SIZE * ((double) (tileDimensions[0]) / 2d))),
								(int) ((((double) vertexPos[0] / (double) mapDimensions[0]) * (double) getHeight())
										- (VERTEX_INDICATOR_RELATIVE_SIZE * ((double) (tileDimensions[1])) / 2d)),
								(int) (VERTEX_INDICATOR_RELATIVE_SIZE * ((double) (tileDimensions[0]))),
								(int) (VERTEX_INDICATOR_RELATIVE_SIZE * ((double) (tileDimensions[1]))));
					}
				} else {
					// tile mode
					int[] dimensions = m_board.getDimensions();
					int[] tilePos = pixelToPos((int) p.getX(), (int) p.getY());
					// check bounds
					if (tilePos[0] >= 0 && tilePos[0] < dimensions[0] && tilePos[1] >= 0
							&& tilePos[1] < dimensions[1]) {
						g2D.drawRect((int) (((float) tilePos[1] / (float) dimensions[1]) * getWidth()),
								(int) (((float) tilePos[0] / (float) dimensions[0]) * getHeight()),
								tileDimensions[0], tileDimensions[1]);
					}
				}
			}

			// paint ui details

		} catch (BoardNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
		}

		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PANEL_HORIZONTAL, PANEL_VERTICAL);
	}

	/**
	 * 
	 * @return an array containing the position of the currently selected
	 *         {@link Tile} {row, col}, or null if no {@link Tile} is selected
	 */
	public int[] getSelectedTile() {
		return m_selected;
	}

	/**
	 * Resizes all drawn {@link BufferedImage}s
	 * 
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 */
	private void resize() throws BoardNotInitializedException {
		for (TileType type : TileType.values()) {
			type.getImage(Tile.getTilePixelDimensions(m_board.getDimensions(), new int[] { getWidth(), getHeight() }));
		}
		for (BoardObject object : m_board.getObjects()) {
			try {
				object.getImage(m_board.getDimensions(), new int[] { getWidth(), getHeight() });
			} catch (BoardObjectNotInitializedException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	/**
	 * Sets the {@link Tile} {@link SelectedPositionListener} for this
	 * {@link BoardPanel}
	 * 
	 * @param listener
	 *            the {@link SelectedPositionListener} to assign
	 */
	public void setOnTileSelectedListener(SelectedPositionListener listener) {
		m_selectedTileListener = listener;
	}

	/**
	 * Sets the {@link SelectedBoardObjectListener} for this {@link BoardPanel}
	 * 
	 * @param listener
	 *            the {@link SelectedObjectListener} to assign
	 */
	public void setOnObjectSelectedListener(SelectedBoardObjectListener listener) {
		m_selectedObjectListener = listener;
	}

	/**
	 * Sets the {@link Vertex} {@link SelectedPositionListener} for this
	 * {@link BoardPanel}
	 * 
	 * @param listener
	 *            the {@link SelectedPositionListener} to assign
	 */
	public void setOnVertexSelectedListener(SelectedPositionListener listener) {
		m_selectedVertexListener = listener;
	}

	/**
	 * Set whether {@link Vertex} (Vertices) or {@link Tile}s should be selected
	 * 
	 * @param select
	 *            true for {@link Vertex} (vertices)
	 */
	public void setSelectVertex(boolean select) {
		m_selectVertex = select;
	}

	/**
	 * Set whether a pre selection indicator should be visible on the board
	 * 
	 * @param select
	 *            true for visible
	 */
	public void setPreSelection(boolean select) {
		m_preSelectionIndicator = select;
	}

	/**
	 * 
	 * @return the {@link Board} being used by this {@link BoardPanel}
	 */
	public Board getBoard() {
		return m_board;
	}
	
	/**
	 * Converts an index of a horizontal {@link Tile} on the board to a point
	 * 
	 * @param row
	 *            the row, 0-BoardSize to get the point for
	 * @return the pixel number for the specified point
	 * @throws BoardNotInitializedException
	 *             if the board has not been initialized
	 */
	private int rowToInt(int row) throws BoardNotInitializedException {
		return ((int) (((double) row / (double) m_board.getDimensions()[0]) * getHeight()));
	}

	/**
	 * Converts an index of a vertical {@link Tile} on the board to a point
	 * 
	 * @param col
	 *            the column, 0-BoardSize to get the point for
	 * @return the pixel number for the specified point
	 * @throws BoardNotInitializedException
	 *             if the board has not been initialized
	 */
	private int colToInt(int col) throws BoardNotInitializedException {
		return ((int) (((double) col / (double) m_board.getDimensions()[1]) * getWidth()));
	}

	/**
	 * Gets the {@link Tile} that the cursor is over
	 * 
	 * @param hor
	 *            the horizontal pixel
	 * @param vert
	 *            the vertical pixel
	 * @return an array containing the row and column of the {@link Tile} {row,
	 *         col}
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 */
	protected int[] pixelToPos(int hor, int vert) throws BoardNotInitializedException {
		return new int[] { (int) (vert / (getHeight() / m_board.getDimensions()[0])),
				(int) (hor / (getWidth() / m_board.getDimensions()[1])) };
	}

	/**
	 * Gets the {@link Vertex} that the cursor is over
	 * 
	 * @param hor
	 *            the horizontal pixel
	 * @param vert
	 *            the vertical pixel
	 * @return an array containing the row and column of the {@link Vertex}
	 *         {row, col}
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 */
	protected int[] pixelToVertex(int hor, int vert) throws BoardNotInitializedException {
		return new int[] {
				(int) (((double) vert / ((double) getHeight() / (double) m_board.getDimensions()[0])) + 0.5d),
				(int) (((double) hor / ((double) getWidth() / (double) m_board.getDimensions()[1])) + 0.5d) };
	}

}
