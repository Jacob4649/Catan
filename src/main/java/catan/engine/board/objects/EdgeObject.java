package catan.engine.board.objects;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import catan.engine.board.tile.Edge;
import catan.engine.board.tile.EdgeNotInitializedException;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.player.Player;

/**
 * Represents a {@link BoardObject} on an {@link Edge} owned by a {@link Player}
 * 
 * @author Jacob
 *
 */
public abstract class EdgeObject extends BoardObject<Edge> {

	/**
	 * Creates a new {@link EdgeObject}
	 * 
	 * @param owner
	 *            the owner of this {@link EdgeObject}
	 * @param position
	 *            the {@link Edge} this {@link EdgeObject} is on
	 * @param baseImage
	 *            the static base {@link BufferedImage} for this
	 *            {@link EdgeObejct}, provided in vertical orientation assuming
	 *            it will be placed on a vertical {@link Edge}
	 * @param ignoreLocation
	 *            if true, don't throw {@link InvalidLocationException} on
	 *            invalid locations
	 * @throws InvalidLocationException
	 *             if initialized in an invalid location
	 */
	public EdgeObject(Player owner, Edge position, BufferedImage baseImage, boolean ignoreLocation)
			throws InvalidLocationException {
		super(position, baseImage, owner, ignoreLocation);
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
	 *             if this {@link EdgeObject} is not initialized
	 */
	@Override
	public int[] getImagePosition(int[] mapDimensions, int[] panelDimensions)
			throws BoardObjectNotInitializedException {

		try {
			int[] vertex1Pos = new int[] {
					(int) (((double) getPosition().getEndPoints()[0].getPosition()[1] / (double) mapDimensions[1])
							* panelDimensions[0]),
					(int) (((double) getPosition().getEndPoints()[0].getPosition()[0] / (double) mapDimensions[0])
							* panelDimensions[1]) };

			int[] vertex2Pos = new int[] {
					(int) (((double) getPosition().getEndPoints()[1].getPosition()[1] / (double) mapDimensions[1])
							* panelDimensions[0]),
					(int) (((double) getPosition().getEndPoints()[1].getPosition()[0] / (double) mapDimensions[0])
							* panelDimensions[1]) };

			int[] midPoint = new int[] { (int) (((double) vertex1Pos[0] + (double) vertex2Pos[0]) / 2d),
					(int) (((double) vertex1Pos[1] + (double) vertex2Pos[1]) / 2d) };

			int[] imageDimensions = getImageDimensions(mapDimensions, panelDimensions);

			return new int[] { (int) ((midPoint[0]) - ((double) (imageDimensions[0]) / 2d)),
					(int) ((midPoint[1]) - ((double) (imageDimensions[1]) / 2d)) };
		} catch (VertexNotInitializedException | EdgeNotInitializedException e) {
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
	 *             if this {@link EdgeObject} is not initialized
	 */
	@Override
	public BufferedImage getImage(int[] mapDimensions, int[] panelDimensions)
			throws BoardObjectNotInitializedException {
		if (m_baseImages.getImageMatching(getClass()) == null) {
			throw new BoardObjectNotInitializedException();
		}
		if (mapDimensions == null || panelDimensions == null) {
			m_image = m_baseImages.getImageMatching(getClass());
		} else {
			int[] dimensions = null;
			try {
				if (getPosition().isVertical()) {
					dimensions = getImageDimensions(mapDimensions, panelDimensions);	
				} else {
					dimensions = new int[] {getImageDimensions(mapDimensions, panelDimensions)[1], getImageDimensions(mapDimensions, panelDimensions)[0]};
				}
			} catch (VertexNotInitializedException e) {
				e.printStackTrace();
				System.exit(0);
				return null;
			}
			Image image = m_baseImages.getImageMatching(getClass()).getScaledInstance(dimensions[0], dimensions[1],
					Image.SCALE_SMOOTH);
			BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2D = bufferedImage.createGraphics();
			g2D.drawImage(image, 0, 0, null);
			g2D.dispose();
			m_image = bufferedImage;
		}

		// rotate image if needed
		try {
			if (!getPosition().isVertical()) {
				int width = (int) Math.floor(m_image.getWidth() * Math.abs(Math.cos(Math.toRadians(90)))
						+ m_image.getHeight() * Math.abs(Math.sin(Math.toRadians(90))));
				int height = (int) Math.floor(m_image.getHeight() * Math.abs(Math.cos(Math.toRadians(90)))
						+ m_image.getWidth() * Math.abs(Math.sin(Math.toRadians(90))));
				AffineTransform transform = new AffineTransform();
				transform.translate(width / 2, height / 2);
				transform.rotate(Math.toRadians(90), 0, 0);
				transform.translate(-m_image.getWidth() / 2, -m_image.getHeight() / 2);
				AffineTransformOp rotateOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
				m_image = rotateOp.filter(m_image, new BufferedImage(width, height, m_image.getType()));
			}
		} catch (VertexNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
		}

		// color image if possible
		if (getOwner() != null) {
			Graphics2D g2D = m_image.createGraphics();
			g2D.setColor(getOwner().getColor().getColor());
			g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.25f));
			g2D.fillRect(0, 0, m_image.getWidth(), m_image.getHeight());
		}

		return m_image;
	}

	@Override
	public String toString() {
		try {
			return "Edge Object: (" + getPosition() + ")";
		} catch (BoardObjectNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return "Edge Object: (Not Initialized)";
		}
	}

}
