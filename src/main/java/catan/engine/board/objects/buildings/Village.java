package catan.engine.board.objects.buildings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.objects.BoardObject;
import catan.engine.board.objects.BoardObjectNotInitializedException;
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
 * Class representing a single village
 * 
 * @author Jacob
 *
 */
public class Village extends VertexObject implements Productive {

	private static final int VILLAGE_PRODUCTIVITY = 1;
	
	/**
	 * Creates a {@link Village}
	 * 
	 * @param owner
	 *            the {@link Player} owning the village
	 * @param position
	 *            the {@link Vertex} where the village is positioned
	 */
	public Village(Player owner, Vertex position) {
		super(owner, position, new BufferedImage(
				(int) (0.4 * ((double) BoardPanel.PANEL_HORIZONTAL) / ((double) Board.DEFAULT_BOARD_DIMENSIONS[1])),
				(int) (0.4 * ((double) BoardPanel.PANEL_VERTICAL) / ((double) Board.DEFAULT_BOARD_DIMENSIONS[0])),
				BufferedImage.TYPE_INT_RGB) {
			{
				Graphics2D g2D = createGraphics();
				g2D.setColor(new Color(155, 155, 155));
				g2D.fillRect(0, 0, getWidth(), getHeight());
			}
		});
	}

	/**
	 * Upgrades this {@link Village} into an {@link City}
	 * 
	 * @return the new {@link City}
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link BoardObject} is not initialized
	 */
	public City upgrade() throws BoardObjectNotInitializedException {
		getPosition().getBoard().removeObject(this);
		City city = new City(getOwner(), getPosition());
		getPosition().getBoard().addObject(city);
		return city;
	}

	/**
	 * 
	 * @return {@link ResourceBundle} containing all resources this
	 *         {@link Village} produces in a turn
	 * @throws BoardObjectNotInitializedException if this {@link BoardObject} has not been initialized
	 * @throws VertexNotInitializedException if the {@link Vertex} this {@link Village} is on has not been initialized
	 * @throws BoardNotInitializedException if the {@link Board} has not been initialized
	 * @throws TileNotInitializedException if any {@link Tile}s have not been initialized
	 */
	@Override
	public ResourceBundle getResources() throws TileNotInitializedException, BoardNotInitializedException, VertexNotInitializedException, BoardObjectNotInitializedException {
		ResourceBundle bundle = new ResourceBundle();
		for (Tile tile : getPosition().getAdjacentTiles()) {
			bundle.add(tile.getResources(VILLAGE_PRODUCTIVITY));
		}
		return bundle;
	}
	
	/**
	 * Adds all resources produced by this {@link Village} to the owning
	 * {@link Player}'s {@link ResourceBundle}
	 * 
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link Village} has not been initialized
	 * @throws VertexNotInitializedException
	 *             if the {@link Vertex} this {@link Village} is on has not been
	 *             initialized
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 * @throws TileNotInitializedException
	 *             if any {@link Tile}s have not been initialized
	 * @throws NoOwnerException
	 *             if this {@link Village} is not owned by an {@link Player}
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
	 *            the dimensions of the map this {@link BoardObject} is on in
	 *            tiles {row, col}
	 * @param panelDimensions
	 *            the dimensions of the panel this {@link BoardObject} is on in
	 *            pixels {x, y}
	 * @return an int array containing the dimensions of the
	 *         {@link BufferedImage} that should be used to display this
	 *         {@link BoardObject}
	 */
	@Override
	public int[] getImageDimensions(int[] mapDimensions, int[] panelDimensions) {
		return new int[] { (int) (0.4 * ((double) panelDimensions[0] / (double) mapDimensions[1])),
				(int) (0.4 * ((double) panelDimensions[1] / (double) mapDimensions[0])) };
	}

	@Override
	public String toString() {
		try {
			return "Village: (" + getPosition() + ")";
		} catch (BoardObjectNotInitializedException e) {
			e.printStackTrace();
			System.exit(0);
			return "Village: (Not Initialized)";
		}
	}

}
