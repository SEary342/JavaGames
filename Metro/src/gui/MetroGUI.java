package gui;

import game.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import utility.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;

/**************************************************************************
 * The GUI display interface for the Metro game.
 * 
 * @author Sam Eary and Tyler Blanchard
 * @version 1.0
 **************************************************************************/
public class MetroGUI extends JFrame implements ActionListener {

	/** The total number of different game tiles. */
	private static final int NUMBEROFTILES = 24;

	/** The default directory for saved games. */
	private static final String BASE = "Games/";

	/** The serial ID of the class. */
	private static final long serialVersionUID = 2040515915386500797L;

	/** The maximum board size. */
	private static final int MAX = 10;

	/** The maximum number of players. */
	private static final int MAX_PLAYERS = 7;

	/** The default font for all text. */
	private static final Font standard = new Font(
			"Copperplate Gothic Bold", Font.BOLD, 14);

	/** The standard size of any side of an image. */
	protected static final int DIM = 64;

	/** The dimension representing the size of an image panel. */
	protected static final Dimension PANEL_SIZE = new Dimension(DIM,
			DIM);

	/** The orientation value of the top terminals. */
	private static final int TOP = 1;

	/** The orientation value of the right terminals. */
	private static final int RIGHT = 2;

	/** The orientation value of the bottom terminals. */
	private static final int BOTTOM = 3;

	/** The orientation value of the left terminals. */
	private static final int LEFT = 4;

	/** The new game window. */
	private JFrame newGameWindow;

	/** The Metro tile fields. */
	private MetroTile[][] tiles;

	/** A list of the board's {@code TerminalTiles}. */
	private ArrayList<TerminalTile> terminals;

	/** Game controls for the drop down menus. */
	private JMenuItem quit, newGame, about, restart, save, load;

	/** Control buttons for the newGameWindow */
	private JButton ok, close, loadgame;

	/** The panel for the main frame */
	private JPanel gamePanel;

	/** The control class for the game. */
	private MetroControl control;

	/** The panels for the new game window. */
	private JPanel gameOptions, commands, labels;

	/** The {@code JComboBoxs} for the new game window. */
	private JComboBox players, size, scores;

	/** Other panels for the frame. */
	private JPanel right, scorePanel, scorePanelBottom, left;

	/** The exit button for the frame. */
	private JButton exit;

	/** {@code ArrayLists} to store loaded images. */
	private ArrayList<BufferedImage> tile, terminalImages;

	/** The current active {@code MetroTile}. */
	private MetroTile current;

	/** {@code MetroTile} background image. */
	private BufferedImage background;

	/** The {@code MetroTile} for each player. */
	private MetroTile[] playerTiles;

	/** The labels for each player's score. */
	private JLabel[] playerScores;

	/** The image of the corner terminals. */
	private BufferedImage cornerTile;

	/**************************************************************************
	 * Constructor for the MetroGUI class. It will start the new game
	 * window and primary frame setup process.
	 **************************************************************************/
	public MetroGUI() {
		super("Metro");
		JLabel metro = new JLabel(
				"<html>M<br><br>E<br><br>T<br><br>R<br>"
						+ "<br>O</html>");
		control = new MetroControl();
		right = new JPanel();
		scorePanel = new JPanel();
		scorePanelBottom = new JPanel();
		left = new JPanel();
		panelSetup(left);
		panelSetup(scorePanelBottom);
		panelSetup(right);
		panelSetup(scorePanel);
		left.add(metro);
		format(metro);
		left.setPreferredSize(PANEL_SIZE);
		variableInstantiator();

		// Sets up the frame.
		setupMenus();
		setLayout(new BorderLayout());
		add(scorePanel, BorderLayout.SOUTH);
		add(right, BorderLayout.EAST);
		add(gamePanel, BorderLayout.CENTER);
		add(left, BorderLayout.WEST);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setUndecorated(true);

		// Sets up and displays the new game window.
		newGameSetup();
	}

	/**************************************************************************
	 * Instantiates most of the instance variables necessary to create
	 * the frame, the menus and the new game window.
	 **************************************************************************/
	private void variableInstantiator() {

		// Instantiates primary GUI variables.
		gamePanel = new JPanel();
		format(gamePanel);
		exit = new JButton();
		loadImages();

		// Instantiates the variables necessary to the menus.
		newGame = new JMenuItem("New Game");
		save = new JMenuItem("Save");
		load = new JMenuItem("Load");
		quit = new JMenuItem("Quit");
		restart = new JMenuItem("Restart");
		about = new JMenuItem("About");

		// Instantiates variables for the new game window.
		gameOptions = new JPanel();
		commands = new JPanel();
		labels = new JPanel();
		newGameWindow = new JFrame("New Game");
		playerSetup();
		scoreTypeSetup();
		size = new JComboBox(sizeListGenerator(2, MAX));
		ok = new JButton("OK");
		close = new JButton("Close");
		loadgame = new JButton("Load");
	}

	/**************************************************************************
	 * Loads all images necessary for operation of the GUI.
	 **************************************************************************/
	private void loadImages() {
		char tileId = (char) MetroEngine.ASCIIJUMP;
		tile = new ArrayList<BufferedImage>();
		terminalImages = new ArrayList<BufferedImage>();
		try {

			// Loads the MetroTile images.
			for (int i = 0; i < NUMBEROFTILES; i++) {
				tile.add(ImageIO.read(new File(String.valueOf(tileId)
						+ ".jpg")));
				tileId += (char) 1;
			}

			// Loads the TerminalTile images
			for (int j = 0; j < MAX_PLAYERS; j++)
				terminalImages.add(ImageIO.read(new File(String
						.valueOf(j)
						+ ".jpg")));

			// Misc images.
			background = ImageIO.read(new File("background.jpg"));
			cornerTile = ImageIO.read(new File("cornertile.jpg"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Image Load Error",
					"Program Terminating", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	/**************************************************************************
	 * Generates an array for each possible number of players.
	 **************************************************************************/
	private void playerSetup() {
		String[] playerList = playerListGenerator(2, 5);
		players = new JComboBox(playerList);
		players.addActionListener(new ActionListener() {

			/****************************************************************
			 * Performs updates to the board size selection when player
			 * numbers change.
			 ****************************************************************/
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				updateBoardSize(Integer.parseInt(cb.getSelectedItem()
						.toString().substring(0, 1)));
			}
		});
	}

	/**************************************************************************
	 * Generates an array for the score {@code JComboBox}.
	 **************************************************************************/
	private void scoreTypeSetup() {
		String[] scoreList = { "Simple", "Crossover", "Time-Placement" };
		scores = new JComboBox(scoreList);
	}

	/**************************************************************************
	 * Updates the size {@code JComboBox}.
	 * 
	 * @param playerNum
	 *            The number of players in the game.
	 **************************************************************************/
	private void updateBoardSize(int playerNum) {
		String[] sizes = sizeListGenerator(playerNum, MAX);
		size.removeAllItems();
		for (int i = 0; i < sizes.length; i++)
			size.addItem(sizes[i]);
	}

	/**************************************************************************
	 * Generates the array of player strings for the player {@code
	 * JComboBox}.
	 * 
	 * @param initial
	 *            The starting player.
	 * @param length
	 *            The length of the array.
	 * @return the array of strings representing the players.
	 **************************************************************************/
	private String[] playerListGenerator(int initial, int length) {
		String[] out = new String[length];
		for (int i = 0; i < out.length; i++) {
			out[i] = initial + " Players";
			initial++;
		}
		return out;
	}

	/**************************************************************************
	 * Generates the array of size strings for the size {@code
	 * JComboBox}.
	 * 
	 * @param low
	 *            The starting size number.
	 * @param high
	 *            The last size number.
	 * @return the array of strings representing the sizes.
	 **************************************************************************/
	private String[] sizeListGenerator(int low, int high) {
		int num = low;
		String[] out = new String[high - low + 1];
		for (int i = 0; i < out.length; i++) {
			out[i] = num + " x " + num;
			num++;
		}
		return out;
	}

	/**************************************************************************
	 * Sets up the menu functions of the primary frame.
	 **************************************************************************/
	private void setupMenus() {
		JMenuBar menu = new JMenuBar();
		JMenu menuList = new JMenu("Menu");
		JMenu options = new JMenu("Options");
		format(menu);
		format(menuList);
		format(options);
		setJMenuBar(menu);
		menu.add(menuList);

		// Creates the menu.
		menuList.add(newGame);
		menuList.add(save);
		menuList.add(load);
		menuList.add(restart);
		menuList.add(quit);

		// Creates the options menu.
		menu.add(options);
		options.add(about);
		menu.add(Box.createHorizontalGlue());
		menu.add(exit);
		menuFormatter();
	}

	/**************************************************************************
	 * Formats the the permanent parts of the menu-bar.
	 **************************************************************************/
	private void menuFormatter() {
		format(newGame);
		format(restart);
		format(load);
		format(save);
		format(quit);
		format(about);

		// Setting up the exit button
		exit.setText("Exit");
		format(exit);
		exit.setFocusable(false);
	}

	/**************************************************************************
	 * Sets up all of the Metro fields in the game board after a game
	 * has been loaded or created.
	 **************************************************************************/
	private void mainFieldSetup() {
		int rows = control.getRows();
		int cols = control.getCols();
		tiles = new MetroTile[rows][cols];
		terminals = new ArrayList<TerminalTile>();
		gamePanel.setLayout(new GridLayout(rows + 2, cols + 2));

		// Generates the game board with terminals
		terminalRowAdder(cols, TOP);
		for (int i = 0; i < tiles.length; i++) {
			terminalAdder(LEFT);
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[i][j] = new MetroTile(new Position(i, j),
						background);
				tileListenerAdder(i, j);
				tiles[i][j].setPreferredSize(PANEL_SIZE);
				tiles[i][j].setBorder(BorderFactory
						.createLineBorder(Color.WHITE));
				format(tiles[i][j]);
				gamePanel.add(tiles[i][j]);
			}
			terminalAdder(RIGHT);
		}
		terminalRowAdder(cols, BOTTOM);
	}

	/**************************************************************************
	 * Adds listeners to {@code MetroTiles}.
	 * 
	 * @param i
	 *            The i coordinate of the tile.
	 * @param j
	 *            The j coordinate of the tile.
	 **************************************************************************/
	private void tileListenerAdder(int i, int j) {
		tiles[i][j].addMouseMotionListener(new MouseMotionAdapter() {

			/****************************************************************
			 * Provides actions when the mouse moves over a tile.
			 ****************************************************************/
			public void mouseMoved(MouseEvent e) {
				metroMotionActions(e);
			}
		});
		tiles[i][j].addMouseListener(new MouseAdapter() {

			/****************************************************************
			 * Provides actions when a tile is clicked.
			 ****************************************************************/
			public void mouseClicked(MouseEvent e) {
				tileAdder(e);
			}
		});
	}

	/**************************************************************************
	 * The actions performed when the mouse moves over a {@code
	 * MetroTile}. All other non-set tiles are cleared and the current
	 * one displays the image preview.
	 * 
	 * @param e
	 *            The {@code MouseEvent} that activated this method.
	 **************************************************************************/
	private void metroMotionActions(MouseEvent e) {
		current = (MetroTile) e.getSource();
		if (!current.getSet())
			current.previewImage(getCurrentActiveTile());
		clearTiles();
	}

	/**************************************************************************
	 * Adds tiles to the game board and responds to exceptions thrown by
	 * the engine.
	 * 
	 * @param e
	 *            The {@code MouseEvent} that activated this method.
	 **************************************************************************/
	private void tileAdder(MouseEvent e) {
		MetroTile temp = (MetroTile) e.getSource();
		BufferedImage image = getCurrentActiveTile();
		try {
			if (!temp.getSet()) {
				control.setTile(temp.getPosition());
				temp.confirmSet(image);
				updatePlayerTileImages();
				updateScores();
				if (control.isComplete())
					endGame();
				restart.setEnabled(true);
				save.setEnabled(true);
			}
		} catch (MiddleOfNowhereException e1) {
			JOptionPane.showMessageDialog(null,
					"You must place tiles so that they are"
							+ " connected to something.",
					"Tile Placement Error",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (CutoffException e2) {
			JOptionPane.showMessageDialog(null,
					"That cuts off a terminal."
							+ "  Look for a different place "
							+ "to put your tile.",
					"Tile Placement Error",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**************************************************************************
	 * Provides a dialog box for when the game has been completed.
	 **************************************************************************/
	private void endGame() {
		int reply = JOptionPane.showConfirmDialog(null,
				"Do you want to play again?", "Game Over",
				JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			setVisible(false);
			startGame();
		} else
			System.exit(0);
	}

	/**************************************************************************
	 * Adds a terminal to the game board.
	 * 
	 * @param orientation
	 *            The orientation number of the tile.
	 **************************************************************************/
	private void terminalAdder(int orientation) {
		BufferedImage terminalImage;
		TerminalTile terminal;
		terminals.add(new TerminalTile());
		terminal = terminals.get(terminals.size() - 1);
		terminalImage = terminalImages.get(control
				.getStationOwner(terminals.size() - 1));
		gamePanel.add(terminal);
		format(terminal);
		terminal.addMouseMotionListener(new MouseMotionAdapter() {

			/****************************************************************
			 * Provides a procedure that occurs when the cursor passes
			 * over a terminal.
			 ****************************************************************/
			public void mouseMoved(MouseEvent e) {
				movedTerminal();
			}
		});
		terminal.setImage(getRotatedImage(orientation, terminalImage));
	}

	/**************************************************************************
	 * Rotates a terminal image so that it is facing in the correct
	 * direction on the game board.
	 * 
	 * @param orientation
	 *            The orientation integer.
	 * @param terminalImage
	 *            The image to be rotated.
	 * @return the rotated image.
	 **************************************************************************/
	private BufferedImage getRotatedImage(int orientation,
			BufferedImage terminalImage) {
		AffineTransform tx = new AffineTransform();
		AffineTransformOp op;

		// Rotates the image.
		switch (orientation) {
		case TOP:
			tx.rotate(0, terminalImage.getWidth() / 2, terminalImage
					.getHeight() / 2);
			break;
		case RIGHT:
			tx.rotate(Math.PI / 2.0, terminalImage.getWidth() / 2,
					terminalImage.getHeight() / 2);
			break;
		case BOTTOM:
			tx.rotate(Math.PI, terminalImage.getWidth() / 2,
					terminalImage.getHeight() / 2);
			break;
		case LEFT:
			tx.rotate(3 * Math.PI / 2.0, terminalImage.getWidth() / 2,
					terminalImage.getHeight() / 2);
			break;
		}
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(terminalImage, null);
	}

	/**************************************************************************
	 * Adds terminal rows to the top and the bottom of the game board.
	 * 
	 * @param cols
	 *            The number of columns in the game board.
	 * @param orientation
	 *            The orientation integer of the row.
	 **************************************************************************/
	private void terminalRowAdder(int cols, int orientation) {

		// These are the corner tiles that have no terminals displayed.
		TerminalTile blank1 = new TerminalTile();
		TerminalTile blank2 = new TerminalTile();
		blank1.addMouseMotionListener(new MouseMotionAdapter() {

			/****************************************************************
			 * Initiates the board clearing sequence for when the cursor
			 * leaves the game board.
			 ****************************************************************/
			public void mouseMoved(MouseEvent e) {
				movedTerminal();
			}
		});
		blank2.addMouseMotionListener(new MouseMotionAdapter() {

			/****************************************************************
			 * Initiates the board clearing sequence for when the cursor
			 * leaves the game board.
			 ****************************************************************/
			public void mouseMoved(MouseEvent e) {
				movedTerminal();
			}
		});
		blank1.setImage(cornerTile);
		blank2.setImage(cornerTile);
		gamePanel.add(blank1);
		for (int i = 0; i < cols; i++)
			terminalAdder(orientation);
		gamePanel.add(blank2);
	}

	/**************************************************************************
	 * Clears the game board when terminals have a cursor over them.
	 **************************************************************************/
	private void movedTerminal() {
		current = null;
		clearTiles();
	}

	/**************************************************************************
	 * Formats any component so that it will match the overall design
	 * scheme.
	 * 
	 * @param comp
	 *            The component being formatted.
	 **************************************************************************/
	private void format(Component comp) {
		comp.setFont(standard);
		comp.setBackground(new Color(56, 29, 10));
		comp.setForeground(Color.WHITE);
	}

	/**************************************************************************
	 * Sets the color of the specified panel to brown and sets the
	 * panel's layout.
	 * 
	 * @param panel
	 *            The panel to be formatted.
	 **************************************************************************/
	private void panelSetup(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		panel.setBackground(new Color(34, 17, 6));
	}

	/**************************************************************************
	 * Resets non-permanent aspects of the frame each time a new game is
	 * loaded.
	 **************************************************************************/
	private void resetFrame() {
		restart.setEnabled(false);
		right.removeAll();
		gamePanel.removeAll();
		scorePanelBottom.removeAll();
		scorePanel.removeAll();
		restart.setEnabled(false);
	}

	/**************************************************************************
	 * Sets up the new game frame with mutually exclusive radio buttons.
	 **************************************************************************/
	private void newGameSetup() {
		JLabel newGameLabel = new JLabel("New Game");
		JLabel playerLab = new JLabel("Players:");
		JLabel boardSize = new JLabel("Board Size:");
		JLabel scoreTypes = new JLabel("Score Type:");
		format(scoreTypes);
		format(newGameLabel);
		format(playerLab);
		format(boardSize);
		newGameSetupFormat();

		// Adds the buttons and labels to the panels.
		labels.add(newGameLabel);
		commands.add(playerLab);
		commands.add(boardSize);
		commands.add(scoreTypes);
		commands.add(players);
		commands.add(size);
		commands.add(scores);
		commands.add(ok);
		commands.add(close);
		commands.add(loadgame);

		// Makes the frame visible.
		newGameWindow.setUndecorated(true);
		newGameWindow.pack();
		newGameWindow.setLocationRelativeTo(null);
		newGameWindow.setVisible(true);
	}

	/**************************************************************************
	 * Sets the layouts, colors and adds the panels to the new game
	 * window.
	 **************************************************************************/
	private void newGameSetupFormat() {

		// Layouts.
		newGameWindow.setLayout(new BorderLayout());
		gameOptions.setLayout(new FlowLayout());
		commands.setLayout(new GridLayout(3, 3));
		labels.setLayout(new FlowLayout());

		// Adding the panels to the frame.
		newGameWindow.add(gameOptions, BorderLayout.CENTER);
		newGameWindow.add(commands, BorderLayout.SOUTH);
		newGameWindow.add(labels, BorderLayout.NORTH);
		buttonSetup();
	}

	/**************************************************************************
	 * Sets up the permanent game buttons.
	 **************************************************************************/
	private void buttonSetup() {

		// New game window listeners.
		ok.addActionListener(this);
		close.addActionListener(this);
		loadgame.addActionListener(this);

		// Action listeners for the frame.
		quit.addActionListener(this);
		restart.addActionListener(this);
		newGame.addActionListener(this);
		about.addActionListener(this);
		exit.addActionListener(this);
		save.addActionListener(this);
		load.addActionListener(this);

		// Formatting components to match the color and design scheme.
		format(commands);
		format(gameOptions);
		format(labels);
		format(ok);
		format(close);
		format(players);
		format(size);
		format(scores);
		format(loadgame);
	}

	/**************************************************************************
	 * Performs all actions necessary for user interaction with the
	 * game.
	 * 
	 * @param e
	 *            The event that called when an action listener is
	 *            activated.
	 **************************************************************************/
	public void actionPerformed(ActionEvent e) {
		JComponent comp = (JComponent) e.getSource();
		if (comp == restart)
			restartButtonResponse();
		else if (comp == save)
			saveGameResponse();
		else if (comp == load || comp == loadgame)
			try {
				loadGameResponse();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null,
						"Error loading file!", "Load Error",
						JOptionPane.ERROR_MESSAGE);
			}
		else if (comp == quit || comp == exit)
			quitButtonResponse();
		else if (comp == newGame)
			displayNewGameWindow();
		else if (comp == about)
			JOptionPane.showMessageDialog(null,
					"Author: Sam Eary and Tyler Blanchard "
							+ "Version: 4/18/2010", "About",
					JOptionPane.INFORMATION_MESSAGE);
		else if (comp == ok || comp == close)
			newGameActions(comp);
	}

	/**************************************************************************
	 * Displays the load game prompt.
	 * 
	 * @throws FileNotFoundException
	 *             if the file was not found.
	 * @throws ParseException
	 *             if the file cannot be loaded.
	 **************************************************************************/
	private void loadGameResponse() throws FileNotFoundException,
			ParseException {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(BASE));
		FileFilter filter = new FileFilter() {

			/****************************************************************
			 * Tests to see if the file types can be displayed.
			 ****************************************************************/
			public boolean accept(File f) {
				return f.isDirectory()
						|| f.getName().toLowerCase().endsWith(".game");
			}

			/****************************************************************
			 * The String representing the file types.
			 ****************************************************************/
			public String getDescription() {
				return ".game files";
			}
		};
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(newGameWindow);

		// Executes the game load sequence.
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			control.loadGame(fc.getSelectedFile().getPath());
			setVisible(false);
			standardStart();
			save.setEnabled(true);
			restart.setEnabled(true);
			setTilesFromLoad();
		}
	}

	/**************************************************************************
	 * Initializes the standard game start process.
	 **************************************************************************/
	private void standardStart() {

		// +1 is for the draw pile.
		playerTiles = new MetroTile[control.getNumOfPlayers() + 1];
		closeNewGameWindow();
		resetFrame();
		mainFieldSetup();
		sidePanelSetup();
		scorePanelSetup();
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**************************************************************************
	 * Provides the save game dialog.
	 **************************************************************************/
	private void saveGameResponse() {
		JFileChooser fc = new JFileChooser();
		String file;
		fc.setCurrentDirectory(new File(BASE));
		int returnVal = fc.showSaveDialog(newGameWindow);
		if (returnVal == JFileChooser.APPROVE_OPTION)
			try {
				file = fc.getSelectedFile().getPath();
				if (file.substring(file.length() - 5).equals(".game"))
					control.saveGame(file);
				else
					control.saveGame(file + ".game");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Error saving file!", "Save Error",
						JOptionPane.ERROR_MESSAGE);
			}
	}

	/**************************************************************************
	 * Provides a y/n dialog when the user selects the restart button.
	 **************************************************************************/
	private void restartButtonResponse() {
		int reply = JOptionPane.showConfirmDialog(null,
				"Are you sure that you want to restart"
						+ " and lose all your progress?",
				"Restarting?", JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			setVisible(false);
			control.newGame(control.getNumOfPlayers(), control
					.getRows(), control.getCols(), control
					.getScoreType());
			save.setEnabled(false);
			standardStart();
		}
	}

	/**************************************************************************
	 * Provides the y/n dialog when the user pushes either the exit or
	 * the quit button.
	 **************************************************************************/
	private void quitButtonResponse() {
		int reply = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to quit?", "Quitting?",
				JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	/**************************************************************************
	 * Performs actions related to the new game window.
	 * 
	 * @param comp
	 *            The component that was activated.
	 **************************************************************************/
	private void newGameActions(Component comp) {
		if (comp == ok)
			startGame();
		else if (comp == close) {
			if (control.inProgress()) {
				closeNewGameWindow();
				setVisible(true);
			} else
				System.exit(0);
		}
	}

	/**************************************************************************
	 * Starts a new game from the game setting input into the new game
	 * window.
	 **************************************************************************/
	private void startGame() {
		int sizeNum = Integer.parseInt(size.getSelectedItem()
				.toString().substring(0, 2).trim());
		int numOfPlayers = Integer.parseInt(players.getSelectedItem()
				.toString().substring(0, 1));
		int scoreType = scores.getSelectedIndex();
		control.newGame(numOfPlayers, sizeNum, sizeNum, scoreType);
		save.setEnabled(false);
		standardStart();
	}

	/**************************************************************************
	 * Sets up the side panel that contains the piles of player tiles
	 * and the draw pile.
	 **************************************************************************/
	private void sidePanelSetup() {
		JLabel player = null;
		JPanel playerPanel = null;
		right.setLayout(new GridLayout(control.getCols(), 2));

		// Creates each players pile
		for (int i = 0; i < playerTiles.length - 1; i++) {
			playerPanel = new JPanel(new FlowLayout());
			player = new JLabel("Player " + Integer.toString(i + 1)
					+ ": ");
			sideSubPanelSetup(playerPanel, player, i);
		}
		playerPanel = new JPanel(new FlowLayout());
		player = new JLabel("Draw Pile: ");
		sideSubPanelSetup(playerPanel, player, playerTiles.length - 1);
		updatePlayerTileImages();
	}

	/**************************************************************************
	 * Creates a label and {@code MetroTile} for each players pile of
	 * tiles.
	 * 
	 * @param panel
	 *            The panel to be created.
	 * @param label
	 *            The label to be added to the panel.
	 * @param index
	 *            The index of the {@code playerTiles} array that
	 *            contains the {@code MetroTiles} for each player.
	 **************************************************************************/
	private void sideSubPanelSetup(JPanel panel, JLabel label, int index) {
		format(label);
		setPlayerScoreColors(index, label);
		panel.add(label);
		playerTiles[index] = new MetroTile(background);
		playerTiles[index].setPreferredSize(PANEL_SIZE);
		playerTiles[index].setId(index);
		panel.add(playerTiles[index]);
		panel.setPreferredSize(new Dimension(3 * DIM, DIM));
		panelSetup(panel);
		right.add(panel);
		playerTiles[index].addMouseListener(new MouseAdapter() {

			/****************************************************************
			 * Activates or deactivates the draw pile.
			 ****************************************************************/
			public void mouseClicked(MouseEvent e) {
				if (((MetroTile) e.getSource()).getId() == control
						.getNumOfPlayers())
					control.activateDrawPile();
				else if (((MetroTile) e.getSource()).getId() == control
						.getDrawPlayer())
					control.deactivateDrawPile();
				updatePlayerTileImages();
			}
		});
	}

	/**************************************************************************
	 * Sets up the score panel at the bottom of the frame.
	 **************************************************************************/
	private void scorePanelSetup() {
		JLabel score = null;
		switch (control.getScoreType()) {
		case 0:
			score = new JLabel("Simple Scores:");
			break;
		case 1:
			score = new JLabel("Crossover Scores:");
			break;
		case 2:
			score = new JLabel("Time-Placement Scores:");
			break;
		}
		playerScores = new JLabel[control.getNumOfPlayers()];
		format(score);
		score.setHorizontalAlignment(JLabel.CENTER);
		scorePanelBottom.setLayout(new FlowLayout(FlowLayout.CENTER));
		for (int i = 0; i < playerScores.length; i++) {
			playerScores[i] = new JLabel("test");
			format(playerScores[i]);
			scorePanelBottom.add(playerScores[i]);
			setPlayerScoreColors(i, playerScores[i]);
		}
		scorePanel.setLayout(new GridLayout(2, 1));
		scorePanel.add(score);
		scorePanel.add(scorePanelBottom);
		updateScores();
	}

	/**************************************************************************
	 * Sets the player colors of a {@code JLabel}.
	 * 
	 * @param playerNum
	 *            The player number of the label.
	 * @param label
	 *            The label to be colored.
	 **************************************************************************/
	private void setPlayerScoreColors(int playerNum, JLabel label) {
		if (playerNum != control.getNumOfPlayers())

			// Sets the colors to exactly match those of the painted
			// terminals.
			switch (playerNum) {
			case 0:
				formatColor(250, 246, 53, label);
				break;
			case 1:
				formatColor(52, 3, 255, label);
				break;
			case 2:
				formatColor(248, 170, 61, label);
				break;
			case 3:
				formatColor(59, 230, 54, label);
				break;
			case 4:
				formatColor(223, 48, 237, label);
				break;
			default:
				formatColor(0, 1, 0, label);
				break;
			}
	}

	/**************************************************************************
	 * Formats the color of the specified label.
	 * 
	 * @param r
	 *            The amount of red.
	 * @param g
	 *            The amount of green.
	 * @param b
	 *            The amount of blue.
	 * @param label
	 *            The label to be colored.
	 **************************************************************************/
	private void formatColor(int r, int g, int b, JLabel label) {
		label.setForeground(new Color(r, g, b));
	}

	/**************************************************************************
	 * Updates the scores for each player.
	 **************************************************************************/
	private void updateScores() {
		for (int i = 0; i < playerScores.length; i++)
			playerScores[i].setText("Player " + Integer.toString(i + 1)
					+ ": " + control.getScore(i));
	}

	/**************************************************************************
	 * Updates the images for each player's pile on the side panel.
	 **************************************************************************/
	private void updatePlayerTileImages() {
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(cs, null);
		for (int i = 0; i < playerTiles.length; i++) {
			if (i != control.getCurrentPlayerNum()
					&& i != playerTiles.length)
				playerTiles[i].previewImage(op.filter(
						getCurrentTile(i), null));
			else
				playerTiles[i].previewImage(getCurrentTile(i));
		}
	}

	/**************************************************************************
	 * Displays the new game window and hides the frame.
	 **************************************************************************/
	private void displayNewGameWindow() {
		newGameWindow.setVisible(true);
		setVisible(false);
	}

	/**************************************************************************
	 * Hides the new game window and resets some of its components.
	 **************************************************************************/
	private void closeNewGameWindow() {
		newGameWindow.setVisible(false);
	}

	/**************************************************************************
	 * Gets the current tile for the specified player number.
	 * 
	 * @param playerNum
	 *            The player number to get the tile for.
	 * @return the current tile image for the specified player number.
	 **************************************************************************/
	private BufferedImage getCurrentTile(int playerNum) {
		return tile.get(control.getCurrentTile(playerNum));
	}

	/**************************************************************************
	 * Gets the current active tile image. This is the image that the
	 * current player holds.
	 * 
	 * @return the current active tile image.
	 **************************************************************************/
	private BufferedImage getCurrentActiveTile() {
		return tile.get(control.getCurrentActiveTile());
	}

	/**************************************************************************
	 * Clears all tiles on the game board that are not locked, have
	 * images, and are not active.
	 **************************************************************************/
	private void clearTiles() {
		for (int i = 0; i < tiles.length; i++)
			for (int j = 0; j < tiles[i].length; j++) {
				if (tiles[i][j].getSet() != true
						&& !tiles[i][j].equals(current)
						&& tiles[i][j].getImageDisplayed())
					tiles[i][j].clear(background);
			}
	}

	/**************************************************************************
	 * Sets the tiles on the game board after a load has been called.
	 **************************************************************************/
	private void setTilesFromLoad() {
		int[][] board = control.getGameBoard();
		BufferedImage image;
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++)
				if (board[i][j] != -1) {
					image = tile.get(board[i][j]);
					tiles[i][j].confirmSet(image);
				}
	}

	/**************************************************************************
	 * The main class of the Metro program. This starts the program.
	 * 
	 * @param args
	 *            Arguments passed to the main method upon execution.
	 **************************************************************************/
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MetroGUI a = new MetroGUI();
	}
}
