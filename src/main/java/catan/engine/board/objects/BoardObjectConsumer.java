package catan.engine.board.objects;

/**
 * Interface for consuming a single {@link BoardObject}
 * @author Jacob
 *
 */
public interface BoardObjectConsumer {

	/**
	 * Runs code on the specified {@link BoardObject}
	 * @param object the specified {@link BoardObject}
	 */
	public void consume(BoardObject object);
	
}
