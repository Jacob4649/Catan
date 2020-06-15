package catan.engine;

/**
 * Class representing an arbitrary number of dice
 * 
 * @author Jacob
 *
 */
public class Dice {

	private static final int DICE_SIDES = 6;
	
	/**
	 * Simulates rolling i dice
	 * @param i the number of dice to roll
	 */
	public static int roll(int i) {
		int sum = 0;
		for (int j = 0; j < i; j++) {
			sum += (int) (Math.random() * DICE_SIDES) + 1;
		}
		return sum;
	}
	
	/**
	 * 
	 * @return result from rolling a single die
	 */
	public static int singleRoll() {
		return roll(1);
	}
	
	/**
	 * 
	 * @return result from rolling two dice
	 */
	public static int doubleRoll() {
		return roll(2);
	}
	
}
