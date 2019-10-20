package game;

import utility.Position;

/**************************************************************************
 * A class to keep track of positions of cell objects.
 * 
 * @author Sam Eary and Tyler Blanchard
 * @version 1.0
 **************************************************************************/
public abstract class Cell {

	/** The position of the cell. */
	private Position position;

	/**************************************************************************
	 * Sets the position of the cell to {@code p}.
	 * 
	 * @param p
	 *            The position to set the cell's position to.
	 **************************************************************************/
	protected void setPosition(Position p) {
		position = p;
	}

	/**************************************************************************
	 * Gets the cell's position.
	 * 
	 * @return the cell's position.
	 **************************************************************************/
	protected Position getPosition() {
		return position;
	}
}
