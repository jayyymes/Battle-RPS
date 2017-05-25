import java.io.*;
import java.util.*;

/**
 * Representation of a Computer for Computini.
 * @author James Janecky
 */
public class Computer implements Serializable {
	/** HashMap to store inputs */
	private HashMap<Pattern, Integer> hashMap = new HashMap<Pattern, Integer>();
	/** An array to store incoming patterns */
	private char[] charArr = new char[4];
	/** A linked list to grab input and move into char array */
	private LinkedList<String> linkList = new LinkedList<String>();
	/** An array of random choices when pattern is less than 4 */
	char[] random = { 'r', 'p', 's' };

	/**
	 * Constructor for computer.
	 * @param map The HashMap used to store patterns.
	 */
	public Computer(HashMap<Pattern, Integer> map) {
		hashMap = map;
	}

	/**
	 * Grabs user input and store it into Char Array, then HashMap.
	 * @return The current character array.
	 */
	public char[] storePattern(String stored) {
		Pattern p;
		// Add first element to array
		linkList.addFirst(stored);
		if (linkList.size() > 4) {
			// Remove last if necessary.
			linkList.removeLast();
			for (int i = 0; i < linkList.size(); i++) {
				// Add first, move down others
				charArr[i] = linkList.get(i).charAt(0);
			}
			
			p = new Pattern(charArr);
			// Adds pattern to HashMap, otherwise increments value.
			if (!hashMap.containsKey(p)) {
				hashMap.put(p, 1);
			} else {
				hashMap.put(p, hashMap.get(p) + 1);
			}
		} else {
			charArr[0] = stored.charAt(0);
		}
		return charArr;
	}

	/**
	 * Decides Computer's next move based upon User input.
	 * @param keyPat Inputed pattern.
	 * @return Move based upon user pattern.
	 */
	public char makePrediction(Pattern keyPat) {
		// Char Arrays for storing each of the pattern options.
		char[] array1 = new char[4];
		char[] array2 = new char[4];
		char[] array3 = new char[4];

		// Linked List for easy access to last node.
		LinkedList<Character> list1 = new LinkedList<Character>();
		LinkedList<Character> list2 = new LinkedList<Character>();
		LinkedList<Character> list3 = new LinkedList<Character>();

		// Adds each character in a pattern to a linked list, minus the last.
		for (int i = 0; i < 3; i++) {
			list1.add(charArr[i]);
			list2.add(charArr[i]);
			list3.add(charArr[i]);
		}

		// Adds one of the following moves to the end of each linked list.
		list1.addLast('r');
		list2.addLast('p');
		list3.addLast('s');

		// Fills up Char Array(s) with the linked list(s).
		int i = 0;
		while (i < 4) {
			array1[i] = list1.get(i);
			array2[i] = list2.get(i);
			array3[i] = list3.get(i);
			i++;
		}

		// Inputs each Char Array into a pattern.
		Pattern pat1 = new Pattern(array1);
		Pattern pat2 = new Pattern(array2);
		Pattern pat3 = new Pattern(array3);

		// Finds the value at each key.
		int rock     = 0;
		int paper    = 0;
		int scissors = 0;
		if (hashMap.containsKey(pat1))
			rock = hashMap.get(keyPat);
		if (hashMap.containsKey(pat2)) 
			paper = hashMap.get(keyPat);
		if (hashMap.containsKey(pat3)) 
			scissors = hashMap.get(keyPat);
		
		// Compares each value to decide on how to counter.
		if (rock > scissors && rock > paper) {
			return 'p';
		} else if (scissors > rock && scissors > paper) {
			return 'r';
		} else if (paper > rock && paper > scissors) {
			return 's';
		} else {
			// Otherwise, a random move is selected.
			return randomMove();
		}
	}
	
	public char randomMove() {
		// Generates a random move if key isn't found.
		Random rand = new Random();
		
		return random[rand.nextInt(3)];
	}
}