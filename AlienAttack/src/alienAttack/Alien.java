package alienAttack;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/*******************************************************************************
 * The alien class for the Alien attack game. Keeps track and controls all of
 * the alien entities and moves them.
 * 
 * @author Sam Eary
 * @version 1.0
 ******************************************************************************/
public class Alien {

	/** The coordinates of the alien */
	private int xPos, yPos;

	/** The dimensions of the aliens */
	private int width, height;

	/** The velocity of the aliens */
	private int velocity;

	/** The probability of firing a missile */
	private double probability;

	/** The walls of the aliens */
	private int wall2, wall;

	/** The missile that the alien can fire */
	private Missile missile;

	/** The missiles velocity */
	public static final int misVelocity = 5;

	/***************************************************************************
	 * Constructs an alien ship.
	 * 
	 * @param pX
	 *            The x position.
	 * @param pY
	 *            The y position.
	 * @param pW
	 *            The width.
	 * @param pH
	 *            The height.
	 * @param velocity
	 *            The velocity of the alien.
	 * @param pMissileLaunchProb
	 *            The probability of missile launch.
	 * @param pWall
	 *            The end wall for a ship.
	 ***************************************************************************/
	public Alien(int pX, int pY, int pW, int pH, int velocity,
			Double pMissileLaunchProb, int pWall) {
		this.xPos = pX;
		this.yPos = pY;
		this.width = pW;
		this.height = pH;
		this.velocity = velocity;
		this.probability = pMissileLaunchProb;
		this.wall2 = pWall;
		this.wall = pX;
		missile = null;
	}

	/***************************************************************************
	 * Gets the alien's missile
	 * 
	 * @return The alien's missile.
	 ***************************************************************************/
	public Missile getMissile() {
		return missile;
	}

	/***************************************************************************
	 * Moves the alien and fires the weapon if the probability is in the correct
	 * range.
	 * 
	 * @param g
	 *            The graphics from the buffer.
	 * @param fighter
	 *            The image of the alien.
	 * @param eMissile
	 *            The image of the alien's missile.
	 * @param misWall
	 *            The end wall for the missiles.
	 ***************************************************************************/
	public void move(Graphics g, BufferedImage fighter, BufferedImage eMissile,
			int misWall) {
		Random fire = new Random();
		xPos += velocity;
		if (xPos > wall2 || xPos < wall) {
			this.velocity = (0 - velocity);
		}
		g.drawImage(fighter, xPos, yPos, null);

		// Missile mover
		if (getMissile() != null) {
			missile.move(g, eMissile);
			if (missile.isOffScreen())
				missile = null;

			// Missile launcher
		} else {
			if (fire.nextDouble() < probability) {
				missile = new Missile(xPos + width / 3, yPos + height, eMissile
						.getWidth(), eMissile.getHeight(), misVelocity, misWall);
				missile.move(g, eMissile);
			}
		}
	}

	/***************************************************************************
	 * Tests to see if the alien is hit given an enemy missile.
	 * 
	 * @param g
	 *            The graphics from the buffer.
	 * @param enemyMissile
	 *            The enemy's missile.
	 * @return True if the alien is hit.
	 ***************************************************************************/
	public boolean isHit(Graphics g, Missile enemyMissile) {
		double x = 0;
		double y = 0;
		if (enemyMissile != null) {
			x = enemyMissile.getNose().getX();
			y = enemyMissile.getNose().getY();
		}
		if ((x <= xPos + width && x >= xPos)
				&& (y >= yPos && y <= yPos + height)) {
			return true;
		} else
			return false;
	}

	/***************************************************************************
	 * Destroys the alien's missile.
	 ***************************************************************************/
	public void remove() {
		if (missile != null) {
			missile = null;
		}
	}

	/***************************************************************************
	 * Gets the x position of the alien.
	 * 
	 * @return The x position.
	 ***************************************************************************/
	public int getX() {
		return xPos;
	}

	/***************************************************************************
	 * The y position of the alien.
	 * 
	 * @return The y position.
	 ***************************************************************************/
	public int getY() {
		return yPos;
	}
}
