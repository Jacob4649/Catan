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
	DESERT, FOREST, PASTURE, CLAY, FIELD, QUARRY;

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
