package catan.engine.board.objects;

import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.tile.Tile;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.Vertex;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.resources.ResourceBundle;

/**
 * Interface for {@link VertexObjects} that produce resources.
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
	public ResourceBundle getResources() throws TileNotInitializedException, BoardNotInitializedException,
			VertexNotInitializedException, BoardObjectNotInitializedException;

	/**
	 * Adds all resources produced by this {@link VertexObject} to the owning
	 * {@link Player}'s {@link ResourceBundle}
	 * 
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
	public void giveResourcesToOwner() throws TileNotInitializedException, BoardNotInitializedException,
			VertexNotInitializedException, BoardObjectNotInitializedException, NoOwnerException;

}
