package tictactoe;

/**
 * The test class for the Tic Tac Toe game. It will start the gui or test the
 * TicTacToe class.
 * 
 * @author Sam Eary
 * @version 11/15/2009
 */
public class Testing {

	/** The tic tac toe game */
	private TicTacToe game;

	/**
	 * Instantiates the game class for testing.
	 */
	public Testing() {
		game = new TicTacToe();
	}

	/**
	 * The GUI instantiator.
	 * 
	 * @param args
	 *            An array of string references.
	 */
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		GUI g = new GUI();
	}

	/**
	 * Tests the TicTacToe Class.
	 */
	public void testing() {
		getSet();
		winCheck1();
		compStuff();
		playerStuff();
		playGame();
		winCheck2();
		statsCheck();
		winCodeChecker();
		newGameRoundCheck();
	}

	/**
	 * Checks the get/set values methods.
	 */
	private void getSet() {

		// Get,set values, and reset.
		System.out.println("Value of board[1][1].");
		System.out.println("Output: " + game.getValue(1, 1));
		System.out.println("Expected: 0");
		System.out.println(" ");
		System.out.println("Set value of board[1][1] to O.");
		System.out.println("Output: " + game.setValue(game.O, 1, 1));
		System.out.println("Expected: false");
		System.out.println(" ");
		System.out.println("Value of board[1][1].");
		System.out.println("Output: " + game.getValue(1, 1));
		System.out.println("Expected: 2");
		System.out.println(" ");
		game.reset();
		System.out.println("Value of board[1][1].");
		System.out.println("Output: " + game.getValue(1, 1));
		System.out.println("Expected: 0");
		System.out.println(" ");
		System.out.println("Set value of board[1][1] to X.");
		System.out.println("Output: " + game.setValue(game.X, 1, 1));
		System.out.println("Expected: false");
		System.out.println(" ");
		System.out.println("Value of board[1][1].");
		System.out.println("Output: " + game.getValue(1, 1));
		System.out.println("Expected: 1");
		game.reset();
		System.out.println(" ");
	}

	/**
	 * Checks win status.
	 */
	private void winCheck1() {

		// playerWon(X) check and game over check.
		System.out.println("Check to see if X won.");
		System.out.println("Ouput: " + game.playerWon(game.X));
		System.out.println("Expected: false");
		System.out.println(" ");
		System.out.println("Check to see if game is over");
		System.out.println("Output: " + game.gameOver());
		System.out.println("Expected: false");
		System.out.println(" ");

		// playerWon(O) check and game over check.
		System.out.println("Check to see if O won.");
		System.out.println("Ouput: " + game.playerWon(game.O));
		System.out.println("Expected: false");
		System.out.println(" ");
		System.out.println("Check to see if game is over");
		System.out.println("Output: " + game.gameOver());
		System.out.println("Expected: false");
		System.out.println(" ");

		// Check for cat game.
		System.out.println("Check to see if board is full.");
		System.out.println("Output: " + game.boardFull());
		System.out.println("Expected: false");
		System.out.println(" ");

		// Total # of Rounds
		System.out.println("Get the total number of rounds.");
		System.out.println("Output: " + game.getTotalRounds());
		System.out.println("Expected: 0");
		System.out.println(" ");
	}

	/**
	 * Checks computer related items.
	 */
	private void compStuff() {

		// Comp start turn.
		System.out.println("Get the computer start turn.");
		System.out.println("Output: " + game.getCompTurn());
		System.out.println("Expected: true or false.");
		System.out.println(" ");

		// Resetting and advancing comp turn.
		System.out.println("Reset the computer turn and check.");
		game.resetCompTurn();
		System.out.println("Output: " + game.getCompTurn());
		System.out.println("Expected: Either true or false");
		System.out.println(" ");
		System.out.println("Advance the CompTurn and check it.");
		game.advanceCompStart();
		System.out.println("Output: " + game.getCompTurn());
		System.out.println("Expected: True if the last message was false.");
		System.out.println(" ");

		// Enable/disable comp and check.
		System.out.println("Enable the computer and check.");
		game.enableDisableComp(true);
		System.out.println("Output: " + game.getCompStatus());
		System.out.println("Expected: true");
		System.out.println(" ");
		System.out.println("Disable the computer and check.");
		game.enableDisableComp(false);
		System.out.println("Output: " + game.getCompStatus());
		System.out.println("Expected: false");
		System.out.println(" ");
	}

	/**
	 * Checks player turns.
	 */
	private void playerStuff() {

		// Get the player turn and advance it.
		System.out.println("Get the player turn.");
		System.out.println("Output: " + game.getPlayerTurn());
		System.out.println("Expected: 1");
		System.out.println(" ");
		System.out.println("Advance the player turn.");
		game.advancePlayerTurn();
		System.out.println("Output: " + game.getPlayerTurn());
		System.out.println("Expected: 2");
		System.out.println(" ");
		System.out.println("Advance the player turn.");
		game.advancePlayerTurn();
		System.out.println("Output: " + game.getPlayerTurn());
		System.out.println("Expected: 1");
		System.out.println(" ");
	}

	/**
	 * Plays a test game to test computer logic.
	 */
	private void playGame() {

		// Play a computer v. computer game with takeTurn method to test basic
		// logic for corners, spaces, and spaceChecker.
		game.reset();
		System.out.println("Get the best move.");
		System.out.println("Output: " + game.takeTurn()[0] + ","
				+ game.takeTurn()[1]);
		System.out.println("Expected: 1,1");
		System.out.println(" ");
		System.out.println("Take that turn.");
		System.out.println("Output: " + game.setValue(game.X, 1, 1));
		System.out.println("Expected: false");
		System.out.println(" ");
		System.out.println("Get the best move.");
		System.out.println("Output: " + game.takeTurn()[0] + ","
				+ game.takeTurn()[1]);
		System.out.println("Expected: 0,0");
		System.out.println(" ");
		System.out.println("Take that turn.");
		System.out.println("Output: " + game.setValue(game.O, 0, 0));
		System.out.println("Expected: false");
		System.out.println(" ");
		System.out.println("Get the best move.");
		System.out.println("Output: " + game.takeTurn()[0] + ","
				+ game.takeTurn()[1]);
		System.out.println("Expected: 2,2");
		System.out.println(" ");
		System.out.println("Take that turn.");
		System.out.println("Output: " + game.setValue(game.X, 2, 2));
		System.out.println("Expected: false");
		System.out.println(" ");
		System.out.println("Get the best move.");
		System.out.println("Output: " + game.takeTurn()[0] + ","
				+ game.takeTurn()[1]);
		System.out.println("Expected: 2,0");
		System.out.println(" ");
		System.out.println("Take that turn.");
		System.out.println("Output: " + game.setValue(game.O, 2, 0));
		System.out.println("Expected: false");
		System.out.println(" ");
		System.out.println("Get the best move.");
		System.out.println("Output: " + game.takeTurn()[0] + ","
				+ game.takeTurn()[1]);
		System.out.println("Expected: 1,0");
		System.out.println(" ");
		System.out.println("Take that turn.");
		System.out.println("Output: " + game.setValue(game.X, 1, 0));
		System.out.println("Expected: false");
		System.out.println(" ");
		System.out.println("Get the best move.");
		System.out.println("Output: " + game.takeTurn()[0] + ","
				+ game.takeTurn()[1]);
		System.out.println("Expected: 1,2");
		System.out.println(" ");
		System.out.println("Take that turn.");
		System.out.println("Output: " + game.setValue(game.O, 1, 2));
		System.out.println("Expected: false");
		System.out.println(" ");
		System.out.println("Get the best move.");
		System.out.println("Output: " + game.takeTurn()[0] + ","
				+ game.takeTurn()[1]);
		System.out.println("Expected: 0,2");
		System.out.println(" ");
		System.out.println("Take that turn.");
		System.out.println("Output: " + game.setValue(game.X, 0, 2));
		System.out.println("Expected: false");
		System.out.println(" ");
		System.out.println("Get the best move.");
		System.out.println("Output: " + game.takeTurn()[0] + ","
				+ game.takeTurn()[1]);
		System.out.println("Expected: 0,1");
		System.out.println(" ");
		System.out.println("Take that turn.");
		System.out.println("Output: " + game.setValue(game.O, 0, 1));
		System.out.println("Expected: false");
		System.out.println(" ");
		System.out.println("Get the best move.");
		System.out.println("Output: " + game.takeTurn()[0] + ","
				+ game.takeTurn()[1]);
		System.out.println("Expected: 2,1");
		System.out.println(" ");
		System.out.println("Take that turn.");
		System.out.println("Output: " + game.setValue(game.X, 2, 1));
		System.out.println("Expected: true");
		System.out.println(" ");
	}

	/**
	 * Checks for a cat game.
	 */
	private void winCheck2() {

		// Check cat game.
		System.out.println("Check for a cat game.");
		System.out.println("Output: " + game.boardFull());
		System.out.println("Expected: true");
		System.out.println(" ");
	}

	/**
	 * Checks and resets stats.
	 */
	private void statsCheck() {

		// Confirm game over and check stats/total rounds.
		System.out.println("Check for a game over.");
		System.out.println("Output: " + game.gameOver());
		System.out.println("Expected: true");
		System.out.println(" ");
		System.out.println("Get the stats.");
		System.out.println("Output: " + game.getStats()[0] + ","
				+ game.getStats()[1] + "," + game.getStats()[2]);
		System.out.println("Expected: 0,0,1");
		System.out.println(" ");
		System.out.println("Get the total rounds.");
		System.out.println("Output: " + game.getTotalRounds());
		System.out.println("Expected: 1");
		System.out.println(" ");
		System.out.println("Get the computer stats");
		System.out.println("Output: " + game.getCompStats());
		System.out.println("Expected: 0");
		System.out.println(" ");

		// Resetting stats
		game.resetStats();
		System.out.println("Get the stats after reset.");
		System.out.println("Output: " + game.getStats()[0] + ","
				+ game.getStats()[1] + "," + game.getStats()[2]);
		System.out.println("Expected: 0,0,0");
		System.out.println(" ");
		System.out.println("Get the total rounds.");
		System.out.println("Output: " + game.getTotalRounds());
		System.out.println("Expected: 0");
		System.out.println(" ");
		System.out.println("Get the computer stats");
		System.out.println("Output: " + game.getCompStats());
		System.out.println("Expected: 0");
		game.reset();
		System.out.println(" ");
	}

	/**
	 * Checks the win code.
	 */
	private void winCodeChecker() {

		// Checking win code
		System.out.println("Checking win code with a row of X's: ");
		game.setValue(game.X, 0, 0);
		game.setValue(game.X, 0, 1);
		game.setValue(game.X, 0, 2);
		System.out.println("Output: " + game.playerWon(game.X));
		System.out.println("Expected: true");
		game.reset();
		System.out.println(" ");
		System.out.println("Checking win code with a row of X's: ");
		game.setValue(game.X, 1, 0);
		game.setValue(game.X, 1, 1);
		game.setValue(game.X, 1, 2);
		System.out.println("Output: " + game.playerWon(game.X));
		System.out.println("Expected: true");
		game.reset();
		System.out.println(" ");
		System.out.println("Checking win code with a row of X's: ");
		game.setValue(game.X, 2, 0);
		game.setValue(game.X, 2, 1);
		game.setValue(game.X, 2, 2);
		System.out.println("Output: " + game.playerWon(game.X));
		System.out.println("Expected: true");
		game.reset();
		System.out.println(" ");
		System.out.println("Checking win code with a row of X's: ");
		game.setValue(game.X, 0, 0);
		game.setValue(game.X, 0, 1);
		game.setValue(game.X, 0, 2);
		System.out.println("Output: " + game.playerWon(game.X));
		System.out.println("Expected: true");
		game.reset();
		System.out.println(" ");
		System.out.println("Checking win code with a column of X's: ");
		game.setValue(game.X, 0, 0);
		game.setValue(game.X, 1, 0);
		game.setValue(game.X, 2, 0);
		System.out.println("Output: " + game.playerWon(game.X));
		System.out.println("Expected: true");
		game.reset();
		System.out.println(" ");
		System.out.println("Checking win code with a column of X's: ");
		game.setValue(game.X, 0, 1);
		game.setValue(game.X, 1, 1);
		game.setValue(game.X, 2, 1);
		System.out.println("Output: " + game.playerWon(game.X));
		System.out.println("Expected: true");
		game.reset();
		System.out.println(" ");
		System.out.println("Checking win code with a column of X's: ");
		game.setValue(game.X, 0, 2);
		game.setValue(game.X, 1, 2);
		game.setValue(game.X, 2, 2);
		System.out.println("Output: " + game.playerWon(game.X));
		System.out.println("Expected: true");
		game.reset();
		System.out.println(" ");
		System.out.println("Checking win code with a diagonal of X's: ");
		game.setValue(game.X, 0, 0);
		game.setValue(game.X, 1, 1);
		game.setValue(game.X, 2, 2);
		System.out.println("Output: " + game.playerWon(game.X));
		System.out.println("Expected: true");
		game.reset();
		System.out.println(" ");
		System.out.println("Checking win code with a diagonal of X's: ");
		game.setValue(game.X, 2, 0);
		game.setValue(game.X, 1, 1);
		game.setValue(game.X, 0, 2);
		System.out.println("Output: " + game.playerWon(game.X));
		System.out.println("Expected: true");
		game.reset();
		System.out.println(" ");
	}

	/**
	 * Checks the new game methods.
	 */
	private void newGameRoundCheck() {

		// Tests the new game method.
		game.newGame();
		System.out.println("Get the stats after new game reset.");
		System.out.println("Output: " + game.getStats()[0] + ","
				+ game.getStats()[1] + "," + game.getStats()[2]);
		System.out.println("Expected: 0,0,0");
		System.out.println(" ");
		System.out.println("Get the total rounds.");
		System.out.println("Output: " + game.getTotalRounds());
		System.out.println("Expected: 0");
		System.out.println(" ");
		System.out.println("Get the computer stats");
		System.out.println("Output: " + game.getCompStats());
		System.out.println("Expected: 0");
		System.out.println(" ");
		System.out.println("Reset the computer turn and check.");
		game.resetCompTurn();
		System.out.println("Output: " + game.getCompTurn());
		System.out.println("Expected: Either true or false");
		System.out.println(" ");

		// Tests the next round method.
		game.nextRound();
		System.out.println("Testing nextRound()");
		System.out.println("Output: " + game.getCompTurn());
		System.out.println("Expected: Either true or false");
		System.out.println(" ");
	}
}
