package catan.engine.board.objects.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import catan.engine.board.Board;
import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.engine.board.objects.EdgeObject;
import catan.engine.board.objects.InvalidLocationException;
import catan.engine.board.tile.Edge;
import catan.engine.board.tile.EdgeNotInitializedException;
import catan.engine.board.tile.Vertex;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.player.Player;
import catan.renderer.panel.BoardPanel;

/**
 * Class representing a single road
 * 
 * @author Jacob
 *
 */
public class Road extends EdgeObject {

	/**
	 * Creates a new {@link Road}
	 * 
	 * @param owner
	 *            the {@link Player} owning the {@link Road}
	 * @param position
	 *            the {@link Edge} this {@link Road} is on
	 * @throws InvalidLocationException
	 *             if initialized in an invalid location
	 */
	public Road(Player owner, Edge position) throws InvalidLocationException {
		super(owner, position, new BufferedImage(
				(int) (0.1 * ((double) BoardPanel.PANEL_HORIZONTAL) / ((double) Board.DEFAULT_BOARD_DIMENSIONS[1])),
				(int) (0.5 * ((double) BoardPanel.PANEL_VERTICAL) / ((double) Board.DEFAULT_BOARD_DIMENSIONS[0])),
				BufferedImage.TYPE_INT_RGB) {
			{
				Graphics2D g2D = createGraphics();
				g2D.setColor(new Color(155, 155, 155));
				g2D.fillRect(0, 0, getWidth(), getHeight());
			}
		});
	}

	/**
	 * Determines whether this {@link City} is in a valid location
	 * 
	 * @returns true if this {@link City} is in a valid location
	 */
	@Override
	public boolean validLocation() {
		try {
			Vertex[] endPoints = getPosition().getEndPoints();
			for (Vertex vertex : endPoints) {
				if (getPosition().getBoard().getAllObjectsMatching((object) -> {
					try {
						return object instanceof Road && object != this
								&& ((Road) object).getOwner() == getOwner() && (((Road) object).getPosition().getEndPoints()[0].isAdjacent(vertex)
										|| ((Road) object).getPosition().getEndPoints()[1].isAdjacent(vertex));
					} catch (VertexNotInitializedException | EdgeNotInitializedException
							| BoardObjectNotInitializedException e) {
						e.printStackTrace();
						System.exit(0);
						return false;
					}
				}).length > 0) {
					return true;
				}
			}
			return false;
		} catch (BoardObjectNotInitializedException | EdgeNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return false;
		}
	}

	/**
	 * 
	 * @param mapDimensions
	 *            the dimensions of the map this {@link Road} is on in tiles
	 *            {row, col}
	 * @param panelDimensions
	 *            the dimensions of the panel this {@link Road} is on in pixels
	 *            {x, y}
	 * @return an int array containing the dimensions of the
	 *         {@link BufferedImage} that should be used to display this
	 *         {@link Road}
	 */
	@Override
	public int[] getImageDimensions(int[] mapDimensions, int[] panelDimensions)
			throws BoardObjectNotInitializedException {
		// rotates dimensions if needed
		try {
			if (getPosition().isVertical()) {
				return new int[] { (int) (0.1 * ((double) panelDimensions[0]) / ((double) mapDimensions[1])),
						(int) (0.5 * ((double) panelDimensions[1]) / ((double) mapDimensions[0])) };
			} else {
				// rotate
				return new int[] { (int) (0.5 * ((double) panelDimensions[1]) / ((double) mapDimensions[0])),
						(int) (0.1 * ((double) panelDimensions[0]) / ((double) mapDimensions[1])) };
			}
		} catch (VertexNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return new int[] { 0, 0 };
		}
	}

}
