package numbrix;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 
 * @author Sam
 * @deprecated
 */
public class NumbrixGUIDeprecated implements ActionListener {

	private static final String BASE = "C:/Users/Sam/workspace/Numbrix/Games/";

	private static final int LOAD = 2;

	private static final int MAX_ROW_LENGTH = 26;

	/** The integer added to get to the letters in the ASCII table. */
	private static final int ASCIIJUMP = 65;

	/** The frame of the GUI */
	private JFrame frame;

	/** The new game window */
	private JFrame newGameWindow;

	/** The Tic Tac Toe grid buttons */
	private JTextField[][] entries;

	private JMenuItem quit, newGame, about;

	private NumbrixEngine game;

	private int newgameType;

	/** Control buttons for the newGameWindow */
	private JButton ok, close;

	private JLabel[] rowLabel, colLabel;

	/** The panels for the main frame */
	private JPanel gamePanel;

	/** The panels for the new game window. */
	private JPanel gameOptions, commands, labels;

	private JRadioButton newBlankGame;

	private JRadioButton loadGame;

	private JLabel rowPrompt;

	private JLabel colPrompt;

	private JTextField rowInput;

	private JTextField colInput;

	private JMenuItem restart;

	/**
	 * 
	 */
	public NumbrixGUIDeprecated() {
		JLabel instruction = new JLabel("Please press enter after each entry.");
		variableInstantiator();

		// Sets up frame and change colors of the message box.
		setupMenus();
		frame.setLayout(new BorderLayout());
		frame.add(gamePanel, BorderLayout.CENTER);
		frame.add(instruction, BorderLayout.SOUTH);

		newBlankGame = new JRadioButton("Blank Game");
		loadGame = new JRadioButton("Load Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		// run new game setup first
		newGameSetup();
	}

	/**
	 * Instantiates most of the instance variables necessary to create
	 * the frame, the menus and the new game window.
	 */
	private void variableInstantiator() {

		// Instantiates primary GUI variables.
		gamePanel = new JPanel();
		game = new NumbrixEngine();
		frame = new JFrame("Numbrix");
		newgameType = 0;
		gamePanel.setBorder(BorderFactory.createTitledBorder("Game in Progress"));

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
		ok = new JButton("OK");
		close = new JButton("Close");
		rowPrompt = new JLabel("Row:");
		colPrompt = new JLabel("Column:");
		rowInput = new JTextField();
		colInput = new JTextField();
	}

	/**
	 * Sets up the menu functions of the primary GUI.
	 */
	private void setupMenus() {
		JMenuBar menu = new JMenuBar();
		JMenu menuList = new JMenu("Menu");
		JMenu options = new JMenu("Options");

		// Adds the menu bar and changes its color.
		frame.setJMenuBar(menu);
		// menu.setBackground(Color.RED);
		menu.add(menuList);

		// Creates the operation menu.
		menuList.add(newGame);
		newGame.addActionListener(this);
		menuList.add(restart);
		restart.setEnabled(false);
		restart.addActionListener(this);
		menuList.add(quit);
		quit.addActionListener(this);

		// Creates the options menu.
		menu.add(options);
		options.add(about);
		about.addActionListener(this);
	}

	private void mainButtonSetup() {
		int rows = game.getRows();
		int cols = game.getCols();
		int[][] board = game.getBoard();

		// Creates the buttons.
		rowLabel = new JLabel[rows];
		colLabel = new JLabel[cols + 1];
		for (int i = 0; i < colLabel.length; i++) {
			if (i == 0)
				colLabel[i] = new JLabel(" ");
			else
				colLabel[i] = new JLabel("" + i);
			colLabel[i].setHorizontalAlignment(JLabel.CENTER);
			gamePanel.add(colLabel[i]);
		}

		entries = new JTextField[rows][cols];
		for (int j = 0; j < entries.length; j++) {
			rowLabel[j] = new JLabel(rowLabeler(j));
			rowLabel[j].setHorizontalAlignment(JLabel.CENTER);
			gamePanel.add(rowLabel[j]);
			for (int k = 0; k < entries[j].length; k++) {
				entries[j][k] = new JTextField(
						entryFormatter(board[j][k]));
				entries[j][k].setPreferredSize(new Dimension(40, 40));
				entries[j][k].setHorizontalAlignment(JTextField.CENTER);
				gamePanel.add(entries[j][k]);
				entries[j][k].addActionListener(this);
			}
		}
	}

	private String rowLabeler(int rowNumber) {

		// If the game has less than 26 rows, then characters will be
		// displayed.
		if (game.getRows() <= MAX_ROW_LENGTH)
			return "" + (char) (rowNumber + ASCIIJUMP);
		else
			return "" + (rowNumber + 1);
	}

	private String entryFormatter(int entry) {
		if (entry == 0)
			return "";
		return Integer.toString(entry);
	}

	private void finalFrameSetup() {
		gamePanel.setLayout(new GridLayout(game.getRows() + 1, game
				.getCols() + 1));
	}

	/**
	 * Sets up the new game frame with mutually exclusive radio buttons.
	 */
	private void newGameSetup() {
		JLabel newGameLabel = new JLabel("New Game");
		ButtonGroup players = new ButtonGroup();
		newGameSetupFormat();
		newGameLabel.setForeground(Color.WHITE);

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
		rowInput.setVisible(false);
		colInput.setVisible(false);
		rowPrompt.setVisible(false);
		colPrompt.setVisible(false);

		// Makes the radio's mutually exclusive.
		players.add(newBlankGame);
		players.add(loadGame);

		// Makes the frame visible.
		// newGameWindow.setSize(300, 300);
		newGameWindow.setUndecorated(true);
		newGameWindow.pack();
		newGameWindow.setLocationRelativeTo(null);
		newGameWindow.setVisible(true);
	}

	/**
	 * Sets the layouts, colors and adds the panels to the new game
	 * window.
	 */
	private void newGameSetupFormat() {

		// Layouts.
		newGameWindow.setLayout(new BorderLayout());
		gameOptions.setLayout(new FlowLayout());
		commands.setLayout(new GridLayout(3, 2));
		labels.setLayout(new FlowLayout());

		// Match the color scheme.
		commands.setBackground(Color.RED);
		gameOptions.setBackground(Color.RED);
		labels.setBackground(Color.BLACK);

		// Adding the panels to the frame.
		newGameWindow.add(gameOptions, BorderLayout.CENTER);
		newGameWindow.add(commands, BorderLayout.SOUTH);
		newGameWindow.add(labels, BorderLayout.NORTH);
		newGameWindow.setAlwaysOnTop(true);
		buttonSetup();
	}

	/**
	 * Sets up the buttons for the new game frame.
	 */
	private void buttonSetup() {

		// completes radiobutton setup and sets colors.
		newBlankGame.setBackground(Color.RED);
		loadGame.setBackground(Color.RED);
		newBlankGame.addActionListener(this);
		loadGame.addActionListener(this);

		// Completes ok and close button creation and sets colors.
		ok.setBackground(Color.BLACK);
		close.setBackground(Color.BLACK);
		ok.setForeground(Color.WHITE);
		close.setForeground(Color.WHITE);
		ok.addActionListener(this);
		close.addActionListener(this);
	}

	private void gameStart() {
		if (newgameType == LOAD)
			loadGame();
		if (game.loadStatus()) {
			gamePanel.removeAll();
			mainButtonSetup();
			finalFrameSetup();
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
	}

	private void loadGame() {

		// point this to the games directory
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(BASE));
		int returnVal = fc.showOpenDialog(newGameWindow);
		
		// did the user select a file?
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filename = fc.getSelectedFile().getPath();
			try {
				game.loadGame(filename);
				closeNewGameWindow();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null,
						"That is not a valid game file.", filename,
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	// /**
	// * Responds to the grid buttons being pushed and tells the
	// computer to go.
	// */
	// private void gridButtonResponse() {
	// int[] bestTurn = new int[2];
	// message.setText("");
	// gameEndCheck();
	//
	// // In game computer moves.
	// if (game.getCompStatus() && enable) {
	// bestTurn = game.takeTurn();
	// buttonAction(bestTurn[0], bestTurn[1]);
	// gameEndCheck();
	// }
	// }
	//
	// /**
	// * This method provides the procedure for a button being pushed.
	// *
	// * @param i
	// * The row number.
	// * @param j
	// * The column number.
	// */
	// private void buttonAction(int i, int j) {
	// ImageIcon x = new ImageIcon("x.jpg");
	// ImageIcon y = new ImageIcon("o.jpg");
	// if (enable) {
	//
	// // Sets the icons for X.
	// if (game.getPlayerTurn() == game.X) {
	// entries[i][j].setIcon(x);
	// game.setValue(game.X, i, j);
	// }
	//
	// // Sets the icons for Y.
	// else if (game.getPlayerTurn() == game.O) {
	// entries[i][j].setIcon(y);
	// game.setValue(game.O, i, j);
	// }
	// }
	// }
	//
	// /**
	// * Provides responses for the buttons of the new game window. This
	// is a
	// * continuation of the actionPerformed method.
	// *
	// * @param comp
	// * The inputs from the action listeners.
	// */
	// private void extraActions(JComponent comp) {
	//
	// // Human v. Computer.
	// if (comp == onePlayer)
	// game.enableDisableComp(true);
	//
	// // Human v. Human.
	// else if (comp == twoPlayer)
	// game.enableDisableComp(false);
	//
	// // Starts a new game.
	// else if (comp == ok)
	// okButton();
	//
	// // Closes the new game menu.
	// else if (comp == close) {
	// 
	// tempEnable = true;
	// }

	private void quitButtonResponse() {
		int reply = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to quit?", "Quitting?",
				JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION)
			System.exit(0);
	}
	
	private void restartButtonResponse(){
		int reply = JOptionPane.showConfirmDialog(null,
				"Are you sure that you want to restart?", "Restarting?",
				JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION){
			game.resetBoard();
			gameStart();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComponent comp = (JComponent) e.getSource();
		if(!game.hasBeenModified())
			restart.setEnabled(true);
		else
			restart.setEnabled(false);
		if (comp == ok) {
			if (rowInput.isVisible()) {
				newBlankGameInit();
			} else
				gameStart();
		} else if (comp == close) {
			closeNewGameWindow();
			if (!game.loadStatus())
				System.exit(0);
			else
				frame.setVisible(true);
		} else if (comp == newBlankGame) {
			showCloseBlankInput(true);
		} else if (comp == loadGame) {
			newgameType = LOAD;
			showCloseBlankInput(false);
		} 
		else if(comp == restart)
			restartButtonResponse();
		else if (comp == quit)
			quitButtonResponse();
		else if (comp == newGame){
			displayNewGameWindow();
		}
		else if (comp == about)
			JOptionPane.showMessageDialog(null,
					"Author: Sam Eary  Version: 12/10/2009", "About",
					JOptionPane.INFORMATION_MESSAGE);
		else
			entryActions(comp);	
	}
	
	private void entryActions(Component comp){
		for (int i = 0; i < entries.length; i++)
				for (int j = 0; j < entries[i].length; j++)
					if (comp == entries[i][j]) {
						try {
							game.addNumber(Integer
									.parseInt(entries[i][j].getText()
											.trim()), i, j);
							entries[i][j].setForeground(Color.BLUE);
							gameCompletenessCheck();
						} catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(null,
									"Please enter integers only.",
									"Entry Error",
									JOptionPane.ERROR_MESSAGE);
						} catch (InvalidGameStateException e2) {
							errorMessageDisplay(e2.getMessage());
							entries[i][j].setText("");
						}
					}
	}
	
	private void gameCompletenessCheck(){
		if(game.isComplete()){
			int reply = JOptionPane.showConfirmDialog(null,
					"Do you wish to play this game again?", "Game Over",
					JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION){
				game.resetBoard();
				gameStart();
				
			}
		}
			
	}
	
	private void displayNewGameWindow(){
		if(loadGame.isSelected())
			showCloseBlankInput(true);
		newGameWindow.setVisible(true);
		frame.setVisible(false);
		
	}

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

	private void newBlankGameInit() {
		try {
			game.newBlankGame(Integer.parseInt(rowInput.getText()),
					Integer.parseInt(colInput.getText()));
			closeNewGameWindow();
			gameStart();
		} catch (NumberFormatException e1) {
			JOptionPane.showMessageDialog(null,
					"Please enter integers only.", "Data Entry Error",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (InvalidGameStateException e1) {
			JOptionPane.showMessageDialog(null,
					"That is not a valid board size.",
					"Invalid Board Size",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void closeNewGameWindow() {
		newgameType = 0;
		newGameWindow.setVisible(false);
		loadGame.setSelected(false);
		newBlankGame.setSelected(false);
		rowInput.setText("");
		colInput.setText("");
		showCloseBlankInput(false);

	}

	private void showCloseBlankInput(boolean type) {
		colInput.setVisible(type);
		rowInput.setVisible(type);
		rowPrompt.setVisible(type);
		colPrompt.setVisible(type);
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		NumbrixGUIDeprecated g = new NumbrixGUIDeprecated();
	}
}
