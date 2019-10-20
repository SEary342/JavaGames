package alienAttack;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/*******************************************************************************
 * The ship class for the Alien Attack game. Creates the player's ship and moves
 * it.
 * 
 * @author Sam Eary
 * @version 1.0
 ******************************************************************************/
public class Ship {

	/** The position and size of the ship */
	private int width, height, xPos, yPos;

	/** ArrayLists to keep track of the rapid fire torpedos */
	private ArrayList<Missile> rightMissiles, leftMissiles, centerMis;

	/** The basic single shot missile */
	private Missile missile;

	/***************************************************************************
	 * Constructs the ship and instantiates the instance variables with the
	 * exception of missile.
	 * 
	 * @param xPos
	 *            The x position
	 * @param yPos
	 *            The y position
	 * @param width
	 *            The width of the ship
	 * @param height
	 *            The height of the ship
	 ***************************************************************************/
	public Ship(int xPos, int yPos, int width, int height) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		rightMissiles = new ArrayList<Missile>();
		leftMissiles = new ArrayList<Missile>();
		centerMis = new ArrayList<Missile>();
	}

	/***************************************************************************
	 * Tests to see if the player's ship has been hit by an enemy weapon.
	 * 
	 * @param alien
	 *            The alien that fired the missile
	 * @return True if the player's ship has been hit.
	 ***************************************************************************/
	public boolean isHit(Alien alien) {
		double x = 0;
		double y = 0;
		if (alien.getMissile() != null) {
			x = alien.getMissile().getNose().getX();
			y = alien.getMissile().getNose().getY();
		}
		if (x <= xPos + width && x >= xPos && y >= yPos && y <= yPos + height) {
			return true;
		}
		return false;
	}

	/***************************************************************************
	 * Draws the ship given an image and graphics from the buffer.
	 * 
	 * @param g
	 *            The graphics from the buffer.
	 * @param ship
	 *            The images of the ship.
	 ***************************************************************************/
	public void drawShip(Graphics g, BufferedImage ship) {
		g.drawImage(ship, xPos, yPos, null);
	}

	/***************************************************************************
	 * Gets the x position.
	 * 
	 * @return The x position.
	 ***************************************************************************/
	public int getXpos() {
		return xPos;
	}

	/***************************************************************************
	 * Get the y position.
	 * 
	 * @return The y position.
	 ***************************************************************************/
	public int getYpos() {
		return yPos;
	}

	/***************************************************************************
	 * Changes the x position of the ship.
	 * 
	 * @param xPos
	 *            The position to be changed to.
	 ***************************************************************************/
	public void setXpos(int xPos) {
		this.xPos = xPos;
	}

	/***************************************************************************
	 * Instantiates a basic single shot missile/torpedo.
	 * 
	 * @param torpedo
	 *            The image for the missile.
	 ***************************************************************************/
	public void fireMissile(BufferedImage torpedo) {
		if (missile == null) {
			missile = new Missile(xPos + 19, yPos, torpedo.getWidth(), torpedo
					.getHeight(), -10, 0);
		}
	}

	/***************************************************************************
	 * Gets the basic missile/torpedo.
	 * 
	 * @return The basic missile
	 ***************************************************************************/
	public Missile getMissile() {
		return missile;
	}

	/***************************************************************************
	 * Nullifies the basic missile.
	 ***************************************************************************/
	public void destroyMissile() {
		if (missile != null)
			missile = null;
	}

	/***************************************************************************
	 * Fire the rapid fire torpedo launchers utilizing arrays. Instantiates the
	 * elements of the arrays.
	 * 
	 * @param type
	 *            the type of weapon
	 * @param torpedo
	 *            the image of the weapon
	 ***************************************************************************/
	public void fire(int type, BufferedImage torpedo) {
		if (type == 1)
			leftMissiles.add(new Missile(xPos, yPos - height / 5, torpedo
					.getWidth(), torpedo.getHeight(), -10, 0));
		else if (type == 2)
			rightMissiles.add(new Missile(xPos + width - torpedo.getWidth(),
					yPos - height / 5, torpedo.getWidth(), torpedo.getHeight(),
					-10, 0));
		else if (type == 3)
			centerMis.add(new Missile(xPos + 8, yPos, torpedo.getWidth(),
					torpedo.getHeight(), -10, 0));
	}

	/***************************************************************************
	 * Gets the ArrayList for the specified torpedo type.
	 * 
	 * @param type
	 *            The type of torpedo.
	 * @return The ArrayList of torpedos.
	 ***************************************************************************/
	public ArrayList<Missile> getMissiles(int type) {
		if (type == 1)
			return leftMissiles;
		else if (type == 2)
			return rightMissiles;
		else
			return centerMis;
	}

	/***************************************************************************
	 * Moves the simple missiles fired by the player.
	 * 
	 * @param g The graphics from the buffer
	 * @param hit True means that the missile has hit something
	 * @param type The image of the missile.
	 ***************************************************************************/
	public void simpleMover(Graphics g, boolean hit, BufferedImage type) {
		missile.move(g, type);
		if (hit)
			destroyMissile();
		else if (missile.isOffScreen())
			destroyMissile();
	}
}
