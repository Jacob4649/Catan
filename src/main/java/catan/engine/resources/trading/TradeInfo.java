package catan.engine.resources.trading;

import java.util.ArrayList;

/**
 * Class representing info about a series of {@link TradeExchange}s
 * 
 * @author Jacob
 *
 */
public class TradeInfo {

	public boolean m_tradeAffordable = false;
	
	public ArrayList<TradeExchange> m_trades = new ArrayList<TradeExchange>();
	
}
