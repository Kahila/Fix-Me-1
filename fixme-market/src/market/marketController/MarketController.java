/**
* class that interfaces between the view and the model 
*
* @author  Akalombo
* @version 1.0
* @since   2020-11-05 
*/

package market.marketController;

import java.io.IOException;

import market.marketModel.MarketModel;
import market.marketView.*;

public class MarketController{
	private static MarketModel marketModel;
	
	public static void main(String[] args) throws IOException {
		session();
	}
	
	//method that will keep client connected until session ends
	private static void session() throws IOException {
		marketModel = new MarketModel();
		boolean connected = false;
		
		MarketView.market();
		
		if (marketModel.getConected()) {
			MarketView.connect(0);
			connected = true;
			while(connected) {
				marketModel.content();
			}
		}else {
			MarketView.connect(-1);
		}
	}
}
