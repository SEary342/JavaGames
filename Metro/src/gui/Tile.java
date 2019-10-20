package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**************************************************************************
 * An abstract class to control basic display functions of a MetroTile
 * or TerminalTile.
 * 
 * @author Sam Eary and Tyler Blanchard
 * @version 1.0
 **************************************************************************/
public abstract class Tile extends JPanel {

	/** Serial ID of the {@code Tile} class. */
	private static final long serialVersionUID = -721448506361790942L;
	
	/** The image to be painted to the tile. */
	private BufferedImage image;

	/**************************************************************************
	 * Primary constructor that sets a tile's initial image.
	 * 
	 * @param image
	 *            The initial image to be set.
	 **************************************************************************/
	public Tile(BufferedImage image) {
		try {
			setImage(image);
		} catch (NullPointerException e) {
			setImage(new BufferedImage(64, 64,
					BufferedImage.TYPE_INT_RGB));
		}
	}

	/**************************************************************************
	 * Allows for alteration of the display image.
	 * 
	 * @param tile
	 *            The image to be displayed.
	 **************************************************************************/
	protected void setImage(BufferedImage tileImage) {
		image = tileImage;
	}

	/**************************************************************************
	 * Paints the current display image onto the {@code Tile}.
	 **************************************************************************/
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, this);
	}

	/**************************************************************************
	 * Removes the displayed image and draws a background.
	 * 
	 * @param image
	 *            The background to be displayed.
	 **************************************************************************/
	protected void clear(BufferedImage image) {
		setImage(image);
		repaint();
	}
}
