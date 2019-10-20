package alienAttack;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/*******************************************************************************
 * The Class Explosion for the Alien Attack game. Creates animated explosions
 * given an array of explosion images.
 * 
 * @author Sam Eary
 * @version 1.0
 ******************************************************************************/
public class Explosion {

	/** The x position. */
	private int xPos;

	/** The y position. */
	private int yPos;

	/** The index id number of an explosion. */
	private int number;

	/** The explosion image array. */
	private BufferedImage[] explode;

	/***************************************************************************
	 * Instantiates a new explosion.
	 * 
	 * @param xPos
	 *            The x position
	 * @param yPos
	 *            The y position
	 * @param explode
	 *            The array of explosion images.
	 ***************************************************************************/
	public Explosion(int xPos, int yPos, BufferedImage[] explode) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.explode = explode;
		number = 0;
	}

	/***************************************************************************
	 * Draw the explosions for non game ending explosions.
	 * 
	 * @param g
	 *            Graphics reference from the buffer.
	 ***************************************************************************/
	public void draw(Graphics g) {
		g.drawImage(explode[number], xPos, yPos, null);
		number++;
	}

	/***************************************************************************
	 * Gets the explosion status. True tells the game class that the explosion
	 * animation is complete.
	 * 
	 * @return The explosion status
	 ***************************************************************************/
	public boolean getExpStatus() {
		if (number == explode.length)
			return true;
		else
			return false;
	}

	/***************************************************************************
	 * Draw the explosion images given a explosion id number. Normally used at
	 * the end of the game.
	 * 
	 * @param g
	 *            Graphics reference from the buffer.
	 * @param number
	 *            The explosion id number.
	 ***************************************************************************/
	public void draw(Graphics g, int number) {
		g.drawImage(explode[number], xPos, yPos, null);
	}

	/***************************************************************************
	 * Sets the x position.
	 * 
	 * @param xPos
	 *            The new x position.
	 ***************************************************************************/
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
}
