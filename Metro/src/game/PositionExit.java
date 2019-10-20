package game;

import game.TileCell.Link;
import utility.Position;

/**************************************************************************
 * A class to keep track of the position and exits of cell objects.
 * 
 * @author Sam Eary and Tyler Blanchard
 * @version 1.0
 **************************************************************************/
public class PositionExit extends Position {

	/** The exit of the object. */
	private Link exit;

	/**************************************************************************
	 * Constructor for the {@code PositionExit} class. Sets the position
	 * and exit.
	 * 
	 * @param x
	 *            The x coordinate of the position.
	 * @param y
	 *            The y coordinate of the position.
	 * @param exit
	 *            The exit link.
	 **************************************************************************/
	public PositionExit(int x, int y, Link exit) {
		super(x, y);
		this.exit = exit;
	}

	/**************************************************************************
	 * Gets the exit associated with the {@code PositionExit} object.
	 * 
	 * @return the exit associated with the {@code PositionExit} object.
	 **************************************************************************/
	protected Link getExit() {
		return exit;
	}
}
