package catan.renderer.window.resources;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import catan.engine.resources.ResourceBundle;
import catan.engine.resources.ResourceMetric;
import catan.renderer.Colors;

/**
 * Window for displaying a {@link ResourceMetric}
 * 
 * @author Jacob
 *
 */
public class ResourceMetricWindow extends JFrame {

	private static final int PANEL_HORIZONTAL = 600;
	private static final int PANEL_VERTICAL = 600;

	private static final int GRAPH_PIXEL_SPACING = 20;
	private static final int GRAPH_BAR_END_PADDING = 10;
	private static final int GRAPH_END_PADDING = 50;

	public static final int TEXT_UNDERLINE_PADDING = 10;

	int[][] m_orderedMetric;

	/**
	 * Creates a new {@link ResourceMetricWindow}
	 * 
	 * @param metric
	 *            the {@link ResourceMetric} to show
	 * @param title
	 *            the window title
	 */
	public ResourceMetricWindow(ResourceMetric metric, String title) {
		super(title);

		setResizable(false);
		
		getContentPane().add(new JPanel() {
			{
				setBorder(BorderFactory.createLineBorder(Colors.RESOURCE_BORDER_COLOR));
				setBackground(Colors.RESOURCE_BACKGROUND_COLOR);
			}

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				Graphics2D g2D = (Graphics2D) g.create();
				g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				if (m_orderedMetric == null) {
					m_orderedMetric = metric.getOrderedList();
				}

				for (int i = 0; i < m_orderedMetric.length; i++) {
					int metricIndex = m_orderedMetric.length - 1 - i;
					double fraction = ((double) i / (double) m_orderedMetric.length);
					double metricFraction = (double) m_orderedMetric[metricIndex][1]
							/ (double) m_orderedMetric[m_orderedMetric.length - 1][1];
					int startHeight = (int) (fraction * getHeight()) + GRAPH_PIXEL_SPACING;
					int width = (int) (metricFraction * (getWidth() - 2 * GRAPH_END_PADDING - GRAPH_BAR_END_PADDING));
					int height = (int) ((double) getHeight() / (double) m_orderedMetric.length)
							- 2 * GRAPH_PIXEL_SPACING;

					Color barColor = Color.getHSBColor((float) fraction, 1f, 1f);
					g2D.setColor(barColor);
					g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

					g2D.fillRect(GRAPH_END_PADDING, startHeight, width, height);

					g2D.setFont(new Font("Arial", Font.PLAIN, 25));
					g2D.setColor(Colors.RESOURCE_TEXT);
					g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
					g2D.setStroke(new BasicStroke(7f));

					String text = ResourceBundle.toString(m_orderedMetric[metricIndex][0]) + ": "
							+ m_orderedMetric[metricIndex][1];
					int textWidth = g2D.getFontMetrics().stringWidth(text);
					int textHeight = g2D.getFontMetrics().getHeight();
					int textStartHor = (getWidth() - 2 * GRAPH_END_PADDING - GRAPH_BAR_END_PADDING) - textWidth;
					int textStartVert = (int) ((startHeight + ((double) height / 2d)) + ((double) textHeight / 2d));

					g2D.drawString(text, textStartHor, textStartVert);
					g2D.drawLine(textStartHor - TEXT_UNDERLINE_PADDING, textStartVert + TEXT_UNDERLINE_PADDING,
							textStartHor + 2 * TEXT_UNDERLINE_PADDING + textWidth,
							textStartVert + TEXT_UNDERLINE_PADDING);
				}

				g2D.setColor(Colors.RESOURCE_BORDER_COLOR);
				g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
				g2D.setStroke(new BasicStroke(5f));
				g2D.drawRect(GRAPH_END_PADDING, GRAPH_PIXEL_SPACING, getWidth() - 2 * GRAPH_END_PADDING,
						getHeight() - 2 * GRAPH_PIXEL_SPACING);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(PANEL_HORIZONTAL, PANEL_VERTICAL);
			}
		});

		pack();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setSize(PANEL_HORIZONTAL + 5, PANEL_VERTICAL + 25);

		setResizable(false);

		setVisible(true);
	}

}
