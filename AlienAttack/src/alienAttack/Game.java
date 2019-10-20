package alienAttack;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

/*******************************************************************************
 * The alien attack game class. Creates and controls the game
 * environment/display and allows player input via keyboard or mouse in
 * order to play the game.
 * 
 * @author Sam Eary
 * @version 1.1
 ******************************************************************************/
public class Game implements ActionListener, KeyListener, Runnable,
		MouseMotionListener, MouseListener {

	/** The canvas where all events are displayed */
	private Canvas display;

	/** The array of alien invaders */
	private Alien[] aliens;

	/** Integers that keep track of game stats and difficulty levels */
	private int deadAliens, lives, score, diffLevel, alienTotal;

	/** Keeps track of difficulty variables until ok button pushed. */
	private int tempAlienCount, tempDiff;

	/** The items in the file menu of the GUI */
	private JMenuItem play, quit, about, difficulty;

	/** The status labels for the game */
	private JLabel deadAlien, scoreLab, life;

	/** The frame that contains the GUI interface */
	private JFrame frame;

	/** The thread that controls the game loop */
	private Thread gameLoop;

	/** The three different player ship versions */
	private BufferedImage ship, damage1, damage2;

	/** The primary images for the game elements */
	private BufferedImage fighter, torpedo, eMissile, bigTorp;

	/**
	 * The images for the buffer, background and other GUI related items
	 */
	private BufferedImage buffer, star, win, lose, pause, cursor;

	/** The buffer's graphics */
	private Graphics buf;

	/** The players ship */
	private Ship player;

	/** Enables and disables the game interface to start the game */
	private boolean enable, okEnable;

	/** The selections for difficulty level */
	private JRadioButton easy, medium, hard, impossible;

	/** JPanels to contain components inside the frame */
	private JPanel gameOptions, commands, labels;

	/** The frame where difficulty options are displayed */
	private JFrame diffFrame;

	/** Buttons in the difficulty window */
	private JButton ok, close;

	/** An array to contain all of the alien explosions */
	private Explosion[] explode;

	/** The explosion of the players ship */
	private Explosion shipExplosion;

	/** The image array of the explosions */
	private BufferedImage[] explosions, shipExplode;

	/***************************************************************************
	 * Instantiates primary variables and constructs the interface.
	 * Also, sets a default difficulty value.
	 ***************************************************************************/
	public Game() {
		okEnable = false;
		score = 0;
		alienTotal = 55;
		diffLevel = 50;
		enable = false;
		explosions = new BufferedImage[7];
		shipExplode = new BufferedImage[7];
		loadImages();
		loadExplodeImages();
		setup();
		clearBuffer();
		frame.setUndecorated(true);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		display.setFocusable(true);
	}

	/***************************************************************************
	 * Loads the ships, torpedos, missiles, large message screens, and
	 * the invisible cursor. Automatically closes the game after
	 * displaying a dialog if an image load error occurred.
	 ***************************************************************************/
	private void loadImages() {
		try {
			ship = ImageIO.read(new File("cruiser.png"));
			damage1 = ImageIO.read(new File("cruiserdamage1.png"));
			damage2 = ImageIO.read(new File("cruiserdamage2.png"));
			fighter = ImageIO.read(new File("fighter.png"));
			torpedo = ImageIO.read(new File("torpedo.png"));
			pause = ImageIO.read(new File("pause.png"));
			eMissile = ImageIO.read(new File("missile.png"));
			win = ImageIO.read(new File("win.png"));
			bigTorp = ImageIO.read(new File("torpbig.png"));
			lose = ImageIO.read(new File("loss.png"));
			cursor = ImageIO.read(new File("cursor.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Image Load Error",
					"Program Terminating", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	/***************************************************************************
	 * Loads the images for the explosion animations. Automatically
	 * closes the game after displaying a dialog if an image load error
	 * occurred.
	 ***************************************************************************/
	private void loadExplodeImages() {
		try {
			explosions[0] = ImageIO.read(new File("fireball12.png"));
			explosions[1] = ImageIO.read(new File("fireball11.png"));
			explosions[2] = ImageIO.read(new File("fireball1.png"));
			explosions[3] = ImageIO.read(new File("fireball2.png"));
			explosions[4] = ImageIO.read(new File("fireball3.png"));
			explosions[5] = ImageIO.read(new File("fireball4.png"));
			explosions[6] = ImageIO.read(new File("fireball5.png"));
			shipExplode[0] = ImageIO.read(new File("shipfire11.png"));
			shipExplode[1] = ImageIO.read(new File("shipfire.png"));
			shipExplode[2] = ImageIO.read(new File("shipfire1.png"));
			shipExplode[3] = ImageIO.read(new File("shipfire2.png"));
			shipExplode[4] = ImageIO.read(new File("shipfire3.png"));
			shipExplode[5] = ImageIO.read(new File("shipfire4.png"));
			shipExplode[6] = ImageIO.read(new File("shipfire5.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Image Load Error",
					"Program Terminating", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	/***************************************************************************
	 * Randomly loads an image for the background picture. Automatically
	 * closes the game after displaying a dialog if an image load error
	 * occurred.
	 ***************************************************************************/
	private void randomImageLoader() {
		Random loader = new Random();
		int loadIm = loader.nextInt(5);
		try {
			if (loadIm == 0)
				star = ImageIO.read(new File("starfield.jpg"));
			else if (loadIm == 1)
				star = ImageIO.read(new File("coronaLoop.jpg"));
			else if (loadIm == 2)
				star = ImageIO.read(new File("moon.jpg"));
			else if (loadIm == 3)
				star = ImageIO.read(new File("galaxy.jpg"));
			else if (loadIm == 4)
				star = ImageIO.read(new File("nebula.jpg"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Image Load Error",
					"Program Terminating", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	/***************************************************************************
	 * Starts the frame setup process and creates a color scheme.
	 ***************************************************************************/
	private void setup() {
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		JPanel messages = new JPanel();
		frameInstantiations();
		frameColorDisplaySetup();
		messages.setLayout(new GridLayout(1, 3));

		// Colors
		messages.setBackground(Color.BLACK);
		menu.setBackground(Color.BLACK);
		menu.setForeground(Color.BLACK);
		file.setForeground(Color.WHITE);
		file.setBackground(Color.BLACK);

		// Adding to the frame and menus.
		frame.setJMenuBar(menu);
		frame.add(messages, BorderLayout.SOUTH);
		menu.add(file);
		file.add(play);
		file.add(difficulty);
		file.add(about);
		file.add(quit);
		messages.add(life);
		messages.add(deadAlien);
		messages.add(scoreLab);
	}

	/***************************************************************************
	 * Instantiates all necessary instance variables for the frame.
	 * Also, add action listeners.
	 ***************************************************************************/
	private void frameInstantiations() {
		deadAlien = new JLabel(" ");
		life = new JLabel("");
		scoreLab = new JLabel(" ");
		quit = new JMenuItem("Quit");
		difficulty = new JMenuItem("Difficulty");
		frame = new JFrame("Alien Attack");
		display = new Canvas();
		play = new JMenuItem("Play");
		about = new JMenuItem("About");
		play.addActionListener(this);
		difficulty.addActionListener(this);
		about.addActionListener(this);
		quit.addActionListener(this);
	}

	/***************************************************************************
	 * Sets up colors of the labels, adds some frame properties and sets
	 * up the display(canvas).
	 ***************************************************************************/
	private void frameColorDisplaySetup() {
		deadAlien.setForeground(Color.WHITE);
		scoreLab.setForeground(Color.WHITE);
		life.setForeground(Color.WHITE);

		// Frame properties
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(display, BorderLayout.CENTER);
		frame.add(play, BorderLayout.SOUTH);
		frame.setResizable(false);
		frame.setIconImage(new ImageIcon("cruiser.png").getImage());

		// Display setup
		display.setBackground(Color.BLACK);
		display.addKeyListener(this);
		display.addMouseListener(this);
		display.addMouseMotionListener(this);
		display.setFocusable(true);
	}

	/***************************************************************************
	 * Sets up the double buffer necessary to eliminate screen flicker.
	 * Creates a large image for all other images to be painted on to.
	 ***************************************************************************/
	private void clearBuffer() {
		buffer = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
		buf = buffer.getGraphics();
		buf.setColor(display.getBackground());
		buf.fillRect(0, 0, 800, 600);
	}

	/***************************************************************************
	 * Initiates the difficulty frame startup.
	 ***************************************************************************/
	private void difficultyResponse() {
		difficultySetup();
		if (gameLoop != null)
			pauseGame();
	}

	/***************************************************************************
	 * Sets up the difficulty frame.
	 ***************************************************************************/
	private void difficultySetup() {
		JLabel diffLabel = new JLabel("Difficulty Level");
		diffInstantiator();
		diffSetupFormat();
		diffLabel.setForeground(Color.WHITE);
		labels.add(diffLabel);

		// Button setup
		radioSetup(easy);
		radioSetup(medium);
		radioSetup(hard);
		radioSetup(impossible);
		commands.add(ok);
		commands.add(close);

		// Makes the window visible.
		diffFrame.setSize(300, 100);
		diffFrame.setUndecorated(true);
		diffFrame.setLocationRelativeTo(null);
		diffFrame.setVisible(true);
		diffFrame.setAlwaysOnTop(true);
	}

	/***************************************************************************
	 * Instantiates variables necessary for the difficulty selector
	 * window.
	 ***************************************************************************/
	private void diffInstantiator() {
		ButtonGroup levels = new ButtonGroup();
		gameOptions = new JPanel();
		commands = new JPanel();
		labels = new JPanel();
		diffFrame = new JFrame();

		// Buttons
		easy = new JRadioButton("Easy");
		medium = new JRadioButton("Medium");
		hard = new JRadioButton("Hard");
		impossible = new JRadioButton("Impossible");
		ok = new JButton("OK");
		close = new JButton("Close");

		// Makes the radio's mutually exclusive.
		levels.add(easy);
		levels.add(medium);
		levels.add(hard);
		levels.add(impossible);
	}

	/***************************************************************************
	 * Sets the layouts, colors and adds the panels to the difficulty
	 * window.
	 ***************************************************************************/
	private void diffSetupFormat() {

		// Layouts.
		diffFrame.setLayout(new BorderLayout());
		gameOptions.setLayout(new FlowLayout());
		commands.setLayout(new FlowLayout());
		labels.setLayout(new FlowLayout());

		// Match the color scheme.
		commands.setBackground(Color.BLACK);
		gameOptions.setBackground(Color.BLACK);
		labels.setBackground(Color.BLACK);

		// Adding the panels to the frame.
		diffFrame.add(gameOptions, BorderLayout.CENTER);
		diffFrame.add(commands, BorderLayout.SOUTH);
		diffFrame.add(labels, BorderLayout.NORTH);
		buttonSetup(ok);
		buttonSetup(close);
	}

	/***************************************************************************
	 * Sets up the buttons for the difficulty selector.
	 ***************************************************************************/
	private void buttonSetup(JButton button) {
		button.setBackground(Color.BLACK);
		button.setForeground(Color.WHITE);
		button.addActionListener(this);
	}

	/***************************************************************************
	 * Sets up each of the radio buttons
	 * 
	 * @param button
	 *            The radio button to be modified.
	 ***************************************************************************/
	private void radioSetup(JRadioButton button) {
		gameOptions.add(button);
		button.setBackground(Color.BLACK);
		button.setForeground(Color.WHITE);
		button.addActionListener(this);
	}

	/***************************************************************************
	 * Starts the game thread to run a game. It also creates an
	 * invisible cursor for the display when the cursor is inside of the
	 * display canvas.
	 ***************************************************************************/
	private void start() {
		Cursor myCursor = Toolkit.getDefaultToolkit()
				.createCustomCursor(cursor, new Point(0, 0), "cursor");
		if (gameLoop != null)
			gameLoop.interrupt();
		gameLoop = new Thread(this);
		gameLoop.start();
		display.setCursor(myCursor);
	}

	/***************************************************************************
	 * Instantiates round specific variables for the game. It creates
	 * the necessary arrays and classes necessary for the game.
	 ***************************************************************************/
	private void startup() {
		int alienX = 10;
		int alienY = 0;
		int alienVelocity = 5;
		explode = new Explosion[alienTotal];
		shipExplosion = null;
		player = new Ship(display.getWidth() / 2, display.getHeight()
				- ship.getHeight(), ship.getWidth(), ship.getHeight());
		aliens = new Alien[alienTotal];
		deadAliens = 0;

		// creates the array of aliens
		for (int i = 0; i < aliens.length; i++) {
			aliens[i] = new Alien(alienX, alienY, fighter.getWidth(),
					fighter.getHeight(), alienVelocity, .01,
					alienX + 40);

			// Alters alien start positions and reverses velocities for
			// each
			// row.
			alienX += 70;
			if (alienX > display.getWidth() - 70) {
				alienX = 10;
				alienY += 45;
				alienVelocity = 0 - alienVelocity;
			}
		}
		labelUpdate();
		display.requestFocus();
	}

	/***************************************************************************
	 * The primary game loop for the run() method.
	 ***************************************************************************/
	private void playRound() {
		Graphics g = display.getGraphics();

		// Draw the background first.
		buf.drawImage(star, 0, 0, null);

		// Move the player
		playerMover();

		// Checks for win.
		if (deadAliens == aliens.length) {
			buf.drawImage(win, display.getWidth() / 6, display
					.getHeight() / 4, display);
			paint(g);
			pause(5000);
		}

		// Move everything else
		missileMovement();
		alienMover();
		explosionUpdate();
		paint(g);
		pause(diffLevel);
	}

	/***************************************************************************
	 * Moves the players ship and changes the image to account for
	 * damage.
	 ***************************************************************************/
	private void playerMover() {
		if (lives == 3)
			player.drawShip(buf, ship);
		else if (lives == 2)
			player.drawShip(buf, damage1);
		else
			player.drawShip(buf, damage2);
	}

	/***************************************************************************
	 * Moves all player missiles.
	 ***************************************************************************/
	private void missileMovement() {
		ArrayList<Missile> rightMis = player.getMissiles(2);
		ArrayList<Missile> leftMis = player.getMissiles(1);
		ArrayList<Missile> center = player.getMissiles(3);
		Missile simple = player.getMissile();
		if (rightMis != null)
			missileMover(rightMis, torpedo);
		if (leftMis != null)
			missileMover(leftMis, torpedo);
		if (center != null)
			missileMover(center, bigTorp);
		if (simple != null)
			player.simpleMover(buf, alienHit(player.getMissile()),
					torpedo);
	}

	/***************************************************************************
	 * Moves aliens, initiates explosions when aliens hit the player,
	 * and ends the game if the alien ships destroy the player ship.
	 ***************************************************************************/
	private void alienMover() {
		Graphics g = display.getGraphics();
		for (int i = 0; i < aliens.length; i++) {
			if (aliens[i] != null) {
				aliens[i].move(buf, fighter, eMissile, display
						.getHeight());
				if (player.isHit(aliens[i])) {
					shipExplosion = new Explosion(
							player.getXpos() - 10, player.getYpos(),
							shipExplode);
					aliens[i].remove();
					lives--;
					labelUpdate();
				}

				// Ends the game and calls a total reset.
				if (lives <= 0) {
					buf.drawImage(star, 0, 0, null);
					buf.drawImage(lose, display.getWidth() / 3, display
							.getHeight() / 4, display);

					// Final Animation
					for (int j = 0; j < shipExplode.length; j++) {
						shipExplosion.draw(buf, j);
						paint(g);
						pause(20);
					}
					score = 0;
					labelUpdate();
					lives = 3;
					pause(5000);
				}
			}
		}
	}

	/***************************************************************************
	 * Updates all explosion animations.
	 ***************************************************************************/
	private void explosionUpdate() {
		for (int i = 0; i < explode.length; i++) {
			if (explode[i] != null) {
				explode[i].draw(buf);
				if (explode[i].getExpStatus())
					explode[i] = null;
			}
		}
		if (shipExplosion != null) {
			shipExplosion.draw(buf);
			if (shipExplosion.getExpStatus())
				shipExplosion = null;
		}
	}

	/***************************************************************************
	 * Moves the rapid fire missiles from the player's ship. The movers
	 * require information from the alien ship to operate correctly.
	 * 
	 * @param playMis
	 *            The missile array of missiles to be moved.
	 * @param type
	 *            The image type for the weapon.
	 ***************************************************************************/
	private void missileMover(ArrayList<Missile> playMis,
			BufferedImage type) {
		for (int i = 0; i < playMis.size(); i++) {
			if (playMis.get(i) != null) {
				playMis.get(i).move(buf, type);
				if (alienHit(playMis.get(i)))
					playMis.set(i, null);
				else if (playMis.get(i).isOffScreen())
					playMis.set(i, null);
			}
		}
	}

	/***************************************************************************
	 * Paints the buffer image to the canvas.
	 * 
	 * @param g
	 *            The canvas graphics.
	 ***************************************************************************/
	private void paint(Graphics g) {
		g.drawImage(buffer, 0, 0, display);
	}

	/***************************************************************************
	 * Tests to see if the alien has been hit.
	 * 
	 * @param playMis
	 * @return
	 ***************************************************************************/
	private boolean alienHit(Missile playMis) {
		for (int i = 0; i < aliens.length; i++) {
			if (aliens[i] != null) {
				if (aliens[i].isHit(buf, playMis)) {
					explode[i] = new Explosion(aliens[i].getX()
							- explosions[0].getWidth() / 4, aliens[i]
							.getY(), explosions);
					aliens[i].remove();
					aliens[i] = null;
					deadAliens++;
					score += 10;
					labelUpdate();
					return true;
				}
			}
		}
		return false;
	}

	/***************************************************************************
	 * The action performed methods for buttons being pushed.
	 ***************************************************************************/
	public void actionPerformed(ActionEvent e) {
		JComponent comp = (JComponent) e.getSource();
		if (comp == quit)
			quitButtonResponse();
		else if (comp == play) {
			playButtonResponse();
		} else if (comp == about)
			JOptionPane
					.showMessageDialog(
							null,
							"Author: Sam Eary  Version: 1.1   Images courtesy of NASA",
							"About", JOptionPane.INFORMATION_MESSAGE);

		else if (comp == difficulty && diffFrame == null) {
			difficultyResponse();
			diffFrame = null;
		} else
			actionPerfExtended(comp);
	}

	/***************************************************************************
	 * Additional action performed method for components of the
	 * difficulty frame.
	 * 
	 * @param comp
	 *            The action component.
	 ***************************************************************************/
	private void actionPerfExtended(JComponent comp) {
		if (comp == easy)
			difficultyLevelSetup(100, 22);
		else if (comp == medium)
			difficultyLevelSetup(50, 55);
		else if (comp == hard)
			difficultyLevelSetup(10, 77);
		else if (comp == impossible)
			difficultyLevelSetup(0, 88);
		else if (comp == ok && okEnable) {
			confirmDiff();
			playButtonResponse();
		} else if (comp == close) {
			diffFrame.dispose();
			diffFrame = null;
			if (player != null)
				start();
		}
	}

	/***************************************************************************
	 * Pauses the game indefinitely.
	 ***************************************************************************/
	private void pauseGame() {
		gameLoop = null;
		Cursor defaultCursor = Cursor.getDefaultCursor();
		display.setCursor(defaultCursor);
		buf.drawImage(pause, display.getWidth() / 6, display
				.getHeight() / 4, display);
		paint(display.getGraphics());
	}

	/***************************************************************************
	 * Provides dialog box for the quit button on the file menu.
	 ***************************************************************************/
	private void quitButtonResponse() {
		int reply = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to quit?", "Quitting?",
				JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	/***************************************************************************
	 * Changes the player's ship location, and keeps it on the screen.
	 ***************************************************************************/
	public void mouseMoved(MouseEvent e) {
		if (enable) {
			if (e.getX() > display.getWidth() - ship.getWidth())
				player.setXpos(display.getWidth() - ship.getWidth());

			// Stops the ship from moving off the right side of the
			// screen.
			else if (player.getXpos() >= 0
					&& player.getXpos() <= display.getWidth()) {
				player.setXpos(e.getX());
				if (shipExplosion != null)
					shipExplosion.setxPos(e.getX() - 10);
			}
		}
	}

	/***************************************************************************
	 * Responds to the mouse being clicked. Provides fire commands for
	 * the missiles.
	 ***************************************************************************/
	public void mouseClicked(MouseEvent e) {
		if (enable)
			if (e.getButton() == MouseEvent.BUTTON1)
				player.fire(1, torpedo);
		if (e.getButton() == MouseEvent.BUTTON3)
			player.fire(2, torpedo);
		if (e.getButton() == MouseEvent.BUTTON2) {
			player.fire(1, torpedo);
			player.fire(2, torpedo);
		}
	}

	/***************************************************************************
	 * Sleeps the thread so the game will be playable.
	 * 
	 * @param mSec
	 *            Time to sleep in milliseconds.
	 ***************************************************************************/
	private void pause(int mSec) {
		try {
			Thread.sleep(mSec);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, "Interuppted Error?",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/***************************************************************************
	 * Resets the game for a new game or round.
	 ***************************************************************************/
	private void reset() {
		randomImageLoader();
		gameLoop = null;
		display.repaint();
		clearBuffer();
		startup();
		start();
	}

	/***************************************************************************
	 * Starts the thread that controls the gameloop so that there is
	 * constant movement.
	 ***************************************************************************/
	public void run() {
		Thread t = Thread.currentThread();
		while (t == gameLoop) {
			playRound();
		}
	}

	/***************************************************************************
	 * The key responder that listens for the keyboard input and
	 * responds by firing missiles or altering the gamestate.
	 ***************************************************************************/
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (enable) {
			keyFireControls(keyCode);
			if (keyCode == KeyEvent.VK_P) {
				if (gameLoop == null)
					start();
				else
					pauseGame();
			}
			keyMoveShip(keyCode);
		}
		if (keyCode == KeyEvent.VK_ESCAPE)
			System.exit(0);
		else if (keyCode == KeyEvent.VK_ENTER)
			playButtonResponse();
		else if (keyCode == KeyEvent.VK_D && diffFrame == null)
			difficultyResponse();
	}

	/***************************************************************************
	 * An extension of the keyPressed() method to move the ship itself.
	 * 
	 * @param keyCode
	 *            The code for the key input form the keyboard.
	 ***************************************************************************/
	private void keyMoveShip(int keyCode) {
		int x = player.getXpos();
		if (x > display.getWidth() - ship.getWidth())
			player.setXpos(display.getWidth() - ship.getWidth());
		else if (x < 0)
			player.setXpos(0);
		else if (x >= 0 && x <= display.getWidth()) {
			if (keyCode == KeyEvent.VK_LEFT) {
				player.setXpos(x - 10);
				if (shipExplosion != null)
					shipExplosion.setxPos(x - 20);
			} else if (keyCode == KeyEvent.VK_RIGHT) {
				player.setXpos(x + 10);
				if (shipExplosion != null)
					shipExplosion.setxPos(x);
			}
		}
	}

	/***************************************************************************
	 * An extension of the keyPressed() method to fire the ships
	 * weapons.
	 * 
	 * @param keyCode
	 *            The code for the key input form the keyboard.
	 ***************************************************************************/
	private void keyFireControls(int keyCode) {

		// These are all if's so that weapons can be fired at the same
		// time
		if (keyCode == KeyEvent.VK_CONTROL)
			player.fire(1, torpedo);
		if (keyCode == KeyEvent.VK_ALT)
			player.fire(2, torpedo);
		if (keyCode == KeyEvent.VK_X)
			player.fire(3, bigTorp);
		if (keyCode == KeyEvent.VK_SPACE) {
			player.fire(1, torpedo);
			player.fire(2, torpedo);
		}
		if (keyCode == KeyEvent.VK_A) {
			player.fire(1, torpedo);
			player.fire(2, torpedo);
			player.fire(3, bigTorp);
			player.fireMissile(torpedo);
		}
		if (keyCode == KeyEvent.VK_F)
			player.fireMissile(torpedo);
	}

	/***************************************************************************
	 * The process for starting a new game when one of the play buttons
	 * was pushed.
	 ***************************************************************************/
	private void playButtonResponse() {
		if (diffFrame != null) {
			diffFrame.dispose();
			diffFrame = null;
		}
		reset();
		lives = 3;
		enable = true;
		labelUpdate();
	}

	/***************************************************************************
	 * Updates the statistics label on the bottom edge of the GUI.
	 ***************************************************************************/
	private void labelUpdate() {
		deadAlien.setText("Aliens Remain: "
				+ (aliens.length - deadAliens));
		scoreLab.setText("Score: " + score);
		life.setText("Lives Remain: " + lives);
	}

	/***************************************************************************
	 * Sets the difficulty level for the game.
	 * 
	 * @param diffLevel
	 *            The parameter value for the pause method. It affects
	 *            speed.
	 * @param alienTot
	 *            The number of aliens.
	 ***************************************************************************/
	private void difficultyLevelSetup(int diffLevel, int alienTot) {
		this.tempDiff = diffLevel;
		this.tempAlienCount = alienTot;
		okEnable = true;
	}

	/***************************************************************************
	 * Confirms the difficulty selection after the ok button is pressed.
	 ***************************************************************************/
	private void confirmDiff() {
		diffLevel = tempDiff;
		alienTotal = tempAlienCount;
	}

	/***************************************************************************
	 * Not Required for this program.
	 ***************************************************************************/
	public void mouseDragged(MouseEvent arg0) {
	}

	/***************************************************************************
	 * Not Required for this program.
	 ***************************************************************************/
	public void mouseEntered(MouseEvent arg0) {
	}

	/***************************************************************************
	 * Not Required for this program.
	 ***************************************************************************/
	public void mouseExited(MouseEvent arg0) {
	}

	/***************************************************************************
	 * Not Required for this program.
	 ***************************************************************************/
	public void mousePressed(MouseEvent arg0) {
	}

	/***************************************************************************
	 * Not Required for this program.
	 ***************************************************************************/
	public void mouseReleased(MouseEvent arg0) {
	}

	/***************************************************************************
	 * Not Required for this program.
	 ***************************************************************************/
	public void keyReleased(KeyEvent arg0) {
	}

	/***************************************************************************
	 * Not Required for this program.
	 ***************************************************************************/
	public void keyTyped(KeyEvent arg0) {
	}

	/***************************************************************************
	 * The main method that instantiates the Alien Attack game.
	 * 
	 * @param args
	 *            The command line inputs.
	 ***************************************************************************/
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Game g = new Game();
	}
}
