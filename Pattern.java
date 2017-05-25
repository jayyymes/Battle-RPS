import java.io.Serializable;
import java.util.Arrays;

/**
 * Representation of a Pattern for Online RPS.
 * @author James Janecky
 */
public class Pattern implements Serializable {
	/** Char Array to store in each pattern */
	private char[] thePattern;

	/**
	 * Constructor for Pattern
	 * @param n The Pattern's Char Array.
	 */
	public Pattern(char[] n) {
		thePattern = n;
	}
	
	/**
	 * Returns the user inputed pattern. 
	 * @return The pattern as a Char Array.
	 */
	public char[] getPattern() {
		return thePattern;
	}

	/**
	 * Overrides default hashCode method to make the Pattern class hashable.
	 */
	@Override
	public int hashCode() {
		return Arrays.hashCode(thePattern);
	}

	/**
	 * Overrides default equals method to make the Pattern class hashable.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Pattern)) {
			return false;
		}
		
		Pattern p = (Pattern) o;
		return Arrays.equals(thePattern, p.getPattern());
	}
}