package utility;

/**************************************************************************
 * An exception for the case of non-adjacent tile placement.
 * 
 * @author Sam Eary and Tyler Blanchard
 * @version 1.0
 **************************************************************************/
public class MiddleOfNowhereException extends Exception {

	/** The serial version ID. */
	private static final long serialVersionUID = 6264061476893730238L;

	/**************************************************************************
	 * Constructor for the {@code MiddleOfNowhereException}. Generates
	 * an exception for non-adjacent tile placement.
	 **************************************************************************/
	public MiddleOfNowhereException() {

	}
}
