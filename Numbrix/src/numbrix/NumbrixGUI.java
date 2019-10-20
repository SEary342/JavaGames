package numbrix;

import javax.swing.*;
import java.io.*;
import java.util.Calendar;
import java.awt.*;
import java.awt.event.*;

/**************************************************************************
 * The GUI display interface for the Numbrix game.
 * 
 * @author Sam Eary
 * @version 1.0
 **************************************************************************/
public class NumbrixGUI implements ActionListener {

	/** The default game directory. */
	private static final String BASE = "Games/";

	/** The boolean specifying when the load button is selected. */
	private static final boolean LOAD = true;

	/** The maximum row length before rows are numbered. */
	private static final int MAX_ROW_LENGTH = 26;

	/** The integer added to get to the letters in the ASCII table. */
	private static final int ASCIIJUMP = 65;

	/** The frame of the GUI. */
	private JFrame frame;

	/** The new game window. */
	private JFrame newGameWindow;

	/** The Numbrix entry fields. */
	private JTextField[][] entries;

	/** Game controls for the drop down menus. */
	private JMenuItem quit, newGame, about, restart;

	/** The controller for the game's engine. */
	private NumbrixControl control;

	/** A boolean representing if load is the game type. */
	private boolean load;

	/** Control buttons for the newGameWindow */
	private JButton ok, close;

	/** Arrays representing the row and column labels. */
	private JLabel[] rowLabel, colLabel;

	/** The panels for the main frame */
	private JPanel gamePanel;

	/** The panels for the new game window. */
	private JPanel gameOptions, commands, labels;

	/** Radio buttons for selection of the type of game. */
	private JRadioButton newBlankGame, loadGame;

	/** The labels displayed when a blank game is created. */
	private JLabel rowPrompt, colPrompt;

	/** The fields displayed when a blank game is created. */
	private JTextField rowInput, colInput;

	/** Panels for the frame. */
	private JPanel right, bottom;

	/** The exit button for the frame. */
	private JButton exit;

	/** Font styles for all components. */
	private Font style, change;

	/** Numbers representing the amount of time in game. */
	private long start;

	/**************************************************************************
	 * Constructor for the NumbrixGUI class. It will start the new game
	 * window and primary frame setup process.
	 **************************************************************************/
	public NumbrixGUI() {
		JLabel instruction = new JLabel(
				"Please press enter after each entry.");
		JLabel numbrix = new JLabel(
				"<html>N<br><br>U<br><br>M<br><br>B<br>"
						+ "<br>R<br><br>I<br><br>X</html>");
		right = new JPanel();
		bottom = new JPanel();
		panelSetup(right);
		panelSetup(bottom);
		right.add(numbrix);
		bottom.add(instruction);
		variableInstantiator();
		labelFormatter(instruction);
		labelFormatter(numbrix);

		// Sets up frame.
		setupMenus();
		frame.setLayout(new BorderLayout());
		frame.add(right, BorderLayout.EAST);
		frame.add(gamePanel, BorderLayout.CENTER);
		frame.add(instruction, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setUndecorated(true);

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
		control = new NumbrixControl();
		frame = new JFrame("Numbrix");
		load = false;
		format(gamePanel);
		exit = new JButton();
		style = new Font("Lucida Calligraphy", Font.BOLD, 14);
		change = new Font("Lucida Calligraphy", Font.PLAIN, 14);

		// Instantiates the variables necessary to the menus.
		newGame = new JMenuItem("New Game");
		quit = new JMenuItem("Quit");
		restart = new JMenuItem("Restart");
		about = new JMenuItem("About");

		// Instantiates variables for the new game window.
		gameOptions = new JPanel();
		commands = new JPanel();
		labels = new JPanel();
		newGameWindow = new JFrame("New Game");
		newBlankGame = new JRadioButton("Blank Game");
		loadGame = new JRadioButton("Load Game");
		ok = new JButton("OK");
		close = new JButton("Close");
		rowPrompt = new JLabel("Rows:");
		colPrompt = new JLabel("Columns:");
		rowInput = new JTextField();
		colInput = new JTextField();
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
		frame.setJMenuBar(menu);
		menu.add(menuList);

		// Creates the menu.
		menuList.add(newGame);
		menuList.add(restart);
		restart.setEnabled(false);
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
		format(quit);
		format(about);

		// Setting up the exit button
		exit.setText("Exit");
		format(exit);
		exit.setFocusable(false);
	}

	/**************************************************************************
	 * Sets up all of the Numbrix fields after a game has been loaded or
	 * created.
	 **************************************************************************/
	private void mainFieldSetup() {
		int rows = control.getRows();
		int cols = control.getCols();
		int[][] board = control.getGameBoard();

		// Creates the buttons.
		rowLabel = new JLabel[rows];
		colLabel = new JLabel[cols + 1];
		colLabelSetup();
		entries = new JTextField[rows][cols];
		for (int j = 0; j < entries.length; j++) {
			rowLabelSetup(j);
			for (int k = 0; k < entries[j].length; k++) {
				entries[j][k] = new JTextField(
						entryFormatter(board[j][k]));
				entries[j][k].setPreferredSize(new Dimension(40, 40));
				entries[j][k].setBorder(BorderFactory
						.createLineBorder(Color.WHITE));
				format(entries[j][k]);
				entries[j][k].setHorizontalAlignment(JTextField.CENTER);
				gamePanel.add(entries[j][k]);
				entries[j][k].setCaretColor(Color.WHITE);
				entries[j][k].addActionListener(this);
			}
		}
	}

	/**************************************************************************
	 * Creates the array of labels for each of the columns of the
	 * game-board.
	 **************************************************************************/
	private void colLabelSetup() {
		for (int i = 0; i < colLabel.length; i++) {
			if (i == 0)
				colLabel[i] = new JLabel(" ");
			else
				colLabel[i] = new JLabel("" + i);
			labelFormatter(colLabel[i]);
			gamePanel.add(colLabel[i]);
		}
	}

	/**************************************************************************
	 * Generates and formats a row label for a given row.
	 * 
	 * @param row
	 *            The row recieving a label.
	 **************************************************************************/
	private void rowLabelSetup(int row) {
		rowLabel[row] = new JLabel(rowLabeler(row));
		labelFormatter(rowLabel[row]);
		gamePanel.add(rowLabel[row]);
	}

	/**************************************************************************
	 * Formats JLabels so that they will match the rest of the GUI's
	 * color and design scheme.
	 * 
	 * @param label
	 *            The label being formatted.
	 **************************************************************************/
	private void labelFormatter(JLabel label) {
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setOpaque(true);
		format(label);
	}

	/**************************************************************************
	 * Formats any component so that it will match the overall design
	 * scheme.
	 * 
	 * @param comp
	 *            The component being formatted.
	 **************************************************************************/
	private void format(Component comp) {
		comp.setBackground(Color.BLACK);
		comp.setForeground(Color.WHITE);
		comp.setFont(style);
	}

	/**************************************************************************
	 * Sets the color of the specified panel to black and sets the
	 * panel's layout.
	 * 
	 * @param panel
	 *            The panel to be formatted.
	 **************************************************************************/
	private void panelSetup(JPanel panel) {
		panel.setLayout(new GridBagLayout());
		panel.setBackground(Color.BLACK);
	}

	/**************************************************************************
	 * Creates the row label for each row of the game board.
	 * 
	 * @param rowNumber
	 *            The number of the row.
	 * @return the string representing the label of the row.
	 **************************************************************************/
	private String rowLabeler(int rowNumber) {

		// If the game has less than 26 rows, then characters will be
		// displayed.
		if (control.getRows() <= MAX_ROW_LENGTH)
			return "" + (char) (rowNumber + ASCIIJUMP);
		else
			return "" + (rowNumber + 1);
	}

	/**************************************************************************
	 * Converts integers to strings that can be used on the game-board.
	 * 
	 * @param entry
	 *            The entry being converted.
	 * @return the string representing the entry.
	 **************************************************************************/
	private String entryFormatter(int entry) {
		if (entry == 0)
			return "";
		return Integer.toString(entry);
	}

	/**************************************************************************
	 * Resets non-permanent aspects of the frame each time a new game is
	 * loaded.
	 **************************************************************************/
	private void resetFrame() {
		Calendar cal = Calendar.getInstance();
		if (entries != null)
			removeActionListeners();
		gamePanel.removeAll();
		gamePanel.setLayout(new GridLayout(control.getRows() + 1,
				control.getCols() + 1));
		restart.setEnabled(false);
		start = cal.getTimeInMillis();
	}

	/**************************************************************************
	 * Sets up the new game frame with mutually exclusive radio buttons.
	 **************************************************************************/
	private void newGameSetup() {
		JLabel newGameLabel = new JLabel("New Game");
		ButtonGroup players = new ButtonGroup();
		newGameSetupFormat();
		labelFormatter(newGameLabel);

		// Adds the buttons and labels to the panels.
		labels.add(newGameLabel);
		gameOptions.add(newBlankGame);
		gameOptions.add(loadGame);
		commands.add(rowPrompt);
		commands.add(colPrompt);
		commands.add(rowInput);
		commands.add(colInput);
		commands.add(ok);
		commands.add(close);
		showCloseBlankInput(false);

		// Makes the radio's mutually exclusive.
		players.add(newBlankGame);
		players.add(loadGame);

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
		commands.setLayout(new GridLayout(3, 2));
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
		newBlankGame.addActionListener(this);
		loadGame.addActionListener(this);
		ok.addActionListener(this);
		close.addActionListener(this);

		// Action listeners for the frame.
		quit.addActionListener(this);
		restart.addActionListener(this);
		newGame.addActionListener(this);
		about.addActionListener(this);
		exit.addActionListener(this);

		// Formatting components to match the color and design scheme.
		format(commands);
		format(gameOptions);
		format(labels);
		format(newBlankGame);
		format(loadGame);
		format(rowInput);
		format(colInput);
		format(ok);
		format(close);
		labelFormatter(rowPrompt);
		labelFormatter(colPrompt);
		rowInput.setCaretColor(Color.WHITE);
		rowInput.setHorizontalAlignment(JTextField.CENTER);
		colInput.setCaretColor(Color.WHITE);
		colInput.setHorizontalAlignment(JTextField.CENTER);
	}

	/**************************************************************************
	 * Performs all actions necessary for user interaction with the
	 * game.
	 * 
	 * @param e
	 *            The event that called when an action listener is
	 *            activated.
	 **************************************************************************/
	@Override
	public void actionPerformed(ActionEvent e) {
		JComponent comp = (JComponent) e.getSource();
		if (!control.gameHasBeenModified())
			restart.setEnabled(true);
		else
			restart.setEnabled(false);
		if (comp == restart)
			restartButtonResponse();
		else if (comp == quit || comp == exit)
			quitButtonResponse();
		else if (comp == newGame)
			displayNewGameWindow();
		else if (comp == about)
			JOptionPane.showMessageDialog(null,
					"Author: Sam Eary  Version: 3/7/2010", "About",
					JOptionPane.INFORMATION_MESSAGE);
		else if (comp == ok || comp == loadGame || comp == newBlankGame
				|| comp == close)
			newGameActions(comp);
		else
			entryResponse(comp);
	}

	/**************************************************************************
	 * Starts the load game procedure if the game type is a loaded game.
	 * If a game has been created it will display the primary frame
	 * containing the game-board.
	 **************************************************************************/
	private void gameStart() {
		if (load == LOAD)
			loadGame();
		if (control.getLoadStatus()) {
			resetFrame();
			mainFieldSetup();

			// Pack is called twice to format the other panels.
			frame.pack();
			right.setPreferredSize(new Dimension(entries[0][0]
					.getWidth(), frame.getWidth()));
			bottom.setPreferredSize(new Dimension(frame.getWidth(),
					entries[0][0].getHeight()));
			frame.pack();
			frame.setLocationRelativeTo(null);
			autoFocus();
			frame.setVisible(true);
		}
	}

	/**************************************************************************
	 * Displays the load game from file prompt. It will then load the
	 * user selected game.
	 **************************************************************************/
	private void loadGame() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(BASE));
		int returnVal = fc.showOpenDialog(newGameWindow);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filename = fc.getSelectedFile().getPath();
			if (!control.loadGame(filename))
				JOptionPane.showMessageDialog(null,
						"That is not a valid game file.", filename,
						JOptionPane.INFORMATION_MESSAGE);
			else
				closeNewGameWindow();
		}
	}

	/**************************************************************************
	 * Provides a y/n dialog when the user selects the restart button or
	 * reaches the end of the game.
	 **************************************************************************/
	private void restartButtonResponse() {
		int reply = JOptionPane.showConfirmDialog(null,
				"Are you sure that you want to restart?",
				"Restarting?", JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			control.reset();
			gameStart();
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
		if (comp == ok) {
			if (rowInput.isVisible()) {
				newBlankGameInit();
			} else
				gameStart();
		} else if (comp == close) {
			closeNewGameWindow();
			if (!control.getLoadStatus())
				System.exit(0);
			else
				frame.setVisible(true);
		} else if (comp == newBlankGame) {
			load = !LOAD;
			showCloseBlankInput(true);
		} else if (comp == loadGame) {
			load = LOAD;
			showCloseBlankInput(false);
		}
	}

	/**************************************************************************
	 * The actions performed when one of the text fields that represent
	 * the game board have been altered.
	 * 
	 * @param comp
	 *            The component that was activated.
	 **************************************************************************/
	private void entryResponse(Component comp) {
		for (int i = 0; i < entries.length; i++)
			for (int j = 0; j < entries[i].length; j++)
				if (comp == entries[i][j]) {
					try {
						control.addEntry(
								entries[i][j].getText().trim(), i, j);
						entries[i][j].setFont(change);
						autoFocus();
						gameCompletenessCheck();
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null,
								"Please enter integers only.",
								"Entry Error",
								JOptionPane.ERROR_MESSAGE);
						entries[i][j].setText("");
					} catch (InvalidGameStateException e2) {
						errorMessageDisplay(e2.getMessage());
						entries[i][j].setText("");
					}
				}
	}

	/**************************************************************************
	 * Gets the first empty text field and then sets the focus on that
	 * field.
	 **************************************************************************/
	private void autoFocus() {
		int[] empty;
		empty = control.getFirstEmptySpace();
		entries[empty[0]][empty[1]].requestFocus();
	}

	/**************************************************************************
	 * Checks to see if the game has been completed and then starts the
	 * restart system.
	 **************************************************************************/
	private void gameCompletenessCheck() {
		if (control.isComplete()) {
			int reply = JOptionPane.showConfirmDialog(null,
					getTimeInGame()
							+ "Do you wish to play this game again?",
					"Game Over", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				control.reset();
				gameStart();
			} else if (reply == JOptionPane.NO_OPTION) {
				displayNewGameWindow();
				control.unloadGame();
			}
		}
	}

	/**************************************************************************
	 * Gets the total time spent in game and formats it to hours,
	 * minutes, and seconds.
	 * 
	 * @return The string representing the amount of time in game.
	 **************************************************************************/
	private String getTimeInGame() {
		Calendar cal = Calendar.getInstance();
		String time = "Your game took ";
		long total = cal.getTimeInMillis() - start;
		long seconds = (total / 1000) % 60;
		long minutes = (total / 1000 * 60) % 60;
		long hours = (total / 1000 * 60 * 60) % 24;
		if (hours > 0)
			time += hours + " hour(s),";
		if (minutes > 0)
			time += +minutes + " minute(s) and ";
		time += seconds + " second(s). ";
		return time;
	}

	/**************************************************************************
	 * Displays the new game window and hides the frame.
	 **************************************************************************/
	private void displayNewGameWindow() {
		if (newBlankGame.isSelected())
			showCloseBlankInput(true);
		newGameWindow.setVisible(true);
		frame.setVisible(false);
	}

	/**************************************************************************
	 * Displays appropriate error messages for each of the input errors.
	 * 
	 * @param message
	 *            The error message thrown by the game engine.
	 **************************************************************************/
	private void errorMessageDisplay(String message) {
		if (message.equals(NumbrixEngine.DUPLICATE))
			JOptionPane.showMessageDialog(null,
					"That entry was a duplicate.", "Duplicate Entry",
					JOptionPane.INFORMATION_MESSAGE);
		else if (message.equals(NumbrixEngine.RANGE_ERROR))
			JOptionPane.showMessageDialog(null,
					"Tha number cannot be on the board.",
					"Number Out of Range",
					JOptionPane.INFORMATION_MESSAGE);
		else if (message.equals(NumbrixEngine.INVALID_ERROR))
			JOptionPane
					.showMessageDialog(
							null,
							"That number is directly invalid with its neighbors.",
							"Invalid Number",
							JOptionPane.INFORMATION_MESSAGE);
	}

	/**************************************************************************
	 * Gets user input from the row and column prompts and attempts to
	 * create a new blank game.
	 **************************************************************************/
	private void newBlankGameInit() {
		int value = control.createNewBlankGame(rowInput.getText(),
				colInput.getText());
		switch (value) {
		case NumbrixControl.VALID:
			closeNewGameWindow();
			gameStart();
			break;
		case NumbrixControl.INVALID_SIZE:
			JOptionPane.showMessageDialog(null,
					"That is not a valid board size.",
					"Invalid Board Size",
					JOptionPane.INFORMATION_MESSAGE);
			break;
		case NumbrixControl.INTEGERS_ONLY:
			JOptionPane.showMessageDialog(null,
					"Please enter integers only.", "Data Entry Error",
					JOptionPane.INFORMATION_MESSAGE);
			break;
		}
	}

	/**************************************************************************
	 * Hides the new game window and resets some of its components.
	 **************************************************************************/
	private void closeNewGameWindow() {
		load = false;
		newGameWindow.setVisible(false);
		newBlankGame.setSelected(true);
		rowInput.setText("");
		colInput.setText("");
		showCloseBlankInput(false);
	}

	/**************************************************************************
	 * Shows or hides the row and column size selection prompt from the
	 * new game window.
	 * 
	 * @param type
	 *            {@code true} if the prompt is to be shown, {@code
	 *            false} if the prompt is being hidden.
	 **************************************************************************/
	private void showCloseBlankInput(boolean type) {
		colInput.setVisible(type);
		rowInput.setVisible(type);
		rowPrompt.setVisible(type);
		colPrompt.setVisible(type);
		if (type)
			rowInput.requestFocus();
	}

	/**************************************************************************
	 * Removes all action listeners from the game-board text fields.
	 **************************************************************************/
	private void removeActionListeners() {
		for (int i = 0; i < entries.length; i++) {
			for (int j = 0; j < entries[i].length; j++) {
				entries[i][j].removeActionListener(this);
			}
		}
	}

	/**************************************************************************
	 * The main class of the NumbrixGUI program. This starts the
	 * program.
	 * 
	 * @param args
	 *            Arguments passed to the main method upon execution.
	 **************************************************************************/
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		NumbrixGUI g = new NumbrixGUI();
	}
}
