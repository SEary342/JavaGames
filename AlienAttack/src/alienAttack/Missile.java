package alienAttack;

import java.awt.*;
import java.awt.image.BufferedImage;

/*******************************************************************************
 * The missile class for the Alien attack game. Keeps track of all relevant
 * information regarding the missile.
 * 
 * @author Sam Eary
 * @version 1.0
 ******************************************************************************/
public class Missile {

	/** The coordinates of the missile */
	private int xPos, yPos;

	/** The velocity of the missile. */
	private int velocity;

	/** The position of the wall. */
	private int wall;

	/** The height and width of the missile. */
	private int width, height;

	/***************************************************************************
	 * Constructs a new missile.
	 * 
	 * @param pX
	 *            The x position of the missile.
	 * @param pY
	 *            The y position of the missile.
	 * @param pWidth
	 *            The width of the missile.
	 * @param pHeight
	 *            The height of the missile.
	 * @param pVel
	 *            The missiles velocity.
	 * @param pWall
	 *            The point where the missile dies.
	 ***************************************************************************/
	public Missile(int pX, int pY, int pWidth, int pHeight, int pVel, int pWall) {
		this.xPos = pX;
		this.yPos = pY;
		this.width = pWidth;
		this.height = pHeight;
		this.wall = pWall;
		this.velocity = pVel;
	}

	/***************************************************************************
	 * Calculates the nose of the missile.
	 * 
	 * @return The nose of the missile.
	 ***************************************************************************/
	public Point getNose() {
		Point nose = new Point();
		if (velocity < 0)
			nose.setLocation(xPos + width / 2, yPos);
		else
			nose.setLocation(xPos + width / 2, yPos + height);
		return nose;
	}

	/***************************************************************************
	 * Moves the missile given an images and graphics from the buffer.
	 * 
	 * @param g
	 *            The graphics from the buffer.
	 * @param image
	 *            The image of the missile.
	 ***************************************************************************/
	public void move(Graphics g, BufferedImage image) {
		yPos += velocity;
		g.drawImage(image, xPos, yPos, null);
	}

	/***************************************************************************
	 * Tests to see if the missile has flown off the screen.
	 * 
	 * @return True means that the missile has flown off the screen.
	 ***************************************************************************/
	public boolean isOffScreen() {
		if (velocity < 0 && yPos <= wall) {
			return true;
		} else if (velocity > 0 && yPos >= wall) {
			return true;
		} else
			return false;
	}
}
