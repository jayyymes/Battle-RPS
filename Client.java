import java.net.*;
import java.lang.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Representation of a Client to work in parallel with a Server.
 * @author James Janecky
 *
 */
public class Client implements ActionListener {
	/** The rock button. */
	JButton rockButton;
   /** The paper button. */
	JButton paperButton;
    /** The scissors button. */
	JButton scissorsButton;
	/** The player's choice for GUI. */
    static JLabel playerChoiceLabel;
    /** The computer's choice for GUI. */
    static JLabel computerChoiceLabel;
    /** The game's outcome for GUI. */
    static JLabel outcomeLabel;
    /** The number of player wins for GUI. */
    static JLabel winsLabel;
    /** The number of computer wins for GUI. */
    static JLabel lossesLabel;
    /** Player's button press to String. */
    static String playerChoice = "";
    /** Player's score. */
    static int userScore = 0;
    /** Computer's score. */
    static int compScore = 0;
    /** The difficulty choice. */
    static String diffChoice;
    /** JFrame for GUI. */
    private JFrame frame = new JFrame();
	
    /**
     * Constructor for Client.  Initializes GUI.
     */
	public Client()  {
		JOptionPane userWindow;
		userWindow = new JOptionPane();
		diffChoice = JOptionPane.showInputDialog("Enter the mode number below: \n"
				+ "1. Novice \n2. Veteran"); 
		
		ImageIcon img1 = new ImageIcon(getClass().getResource("rock.png"));
		rockButton = new JButton(img1);
		rockButton.addActionListener(this);
	    
		ImageIcon img2 = new ImageIcon(getClass().getResource("paper.png"));
		paperButton = new JButton(img2);
		paperButton.addActionListener(this);
		
		ImageIcon img3 = new ImageIcon(getClass().getResource("scissors.png"));
		scissorsButton = new JButton(img3);
		scissorsButton.addActionListener(this);
		
		playerChoiceLabel = new JLabel("Player choice:");
		computerChoiceLabel = new JLabel("Computer choice:");
		outcomeLabel = new JLabel("  Shoot!");
		winsLabel = new JLabel("Player: " + userScore);
		lossesLabel = new JLabel("Computer: "+ compScore);
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(0,3));
		panel1.add(rockButton);
		panel1.add(paperButton);
		panel1.add(scissorsButton);
		
		JPanel panel2 = new JPanel();
		panel2.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 20));
		panel2.setLayout(new GridLayout());
		panel2.add(playerChoiceLabel, BorderLayout.WEST);
		panel2.add(computerChoiceLabel, BorderLayout.EAST);
		
		JPanel panel3 = new JPanel();
		panel3.setBorder(BorderFactory.createEmptyBorder(5, 40, 4, 0));
		panel3.setLayout(new GridLayout());
		panel3.add(winsLabel, BorderLayout.WEST);
		panel3.add(outcomeLabel, BorderLayout.CENTER);
		panel3.add(lossesLabel, BorderLayout.EAST);
	
		frame.add(panel1, BorderLayout.CENTER);
		frame.add(panel2, BorderLayout.SOUTH);
		frame.add(panel3, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Rock Paper Scissors");
        frame.pack();
        frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		//Creates Client Object for GUI
		Client myClient = new Client();
		Socket sock = null;
		BufferedReader in = null;
		PrintStream out = null;
		
		//Connection to server
		try {
			sock = new Socket("localhost", 1245);
			in = new BufferedReader (new InputStreamReader(sock.getInputStream()));
			out = new PrintStream(sock.getOutputStream());
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//Sends difficulty choice to server
		out.println(diffChoice);
		
		boolean flag = true;
		while(flag) {
			//Waits for user to click a button.
			if (!(playerChoice.equals(""))) {
				//Sends choice to server
				out.println(playerChoice);
				out.flush();
				
				//Prompts server to get computer's prediction.
				char compMove = getPredicition(in);
				//Updates GUI label to reflect comp's prediction.
				setCompChoice(compMove);
				//Determines the winner and updates score.
				determineWinnerGUI(compMove);
				
				//resets PlayerChoice
				playerChoice = "";
			}
		}
	}
	
	/**
	 * Get's prediction from server.
	 * @param in The BufferedReader reading in from the Server.
	 * @return The computer's prediction.
	 */
	public static char getPredicition(BufferedReader in) {
		char bal = ' ';
		try {
			bal = in.readLine().charAt(0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return bal;
	}
	
	/**
	 * Displays computer choice to GUI.
	 * @param c The computer's choice as a char.
	 */
	public static void setCompChoice(char c) {
		if (c == 'r') {
			computerChoiceLabel.setText("Computer choice: Rock");
		} else if (c == 's') {
			computerChoiceLabel.setText("Computer choice: Scissors");
		} else if (c == 'p') {
			computerChoiceLabel.setText("Computer choice: Paper");
		}
	}
	
	/**
	 * Compares user and computer to determine winner.
	 * @param c The computer's prediction.
	 */
	public static void determineWinnerGUI(char c) {
		if (c == 'r' && playerChoice.equals("PAPER")) {
			outcomeLabel.setText("You won!");
			userScore++;
			winsLabel.setText("Player: " + userScore);
		} else if (c == 'r' && playerChoice.equals("SCISSORS")) {
			outcomeLabel.setText("You lost!");
			compScore++;
			lossesLabel.setText("Computer: "+ compScore);
		} else if (c == 'p' && playerChoice.equals("ROCK")) {
			outcomeLabel.setText("You lost!");
			compScore++;
			lossesLabel.setText("Computer: "+ compScore);
		} else if (c == 'p' && playerChoice.equals("SCISSORS")) {
			outcomeLabel.setText("You won!");
			userScore++;
			winsLabel.setText("Player: " + userScore);
		} else if (c == 's' && playerChoice.equals("PAPER")) {
			outcomeLabel.setText("You lost!");
			compScore++;
			lossesLabel.setText("Computer: "+ compScore);
		} else if (c == 's' && playerChoice.equals("ROCK")) {
			outcomeLabel.setText("You won!");
			userScore++;
			winsLabel.setText("Player: " + userScore);
		} else {
			outcomeLabel.setText("It's a tie!");
		}
	}
	
	/**
	 * Overrides the ActionPerformed action to implement button pushing.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == rockButton) {
			playerChoiceLabel.setText("Player choice: Rock");
			playerChoice = "ROCK";
		} else if (e.getSource() == paperButton) {
			playerChoiceLabel.setText("Player choice: Paper");
			playerChoice = "PAPER";
		} else if (e.getSource() == scissorsButton) {
			playerChoiceLabel.setText("Player choice: Scissors");
			playerChoice = "SCISSORS";
		}
	}
	
	/**
	* Method to validate that the user's integer
	* input is valid and within a specified range. *
	* @param low The lowest number.
	* @param high the highest number.
	* @return */
	public static int checkInt(int low, int high) {
		Scanner in = new Scanner(System.in);
		boolean valid = false;
		int validNum = 0;
		while (!valid) {
			if (in.hasNextInt()) {
				validNum = in.nextInt();
				if (validNum >= low && validNum <= high) {
					valid = true;
				} else {
					System.out.print("Invalid- Retry: ");
				}
			} else {
				// clear buffer of junk input
				in.next();
				System.out.print("Invalid input- Retry: ");
			}
		}
		return validNum;
	}
}