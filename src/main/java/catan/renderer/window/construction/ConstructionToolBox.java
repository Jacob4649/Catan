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
 * Class representing toolbox window the user uses to pick actions on their turn
 * 
 * @author Jacob
 *
 */
public class ConstructionToolBox extends JFrame {

	private static final int HORIZONTAL = 300;
	private static final int VERTICAL = 600;
	private Vertex m_selectHistory = null;

	private ResourceMetricWindow m_resourceWindow;

	private Catan m_catan;
	private JTextField m_wood, m_clay, m_stone, m_grain, m_sheep, m_selectedTile;
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
	 * Catan game to tie this {@link ConstructionToolBox} to
	 * 
	 * @param catan
	 */
	public ConstructionToolBox(Catan catan) {
		super("Actions");

		m_catan = catan;

		m_catan.getPlayer().getResources().setOnChangeListener(this::updateResources);
		m_catan.getBoardPanel().setOnTileSelectedListener(m_updateSelection);
		catan.getBoardPanel().setOnDeselectListener(m_deselect);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		setBounds(BoardPanel.PANEL_HORIZONTAL + 55, 0, HORIZONTAL, VERTICAL);
		
		setResizable(false);

		m_panel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(m_panel);

		m_panel.setLayout(new BoxLayout(m_panel, BoxLayout.Y_AXIS));

		m_panel.add(new JLabel("Player") {
			{
				setFont(new Font("Tahoma", Font.BOLD, 18));
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
			}
		});
		
		m_panel.add(Box.createVerticalStrut(20));
		
		String color = catan.getPlayer().getColor().toString().toLowerCase();
		color = color.substring(0, 1).toUpperCase() + color.substring(1);
		m_panel.add(new JLabel("Player Is " + color + " ") {
			{
				setFont(new Font("Tahoma", Font.ITALIC, 11));
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
			}
		});
		
		m_panel.add(Box.createVerticalStrut(20));
		
		m_panel.add(new JLabel("Resources") {
			{
				setFont(new Font("Tahoma", Font.BOLD, 18));
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
			}
		});

		m_panel.add(Box.createVerticalStrut(20));

		m_panel.add(new JButton("View Production Levels") {
			{
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
				addActionListener((performedAction) -> {
					try {
						if (m_resourceWindow != null) {
							m_resourceWindow.dispose();
						}
						m_resourceWindow = new ResourceMetricWindow(
								m_catan.getPlayer().getProductionMetric(m_catan.getBoard()),
								"Player Production Statistics");
					} catch (BoardNotInitializedException e) {
						e.printStackTrace();
						System.exit(0);
					}
				});
			}
		});

		m_wood = createResourceField("Wood:");
		m_clay = createResourceField("Clay:");
		m_stone = createResourceField("Stone:");
		m_grain = createResourceField("Grain:");
		m_sheep = createResourceField("Sheep:");

		updateResources();

		m_panel.add(Box.createVerticalStrut(20));

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

		m_selectGroup.add((JToggleButton) m_panel.add(new JToggleButton("Build Road") {
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
								if (m_selectHistory == null) {
									m_selectHistory = catan.getBoard().getVertex(row, col);
								} else {
									Edge location = new Edge(new int[] { row, col }, m_selectHistory.getPosition(),
											catan.getBoard());
									if (Road.isValidLocation(location, catan.getPlayer())) {
										new PurchaseMove(new ConstructRoad(new Road(catan.getPlayer(), location)),
												catan.getPlayer()).apply();
										catan.syncNextTurn();
									}
									m_catan.getBoardPanel().deselect();
									m_selectGroup.clearSelection();
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

		m_selectGroup.add((JToggleButton) m_panel.add(new JToggleButton("Build Village") {
			{
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
				addActionListener((performedAction) -> {
					m_catan.getBoardPanel().setDefault();
					if (isSelected()) {
						m_catan.getBoardPanel().setPreSelection(true);
						m_catan.getBoardPanel().setSelectVertex(true);
						m_catan.getBoardPanel().setOnDeselectListener(m_deselect);
						m_catan.getBoardPanel().setOnVertexSelectedListener((row, col) -> {
							try {
								Vertex location = catan.getBoard().getVertex(row, col);
								if (Village.isValidLocation(location, catan.getPlayer())) {
									new PurchaseMove(new ConstructVillage(new Village(catan.getPlayer(), location)),
											catan.getPlayer()).apply();
									catan.syncNextTurn();
								}
								m_catan.getBoardPanel().deselect();
								m_selectGroup.clearSelection();
							} catch (BoardNotInitializedException | InvalidLocationException e) {
								e.printStackTrace();
								System.exit(0);
							}
						});
					}
				});
			}
		}));

		m_panel.add(Box.createVerticalStrut(20));

		m_selectGroup.add((JToggleButton) m_panel.add(new JToggleButton("Build City") {
			{
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
				addActionListener((performedAction) -> {
					m_catan.getBoardPanel().setDefault();
					if (isSelected()) {
						m_catan.getBoardPanel().setPreSelection(true);
						m_catan.getBoardPanel().setSelectObject(true);
						m_catan.getBoardPanel().setOnDeselectListener(m_deselect);
						m_catan.getBoardPanel().setOnObjectSelectedListener((object) -> {
							if (object instanceof VertexObject) {
								Vertex location = ((VertexObject) object).getPosition();
								if (City.isValidLocation(location, catan.getPlayer())) {
									new PurchaseMove(new UpgradeVillage((Village) object), catan.getPlayer()).apply();
									catan.syncNextTurn();
								}
							}
							m_catan.getBoardPanel().deselect();
							m_selectGroup.clearSelection();
						});
					}
				});
			}
		}));

		m_panel.add(Box.createVerticalStrut(20));

		m_panel.add(new JButton("End Turn") {
			{
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
				addActionListener((performedAction) -> {
					m_catan.getBoardPanel().deselect();
					m_selectGroup.clearSelection();
					catan.syncNextTurn();
				});
			}
		});

		m_panel.add(Box.createVerticalStrut(20));

		getContentPane().add(scrollPane);

		setVisible(true);
	}

	/**
	 * Creates a new {@link JTextField} for a resource and adds it to the
	 * {@link JPanel}
	 * 
	 * @param name
	 *            the {@link String} name of the new resource
	 * @return the new {@link JTextField}
	 */
	private JTextField createResourceField(String name) {

		JTextField field = new JTextField() {
			{
				setText("" + 0);
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
				setTopComponent(new JLabel(name) {
					{
						setAlignmentX(Component.CENTER_ALIGNMENT);
						setAlignmentY(Component.CENTER_ALIGNMENT);
					}
				});
				setBottomComponent(field);
			}
		});

		return field;
	}

	/**
	 * Displays updated resource values for all resources
	 */
	private void updateResources() {
		m_wood.setText("" + m_catan.getPlayer().getResources().getWood());
		m_clay.setText("" + m_catan.getPlayer().getResources().getClay());
		m_stone.setText("" + m_catan.getPlayer().getResources().getStone());
		m_grain.setText("" + m_catan.getPlayer().getResources().getGrain());
		m_sheep.setText("" + m_catan.getPlayer().getResources().getSheep());
	}

	@Override
	public void dispose() {
		super.dispose();
		if (m_resourceWindow != null) {
			m_resourceWindow.dispose();
		}
		m_catan.getPlayer().getResources().setOnChangeListener(null);
	}

}
