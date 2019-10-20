package utility;

/**************************************************************************
 * A class to keep track of x and y positions.
 * 
 * @author Sam Eary and Tyler Blanchard
 * @version 1.0
 **************************************************************************/
public class Position {

	/** The x position. */
	private int x;

	/** The y position. */
	private int y;

	/**************************************************************************
	 * Constructor for the {@code Position} class. Sets the initial
	 * position.
	 * 
	 * @param x
	 *            The x position.
	 * @param y
	 *            The y position.
	 **************************************************************************/
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**************************************************************************
	 * Gets the x value of the position.
	 * 
	 * @return the x value of the position.
	 **************************************************************************/
	public int getX() {
		return x;
	}

	/**************************************************************************
	 * Gets the y value of the position.
	 * 
	 * @return the y value of the position.
	 **************************************************************************/
	public int getY() {
		return y;
	}

	/**************************************************************************
	 * Tests to see if one position is equal to another.
	 * 
	 * @param pos
	 *            The position to be tested for equality.
	 * @return {@code true} if the positions are equal, {@code false}
	 *         otherwise.
	 **************************************************************************/
	public boolean equals(Position pos) {
		return pos.getX() == getX() && pos.getY() == getY();
	}
}
