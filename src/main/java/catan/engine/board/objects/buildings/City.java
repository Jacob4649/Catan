package catan.engine.board.objects.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.objects.BoardObject;
import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.engine.board.objects.InvalidLocationException;
import catan.engine.board.objects.NoOwnerException;
import catan.engine.board.objects.Productive;
import catan.engine.board.objects.VertexObject;
import catan.engine.board.tile.Tile;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.Vertex;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.player.Player;
import catan.engine.resources.ResourceBundle;
import catan.renderer.panel.BoardPanel;

/**
 * Class representing a single city
 * 
 * @author Jacob
 *
 */
public class City extends VertexObject implements Productive {

	private static final int CITY_PRODUCTIVITY = 2;

	/**
	 * Creates a {@link City}
	 * 
	 * @param owner
	 *            the {@link Player} owning the city
	 * @param position
	 *            the {@link Vertex} where the city is positioned
	 * @throws InvalidLocationException
	 *             if initialized in an invalid location
	 */
	public City(Player owner, Vertex position) throws InvalidLocationException {
		super(owner, position, new BufferedImage(
				(int) (0.6 * ((double) BoardPanel.PANEL_HORIZONTAL) / ((double) Board.DEFAULT_BOARD_DIMENSIONS[1])),
				(int) (0.6 * ((double) BoardPanel.PANEL_VERTICAL) / ((double) Board.DEFAULT_BOARD_DIMENSIONS[0])),
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
			return getPosition().getBoard().getAllObjectsMatching((object) -> {
				try {
					return (object instanceof Village || object instanceof City)
							&& ((Vertex) object.getPosition()).isAdjacent(getPosition());
				} catch (VertexNotInitializedException | BoardObjectNotInitializedException e) {
					e.printStackTrace();
					System.exit(0);
					return false;
				}
			}).length == 0;
		} catch (BoardObjectNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return false;
		}
	}

	/**
	 * 
	 * @return {@link ResourceBundle} containing all resources this {@link City}
	 *         produces in a turn
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link BoardObject} has not been initialized
	 * @throws VertexNotInitializedException
	 *             if the {@link Vertex} this {@link City} is on has not been
	 *             initialized
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 * @throws TileNotInitializedException
	 *             if any {@link Tile}s have not been initialized
	 */
	@Override
	public ResourceBundle getResources() throws TileNotInitializedException, BoardNotInitializedException,
			VertexNotInitializedException, BoardObjectNotInitializedException {
		ResourceBundle bundle = new ResourceBundle();
		for (Tile tile : getPosition().getAdjacentTiles()) {
			bundle.add(tile.getResources(CITY_PRODUCTIVITY));
		}
		return bundle;
	}

	/**
	 * Adds all resources produced by this {@link City} to the owning
	 * {@link Player}'s {@link ResourceBundle}
	 * 
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link City} has not been initialized
	 * @throws VertexNotInitializedException
	 *             if the {@link Vertex} this {@link City} is on has not been
	 *             initialized
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 * @throws TileNotInitializedException
	 *             if any {@link Tile}s have not been initialized
	 * @throws NoOwnerException
	 *             if this {@link City} is not owned by an {@link Player}
	 */
	@Override
	public void giveResourcesToOwner() throws TileNotInitializedException, BoardNotInitializedException,
			VertexNotInitializedException, BoardObjectNotInitializedException, NoOwnerException {
		if (m_owner == null) {
			throw new NoOwnerException(this);
		}
		m_owner.getResources().add(getResources());
	}

	/**
	 * 
	 * @param mapDimensions
	 *            the dimensions of the map this {@link City} is on in tiles
	 *            {row, col}
	 * @param panelDimensions
	 *            the dimensions of the panel this {@link City} is on in pixels
	 *            {x, y}
	 * @return an int array containing the dimensions of the
	 *         {@link BufferedImage} that should be used to display this
	 *         {@link City}
	 */
	@Override
	public int[] getImageDimensions(int[] mapDimensions, int[] panelDimensions) {
		return new int[] { (int) (0.6 * ((double) panelDimensions[0] / (double) mapDimensions[1])),
				(int) (0.6 * ((double) panelDimensions[1] / (double) mapDimensions[0])) };
	}

	@Override
	public String toString() {
		try {
			return "City: (" + getPosition() + ")";
		} catch (BoardObjectNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return "City: (Not Initialized)";
		}
	}
}
