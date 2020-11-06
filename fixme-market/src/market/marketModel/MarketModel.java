/**
* class that gets data from the file database,
* and prompts the server 
*
* @author  Akalombo
* @version 1.0
* @since   2020-11-05 
*/

package market.marketModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class MarketModel {
	private Socket client_socket;
	private InputStream in;
	private OutputStream out;
	private PrintWriter pr = null;
	private BufferedReader br;
	private boolean connected = false;
	private String MarketID = "";
	private String clientID = "";
	private File Obj = null;
	private Scanner myReader = null;
	private String info = "";
	
	/*method that connects to server*/
	private void connect() {
		initDatabase();//setting up file database before connection
		try {
			client_socket = new Socket("localhost",5001);
			out = client_socket.getOutputStream();
			in = client_socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(in));
			pr = new PrintWriter(client_socket.getOutputStream(), true);
			connected = true;
			
		}catch(UnknownHostException e){
			connected = false;
		}catch(IOException e){
			connected = false;
		}
	}
	
	/*method that reads broker content*/
	public void content() {
		if (connected) {
			try {
				String data = br.readLine();				
				switch (data) {
				case "LIST":
					//getting list of content
					getList();
					break;
				case "INIT":
					//parsing ClientID, MarketID
					init();
					break;
				case "BUY":
					//buying content from shop
					transaction();
					break;
				case "CLOSE":
					//ending transaction process
					out.close();
					in.close();
					client_socket.close();
					br.close();
					pr.close();
					System.exit(0);
					break;
				case "SELL":
					//run the failed transactions
					break;
				}
			} catch (IOException e) {
				System.out.println("Connection to server lost..");
				System.exit(-1);
			}
		}
				
	}
	
	/*method that create file database if not existent*/
	private void initDatabase() {
		try {
			File Obj = new File("data.txt");
			if (Obj.createNewFile()) {
				System.out.println("* Database Created " + Obj.getName());
				initProducts();
			} else {
				System.out.println("- Database already exists.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*method to write initial data to file database*/
	private void initProducts() {
		try {
			FileWriter myWriter = new FileWriter("data.txt");
			myWriter.write("1\ttape\t\t\t53\t\tR50\n"
					+ "2\tknife\t\t\t10\t\tR100\n"
					+ "3\themmer\t\t\t60\t\tR130\n"
					+ "4\tscrewdriver\t\t999\t\tR200\n"
					+ "5\tsaw\t\t\t33\t\tR350\n"
					+ "6\tpliers\t\t\t678\t\tR230\n"
					+ "7\tlevel\t\t\t13\t\tR500\n"
					+ "8\tsander\t\t\t2000\t\tR20\n"
					+ "9\twrench\t\t\t5\t\tR700\n"
					+ "10\tratchet\t\t\t40\t\tR130");
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*method that sends file data to server when prompted*/
	private void getList() {
		String info = ("________________________________________________\n");
		info += ("ID\tINSTRUMENT\t\tQUANTITY\tPRICE\n");
		info += ("________________________________________________\n");
		try {
			File Obj = new File("data.txt");
			Scanner myReader = new Scanner(Obj);
			while (myReader.hasNextLine()) {
				info += myReader.nextLine();
				info += "\n";
			}
			info += ("________________________________________________");
			myReader.close();
			pr.println(info);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/*method that writes to file database after transaction*/
	private void transaction() {
		String id, quantity, clientID, marketID;
		try {
			id = br.readLine();
			quantity = br.readLine();
			marketID = br.readLine();
			setMarketID(marketID);
			getContent(id, quantity);
		} catch (IOException e) {
			System.out.println("- Connection to server lost while reading Buyers list..");
			System.exit(-1);
		}
	}
	
	private void init() {
		String clientID, marketID;
		try {
			clientID = br.readLine();
			marketID = br.readLine();
			setMarketID(marketID);
			setClientID(clientID);
		} catch (IOException e) {
			System.out.println("- Connection to server lost while reading Buyers list..");
			System.exit(-1);
		}
	}
	
	/*method that checks for instrument in file database */
	private void getContent(String id, String quantity) {
		String [] subStrings = null;
		
		try {
			Obj = new File("data.txt");
			myReader = new Scanner(Obj);
			while (myReader.hasNextLine()) {
				info = myReader.nextLine();
				subStrings = info.split("\t");
				int i = 0;
				while (i < subStrings.length) {
					if (subStrings[i].length() > 0) {
						if (subStrings[i].equals(id)) {
							myReader.close();//scanner closed here
							//attributes of the content Broker is looking for
							int QUANTITY = Integer.parseInt(subStrings[i+4]) - Integer.parseInt(quantity);
							if (QUANTITY < 0) {
								pr.println("*Market: Invalid quantity request");
							}else {
								//getting product from database
								if (status()) {
									update(QUANTITY, id);
								}else {
									pr.println("Transection Denied");
								}
							}
//							System.out.println(QUANTITY);
							return;
						}
					}
					i++;
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/*method that decides whether transaction went through*/
	private boolean status() {
		Random rand = new Random();
		int val = rand.nextInt(1000);
		val = 2;
		if (val%5 == 0) {
			return (false);
		}
		return (true);
	}
	
	/*method that updates database after transaction*/
	private void update(int QUANTITY, String ID) {
		try {
			myReader = new Scanner(Obj);
			String tmp[] = null;
			String saved = "";
			while (myReader.hasNextLine()) {
				info = myReader.nextLine();
				tmp = info.trim().split("\t");
				if (tmp[0].equals(ID)) {
					System.out.println();
					saved += (tmp[0] + "\t" + tmp[1] + "\t\t\t" + QUANTITY + "\t\t" + tmp[6] + "\n");
				}else {
					saved += (info + "\n");
				}
			}
			FileWriter myWriter = new FileWriter("data.txt");
			myWriter.write(saved);
			myWriter.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();//unable to write to database
		}
	}
	
	//setter for marketID
	private void setMarketID(String ID) {
		MarketID = ID;
	}
	
	//setter for marketID
	private void setClientID(String ID) {
		clientID = ID;
	}
	
	//getters
	public boolean getConected() {
		connect();//connecting to server on port 5001
		return (connected);
	}
}
