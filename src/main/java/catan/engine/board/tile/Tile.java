package catan.engine.board.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

/**
 * Class representing a single game tile
 * 
 * @author Jacob
 *
 */
public class Tile {

	public static final int FREQUENCY_MIN = 2;
	public static final int FREQUENCY_MAX = 12;

	private int m_frequency;
	private TileType m_type;

	/**
	 * Creates a {@link Tile} with the specified frequency and type
	 * 
	 * @param frequency
	 *            the number to be rolled on the dice to get resources from this
	 *            tile
	 * @param type
	 *            the {@link TileType} of this tile
	 */
	public Tile(int frequency, TileType type) {
		m_frequency = frequency;
		m_type = type;
	}

	/**
	 * Gets a specific number of evenly distributed frequencies from min
	 * frequency to max frequency
	 * 
	 * @param number
	 *            the number of frequencies to get
	 * @return an array containing the frequencies
	 */
	public static int[] getFrequencies(int number) {
		int[] frequencies = new int[number];
		int[] frequencyRange = new int[FREQUENCY_MAX - FREQUENCY_MIN + 1];
		
		for (int i = 0; i < frequencyRange.length; i++) {
			frequencyRange[i] = FREQUENCY_MIN + i;
		}
		
		int arrayIndex = 0;
		
		for (int i = 0; i < number / frequencyRange.length; i++) {
			for (int j = 0; j < frequencyRange.length; j++) {
				frequencies[arrayIndex] = frequencyRange[j];
				arrayIndex++;
			}
		}
		
		Stack<Integer> unevenInt = new Stack<Integer>();
		
		for (int i = 0; i < frequencyRange.length; i++) {
			unevenInt.add(frequencyRange[i]);
		}
		
		Collections.shuffle(unevenInt);

		while (arrayIndex < frequencies.length) {
			frequencies[arrayIndex] = unevenInt.pop();
			arrayIndex++;
		}
		
		ArrayList<Integer> tempFrequencies = new ArrayList<Integer>();
		
		for (int i : frequencies) {
			tempFrequencies.add(i);
		}
		
		Collections.shuffle(tempFrequencies);
		
		for (int i = 0; i < frequencies.length; i++) {
			frequencies[i] = tempFrequencies.get(i);
		}
		
		return frequencies;
	}

}
