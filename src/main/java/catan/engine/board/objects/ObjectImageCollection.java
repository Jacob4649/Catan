package catan.engine.board.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Class for storing and accessing a group of {@link ObjectImage}s
 * @author Jacob
 *
 */
public class ObjectImageCollection {

	private ArrayList<ObjectImage> m_images = new ArrayList<ObjectImage>();
	
	/**
	 * Gets the {@link BufferedImage} specified
	 * @param c {@link Class} to get the image matching
	 * @return the {@link BufferedImage}
	 */
	public BufferedImage getImageMatching(Class c) {
		for (ObjectImage image : m_images) {
			if (image.getStoredClass().equals(c)) {
				return image.getImage();
			}
		}
		return null;
	}
	
	/**
	 * Adds a {@link ObjectImage} to the collection
	 * @param image the {@link BufferedImage} to add
	 * @param c the {@link Class} to associate it with
	 */
	public void addImage(BufferedImage image, Class c) {
		m_images.add(new ObjectImage(image, c));
	}
	
}
