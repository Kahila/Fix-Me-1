/**
* class that prompts market user what's going on within market,
* this view is only for debug purposes
*
* @author  Akalombo
* @version 1.0
* @since   2020-11-05 
*/


package market.marketView;

import market.marketController.*;
public class MarketView {
	
	//method that prompts connection to server
	public static void market() {
		System.out.printf("###############################################################\n\t\tMARKET IS LISTENING ON PORT 5001\n");
		System.out.printf("###############################################################\n");
	}
	
	//connected prompt
	public static void connect(int i) {
		if (i == 0) {
			System.out.println("* Connected to Server On PORT 5001");
		}else {
			System.out.println("- Connection to Server FAILED");
		}
	}
}
