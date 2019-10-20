package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A GUI class for the Tic Tac Toe Game. Allows selections for Human v. Computer
 * play and Human v. Human play.
 * 
 * @author Sam Eary
 * @version 12/10/2009
 */
public class GUI implements ActionListener {

	/** The frames of the GUI */
	private JFrame frame;

	/** The new game window */
	private JWindow newGameWindow;

	/** The Tic Tac Toe grid buttons */
	private JButton[][] buttons;

	/** The menu items for quit, new, about, stats, and reset stats */
	private JMenuItem quit, newGame, about, stats, resetStats;

	/** The Tic Tac Toe game class */
	private TicTacToe game;

	/** Enabler for the Tic Tac Toe grid buttons */
	private boolean enable;

	/** Radio Buttons for selecting game type */
	private JRadioButton onePlayer, twoPlayer;

	/** Control buttons for the newGameWindow */
	private JButton ok, close;

	/** Resets the game board */
	private JButton nextGame;

	/** The blank icon for the grid buttons */
	private ImageIcon empty;

	/** The message displayed for game events */
	private JLabel message;

	/** Disables the other buttons while the new game frame is open. */
	private boolean tempEnable;

	/** The panels for the main frame */
	private JPanel gamePanel, messages;

	/** The panels for the new game window. */
	private JPanel gameOptions, commands, labels;

	/**
	 * Primary constructor for the GUI class. Instantiates primary GUI
	 * interface. Disables the Tic Tac Toe grid so buttons cannot be pushed
	 * until game type is selected.
	 */
	public GUI() {
		variableInstantiator();

		// Sets up frame and change colors of the message box.
		setupMenus();
		frame.setLayout(new BorderLayout());
		frame.add(messages, BorderLayout.SOUTH);
		frame.add(gamePanel, BorderLayout.CENTER);
		gamePanel.setLayout(new GridLayout(3, 3));
		messages.setLayout(new GridLayout(2, 1));
		messages.setBackground(Color.RED);
		messages.add(message);
		mainButtonSetup();

		// Sets size and location.
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setSize(550, 550);
		frame.setIconImage(new ImageIcon("tic.jpg").getImage());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * Instantiates most of the instance variables necessary to create the
	 * frame, the menus and the new game window.
	 */
	private void variableInstantiator() {

		// Instantiates primary GUI variables.
		gamePanel = new JPanel();
		messages = new JPanel();
		tempEnable = true;
		enable = false;
		game = new TicTacToe();
		frame = new JFrame("Tic Tac Toe");
		empty = new ImageIcon("");
		message = new JLabel("");

		// Instantiates the variables necessary to the menus.
		newGame = new JMenuItem("New Game");
		quit = new JMenuItem("Quit");
		about = new JMenuItem("About");
		stats = new JMenuItem("Statistics");
		resetStats = new JMenuItem("Reset Statistics");

		// Instantiates variables for the new game window.
		gameOptions = new JPanel();
		commands = new JPanel();
		labels = new JPanel();
		newGameWindow = new JWindow();
		onePlayer = new JRadioButton("Human v. Computer");
		twoPlayer = new JRadioButton("Human v. Human");
		ok = new JButton("OK");
		close = new JButton("Close");
	}

	/**
	 * Sets up the menu functions of the primary GUI.
	 */
	private void setupMenus() {
		JMenuBar menu = new JMenuBar();
		JMenu operation = new JMenu("Operation");
		JMenu options = new JMenu("Options");

		// Adds the menu bar and changes its color.
		frame.setJMenuBar(menu);
		menu.setBackground(Color.RED);
		menu.add(operation);

		// Creates the operation menu.
		operation.add(newGame);
		newGame.addActionListener(this);
		operation.add(quit);
		quit.addActionListener(this);

		// Creates the options menu.
		menu.add(options);
		options.add(about);
		about.addActionListener(this);
		options.add(stats);
		stats.addActionListener(this);
		options.add(resetStats);
		resetStats.addActionListener(this);
	}

	/**
	 * Sets up the tic tac toe grid buttons and the next button.
	 */
	private void mainButtonSetup() {

		// Creates the buttons.
		buttons = new JButton[3][3];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				buttons[i][j] = new JButton("", empty);
				gamePanel.add(buttons[i][j]);
				buttons[i][j].setBackground(Color.BLACK);
				buttons[i][j].addActionListener(this);
			}

		// Instantiates the next game button for use later in the program.
		nextGame = new JButton("Next Game");
		nextGame.setBackground(Color.BLACK);
		nextGame.setForeground(Color.WHITE);
		nextGame.addActionListener(this);
	}

	/**
	 * Sets up the new game frame with mutually exclusive radio buttons.
	 */
	private void newGameSetup() {
		JLabel newGameLabel = new JLabel("New Game");
		ButtonGroup players = new ButtonGroup();
		tempEnable = false;
		newGameSetupFormat();
		newGameLabel.setForeground(Color.WHITE);

		// Adds the buttons and labels to the panels.
		labels.add(newGameLabel);
		gameOptions.add(onePlayer);
		gameOptions.add(twoPlayer);
		commands.add(ok);
		commands.add(close);

		// Makes the radio's mutually exclusive.
		players.add(onePlayer);
		players.add(twoPlayer);

		// Makes the frame visible.
		newGameWindow.setSize(300, 100);
		newGameWindow.setLocationRelativeTo(null);
		newGameWindow.setVisible(true);
		newGameWindow.setAlwaysOnTop(true);
	}

	/**
	 * Sets the layouts, colors and adds the panels to the new game window.
	 */
	private void newGameSetupFormat() {

		// Layouts.
		newGameWindow.setLayout(new BorderLayout());
		gameOptions.setLayout(new FlowLayout());
		commands.setLayout(new FlowLayout());
		labels.setLayout(new FlowLayout());

		// Match the color scheme.
		commands.setBackground(Color.RED);
		gameOptions.setBackground(Color.RED);
		labels.setBackground(Color.BLACK);

		// Adding the panels to the frame.
		newGameWindow.add(gameOptions, BorderLayout.CENTER);
		newGameWindow.add(commands, BorderLayout.SOUTH);
		newGameWindow.add(labels, BorderLayout.NORTH);
		buttonSetup();
	}

	/**
	 * Sets up the buttons for the new game frame.
	 */
	private void buttonSetup() {

		// completes radiobutton setup and sets colors.
		onePlayer.setBackground(Color.RED);
		twoPlayer.setBackground(Color.RED);
		onePlayer.addActionListener(this);
		twoPlayer.addActionListener(this);

		// Completes ok and close button creation and sets colors.
		ok.setBackground(Color.BLACK);
		close.setBackground(Color.BLACK);
		ok.setForeground(Color.WHITE);
		close.setForeground(Color.WHITE);
		ok.addActionListener(this);
		close.addActionListener(this);
	}

	/**
	 * Resets the stats and shows a confirmation box.
	 */
	private void resetStats() {
		int reply = JOptionPane.showConfirmDialog(null,
				"Are you sure you reset statistics?", "Are you sure?",
				JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			game.resetStats();
			getStats();
		}
	}

	/**
	 * Provides an message label with the statistics.
	 */
	private void getStats() {
		if (game.getCompStatus()) {
			message.setText("X Wins:  "
					+ game.getStats()[0]
					+ "   O Wins:  "
					+ game.getStats()[1]
					+ "   Cat Games:  "
					+ game.getStats()[2]
					+ "   Computer Wins:  "
					+ game.getCompStats()
					+ "   Human Wins:  "
					+ (game.getTotalRounds() - game.getCompStats() - game
							.getStats()[2]) + "   Total Rounds:  "
					+ game.getTotalRounds());
		} else
			message.setText("X Wins:  " + game.getStats()[0] + "   O Wins:  "
					+ game.getStats()[1] + "   Cat Games:  "
					+ game.getStats()[2]);
	}

	/**
	 * Resets the buttons to an empty state.
	 */
	private void resetButtons() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				buttons[i][j].setIcon(empty);
	}

	/**
	 * Changes the labels to show endgame status and provides the next game
	 * button.
	 */
	private void gameEndCheck() {
		if (game.gameOver()) {
			if (game.playerWon(game.X))
				message.setText("X Won");
			else if (game.playerWon(game.O))
				message.setText("O Won");
			else if (game.boardFull())
				message.setText("Cat Game");

			// The next game button and disables random other button pushing.
			messages.add(nextGame);
			enable = false;
		}
	}

	/**
	 * Tells the computer to take its turn if it's going first. Then pushes the
	 * button of the best turn.
	 */
	private void initCompTurn() {
		if (game.getCompTurn() && game.getCompStatus()) {
			int[] bestTurn = game.takeTurn();
			buttonAction(bestTurn[0], bestTurn[1]);
		}
	}

	/**
	 * Provides the response for the ok button of the new game frame.
	 */
	private void okButton() {
		enable = true;
		tempEnable = true;
		newGameWindow.dispose();
		resetButtons();
		game.newGame();
		message.setText("");
		messages.remove(nextGame);
		initCompTurn();
	}

	/**
	 * Provides the response for the next button.
	 */
	private void nextButtonResponse() {
		resetButtons();
		game.nextRound();
		messages.remove(nextGame);
		enable = true;
		message.setText("");
		initCompTurn();
	}

	/**
	 * The responses for the quit button.
	 */
	private void quitButtonResponse() {
		int reply = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to quit?", "Quitting?",
				JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	/**
	 * Responds to the grid buttons being pushed and tells the computer to go.
	 */
	private void gridButtonResponse() {
		int[] bestTurn = new int[2];
		message.setText("");
		gameEndCheck();

		// In game computer moves.
		if (game.getCompStatus() && enable) {
			bestTurn = game.takeTurn();
			buttonAction(bestTurn[0], bestTurn[1]);
			gameEndCheck();
		}
	}

	/**
	 * This method provides the procedure for a button being pushed.
	 * 
	 * @param i
	 *            The row number.
	 * @param j
	 *            The column number.
	 */
	private void buttonAction(int i, int j) {
		ImageIcon x = new ImageIcon("x.jpg");
		ImageIcon y = new ImageIcon("o.jpg");
		if (enable) {

			// Sets the icons for X.
			if (game.getPlayerTurn() == game.X) {
				buttons[i][j].setIcon(x);
				game.setValue(game.X, i, j);
			}

			// Sets the icons for Y.
			else if (game.getPlayerTurn() == game.O) {
				buttons[i][j].setIcon(y);
				game.setValue(game.O, i, j);
			}
		}
	}

	/**
	 * Provides responses for the buttons of the new game window. This is a
	 * continuation of the actionPerformed method.
	 * 
	 * @param comp
	 *            The inputs from the action listeners.
	 */
	private void extraActions(JComponent comp) {

		// Human v. Computer.
		if (comp == onePlayer)
			game.enableDisableComp(true);

		// Human v. Human.
		else if (comp == twoPlayer)
			game.enableDisableComp(false);

		// Starts a new game.
		else if (comp == ok)
			okButton();

		// Closes the new game menu.
		else if (comp == close) {
			newGameWindow.dispose();
			tempEnable = true;
		}
	}

	/**
	 * Provides the actions performed for each button.
	 */
	public void actionPerformed(ActionEvent e) {
		JComponent comp = (JComponent) e.getSource();
		if (tempEnable) {
			if (comp == quit)
				quitButtonResponse();

			// Displays the new game window.
			else if (comp == newGame)
				newGameSetup();

			// Displays the about label.
			else if (comp == about)
				message.setText("Author: Sam Eary  Version: 12/10/2009");

			// Displays the stats label.
			else if (comp == stats)
				getStats();

			// Resets the stats.
			else if (comp == resetStats)
				resetStats();

			// Starts a new game after one has ended.
			else if (comp == nextGame)
				nextButtonResponse();

			// Responds to the tic tac toe grid buttons.
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++)
					if (comp == buttons[i][j]
							&& game.getValue(i, j) == game.NULL) {
						buttonAction(i, j);
						gridButtonResponse();
					}
		}

		// The buttons for the new game window.
		else if (comp == onePlayer || comp == twoPlayer || comp == ok
				|| comp == close) {
			extraActions(comp);
		}
	}
}
