package catan.engine.board.tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import catan.engine.board.Board;
import catan.engine.resources.ResourceBundle;
import catan.renderer.Colors;
import catan.renderer.panel.BoardPanel;

/**
 * Enum representing the possible types of tiles
 * 
 * @author Jacob
 *
 */
public enum TileType {

	// @formatter:off
	
	DESERT(0, ResourceBundle.NULL, getBufferedImageOfColor(Colors.DESERT_COLOR)), 
	FOREST(1, ResourceBundle.WOOD, getBufferedImageOfColor(Colors.FOREST_COLOR)), 
	PASTURE(2, ResourceBundle.SHEEP, getBufferedImageOfColor(Colors.PASTURE_COLOR)), 
	CLAY(3, ResourceBundle.CLAY, getBufferedImageOfColor(Colors.CLAY_COLOR)), 
	FIELD(4, ResourceBundle.GRAIN, getBufferedImageOfColor(Colors.FIELD_COLOR)), 
	QUARRY(5, ResourceBundle.STONE, getBufferedImageOfColor(Colors.QUARRY_COLOR)), 
	COAST(6, ResourceBundle.NULL, getBufferedImageOfColor(Colors.COAST_COLOR)), 
	OCEAN(7, ResourceBundle.NULL, getBufferedImageOfColor(Colors.OCEAN_COLOR));

	// @formatter:on

	private int m_value;
	private int m_resource;
	private BufferedImage m_image;
	private BufferedImage m_shownImage;

	private TileType(int value, int resource, BufferedImage image) {
		m_value = value;
		m_resource = resource;
		m_image = image;
		m_shownImage = m_image;
	}

	public int getValue() {
		return m_value;
	}

	public int getResource() {
		return m_resource;
	}

	public BufferedImage getBaseImage() {
		return m_image;
	}

	public BufferedImage getImage() {
		return m_shownImage;
	}

	/**
	 * Scales the {@link BufferedImage} to work with the new dimensions, gets
	 * the new {@link BufferedImage}
	 * 
	 * @param tileDimensions
	 *            an int array containing the dimensions of a single
	 *            {@link Tile} in pixels
	 * @return the new {@link BufferedImage}
	 */
	public BufferedImage getImage(int[] tileDimensions) {
		Image image = m_image.getScaledInstance(tileDimensions[0], tileDimensions[1], Image.SCALE_SMOOTH);
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2D = bufferedImage.createGraphics();
		g2D.drawImage(image, 0, 0, null);
		g2D.dispose();
		m_shownImage = bufferedImage;
		return m_shownImage;
	}

	/**
	 * Gets a {@link BufferedImage} for a {@link Tile} of the specified color
	 * and the default size
	 * 
	 * @param color
	 *            the {@link Color} for the {@link Tile}
	 * @return the {@link BufferedImage}
	 */
	private static BufferedImage getBufferedImageOfColor(Color color) {
		return new BufferedImage(
				Tile.getTilePixelDimensions(Board.DEFAULT_BOARD_DIMENSIONS,
						new int[] { BoardPanel.PANEL_HORIZONTAL, BoardPanel.PANEL_VERTICAL })[0],
				Tile.getTilePixelDimensions(Board.DEFAULT_BOARD_DIMENSIONS,
						new int[] { BoardPanel.PANEL_HORIZONTAL, BoardPanel.PANEL_VERTICAL })[1],
				BufferedImage.TYPE_INT_RGB) {
			{
				Graphics2D g2D = createGraphics();
				g2D.setPaint(color);
				g2D.fillRect(0, 0, getWidth(), getHeight());
			}
		};
	}

	/**
	 * Gets the {@link TileType} with the specified value
	 * 
	 * @param i
	 *            the value
	 * @return the {@link TileType} with a value of i, null if no
	 *         {@link TileType} has the desired value
	 */
	public static TileType getWithValue(int i) {
		for (TileType type : TileType.values()) {
			if (type.getValue() == i) {
				return type;
			}
		}
		return null;
	}

	/**
	 * Gets the {@link TileType} with the specified resource
	 * 
	 * @param i
	 *            the resource
	 * @return the {@link TileType} with a resource of i, null if no
	 *         {@link TileType} has the desired resource
	 */
	public static TileType getWithResource(int i) {
		for (TileType type : TileType.values()) {
			if (type.getResource() == i) {
				return type;
			}
		}
		return null;
	}

	/**
	 * Gets a specific number of evenly distributed {@link TileType}s
	 * 
	 * @param number
	 *            the number of {@link TileType}s to get
	 * @return an array containing the {@link TileType}s
	 */
	public static TileType[] getTileTypes(int number) {
		TileType[] types = new TileType[number];

		int arrayIndex = 0;

		for (int i = 0; i < number / TileType.values().length; i++) {
			for (int j = 0; j < TileType.values().length; j++) {
				types[arrayIndex] = TileType.values()[j];
				arrayIndex++;
			}
		}

		Stack<TileType> unevenTypes = new Stack<TileType>();

		unevenTypes.addAll(Arrays.asList(TileType.values()));

		Collections.shuffle(unevenTypes);

		while (arrayIndex < types.length) {
			types[arrayIndex] = unevenTypes.pop();
			arrayIndex++;
		}

		List<TileType> tempTypes = Arrays.asList(types);
		Collections.shuffle(tempTypes);

		for (int i = 0; i < types.length; i++) {
			types[i] = tempTypes.get(i);
		}

		return types;
	}

	/**
	 * Gets a specific number of evenly distributed {@link TileType}s
	 * 
	 * @param number
	 *            the number of {@link TileType}s to get
	 * @param exclude
	 *            {@link TileType}s to exclude
	 * @return an array containing the {@link TileType}s
	 */
	public static TileType[] getTileTypes(int number, TileType... exclude) {
		TileType[] types = new TileType[number];

		int arrayIndex = 0;

		int length = TileType.values().length - exclude.length;

		TileType[] values = new TileType[length];

		int assigning = 0;

		for (int i = 0; i < length; i++) {
			if (Arrays.asList(exclude).contains(TileType.values()[i])) {
				continue;
			}
			values[assigning] = TileType.values()[i];
			assigning++;
		}

		for (int i = 0; i < number / length; i++) {
			for (int j = 0; j < length; j++) {
				types[arrayIndex] = values[j];
				arrayIndex++;
			}
		}

		Stack<TileType> unevenTypes = new Stack<TileType>();

		unevenTypes.addAll(Arrays.asList(values));

		Collections.shuffle(unevenTypes);

		while (arrayIndex < types.length) {
			types[arrayIndex] = unevenTypes.pop();
			arrayIndex++;
		}

		List<TileType> tempTypes = Arrays.asList(types);
		Collections.shuffle(tempTypes);

		for (int i = 0; i < types.length; i++) {
			types[i] = tempTypes.get(i);
		}

		return types;
	}

	@Override
	public String toString() {
		return "TileType: (" + super.toString() + ")";
	}
}
