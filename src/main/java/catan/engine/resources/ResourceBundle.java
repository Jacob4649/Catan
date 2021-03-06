package catan.engine.resources;

import java.util.Arrays;

/**
 * Class representing a collection of resources
 * 
 * @author Jacob
 *
 */
public class ResourceBundle {

	public static final int WOOD = 0;
	public static final int CLAY = 1;
	public static final int STONE = 2;
	public static final int GRAIN = 3;
	public static final int SHEEP = 4;
	public static final int NULL = 5;

	public static final int RESOURCE_NUMBER = 5;

	private int m_wood = 0;
	private int m_clay = 0;
	private int m_stone = 0;
	private int m_grain = 0;
	private int m_sheep = 0;

	private Runnable m_largeScaleUpdateListener = null;

	/**
	 * Creates a new {@link ResourceBundle}
	 */
	public ResourceBundle() {
	}

	/**
	 * Creates a new {@link ResourceBundle} with the specified resources
	 * 
	 * @param resources
	 *            array containing int values for all the resources in the
	 *            following order:
	 *            <p>
	 *            <li>Wood
	 *            <li>Clay
	 *            <li>Stone
	 *            <li>Grain
	 *            <li>Sheep
	 */
	public ResourceBundle(int... resources) {
		for (int i = 0; i < resources.length; i++) {
			switch (i) {
			case WOOD:
				setWood(resources[i]);
				break;

			case CLAY:
				setClay(resources[i]);
				break;

			case STONE:
				setStone(resources[i]);
				break;

			case GRAIN:
				setGrain(resources[i]);
				break;

			case SHEEP:
				setSheep(resources[i]);
			}
		}
	}

	public int getWood() {
		return m_wood;
	}

	public void setWood(int wood) {
		m_wood = wood;
	}

	public void addWood(int wood) {
		m_wood += wood;
	}

	public int getClay() {
		return m_clay;
	}

	public void setClay(int clay) {
		m_clay = clay;
	}

	public void addClay(int clay) {
		m_clay += clay;
	}

	public int getStone() {
		return m_stone;
	}

	public void setStone(int stone) {
		m_stone = stone;
	}

	public void addStone(int stone) {
		m_stone += stone;
	}

	public int getGrain() {
		return m_grain;
	}

	public void setGrain(int grain) {
		m_grain = grain;
	}

	public void addGrain(int grain) {
		m_grain += grain;
	}

	public int getSheep() {
		return m_sheep;
	}

	public void setSheep(int sheep) {
		m_sheep = sheep;
	}

	public void addSheep(int sheep) {
		m_sheep += sheep;
	}

	/**
	 * Adds the specified number of random resources to this
	 * {@link ResourceBundle}
	 * 
	 * @param number
	 *            the number of resources to add
	 * @return the calling {@link ResourceBundle}
	 */
	public ResourceBundle addRandom(int number) {
		for (int i = 0; i < number; i++) {
			int j = (int) (Math.random() * RESOURCE_NUMBER);
			int[] resources = new int[RESOURCE_NUMBER];
			resources[j] = 1;
			add(new ResourceBundle(resources));
		}
		return this;
	}

	/**
	 * Adds the specified {@link ResourceBundle} to this {@link ResourceBundle}
	 * 
	 * @param bundle
	 *            the {@link ResourceBundle} to add to this
	 *            {@link ResourceBundle}
	 * @return the calling {@link ResourceBundle}
	 */
	public ResourceBundle add(ResourceBundle bundle) {
		addWood(bundle.getWood());
		addClay(bundle.getClay());
		addStone(bundle.getStone());
		addGrain(bundle.getGrain());
		addSheep(bundle.getSheep());
		if (m_largeScaleUpdateListener != null) {
			m_largeScaleUpdateListener.run();
		}
		return this;
	}

	/**
	 * Subtracts the specified {@link ResourceBundle} from this
	 * {@link ResourceBundle}
	 * 
	 * @param bundle
	 *            the {@link ResourceBundle} to add to this
	 *            {@link ResourceBundle}
	 * @return the calling {@link ResourceBundle}
	 */
	public ResourceBundle subtract(ResourceBundle bundle) {
		addWood(-bundle.getWood());
		addClay(-bundle.getClay());
		addStone(-bundle.getStone());
		addGrain(-bundle.getGrain());
		addSheep(-bundle.getSheep());
		if (m_largeScaleUpdateListener != null) {
			m_largeScaleUpdateListener.run();
		}
		return this;
	}

	/**
	 * 
	 * @param bundle
	 *            {@link ResourceBundle} to compare to
	 * @return true if this {@link ResourceBundle} is greater than or equal to
	 *         the specified {@link ResourceBundle} for all resources
	 */
	public boolean greaterOrEqualTo(ResourceBundle bundle) {
		return getWood() >= bundle.getWood() && getClay() >= bundle.getClay() && getGrain() >= bundle.getGrain()
				&& getStone() >= bundle.getStone() && getSheep() >= bundle.getSheep();
	}

	/**
	 * Sets the code to run when large scale changes are made to this
	 * {@link ResourceBundle}
	 * 
	 * @param listener
	 *            {@link Runnable} containing the code to run
	 */
	public void setOnChangeListener(Runnable listener) {
		m_largeScaleUpdateListener = listener;
	}

	/**
	 * 
	 * @return this {@link ResourceBundle} as an int array
	 */
	public int[] getRawBundle() {
		return new int[] { getWood(), getClay(), getStone(), getGrain(), getSheep() };
	}

	/**
	 * 
	 * @param resource
	 *            int resource
	 * @return the specified int resource as a {@link String}
	 */
	public static String toString(int resource) {
		switch (resource) {
		default:
		case NULL:
			return "Null";
		case WOOD:
			return "Wood";
		case CLAY:
			return "Clay";
		case STONE:
			return "Stone";
		case GRAIN:
			return "Grain";
		case SHEEP:
			return "Sheep";
		}
	}

	@Override
	public boolean equals(Object bundle) {
		return bundle instanceof ResourceBundle && ((ResourceBundle) bundle).getWood() == getWood()
				&& ((ResourceBundle) bundle).getClay() == getClay()
				&& ((ResourceBundle) bundle).getStone() == getStone()
				&& ((ResourceBundle) bundle).getGrain() == getGrain()
				&& ((ResourceBundle) bundle).getSheep() == getSheep();
	}

	@Override
	public ResourceBundle clone() {
		return new ResourceBundle(getWood(), getClay(), getStone(), getGrain(), getSheep());
	}

	@Override
	public String toString() {
		return "ResourceBundle: (Wood: (" + getWood() + "), Clay: (" + getClay() + "), Stone: (" + getStone()
				+ "), Grain: (" + getGrain() + "), Sheep: (" + getSheep() + "))";
	}
}
