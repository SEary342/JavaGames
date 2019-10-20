package game;

import game.TileCell.Link;
import java.util.ArrayList;
import utility.Position;

/**************************************************************************
 * A class to keep track of each of the stations and the {@code
 * TileCells} attached to them.
 * 
 * @author Sam Eary and Tyler Blanchard
 * @version 1.0
 **************************************************************************/
public class StationCell extends Cell {

	/** The array of {@code PositionExits} connected to the station. */
	private ArrayList<PositionExit> cells;

	/** The exit of the station. */
	private Link exit;

	/** The owner of the station. */
	private int owner;

	/** The completion status of the station. */
	private boolean complete;

	/** The completion status of scoring for the station. */
	private boolean scored;

	/**************************************************************************
	 * Primary constructor for the {@code StationCell} class. Sets the
	 * position and the exit of the station.
	 * 
	 * @param position
	 *            The position of the station.
	 * @param rows
	 *            The total rows in the game board.
	 * @param cols
	 *            The total columns in the game board.
	 **************************************************************************/
	public StationCell(Position position, int rows, int cols) {
		super();
		super.setPosition(position);
		exit = getExit(super.getPosition(), rows, cols);
		cells = new ArrayList<PositionExit>(0);
	}

	/**************************************************************************
	 * Sets the owner of the {@code StationCell}.
	 * 
	 * @param owner
	 *            The owner number.
	 **************************************************************************/
	protected void setOwner(int owner) {
		this.owner = owner;
	}

	/**************************************************************************
	 * Gets the owner of the {@code StationCell}.
	 * 
	 * @return the owner of the {@code StationCell}.
	 **************************************************************************/
	protected int getOwner() {
		return owner;
	}

	/**************************************************************************
	 * Adds a tile to the list of cells connected to this {@code
	 * StationCell}.
	 * 
	 * @param x
	 *            The x coordinate of the new cell.
	 * @param y
	 *            The y coordinate of the new cell.
	 * @param exit
	 *            The exit of the new cell.
	 **************************************************************************/
	protected void addTile(int x, int y, Link exit) {
		cells.add(new PositionExit(x, y, exit));
	}

	/**************************************************************************
	 * Gets the exit of the {@code StationCell}.
	 * 
	 * @param position
	 *            The position of the {@code StationCell}.
	 * @param rows
	 *            The total number of rows in the game board.
	 * @param cols
	 *            The total number of columns in the game board.
	 * @return the exit of the {@code StationCell}.
	 **************************************************************************/
	private Link getExit(Position position, int rows, int cols) {
		int x = position.getX();
		int y = position.getY();
		if (x == -1)
			return Link.SouthLeft;
		else if (x == rows)
			return Link.NorthRight;
		else if (y == -1)
			return Link.EastLower;
		else
			return Link.WestUpper;
	}

	/**************************************************************************
	 * Gets the tail of the array of {@code PositionExits}.
	 * 
	 * @return The last element in the array of {@code PositionExits}.
	 **************************************************************************/
	protected PositionExit getTail() {
		if (cells.size() == 0)
			return (new PositionExit(super.getPosition().getX(), super
					.getPosition().getY(), exit));
		else
			return cells.get(cells.size() - 1);
	}

	/**************************************************************************
	 * Gets the first {@code PositionExit} of the array.
	 * 
	 * @return the first {@code PositionExit} of the array.
	 **************************************************************************/
	protected Position getFirstCell() {
		return cells.get(0);
	}

	/**************************************************************************
	 * Gets the array of {@code PositionExits}.
	 * 
	 * @return the array of {@code PositionExits}.
	 **************************************************************************/
	protected ArrayList<PositionExit> getCells() {
		return cells;
	}

	/**************************************************************************
	 * Sets the completion status of the station.
	 **************************************************************************/
	protected void setComplete() {
		complete = true;
	}

	/**************************************************************************
	 * Sets the scored status of the station.
	 **************************************************************************/
	protected void setScored() {
		scored = true;
	}

	/**************************************************************************
	 * Gets the completion status of the {@code StationCell}.
	 * 
	 * @return {@code true} if the station is complete, {@code false}
	 *         otherwise.
	 **************************************************************************/
	protected boolean isComplete() {
		return complete;
	}

	/**************************************************************************
	 * Tests to see station has been started.
	 * 
	 * @return {@code true} if the station has been started, {@code
	 *         false} otherwise.
	 **************************************************************************/
	protected boolean isStarted() {
		return cells.size() != 0;
	}

	/**************************************************************************
	 * Gets the score completion status of the {@code StationCell}.
	 * 
	 * @return {@code true} if the station scoring is complete, {@code
	 *         false} otherwise.
	 **************************************************************************/
	protected boolean isScored() {
		return scored;
	}

	/**************************************************************************
	 * Gets the number of cells in the {@code cell} array.
	 * 
	 * @return the number of cells in the {@code cell} array.
	 **************************************************************************/
	protected int numberOfCells() {
		return cells.size();
	}
}
