package catan.engine.board;

import catan.engine.board.tile.Tile;
import catan.engine.board.tile.TileNotInitializedException;
import catan.renderer.panel.BoardPanel;

/**
 * Interface for when a {@link Tile} or a {@link Vertex} on a {@link BoardPanel} is selected
 * 
 * @author Jacob
 *
 */
public interface SelectedPositionListener {

	/**
	 * Runs when a {@link Tile} or {@link Vertex} is selected
	 * 
	 * @param row
	 *            the row the {@link Tile} or {@link Vertex} that has been selected is on
	 * @param col
	 *            the column the {@link Tile} or {@link Vertex} that has been selected is on
	 * @throws TileNotInitializedException
	 *             when selected {@link Tile} has not been initialized
	 */
	public void onSelect(int row, int col) throws TileNotInitializedException;

}
