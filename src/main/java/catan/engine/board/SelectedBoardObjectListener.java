package catan.engine.board;

import catan.engine.board.objects.BoardObject;
import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.renderer.panel.BoardPanel;

/**
 * Interface for when a {@link BoardObject} on a {@link BoardPanel} is selected
 * 
 * @author Jacob
 *
 */
public interface SelectedBoardObjectListener {

	/**
	 * Runs when a {@link BoardObject} is selected
	 * 
	 * @param object
	 *            the {@link BoardObject} that has been selected
	 * @throws BoardObjectNotInitializedException
	 *             when selected {@link BoardObject} has not been initialized
	 */
	public void onSelect(BoardObject object) throws BoardObjectNotInitializedException;

}
