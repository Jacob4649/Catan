package catan.renderer.window.menu;

import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import catan.Catan;
import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.IO;
import catan.engine.board.objects.InvalidLocationException;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.player.PlayerCountOutOfBoundsException;
import catan.engine.player.PlayerIndexOutOfBoundsException;

public class GameSetupMenu {

	private JFrame m_frame;
	private MainMenu m_menu;

	/**
	 * Create a new {@link GameSetupMenu}
	 */
	public GameSetupMenu(MainMenu menu) {
		m_menu = menu;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		m_frame = new JFrame() {
			@Override
			public void dispose() {
				super.dispose();
				m_menu.setVisible(true);
			}
		};
		m_frame.setBounds(100, 100, 450, 600);
		m_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		m_frame.getContentPane().setLayout(new BoxLayout(m_frame.getContentPane(), BoxLayout.Y_AXIS));

		m_frame.getContentPane().add(Box.createVerticalStrut(20));

		JLabel setupLabel = new JLabel("Setup Game");
		setupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		setupLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		setupLabel.setHorizontalAlignment(SwingConstants.CENTER);
		m_frame.getContentPane().add(setupLabel);

		m_frame.getContentPane().add(Box.createVerticalStrut(20));

		JSpinner playerCount = new JSpinner(
				new SpinnerNumberModel(Catan.NORMAL_PLAYERS, Catan.MIN_PLAYERS, Catan.MAX_PLAYERS, 1));

		JSplitPane playerCountPane = new JSplitPane();
		playerCountPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		playerCountPane.setTopComponent(new JLabel("Player Count:") {
			{
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setFont(new Font("Tahoma", Font.PLAIN, 11));
				setHorizontalAlignment(SwingConstants.CENTER);
			}
		});
		playerCountPane.setBottomComponent(playerCount);
		playerCountPane.setEnabled(false);
		m_frame.getContentPane().add(playerCountPane);

		m_frame.getContentPane().add(Box.createVerticalStrut(20));

		JLabel mapsLabel = new JLabel("Setup Map");
		mapsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mapsLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mapsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		m_frame.getContentPane().add(mapsLabel);

		m_frame.getContentPane().add(Box.createVerticalStrut(20));

		ButtonGroup mapTypeGroup = new ButtonGroup();

		JRadioButton randomMap = new JRadioButton("Random Map");
		randomMap.setAlignmentX(Component.CENTER_ALIGNMENT);

		m_frame.getContentPane().add(randomMap);
		mapTypeGroup.add(randomMap);

		JRadioButton loadMap = new JRadioButton("Load Map");
		loadMap.setAlignmentX(Component.CENTER_ALIGNMENT);

		m_frame.getContentPane().add(loadMap);
		mapTypeGroup.add(loadMap);

		randomMap.setSelected(true);

		m_frame.getContentPane().add(Box.createVerticalStrut(20));

		JSpinner row = new JSpinner(new SpinnerNumberModel(Board.DEFAULT_BOARD_DIMENSIONS[0],
				Board.MINIMUM_BOARD_DIMENSIONS[0], Board.MAXIMUM_BOARD_DIMENSIONS[0], 1));

		JSplitPane rowPane = new JSplitPane();
		rowPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		rowPane.setTopComponent(new JLabel("Rows:") {
			{
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setFont(new Font("Tahoma", Font.PLAIN, 11));
				setHorizontalAlignment(SwingConstants.CENTER);
			}
		});
		rowPane.setBottomComponent(row);
		rowPane.setEnabled(false);
		m_frame.getContentPane().add(rowPane);

		JSpinner column = new JSpinner(new SpinnerNumberModel(Board.DEFAULT_BOARD_DIMENSIONS[1],
				Board.MINIMUM_BOARD_DIMENSIONS[1], Board.MAXIMUM_BOARD_DIMENSIONS[1], 1));

		JSplitPane columnPane = new JSplitPane();
		columnPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		columnPane.setTopComponent(new JLabel("Columns:") {
			{
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setFont(new Font("Tahoma", Font.PLAIN, 11));
				setHorizontalAlignment(SwingConstants.CENTER);
			}
		});
		columnPane.setBottomComponent(column);
		columnPane.setEnabled(false);
		m_frame.getContentPane().add(columnPane);

		m_frame.getContentPane().add(Box.createVerticalStrut(20));

		File[] files = IO.getSavedBoards();

		JList<File> list = new JList<File>(files);
		list.setAlignmentX(Component.CENTER_ALIGNMENT);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		scrollPane.setAlignmentY(Component.CENTER_ALIGNMENT);
		scrollPane.setEnabled(false);

		m_frame.getContentPane().add(scrollPane);

		m_frame.getContentPane().add(Box.createVerticalStrut(20));

		JButton done = new JButton("Start Game");
		done.setAlignmentX(Component.CENTER_ALIGNMENT);
		done.setAlignmentY(Component.CENTER_ALIGNMENT);
		done.setEnabled(true);

		done.addActionListener((performedAction) -> {
			try {
				Board board;
				if (randomMap.isSelected()) {
					board = Board.randomLandBoard(new int[] { (int) row.getValue(), (int) column.getValue() });
				} else {
					board = IO.readBoard(list.getSelectedValue());
				}
				m_frame.dispose();
				m_menu.setVisible(false);
				new Thread(() -> {
					try {
						Catan.startCatan((int) playerCount.getValue(), board, m_menu);
					} catch (PlayerCountOutOfBoundsException | PlayerIndexOutOfBoundsException
							| BoardNotInitializedException | TileNotInitializedException | VertexNotInitializedException
							| InvalidLocationException e) {
						e.printStackTrace();
						System.exit(0);
					}
				}).start();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		});

		m_frame.getContentPane().add(done);

		list.addListSelectionListener((event) -> {
			done.setEnabled(list.getSelectedValue() != null);
		});

		randomMap.addActionListener((performedAction) -> {
			if (randomMap.isSelected()) {
				scrollPane.setEnabled(false);
				list.setEnabled(false);
				row.setEnabled(true);
				column.setEnabled(true);
			}
		});

		loadMap.addActionListener((performedAction) -> {
			if (loadMap.isSelected()) {
				scrollPane.setEnabled(true);
				list.setEnabled(true);
				row.setEnabled(false);
				column.setEnabled(false);
				done.setEnabled(list.getSelectedValue() != null);
			}
		});

		m_frame.setVisible(true);

		playerCountPane.setDividerLocation(0.5);
		rowPane.setDividerLocation(0.5);
		columnPane.setDividerLocation(0.5);
	}

}
