package catan.renderer.window.resources;

import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.SpinnerListModel;
import javax.swing.SwingConstants;

import catan.Catan;
import catan.engine.moves.PurchaseMove;
import catan.engine.resources.PurchaseCosts;
import catan.engine.resources.ResourceBundle;
import catan.engine.resources.trading.TradeExchange;
import catan.renderer.panel.BoardPanel;

/**
 * Window for trading
 * 
 * @author Jacob
 *
 */
public class TradeWindow extends JFrame {

	private static final int HORIZONTAL = 450;
	private static final int VERTICAL = 300;

	private Catan m_catan;

	private ArrayList<String> m_resources = new ArrayList<String>();

	/**
	 * Creates a new {@link TradeWindow}
	 * 
	 * @param catan
	 */
	public TradeWindow(Catan catan) {
		super("Trade");

		m_catan = catan;

		for (int i = 0; i < ResourceBundle.RESOURCE_NUMBER; i++) {
			m_resources.add(ResourceBundle.toString(i));
		}

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setBounds(BoardPanel.PANEL_HORIZONTAL + 55, 50, HORIZONTAL, VERTICAL);

		setResizable(false);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		getContentPane().add(new JLabel("Trade") {
			{
				setFont(new Font("Tahoma", Font.BOLD, 18));
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
			}
		});

		getContentPane().add(Box.createVerticalStrut(20));

		JSpinner input = new JSpinner(new SpinnerListModel(m_resources));

		JSplitPane inputPane = new JSplitPane();
		inputPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		inputPane.setTopComponent(new JLabel(PurchaseCosts.TRADEEXCHANGE_COST + "x ") {
			{
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setFont(new Font("Tahoma", Font.PLAIN, 11));
				setHorizontalAlignment(SwingConstants.CENTER);
			}
		});
		inputPane.setBottomComponent(input);
		inputPane.setEnabled(false);
		getContentPane().add(inputPane);

		getContentPane().add(Box.createVerticalStrut(20));

		getContentPane().add(new JLabel("For ") {
			{
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setFont(new Font("Tahoma", Font.PLAIN, 18));
				setHorizontalAlignment(SwingConstants.CENTER);
			}
		});

		getContentPane().add(Box.createVerticalStrut(20));

		JSpinner output = new JSpinner(new SpinnerListModel(m_resources));

		JSplitPane outputPane = new JSplitPane();
		outputPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		outputPane.setTopComponent(new JLabel(PurchaseCosts.TRADEEXCHANGE_YIELD + "x ") {
			{
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setFont(new Font("Tahoma", Font.PLAIN, 11));
				setHorizontalAlignment(SwingConstants.CENTER);
			}
		});
		outputPane.setBottomComponent(output);
		outputPane.setEnabled(false);
		getContentPane().add(outputPane);

		getContentPane().add(Box.createVerticalStrut(20));

		getContentPane().add(new JButton("Trade") {
			{
				setAlignmentX(Component.CENTER_ALIGNMENT);
				setAlignmentY(Component.CENTER_ALIGNMENT);
				addActionListener((performedAction) -> {
					if (new PurchaseMove(new TradeExchange(m_resources.indexOf(input.getValue()),
							m_resources.indexOf(output.getValue())), m_catan.getPlayer()).apply()) {
						dispose();
					}
				});
			}
		});

		getContentPane().add(Box.createVerticalStrut(20));

		setVisible(true);

		inputPane.setDividerLocation(0.5);
		outputPane.setDividerLocation(0.5);
	}

}
