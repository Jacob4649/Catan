package catan.renderer.window.menu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import catan.engine.board.IO;
import catan.renderer.window.boardbuilder.BoardBuilderWindow;

public class MapsMenu {

	private JFrame m_frame;
	private JList<File> m_list;

	/**
	 * Create a new {@link MapsMenu}
	 */
	public MapsMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		if (m_frame != null) {
			m_frame.dispose();
		}
		m_frame = new JFrame();
		m_frame.setBounds(550, 100, 450, 300);
		m_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		m_frame.setResizable(false);
		m_frame.getContentPane().setLayout(new BoxLayout(m_frame.getContentPane(), BoxLayout.Y_AXIS));
		
		m_frame.getContentPane().add(Box.createVerticalStrut(20));
		
		JLabel mapLabel = new JLabel("Maps");
		mapLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mapLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		mapLabel.setHorizontalAlignment(SwingConstants.CENTER);
		m_frame.getContentPane().add(mapLabel);
		
		m_frame.getContentPane().add(Box.createVerticalStrut(20));
		
		JButton addMap = new JButton("Add Map");
		addMap.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		addMap.addActionListener((performedAction) -> {
			new BoardBuilderWindow() {
				@Override
				public void dispose() {
					super.dispose();
					m_frame.setVisible(true);
					initialize();
				}
			};
			m_frame.setVisible(false);
		});
		
		JButton deleteMap = new JButton("Delete Map");
		deleteMap.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteMap.setEnabled(false);
		
		deleteMap.addActionListener((performedAction) -> {
			if (m_list.getSelectedValue().delete()) {
				initialize();
			}
		});
		
		File[] files = IO.getSavedBoards();
		
		m_list = new JList<File>(files);
		m_list.setAlignmentX(Component.CENTER_ALIGNMENT);
		m_list.setLayoutOrientation(JList.VERTICAL);
		m_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_list.addListSelectionListener((event) -> {
			if (m_list.getSelectedValue() == null) {
				deleteMap.setEnabled(false);
			} else {
				deleteMap.setEnabled(true);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(m_list);
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		scrollPane.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		m_frame.getContentPane().add(scrollPane);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		splitPane.setTopComponent(addMap);
		splitPane.setBottomComponent(deleteMap);
		splitPane.setEnabled(false);
		m_frame.getContentPane().add(splitPane);
		
		m_frame.setVisible(true);
		
		splitPane.setDividerLocation(0.5);
	}

}
