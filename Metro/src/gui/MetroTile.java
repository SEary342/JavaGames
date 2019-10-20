package gui;

import java.awt.image.BufferedImage;
import utility.Position;

/**************************************************************************
 * A class to control the display image of the Metro tiles on the game
 * board.
 * 
 * @author Sam Eary and Tyler Blanchard
 * @version 1.0
 **************************************************************************/
public class MetroTile extends Tile {

	/** The serial id of this class. */
	private static final long serialVersionUID = 3693034604716431690L;

	/** Image display status. */
	private boolean set, imageDisplayed;

	/** The position of the tile. */
	private Position position;

	/** The current owner of the {@code MetroTile}. */
	private int playerId;

	/**************************************************************************
	 * Constructs this {@code MetroTile} with an initial image.
	 * 
	 * @param image
	 *            The initial image of the {@code MetroTile}.
	 **************************************************************************/
	protected MetroTile(BufferedImage image) {
		super(image);
	}

	/**************************************************************************
	 * Constructs this {@code MetroTile} with an initial image and
	 * {@code Position}.
	 * 
	 * @param position
	 *            The initial {@code Position}.
	 * @param image
	 *            The initial image of the {@code MetroTile}.
	 **************************************************************************/
	protected MetroTile(Position position, BufferedImage image) {
		super(image);
		this.position = position;
	}

	/**************************************************************************
	 * Sets the id of the {@code MetroTile}.
	 * 
	 * @param id
	 *            The id of the {@code MetroTile}.
	 **************************************************************************/
	protected void setId(int id) {
		playerId = id;
	}

	/**************************************************************************
	 * Gets the id of the {@code MetroTile}.
	 * 
	 * @return the id of the {@code MetroTile}.
	 **************************************************************************/
	protected int getId() {
		return playerId;
	}

	/**************************************************************************
	 * Gets the {@code Position} of the {@code MetroTile}.
	 * 
	 * @return the {@code Position} of the {@code MetroTile}.
	 **************************************************************************/
	protected Position getPosition() {
		return position;
	}

	/**************************************************************************
	 * Sets an image to the {@code MetroTile} and locks it in place.
	 * 
	 * @param tile
	 *            The image to set the {@code MetroTile} to.
	 **************************************************************************/
	protected void confirmSet(BufferedImage tile) {
		setImage(tile);
		set = true;
		repaint();
	}

	/**************************************************************************
	 * Sets the image without locking it.
	 * 
	 * @param tile
	 *            The image to set the {@code MetroTile} to.
	 **************************************************************************/
	protected void previewImage(BufferedImage tile) {
		setImage(tile);
		repaint();
		imageDisplayed = true;
	}

	/**************************************************************************
	 * Resets the image to the initial state.
	 **************************************************************************/
	protected void clear(BufferedImage image) {
		super.clear(image);
		imageDisplayed = false;
	}

	/**************************************************************************
	 * Gets the set status.
	 * 
	 * @return {@code true} if the image is set, {@code false}
	 *         otherwise.
	 **************************************************************************/
	protected boolean getSet() {
		return set;
	}

	/**************************************************************************
	 * Gets the status of image if displayed.
	 * 
	 * @return {@code true} if the image is displayed, otherwise {@code
	 *         false}.
	 **************************************************************************/
	protected boolean getImageDisplayed() {
		return imageDisplayed;
	}
}
