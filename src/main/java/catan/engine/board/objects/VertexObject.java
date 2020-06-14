package catan.engine.board.objects;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import catan.engine.board.tile.Vertex;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.player.Player;
import catan.renderer.panel.BoardPanel;

/**
 * Represents an {@link BoardObject} on a {@link Vertex} owned by an
 * {@link Player}
 * 
 * @author Jacob
 *
 */
public abstract class VertexObject extends BoardObject<Vertex> {

	protected Player m_owner = null;

	/**
	 * Creates a new {@link VertexObject}
	 * 
	 * @param owner
	 *            the owner of this {@link VertexObject}
	 * @param position
	 *            the {@link Vertex} this {@link VertexObject} is on
	 * @param baseImage
	 *            the static base {@link BufferedImage} for this
	 *            {@link VertexObejct}
	 * @param ignoreLocation
	 *            if true, don't throw {@link InvalidLocationException} on
	 *            invalid locations
	 * @throws InvalidLocationException
	 *             if initialized in an invalid location
	 */
	public VertexObject(Player owner, Vertex position, BufferedImage baseImage, boolean ignoreLocation)
			throws InvalidLocationException {
		super(position, baseImage, owner, ignoreLocation);
	}

	/**
	 * Creates a new {@link VertexObject}
	 * 
	 * @param position
	 *            the {@link Vertex} this {@link VertexObject} is on
	 * @param baseImage
	 *            the static base {@link BufferedImage} for this
	 *            {@link VertexObejct}
	 * @param ignoreLocation
	 *            if true, don't throw {@link InvalidLocationException} on
	 *            invalid locations
	 * @throws InvalidLocationException
	 *             if initialized in an invalid location
	 */
	public VertexObject(Vertex position, BufferedImage baseImage, boolean ignoreLocation)
			throws InvalidLocationException {
		this(null, position, baseImage, ignoreLocation);
	}

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
	 *             if this {@link VertexObject} is not initialized
	 */
	@Override
	public int[] getImagePosition(int[] mapDimensions, int[] panelDimensions)
			throws BoardObjectNotInitializedException {

		try {
			return new int[] {
					(int) ((((double) getPosition().getPosition()[1] / (double) mapDimensions[1]) * panelDimensions[0])
							- ((double) (getImageDimensions(mapDimensions, panelDimensions)[0]) / 2d)),
					(int) ((((double) getPosition().getPosition()[0] / (double) mapDimensions[0]) * panelDimensions[1])
							- ((double) (getImageDimensions(mapDimensions, panelDimensions)[1]) / 2d)) };
		} catch (VertexNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return new int[] { 0, 0 };
		}
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
	 *             if this {@link VertexObject} is not initialized
	 */
	@Override
	public BufferedImage getImage(int[] mapDimensions, int[] panelDimensions)
			throws BoardObjectNotInitializedException {
		super.getImage(mapDimensions, panelDimensions);

		// color image if possible
		if (m_owner != null) {
			Graphics2D g2D = m_image.createGraphics();
			g2D.setColor(m_owner.getColor().getColor());
			g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.25f));
			g2D.fillRect(0, 0, m_image.getWidth(), m_image.getHeight());
		}

		return m_image;
	}

	@Override
	public String toString() {
		try {
			return "Vertex Object: (" + getPosition() + ")";
		} catch (BoardObjectNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return "Vertex Object: (Not Initialized)";
		}
	}

}
