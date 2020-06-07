package catan.engine.board.tile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Enum representing the possible types of tiles
 * 
 * @author Jacob
 *
 */
public enum TileType {
	DESERT(0), FOREST(1), PASTURE(2), CLAY(3), FIELD(4), QUARRY(5), COAST(6), OCEAN(7);

	private int m_value;

	private TileType(int value) {
		m_value = value;
	}

	public int getValue() {
		return m_value;
	}

	/**
	 * Gets the {@link TileType} with the specified value
	 * 
	 * @param i
	 *            the value
	 * @return the {@link TileType} with a value of i, null if no
	 *         {@link TileType} has the desired value
	 */
	public static TileType getWithValue(int i) {
		for (TileType type : TileType.values()) {
			if (type.getValue() == i) {
				return type;
			}
		}
		return null;
	}

	/**
	 * Gets a specific number of evenly distributed {@link TileType}s
	 * 
	 * @param number
	 *            the number of {@link TileType}s to get
	 * @return an array containing the {@link TileType}s
	 */
	public static TileType[] getTileTypes(int number) {
		TileType[] types = new TileType[number];

		int arrayIndex = 0;

		for (int i = 0; i < number / TileType.values().length; i++) {
			for (int j = 0; j < TileType.values().length; j++) {
				types[arrayIndex] = TileType.values()[j];
				arrayIndex++;
			}
		}

		Stack<TileType> unevenTypes = new Stack<TileType>();

		unevenTypes.addAll(Arrays.asList(TileType.values()));

		Collections.shuffle(unevenTypes);

		while (arrayIndex < types.length) {
			types[arrayIndex] = unevenTypes.pop();
			arrayIndex++;
		}

		List<TileType> tempTypes = Arrays.asList(types);
		Collections.shuffle(tempTypes);

		for (int i = 0; i < types.length; i++) {
			types[i] = tempTypes.get(i);
		}

		return types;
	}
}
