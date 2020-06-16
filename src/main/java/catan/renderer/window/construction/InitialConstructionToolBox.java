package catan.renderer.window.construction;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import catan.Catan;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.SelectedPositionListener;
import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.engine.board.objects.InvalidLocationException;
import catan.engine.board.objects.VertexObject;
import catan.engine.board.objects.buildings.City;
import catan.engine.board.objects.buildings.Road;
import catan.engine.board.objects.buildings.Village;
import catan.engine.board.objects.buildings.construction.ConstructRoad;
import catan.engine.board.objects.buildings.construction.ConstructVillage;
import catan.engine.board.objects.buildings.construction.UpgradeVillage;
import catan.engine.board.tile.Edge;
import catan.engine.board.tile.Vertex;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.moves.PurchaseMove;
import catan.renderer.panel.BoardPanel;
import catan.renderer.window.resources.ResourceMetricWindow;

/**
 * ToolBox for constructing the player's first settlements
 * 
 * @author Jacob
 *
 */
public class InitialConstructionToolBox extends JFrame {

	private static final int HORIZONTAL = 300;
	private static final int VERTICAL = 300;
	private Vertex m_selectHistory = null;

	private ResourceMetricWindow m_resourceWindow;

	private Catan m_catan;
	private JTextField m_selectedTile;
	private JPanel m_panel;
	private ButtonGroup m_selectGroup = new ButtonGroup();

	private final SelectedPositionListener m_updateSelection = (row, col) -> {
		try {
			m_selectedTile.setText(m_catan.getBoard().getTileAt(row, col).toString());
		} catch (BoardNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	};

	private final Runnable m_deselect = () -> {
		m_selectedTile.setText("None");
		m_catan.getBoardPanel().setOnTileSelectedListener(m_updateSelection);
		m_selectGroup.clearSelection();
	};

	/**
	 * Creates a new {@link InitialConstructionToolBox}
	 * 
	 * @param catan
	 *            the game of {@link Catan} to create for
	 */
	public InitialConstructionToolBox(Catan catan) {

		super("Actions");

		m_catan = catan;
		
		catan.getBoardPanel().setOnTileSelectedListener(m_updateSelection);
		catan.getBoardPanel().setOnDeselectListener(m_deselect);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		setBounds(BoardPanel.PANEL_HORIZONTAL + 55, 0, HORIZONTAL, VERTICAL);

		setResizable(false);

		m_panel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(m_panel);

		m_panel.setLayout(new BoxLayout(m_panel, BoxLayout.Y_AXIS));

		m_panel.add(new JLabel("Selected") {
			{
				setFont(new Font("Tahoma", Font.BOLD, 18));
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
			}
		});

		m_selectedTile = new JTextField() {
			{
				setText("None");
				setEditable(false);
				setColumns(10);
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
			}
		};

		m_panel.add(Box.createVerticalStrut(20));

		m_panel.add(new JSplitPane() {
			{
				setEnabled(false);
				setAlignmentY(Component.CENTER_ALIGNMENT);
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setTopComponent(new JLabel("Item:") {
					{
						setAlignmentX(Component.CENTER_ALIGNMENT);
						setAlignmentY(Component.CENTER_ALIGNMENT);
					}
				});
				setBottomComponent(m_selectedTile);
			}
		});

		m_panel.add(Box.createVerticalStrut(20));

		m_panel.add(new JLabel("Construction") {
			{
				setFont(new Font("Tahoma", Font.BOLD, 18));
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
			}
		});

		m_panel.add(Box.createVerticalStrut(20));

		m_panel.add(new JLabel("Please Build Two Starting Villages, And Roads") {
			{
				setFont(new Font("Tahoma", Font.PLAIN, 11));
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
			}
		});

		m_panel.add(Box.createVerticalStrut(20));

		m_selectGroup.add((JToggleButton) m_panel.add(new JToggleButton("Build Village And Road") {
			{
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
				addActionListener((performedAction) -> {
					m_selectHistory = null;
					m_catan.getBoardPanel().setDefault();
					if (isSelected()) {
						m_catan.getBoardPanel().setPreSelection(true);
						m_catan.getBoardPanel().setSelectVertex(true);
						m_catan.getBoardPanel().setOnDeselectListener(m_deselect);
						m_catan.getBoardPanel().setOnVertexSelectedListener((row, col) -> {
							try {
								Vertex location = catan.getBoard().getVertex(row, col);
								// build village and road
								if (m_selectHistory == null) {
									// find village location
									if (catan.getBoard().getAllObjectsMatching((object) -> {
										try {
											return object instanceof City || object instanceof Village
													&& ((VertexObject) object).getPosition().isAdjacent(location);
										} catch (VertexNotInitializedException | BoardObjectNotInitializedException e) {
											e.printStackTrace();
											System.exit(0);
											return false;
										}
									}).length == 0) {
										m_selectHistory = location;
									}
								} else {
									// build road and village
									Edge edge = new Edge(location.getPosition(), m_selectHistory.getPosition(),
											catan.getBoard());
									if (Math.abs(edge.getDelta()[0]) + Math.abs(edge.getDelta()[1]) == 1) {
										// if single length
										catan.getBoard().addObject(new Road(catan.getPlayer(), edge, true));
										catan.getBoard().addObject(new Village(catan.getPlayer(), m_selectHistory));
										m_catan.getBoardPanel().deselect();
										m_selectGroup.clearSelection();
									}
								}

								if (catan.getBoard().getAllObjectsMatching((object) -> object instanceof Village
										&& object.getOwner() == catan.getPlayer()).length >= 2) {
									// 2 villages built
									catan.notifyGame();
								}
							} catch (BoardNotInitializedException | InvalidLocationException
									| VertexNotInitializedException e) {
								e.printStackTrace();
								System.exit(0);
							}
						});
					}
				});
			}
		}));

		m_panel.add(Box.createVerticalStrut(20));

		getContentPane().add(scrollPane);

		setVisible(true);

	}

}
