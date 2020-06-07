package catan.engine.board.objects;

import java.awt.image.BufferedImage;

public abstract class BoardObject {

	private static BufferedImage m_image;
	
	/**
	 * 
	 * @return the {@link BufferedImage} that should be used to display this piece
	 */
	public BufferedImage getImage() {
		return m_image;
	}
	
	/**
	 * Sets the {@link BufferedImage} that should be used to display this piece
	 * @param image the {@link BufferedImage} to assign
	 */
	public void setImage(BufferedImage image) {
		m_image = image;
	}
	
}
