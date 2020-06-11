package catan.engine.board.objects;

import catan.engine.player.Player;

/**
 * Exception for when a {@link VertexObject} implementing the {@link Productive}
 * interface does not have a {@link Player} owner
 * 
 * @author Jacob
 *
 */
public class NoOwnerException extends Exception {

	/**
	 * Creates a new {@link NoOwnerException}
	 */
	public NoOwnerException() {
		super("Unowned Productive Vertex Object");
	}
	
	public NoOwnerException(VertexObject object) {
		super("Unowned Productive Vertex Object: " + object);
	}
	
}
