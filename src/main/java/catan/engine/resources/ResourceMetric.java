package catan.engine.resources;

import java.util.Arrays;

import catan.engine.board.Board;
import catan.engine.board.BoardNotInitializedException;
import catan.engine.board.objects.BoardObjectNotInitializedException;
import catan.engine.board.objects.Productive;
import catan.engine.board.objects.VertexObject;
import catan.engine.board.tile.TileNotInitializedException;
import catan.engine.board.tile.VertexNotInitializedException;
import catan.engine.player.Player;

/**
 * Class for production metrics for a single {@link Player} concerning resources
 * on a {@link Board}
 * 
 * @author Jacob
 *
 */
public class ResourceMetric {

	private int[] m_metric = new int[ResourceBundle.RESOURCE_NUMBER];

	/**
	 * Creates a new {@link ResourceMetric}
	 * 
	 * @param board
	 *            the {@link Board} to create this {@link ResourceMetric} for
	 * @param player
	 *            the {@link Player} to create this {@link ResourceMetric} for
	 */
	public ResourceMetric(Board board, Player player) {
		board.forAllObjects((object) -> {
			if (object instanceof Productive && ((VertexObject) object).getOwner() == player) {
				try {
					add(((Productive) object).getMetric());
				} catch (TileNotInitializedException | BoardNotInitializedException | VertexNotInitializedException
						| BoardObjectNotInitializedException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Creates a new {@link ResourceMetric}
	 * 
	 * @param metric
	 *            int array containing frequency probability of each resource
	 */
	public ResourceMetric(int[] metric) {
		m_metric = metric;
	}

	/**
	 * Adds the specified {@link ResourceMetric} to this {@link ResourceMetric}
	 * 
	 * @param metric
	 *            the {@link ResourceMetic} to add
	 * @return the calling {@link ResourceMetric}
	 */
	public ResourceMetric add(ResourceMetric metric) {
		for (int i = 0; i < metric.getRawMetric().length; i++) {
			m_metric[i] += metric.getRawMetric()[i];
		}
		return this;
	}

	/**
	 * Subtracts the specified {@link ResourceMetric} from this
	 * {@link ResourceMetric}
	 * 
	 * @param metric
	 *            the {@link ResourceMetic} to subtract
	 * @return the calling {@link ResourceMetric}
	 */
	public ResourceMetric subtract(ResourceMetric metric) {
		for (int i = 0; i < metric.getRawMetric().length; i++) {
			m_metric[i] -= metric.getRawMetric()[i];
		}
		return this;
	}

	/**
	 * 
	 * @return an ordered int[][] of resources in this {@link ResourceMetric}
	 *         from least to most productive, containing {resource,
	 *         productivity}
	 */
	public int[][] getOrderedList() {
		int[][] metric = new int[m_metric.length][2];
		for (int i = 0; i < m_metric.length; i++) {
			metric[i][0] = i;
			metric[i][1] = m_metric[i];
		}
		Arrays.sort(metric, (base, compareTo) -> {
			return Integer.compare(base[1], compareTo[1]);
		});
		return metric;
	}

	/**
	 * 
	 * @return this {@link ResourceMetric} as an int array
	 */
	public int[] getRawMetric() {
		return m_metric;
	}

	@Override
	public String toString() {
		return "ResourceMetric: (Wood: (" + m_metric[ResourceBundle.WOOD] + "), Clay: (" + m_metric[ResourceBundle.CLAY]
				+ "), Stone: (" + m_metric[ResourceBundle.STONE] + "), Grain: (" + m_metric[ResourceBundle.GRAIN]
				+ "), Sheep: (" + m_metric[ResourceBundle.SHEEP] + "))";
	}

}
