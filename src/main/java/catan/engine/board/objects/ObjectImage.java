package catan.engine.board.objects;

import java.awt.image.BufferedImage;

/**
 * Class for associating a static {@link BufferedImage} with a class
 * @author Jacob
 *
 */
public class ObjectImage {
	
	private Class m_class;
	private BufferedImage m_image;
	
	/**
	 * Creates a new {@link ObjectImage}
	 * @param image {@link BufferedImage} to store
	 * @param object {@link Class} to associate it with
	 */
	public ObjectImage(BufferedImage image, Class object) {
		m_class = object;
		m_image = image;
	}
	
	/**
	 * 
	 * @return gets a deep copy of the {@link BufferedImage}
	 */
	public BufferedImage getImage() {
		BufferedImage copy = new BufferedImage(m_image.getWidth(), m_image.getHeight(), m_image.getType());
		copy.createGraphics().drawImage(m_image, 0, 0, null);
		return copy;
	}
	
	/**
	 * @return gets the {@link Class} associated with the image
	 */
	public Class getStoredClass() {
		return m_class;
	}
	
}
