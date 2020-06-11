package catan.engine.board;

import catan.engine.board.tile.Tile;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.TileType;

/**
 * Class for programatically constructing a {@link Board}
 * 
 * @author Jacob
 *
 */
public class BoardBuilder extends Board {

	/**
	 * Creates a {@link BoardBuilder} filled with ocean {@link Tile}s
	 */
	public BoardBuilder() {
		super(new Tile[Board.DEFAULT_BOARD_DIMENSIONS[0]][Board.DEFAULT_BOARD_DIMENSIONS[1]]);

		int[] frequencies = Tile.getFrequencies(m_tileMap.length * m_tileMap[0].length);

		for (int row = 0; row < m_tileMap.length; row++) {
			for (int col = 0; col < m_tileMap[0].length; col++) {
				m_tileMap[row][col] = new Tile(frequencies[row * m_tileMap[0].length + col], TileType.OCEAN);
			}
		}
	}

	/**
	 * Sets the specified {@link Tile} to a given {@link TileType} and
	 * frequency, while also setting all oceanic adjacent {@link Tile}s to be
	 * coast {@link Tile}s
	 * 
	 * @param row
	 *            the row of the {@link Tile} to modify
	 * @param col
	 *            the column of the {@link Tile} to modify
	 * @param frequency
	 *            the frequency to assign
	 * @param tileType
	 *            the {@link TileType} to assign
	 * @throws TileNotInitializedException
	 *             if any of the edited {@link Tile}s are not initialized
	 */
	public void setTileAt(int row, int col, int frequency, TileType tileType) throws TileNotInitializedException {
		getTileAt(row, col).setTileType(tileType);
		getTileAt(row, col).setFrequency(frequency);
		if (tileType == TileType.OCEAN) {
			return;
		}
		for (Tile tile : getAdjacentTiles(row, col)) {
			if (tile.getTileType() == TileType.OCEAN) {
				tile.setTileType(TileType.COAST);
			}
		}
	}

	/**
	 * Sets the dimensions of the {@link BoardBuilder} and resets all
	 * {@link Tile}s to be oceanic
	 * 
	 * @param rows
	 *            the number of rows the {@link BoardBuilder} should have
	 * @param columns
	 *            the number of columns the {@link BoardBuilder} should have
	 */
	public void setDimensions(int rows, int columns) {
		m_tileMap = new Tile[rows][columns];

		int[] frequencies = Tile.getFrequencies(m_tileMap.length * m_tileMap[0].length);

		for (int row = 0; row < m_tileMap.length; row++) {
			for (int col = 0; col < m_tileMap[0].length; col++) {
				m_tileMap[row][col] = new Tile(frequencies[row * m_tileMap[0].length + col], TileType.OCEAN);
			}
		}
	}

	/**
	 * 
	 * @return the {@link Board} produced by this {@link BoardBuilder}
	 */
	public Board toBoard() {
		return (Board) this;
	}

}
