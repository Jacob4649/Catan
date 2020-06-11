package catan.renderer.window.boardbuilder;

import java.awt.Component;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.text.NumberFormatter;

import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.IO;
import catan.engine.board.tile.Tile;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.TileType;
import catan.renderer.panel.BoardPanel;

/**
 * Class for displaying tools used by the {@link BoardBuilderWindow}
 * 
 * @author Jacob
 *
 */
public class BoardBuilderToolBox extends JFrame {

	private static final int HORIZONTAL = 300;
	private static final int VERTICAL = 800;

	private BoardBuilderWindow m_window;

	/**
	 * Creates a new {@link BoardBuilderToolBox}
	 * 
	 * @param window
	 *            the {@link BoardBuilderWindow} to tie this
	 *            {@link BoardBuilderToolBox} to
	 */
	public BoardBuilderToolBox(BoardBuilderWindow window) {
		super("Tools");

		m_window = window;

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		setBounds(BoardPanel.PANEL_HORIZONTAL + 55, 0, HORIZONTAL, VERTICAL);

		setResizable(false);

		JPanel panel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(panel);

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(Box.createVerticalStrut(20));

		JTextField nameField = new JTextField();
		nameField.setColumns(10);

		JButton save = new JButton("Save");

		save.addActionListener((performedAction) -> {
			try {
				if (nameField.getText().trim().isEmpty()) {
					IO.writeBoard(m_window.getBoardBuilder().toBoard());
				} else {
					IO.writeBoard(m_window.getBoardBuilder().toBoard(), IO.toBoardFile(nameField.getText()));
				}
			} catch (BoardNotInitializedException | TileNotInitializedException e) {
				e.printStackTrace();
				System.exit(0);
			}
		});

		JSplitPane filePane = new JSplitPane();
		filePane.setEnabled(false);
		filePane.setAlignmentY(Component.CENTER_ALIGNMENT);
		filePane.setAlignmentX(Component.CENTER_ALIGNMENT);
		filePane.setTopComponent(nameField);
		filePane.setBottomComponent(save);
		panel.add(filePane);

		panel.add(Box.createVerticalStrut(20));

		JLabel rowsLabel = new JLabel("Rows:");
		rowsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		NumberFormatter rowsFormatter = new NumberFormatter(NumberFormat.getInstance());
		rowsFormatter.setValueClass(Integer.class);
		rowsFormatter.setMinimum(Board.MINIMUM_BOARD_DIMENSIONS[0]);
		rowsFormatter.setMaximum(Board.MAXIMUM_BOARD_DIMENSIONS[0]);
		rowsFormatter.setAllowsInvalid(true);
		rowsFormatter.setCommitsOnValidEdit(true);

		JFormattedTextField rowsField = new JFormattedTextField(rowsFormatter);
		rowsField.setText("" + Board.DEFAULT_BOARD_DIMENSIONS[0]);
		rowsField.setColumns(10);

		JSplitPane rowsPane = new JSplitPane();
		rowsPane.setEnabled(false);
		rowsPane.setAlignmentY(Component.CENTER_ALIGNMENT);
		rowsPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		rowsPane.setTopComponent(rowsLabel);
		rowsPane.setBottomComponent(rowsField);
		panel.add(rowsPane);

		panel.add(Box.createVerticalStrut(20));

		JLabel columnsLabel = new JLabel("Columns:");
		columnsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		NumberFormatter columnsFormatter = new NumberFormatter(NumberFormat.getInstance());
		columnsFormatter.setValueClass(Integer.class);
		columnsFormatter.setMinimum(Board.MINIMUM_BOARD_DIMENSIONS[1]);
		columnsFormatter.setMaximum(Board.MAXIMUM_BOARD_DIMENSIONS[1]);
		columnsFormatter.setAllowsInvalid(true);
		columnsFormatter.setCommitsOnValidEdit(true);

		JFormattedTextField columnsField = new JFormattedTextField(columnsFormatter);
		columnsField.setText("" + Board.DEFAULT_BOARD_DIMENSIONS[1]);
		columnsField.setColumns(10);

		JSplitPane columnsPane = new JSplitPane();
		columnsPane.setEnabled(false);
		columnsPane.setAlignmentY(Component.CENTER_ALIGNMENT);
		columnsPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		columnsPane.setTopComponent(columnsLabel);
		columnsPane.setBottomComponent(columnsField);
		panel.add(columnsPane);

		panel.add(Box.createVerticalStrut(20));

		JToggleButton overrideFrequency = new JToggleButton("Override Frequency");
		overrideFrequency.setAlignmentX(Component.CENTER_ALIGNMENT);

		NumberFormatter frequencyFormatter = new NumberFormatter(NumberFormat.getInstance());
		frequencyFormatter.setValueClass(Integer.class);
		frequencyFormatter.setMinimum(Tile.FREQUENCY_MIN);
		frequencyFormatter.setMaximum(Tile.FREQUENCY_MAX);
		frequencyFormatter.setAllowsInvalid(true);
		frequencyFormatter.setCommitsOnValidEdit(true);

		JFormattedTextField frequencyField = new JFormattedTextField(frequencyFormatter);
		frequencyField.setText("" + Tile.FREQUENCY_MIN);
		frequencyField.setColumns(10);

		JSplitPane frequencyPane = new JSplitPane();
		frequencyPane.setEnabled(false);
		frequencyPane.setAlignmentY(Component.CENTER_ALIGNMENT);
		frequencyPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		frequencyPane.setTopComponent(overrideFrequency);
		frequencyPane.setBottomComponent(frequencyField);
		panel.add(frequencyPane);

		panel.add(Box.createVerticalStrut(20));

		JButton oceanButton = new JButton(new ImageIcon(TileType.OCEAN.getBaseImage()));
		oceanButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		oceanButton.addActionListener((performedAction) -> {
			m_window.setOnTileSelectedListener((row, col) -> {
				m_window.getBoardBuilder()
						.setTileAt(row, col,
								(overrideFrequency.isSelected() ? (int) frequencyField.getValue()
										: m_window.getBoardBuilder().getTileAt(row, col).getFrequency()),
								TileType.OCEAN);
			});
		});
		panel.add(oceanButton);

		panel.add(Box.createVerticalStrut(20));

		JButton quarryButton = new JButton(new ImageIcon(TileType.QUARRY.getBaseImage()));
		quarryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		quarryButton.addActionListener((performedAction) -> {
			m_window.setOnTileSelectedListener((row, col) -> {
				m_window.getBoardBuilder()
						.setTileAt(row, col,
								(overrideFrequency.isSelected() ? (int) frequencyField.getValue()
										: m_window.getBoardBuilder().getTileAt(row, col).getFrequency()),
								TileType.QUARRY);
			});
		});
		panel.add(quarryButton);

		panel.add(Box.createVerticalStrut(20));

		JButton fieldButton = new JButton(new ImageIcon(TileType.FIELD.getBaseImage()));
		fieldButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		fieldButton.addActionListener((performedAction) -> {
			m_window.setOnTileSelectedListener((row, col) -> {
				m_window.getBoardBuilder()
						.setTileAt(row, col,
								(overrideFrequency.isSelected() ? (int) frequencyField.getValue()
										: m_window.getBoardBuilder().getTileAt(row, col).getFrequency()),
								TileType.FIELD);
			});
		});
		panel.add(fieldButton);

		panel.add(Box.createVerticalStrut(20));

		JButton clayButton = new JButton(new ImageIcon(TileType.CLAY.getBaseImage()));
		clayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		clayButton.addActionListener((performedAction) -> {
			m_window.setOnTileSelectedListener((row, col) -> {
				m_window.getBoardBuilder()
						.setTileAt(row, col,
								(overrideFrequency.isSelected() ? (int) frequencyField.getValue()
										: m_window.getBoardBuilder().getTileAt(row, col).getFrequency()),
								TileType.CLAY);
			});
		});
		panel.add(clayButton);

		panel.add(Box.createVerticalStrut(20));

		JButton pastureButton = new JButton(new ImageIcon(TileType.PASTURE.getBaseImage()));
		pastureButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		pastureButton.addActionListener((performedAction) -> {
			m_window.setOnTileSelectedListener((row, col) -> {
				m_window.getBoardBuilder()
						.setTileAt(row, col,
								(overrideFrequency.isSelected() ? (int) frequencyField.getValue()
										: m_window.getBoardBuilder().getTileAt(row, col).getFrequency()),
								TileType.PASTURE);
			});
		});
		panel.add(pastureButton);

		panel.add(Box.createVerticalStrut(20));

		JButton forestButton = new JButton(new ImageIcon(TileType.FOREST.getBaseImage()));
		forestButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		forestButton.addActionListener((performedAction) -> {
			m_window.setOnTileSelectedListener((row, col) -> {
				m_window.getBoardBuilder()
						.setTileAt(row, col,
								(overrideFrequency.isSelected() ? (int) frequencyField.getValue()
										: m_window.getBoardBuilder().getTileAt(row, col).getFrequency()),
								TileType.FOREST);
			});
		});
		panel.add(forestButton);

		panel.add(Box.createVerticalStrut(20));

		JButton desertButton = new JButton(new ImageIcon(TileType.DESERT.getBaseImage()));
		desertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		desertButton.addActionListener((performedAction) -> {
			m_window.setOnTileSelectedListener((row, col) -> {
				m_window.getBoardBuilder()
						.setTileAt(row, col,
								(overrideFrequency.isSelected() ? (int) frequencyField.getValue()
										: m_window.getBoardBuilder().getTileAt(row, col).getFrequency()),
								TileType.DESERT);
			});
		});
		panel.add(desertButton);

		getContentPane().add(scrollPane);

		setVisible(true);

		// Split Pane Dividers
		rowsPane.setDividerLocation(0.5);
		columnsPane.setDividerLocation(0.5);
		filePane.setDividerLocation(0.5);

		// Column and row change listeners
		columnsField.addPropertyChangeListener((event) -> {
			m_window.getBoardBuilder().setDimensions((int) rowsField.getValue(), (int) columnsField.getValue());
		});
		rowsField.addPropertyChangeListener((event) -> {
			m_window.getBoardBuilder().setDimensions((int) rowsField.getValue(), (int) columnsField.getValue());
		});
	}

}
