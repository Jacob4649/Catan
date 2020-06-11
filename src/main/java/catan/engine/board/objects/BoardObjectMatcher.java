package catan.engine.board.objects;

/**
 * Interface to be used to determine if a {@link BoardObject} matches a
 * boolean condition
 * 
 * @author Jacob
 *
 */
public interface BoardObjectMatcher {
	/**
	 * Determines whether a {@link BoardObject} matches set parameters
	 * 
	 * @param object
	 *            the {@link BoardObject} to check
	 * @return true if the {@link BoardObject} matches the specified
	 *         parameters
	 */
	public boolean matches(BoardObject object);
}
