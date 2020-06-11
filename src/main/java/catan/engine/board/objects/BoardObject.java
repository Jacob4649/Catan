package catan.engine.board.objects;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import catan.engine.board.Board;
import catan.engine.board.tile.Tile;
import catan.engine.board.tile.Vertex;
import catan.renderer.panel.BoardPanel;

/**
 * Class representing a non-{@link Tile} object on the {@link Board}
 * 
 * @author Jacob
 *
 * @param <T>
 *            the type used to track the position of this {@link BoardObject}
 *            (i.e. {@link Vertex}, {@link Tile}, int[], etc...)
 */
public abstract class BoardObject<T> {

	private static BufferedImage m_baseImage;
	protected BufferedImage m_image;
	private T m_position;

	/**
	 * Creates a new {@link BoardObject} at the specified position
	 * 
	 * @param position
	 *            the position of the {@link BoardObject} {row, col}
	 * @param baseImage
	 *            the base {@link BufferedImage} to be scaled and displayed for
	 *            this {@link BoardObject}
	 */
	public BoardObject(T position, BufferedImage baseImage) {
		m_position = position;
		m_baseImage = baseImage;
	}

	/**
	 * 
	 * @return the {@link BufferedImage} that should be used to display this
	 *         {@link BoardObject}
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link BoardObject} has not been initialized
	 */
	public BufferedImage getImage() throws BoardObjectNotInitializedException {
		return getImage(false);
	}

	/**
	 * 
	 * @param forceRefresh
	 *            whether to force a refresh, but not a rescale of this
	 *            {@link BufferedImage}
	 * @return the {@link BufferedImage} that should be used to display this
	 *         {@link BoardObject}
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link BoardObject} has not been initialized
	 */
	public BufferedImage getImage(boolean forceRefresh) throws BoardObjectNotInitializedException {
		if (forceRefresh || m_image == null) {
			getImage(null, null);
		}

		return m_image;
	}

	/**
	 * 
	 * @param mapDimensions
	 *            the dimensions of the map this {@link BoardObject} is on in
	 *            tiles {row, col}
	 * @param panelDimensions
	 *            the dimensions of the panel this {@link BoardObject} is on in
	 *            pixels {x, y}
	 * @return a scaled version of the {@link BufferedImage} that should be used
	 *         to display this {@link BoardObject}
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link BoardObject} has not been initialized
	 */
	public BufferedImage getImage(int[] mapDimensions, int[] panelDimensions)
			throws BoardObjectNotInitializedException {
		if (m_baseImage == null) {
			throw new BoardObjectNotInitializedException();
		}
		if (mapDimensions == null || panelDimensions == null) {
			m_image = m_baseImage;
		} else {
			int[] dimensions = getImageDimensions(mapDimensions, panelDimensions);
			Image image = m_baseImage.getScaledInstance(dimensions[0], dimensions[1], Image.SCALE_SMOOTH);
			BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2D = bufferedImage.createGraphics();
			g2D.drawImage(image, 0, 0, null);
			g2D.dispose();
			m_image = bufferedImage;
		}

		return m_image;
	}

	/**
	 * Sets the {@link BufferedImage} that should be used to display this
	 * {@link BoardObject}
	 * 
	 * @param image
	 *            the {@link BufferedImage} to assign
	 */
	public void setImage(BufferedImage image) {
		m_image = image;
	}

	/**
	 * 
	 * @return a T containing the position of this {@link BoardObject} {row,
	 *         col}
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link BoardObject} has not been initialized
	 */
	public T getPosition() throws BoardObjectNotInitializedException {
		if (m_position == null) {
			throw new BoardObjectNotInitializedException();
		}
		return m_position;
	}

	/**
	 * 
	 * @param mapDimensions
	 *            the dimensions of the map this {@link BoardObject} is on in
	 *            tiles {row, col}
	 * @param panelDimensions
	 *            the dimensions of the panel this {@link BoardObject} is on in
	 *            pixels {x, y}
	 * @return an int array containing the dimensions of the
	 *         {@link BufferedImage} that should be used to display this
	 *         {@link BoardObject}
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link BoardObject} has not been initialized
	 */
	public abstract int[] getImageDimensions(int[] mapDimensions, int[] panelDimensions)
			throws BoardObjectNotInitializedException;

	/**
	 * 
	 * @param mapDimensions
	 *            the dimensions of the map this {@link BoardObject} is on in
	 *            tiles {row, col}
	 * @param panelDimensions
	 *            the dimensions of the panel this {@link BoardObject} is on in
	 *            pixels {x, y}
	 * @return an int array containing the position of this piece on the
	 *         {@link BoardPanel} in pixels {x, y}
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link BoardObject} has not been initialized
	 */
	public abstract int[] getImagePosition(int[] mapDimensions, int[] panelDimensions)
			throws BoardObjectNotInitializedException;

	@Override
	public String toString() {
		try {
			return "Board Object: (" + getPosition() + ")";
		} catch (BoardObjectNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return "Board Object: (Not Initialized)";
		}
	}

}
