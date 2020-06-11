package catan.engine.board.tile;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import catan.engine.resources.ResourceBundle;
import catan.renderer.Colors;

/**
 * Class representing a single game tile
 * 
 * @author Jacob
 *
 */
public class Tile {

	public static final int FREQUENCY_MIN = 2;
	public static final int FREQUENCY_MAX = 12;

	private int m_frequency = -1;
	private TileType m_type;

	private BufferedImage m_image;

	/**
	 * Calculates the dimensions of each tile in pixels
	 * 
	 * @param mapDimensions
	 *            an int array containing the dimensions of the map in
	 *            {@link Tile}s {row, col}
	 * @param panelDimensions
	 *            an int array containing the dimensions of the panel the map is
	 *            shown on in pixels {x, y}
	 * @return an int array containing the dimensions for a single {@link Tile}
	 *         in pixels {x, y}
	 */
	public static int[] getTilePixelDimensions(int[] mapDimensions, int[] panelDimensions) {
		return new int[] { (int) ((double) panelDimensions[0] / (double) mapDimensions[1]),
				(int) ((double) panelDimensions[1] / (double) mapDimensions[0]) };
	}

	/**
	 * 
	 * @return the frequency of this tile
	 * @throws TileNotInitializedException
	 *             if the tile has not been initialized
	 */
	public int getFrequency() throws TileNotInitializedException {
		if (m_frequency != -1) {
			return m_frequency;
		} else {
			throw new TileNotInitializedException();
		}
	}

	/**
	 * Sets the frequency to the specified value
	 * 
	 * @param i
	 *            the int to set the frequency to
	 * @throws TileNotInitializedException
	 *             if this {@link Tile} has not been initialized
	 */
	public void setFrequency(int i) throws TileNotInitializedException {
		m_frequency = i;
		getImage(true);
	}

	/**
	 * Sets the {@link TileType} to the specified value
	 * 
	 * @param t
	 *            the {@link TileType} to assign
	 * @throws TileNotInitializedException
	 *             if this {@link Tile} has not been initialized
	 */
	public void setTileType(TileType t) throws TileNotInitializedException {
		m_type = t;
		getImage(true);
	}

	/**
	 * 
	 * @return the {@link TileType} of this tile
	 * @throws TileNotInitializedException
	 *             if the tile has not been initialized
	 */
	public TileType getTileType() throws TileNotInitializedException {
		if (m_type != null) {
			return m_type;
		} else {
			throw new TileNotInitializedException();
		}
	}

	/**
	 * Creates a {@link Tile} with the specified frequency and type
	 * 
	 * @param frequency
	 *            the number to be rolled on the dice to get resources from this
	 *            tile
	 * @param type
	 *            the {@link TileType} of this tile
	 */
	public Tile(int frequency, TileType type) {
		m_frequency = frequency;
		m_type = type;
	}

	/**
	 * 
	 * @return the {@link BufferedImage} to be used to draw this {@link Tile}
	 * @throws TileNotInitializedException
	 *             if the {@link Tile} has not been initialized
	 */
	public BufferedImage getImage() throws TileNotInitializedException {
		return getImage(false);
	}

	/**
	 * 
	 * @param forceRefresh
	 *            if true force {@link Tile} to fetch new {@link BufferedImage}
	 * @return the {@link BufferedImage} used to draw this {@link Tile}
	 * @throws TileNotInitializedException
	 *             if the {@link Tile} has not been initialized
	 */
	public BufferedImage getImage(boolean forceRefresh) throws TileNotInitializedException {
		if (forceRefresh || m_image == null) {
			BufferedImage initialImage = getTileType().getImage();

			ColorModel cm = initialImage.getColorModel();
			boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
			WritableRaster raster = initialImage.copyData(initialImage.getRaster().createCompatibleWritableRaster());
			m_image = new BufferedImage(cm, raster, isAlphaPremultiplied, null);

			if (m_type != TileType.COAST && m_type != TileType.OCEAN) {
				Graphics2D g2D = m_image.createGraphics();

				g2D.setFont(new Font("Arial", Font.PLAIN, 25));
				g2D.setColor(Colors.FREQUENCY_COLOR);
				g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
				String text = "" + m_frequency;
				int hor = (int) ((double) (m_image.getWidth() - g2D.getFontMetrics().stringWidth(text)) / 2d);
				int vert = (int) ((double) (m_image.getHeight() + g2D.getFontMetrics().getHeight()) / 2d);
				g2D.drawString(text, hor, vert);
			}
		}

		return m_image;
	}

	/**
	 * 
	 * @param tileDimensions
	 *            an int array containing the new {@link Tile} dimensions in
	 *            pixels
	 * @return a scaled {@link BufferedImage}
	 * @throws TileNotInitializedException
	 *             if the {@link Tile} has not been initialized
	 */
	public BufferedImage getAndScaleImage(int[] tileDimensions) throws TileNotInitializedException {
		BufferedImage initialImage = getTileType().getImage(tileDimensions);

		ColorModel cm = initialImage.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = initialImage.copyData(initialImage.getRaster().createCompatibleWritableRaster());
		m_image = new BufferedImage(cm, raster, isAlphaPremultiplied, null);

		if (m_type != TileType.COAST && m_type != TileType.OCEAN) {
			Graphics2D g2D = m_image.createGraphics();

			g2D.setFont(new Font("Arial", Font.PLAIN, 25));
			g2D.setColor(Colors.FREQUENCY_COLOR);
			g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			g2D.drawString("" + m_frequency, 5, 20);
		}

		return m_image;
	}

	/**
	 * 
	 * @param num
	 *            the number of resources to produce from this {@link Tile}
	 * @return a {@link ResourceBundle} containing the specified amount of
	 *         resources this tile produces
	 */
	public ResourceBundle getResources(int num) {
		switch (m_type) {
		case FOREST:
			return new ResourceBundle(num);
		case CLAY:
			return new ResourceBundle(0, num);
		case QUARRY:
			return new ResourceBundle(0, 0, num);
		case FIELD:
			return new ResourceBundle(0, 0, 0, num);
		case PASTURE:
			return new ResourceBundle(0, 0, 0, 0, num);
		case OCEAN:
		case DESERT:
		case COAST:
		default:
			return new ResourceBundle();
		}
	}

	/**
	 * Gets a specific number of evenly distributed frequencies from min
	 * frequency to max frequency
	 * 
	 * @param number
	 *            the number of frequencies to get
	 * @return an array containing the frequencies
	 */
	public static int[] getFrequencies(int number) {
		int[] frequencies = new int[number];
		int[] frequencyRange = new int[FREQUENCY_MAX - FREQUENCY_MIN + 1];

		for (int i = 0; i < frequencyRange.length; i++) {
			frequencyRange[i] = FREQUENCY_MIN + i;
		}

		int arrayIndex = 0;

		for (int i = 0; i < number / frequencyRange.length; i++) {
			for (int j = 0; j < frequencyRange.length; j++) {
				frequencies[arrayIndex] = frequencyRange[j];
				arrayIndex++;
			}
		}

		Stack<Integer> unevenInt = new Stack<Integer>();

		for (int i = 0; i < frequencyRange.length; i++) {
			unevenInt.add(frequencyRange[i]);
		}

		Collections.shuffle(unevenInt);

		while (arrayIndex < frequencies.length) {
			frequencies[arrayIndex] = unevenInt.pop();
			arrayIndex++;
		}

		ArrayList<Integer> tempFrequencies = new ArrayList<Integer>();

		for (int i : frequencies) {
			tempFrequencies.add(i);
		}

		Collections.shuffle(tempFrequencies);

		for (int i = 0; i < frequencies.length; i++) {
			frequencies[i] = tempFrequencies.get(i);
		}

		return frequencies;
	}

}
