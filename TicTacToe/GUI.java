package tictactoe;

import javax.swing.*;

import tictactoe.TicTacToe;

import java.awt.*;
import java.awt.event.*;

/**
 * A GUI class for the Tic Tac Toe Game. Allows selections for Human v. Computer
 * play and Human v. Human play.
 * 
 * @author Sam
 * @version 1.0
 */
public class GUI implements ActionListener {

	/** The frames of the GUI */
	private JFrame frame, newGameFrame;

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

	/** Control buttons for the newGameFrame */
	private JButton ok, close;

	/** Resets the game board */
	private JButton nextGame;

	/** The blank icon for the grid buttons */
	private ImageIcon empty;

	/** The message displayed for game events */
	private JLabel message;

	/** The panel where messages are contained */
	private JPanel messages;

	/** The player number for X */
	private static final int X = 1;

	/** The player number for Y */
	private static final int O = 2;

	/** The empty space number */
	private static final int NULL = 0;

	/**
	 * Primary constructor for the GUI class. Instantiates primary GUI
	 * interface. Disables the Tic Tac Toe grid so buttons cannot be pushed
	 * until game type is selected.
	 */
	public GUI() {
		JPanel gamePanel = new JPanel();
		messages = new JPanel();

		// Instantiates primary GUI variables.
		enable = false;
		game = new TicTacToe();
		frame = new JFrame("Tic Tac Toe");
		empty = new ImageIcon("");
		message = new JLabel("");

		// Sets up frame and change colors of the message box.
		setupMenus();
		frame.setLayout(new BorderLayout());
		frame.add(messages, BorderLayout.SOUTH);
		frame.add(gamePanel, BorderLayout.CENTER);
		gamePanel.setLayout(new GridLayout(3, 3));
		messages.setLayout(new GridLayout(1, 2));
		messages.setBackground(Color.RED);
		messages.add(message);

		// Creates the buttons.
		buttons = new JButton[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				buttons[i][j] = new JButton("", empty);
				gamePanel.add(buttons[i][j]);
				buttons[i][j].setBackground(Color.BLACK);
				buttons[i][j].addActionListener(this);
			}
		}

		// Instantiates the next game button for use later in the program.
		nextGame = new JButton("Next Game");
		nextGame.setBackground(Color.BLACK);
		nextGame.setForeground(Color.WHITE);
		nextGame.addActionListener(this);

		frame.setSize(500, 500);
		frame.setVisible(true);
	}

	/**
	 * Sets up the menu functions of the primary GUI.
	 */
	private void setupMenus() {
		JMenuBar menu = new JMenuBar();
		JMenu operation = new JMenu("Operation");
		JMenu options = new JMenu("Options");

		// Instantiates the variables necessary to the menus.
		newGame = new JMenuItem("New Game");
		quit = new JMenuItem("Quit");
		about = new JMenuItem("About");
		stats = new JMenuItem("Statistics");
		resetStats = new JMenuItem("Reset Statistics");

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
	 * Sets up the new game frame with mutually exclusive radio buttons.
	 */
	private void newGameSetup() {

		// Instantiations.
		JPanel gameOptions = new JPanel();
		JPanel commands = new JPanel();
		ButtonGroup players = new ButtonGroup();
		newGameFrame = new JFrame("New Game");

		// Layouts.
		newGameFrame.setLayout(new BorderLayout());
		gameOptions.setLayout(new GridLayout(1, 2));
		commands.setLayout(new FlowLayout());

		// Match the color scheme.
		commands.setBackground(Color.RED);

		// Adding the panels to the frame.
		newGameFrame.add(gameOptions, BorderLayout.CENTER);
		newGameFrame.add(commands, BorderLayout.SOUTH);

		// Creating the radio buttons and setting colors.
		onePlayer = new JRadioButton("1 Player");
		twoPlayer = new JRadioButton("2 Players");
		onePlayer.setBackground(Color.RED);
		twoPlayer.setBackground(Color.RED);
		gameOptions.add(onePlayer);
		gameOptions.add(twoPlayer);
		onePlayer.addActionListener(this);
		twoPlayer.addActionListener(this);

		// Makes the radio's mutually exclusive.
		players.add(onePlayer);
		players.add(twoPlayer);

		// Confirm selection buttons, close buttons, and sets colors.
		ok = new JButton("OK");
		close = new JButton("Close");
		ok.setBackground(Color.BLACK);
		close.setBackground(Color.BLACK);
		ok.setForeground(Color.WHITE);
		close.setForeground(Color.WHITE);
		commands.add(ok);
		commands.add(close);
		ok.addActionListener(this);
		close.addActionListener(this);

		// Makes the frame visible.
		newGameFrame.setSize(250, 100);
		newGameFrame.setVisible(true);
	}

	/**
	 * Resets the stats and shows a confirmation box.
	 */
	private void resetStats() {
		int reply = JOptionPane.showConfirmDialog(null,
				"Are you sure you reset statistics?", "Are you sure?",
				JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION)
			game.resetStats();
	}

	/**
	 * Resets the stats for a new game.
	 */
	private void newGameStats() {
		game.resetStats();
	}

	/**
	 * Provides an popup box with the statistics.
	 */
	private void getStats() {
		if (game.getCompStatus()) {
			JOptionPane.showMessageDialog(null, "X wins:  "
					+ game.getStats()[0] + "    O Wins: " + game.getStats()[1]
					+ "    Cat Games: " + game.getStats()[2], "Statisitics",
					JOptionPane.INFORMATION_MESSAGE);
//			JOptionPane.showMessageDialog(null, "    Computer Wins:  "
//					+ game.getCompStats() + "    Human Wins:  "
//					+ (game.getTotalRounds() - game.getCompStats())
//					+ "    Total Rounds:  " + game.getTotalRounds(),
//					"Computer Game Statisitics",
//					JOptionPane.INFORMATION_MESSAGE);
		}
		else
			JOptionPane.showMessageDialog(null, "X wins:  "
					+ game.getStats()[0] + "    O Wins: " + game.getStats()[1]
					+ "    Cat Games: " + game.getStats()[2]
					+ "    Total Rounds:  " + game.getTotalRounds(),
					"Statisitics", JOptionPane.INFORMATION_MESSAGE);
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
			if (game.boardFull()) {
				message.setText("Cat Game");
			} else if (game.playerWon(X)) {
				message.setText("X Won");
			} else if (game.playerWon(O)) {
				message.setText("O Won");
			}

			// The next game button and disables random other button pushing.
			messages.add(nextGame);
			enable = false;
		}
	}

	/**
	 * Tells the computer to take its turn if it's going first.
	 */
	private void initCompTurn() {
		if (game.getCompTurn() && game.getCompStatus()) {
			int[] bestTurn = game.takeTurn();
			buttonAction(bestTurn[0], bestTurn[1]);
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
			if (game.getPlayerTurn() == X) {
				buttons[i][j].setIcon(x);

				// Tells the game class what's happening.
				game.setValue(X, i, j);
				game.advancePlayerTurn();
			}

			// Sets the icons for Y.
			else if (game.getPlayerTurn() == O) {
				buttons[i][j].setIcon(y);

				// Tells the game class what's happening.
				game.setValue(O, i, j);
				game.advancePlayerTurn();
			}
		}
	}

	/**
	 * Provides the actions performed for each button.
	 */
	public void actionPerformed(ActionEvent e) {

		JComponent comp = (JComponent) e.getSource();

		if (comp == quit) {
			int reply = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to quit?", "Quitting?",
					JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION)
				System.exit(0);
		}

		// Displays the new game frame.
		else if (comp == newGame) {
			newGameSetup();
		}

		// Displays the about popup.
		else if (comp == about) {
			JOptionPane.showMessageDialog(null,
					"Author: Sam Eary    Version: 1.0", "About",
					JOptionPane.INFORMATION_MESSAGE);
		}

		// Displays the stats popup.
		else if (comp == stats) {
			getStats();
		}

		// Resets the stats.
		else if (comp == resetStats) {
			resetStats();
		}

		// Human v. Computer.
		else if (comp == onePlayer) {
			game.enableComp();
		}

		// Human v. Human.
		else if (comp == twoPlayer) {
			game.disableComp();
		}

		// Starts a new game.
		else if (comp == ok) {
			enable = true;
			newGameFrame.dispose();
			game.reset();
			game.resetCompTurn();
			resetButtons();
			newGameStats();
			initCompTurn();
		}

		// Closes the new game menu.
		else if (comp == close) {
			newGameFrame.dispose();
		}

		// Starts a new game after one has ended.
		else if (comp == nextGame) {
			resetButtons();
			game.reset();
			game.advanceCompTurn();
			messages.remove(nextGame);
			enable = true;
			message.setText("");
			initCompTurn();
		}

		// Responds to the tic tac toe grid buttons.
		int[] bestTurn = new int[2];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (comp == buttons[i][j] && game.getValue(i, j) == NULL) {
					buttonAction(i, j);
					gameEndCheck();

					// In game computer moves.
					if (game.getCompStatus() && enable) {
						bestTurn = game.takeTurn();
						buttonAction(bestTurn[0], bestTurn[1]);
						gameEndCheck();
					}
				}
	}

}
