package catan.engine.board.objects;

import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.tile.Tile;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.Vertex;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.player.Player;
import catan.engine.resources.ResourceBundle;
import catan.engine.resources.ResourceMetric;

/**
 * Interface for {@link VertexObject}s that produce resources.
 * {@link VertexObject}s implementing this interface must have an owning
 * {@link Player}
 * 
 * @author Jacob
 *
 */
public interface Productive {

	/**
	 * 
	 * @return {@link ResourceBundle} containing all resources this
	 *         {@link VertexObject} produces in a turn
	 * @param frequency
	 *            the int frequency for that turn
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link VertexObject} has not been initialized
	 * @throws VertexNotInitializedException
	 *             if the {@link Vertex} this {@link VertexObject} is on has not
	 *             been initialized
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 * @throws TileNotInitializedException
	 *             if any {@link Tile}s have not been initialized
	 */
	public ResourceBundle getResources(int frequency) throws TileNotInitializedException, BoardNotInitializedException,
			VertexNotInitializedException, BoardObjectNotInitializedException;

	/**
	 * 
	 * @return {@link ResourceMetric} for this {@link VertexObject}
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link VertexObject} has not been initialized
	 * @throws VertexNotInitializedException
	 *             if the {@link Vertex} this {@link VertexObject} is on has not
	 *             been initialized
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} this {@link VertexObject} is on has not
	 *             been initialized
	 * @throws TileNotInitializedException
	 *             if any {@link Tile}s surrounding this {@link VertexObject}
	 *             have not been initialized
	 */
	public ResourceMetric getMetric() throws TileNotInitializedException, BoardNotInitializedException,
			VertexNotInitializedException, BoardObjectNotInitializedException;

	/**
	 * Adds all resources produced by this {@link VertexObject} to the owning
	 * {@link Player}'s {@link ResourceBundle}
	 * 
	 * @param frequency
	 *            the int frequency for that turn
	 * @return true if any resources were fetched
	 * @throws BoardObjectNotInitializedException
	 *             if this {@link VertexObject} has not been initialized
	 * @throws VertexNotInitializedException
	 *             if the {@link Vertex} this {@link VertexObject} is on has not
	 *             been initialized
	 * @throws BoardNotInitializedException
	 *             if the {@link Board} has not been initialized
	 * @throws TileNotInitializedException
	 *             if any {@link Tile}s have not been initialized
	 * @throws NoOwnerException
	 *             if this {@link VertexObject} is not owned by an
	 *             {@link Player}
	 */
	public boolean giveResourcesToOwner(int frequency) throws TileNotInitializedException, BoardNotInitializedException,
			VertexNotInitializedException, BoardObjectNotInitializedException, NoOwnerException;

}
