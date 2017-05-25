import java.net.*;
import java.util.HashMap;
import java.lang.*;
import java.io.*;

/**
 * Representation of a Server to work in parallel with a client.
 * @author James Janecky
 *
 */
public class Server {
	public static void main(String[] args) {
		/** The ServerSocket to connect to. */
		ServerSocket server;
		/** The Socket to be used. */
		Socket sock = null;
		/** The BufferReader to read in to. */
		BufferedReader in;
		/** The PrintWriter to sent out to. */
		PrintWriter out;
		/** The computer object. */
		Computer enemyComp = new Computer(new HashMap<Pattern, Integer>());
		
		//Waits for connection to client.
		System.out.println("Waiting...");
		try {
			server = new ServerSocket(1245);
			sock = server.accept();
			System.out.println("Connected: " + sock.getInetAddress());
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream());
			
			//Loads up difficulty if veteran mode is selected.
			String difficulty = in.readLine();
			if (difficulty.equals("2")) {
				load(enemyComp);
			}
	
			while (true) {
				//Reads in from client.
				String command = in.readLine();
				char array[] = null; 
				
				//Pattern gets added to Hash Map.
				if (command.equals("ROCK")) {
					array = enemyComp.storePattern("r");
				} else if (command.equals("PAPER")) {
					array = enemyComp.storePattern("p");
				} else if (command.equals("SCISSORS")) {
					array = enemyComp.storePattern("s");
				}
				
				// Comp Prediction is sent back to client.
				char compChoice = enemyComp.makePrediction(new Pattern(array));
				out.println(compChoice);
				out.flush();
				
				//Saves HashMap after each move
				save(enemyComp);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Loads up the saved HashMap if the user selects Veteran mode.
	 * @param comp The object to be loaded.
	 */
	public static void load(Computer comp) {
		
		File f = new File("hashmap.dat");
		if (f.exists()) {
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
				comp = (Computer) in.readObject();
				System.out.println("Loading...");
				in.close();
			} catch (IOException exe) {
				System.out.println("Error processing file.");
			} catch (ClassNotFoundException e) {
				System.out.println("Could not find class.");
			}
		} 
	}

	/**
	 * Saves the HashMap to be used for Veteran mode.
	 * @param comp The object to be stored.
	 */
	public static void save(Computer comp) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hashmap.dat"));
			out.writeObject(comp);
			out.close();
		} catch (IOException e) {
			System.out.println("Error.");
		}
	}
}