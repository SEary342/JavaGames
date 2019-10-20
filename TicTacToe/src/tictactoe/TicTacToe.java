package tictactoe;

/*******************************************************************************
 * This the TicTacToe class for the Tic Tac Toe game. It handles all game
 * computations and logic. The starting player initially is random and then it
 * alternates. Warning the computer player is unbeatable. Also, in a computer
 * game the computer alternates starting turns with the human player, so a human
 * will alternate between X's and O's in this type of game.
 * 
 * @author Sam Eary
 * @version 11/15/2009
 */
public class TicTacToe {

	/** The board positions. */
	private int[][] board;

	/** The game state. True means the game is over. */
	private boolean state;

	/** The basic stats array of X, O, and Cat games. */
	private int[] stats;

	/** The win count for a computer player and the total number of rounds. */
	private int compGameStats, totalRounds;

	/** The player turn */
	private int playerTurn;

	/** Computer player on/off. */
	private boolean compEnable;

	/** Controls the initial turn. True makes the computer go first. */
	private boolean compStart;

	/** The X player number. */
	final int X = 1;

	/** The O player number. */
	final int O = 2;

	/** The empty space number. */
	final int NULL = 0;

	/**
	 * Primary constructor for the TicTacToe class. Instantiates all fields.
	 */
	public TicTacToe() {
		compEnable = false;

		// Randomizes starting computer turn.
		resetCompTurn();
		playerTurn = 1;
		board = new int[3][3];
		state = false;
		stats = new int[3];
		compGameStats = 0;

		// Board position construction.
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				board[i][j] = NULL;
	}

	/**
	 * Gives each position a value. Also checks for game ends and updates stats.
	 * 
	 * @param value
	 *            The player number.
	 * @param iLocation
	 *            The row number.
	 * @param jLocation
	 *            The column number.
	 * @return The state of the game. True means the game is over.
	 */
	public boolean setValue(int value, int iLocation, int jLocation) {

		// Adds a value to the given position if that position is open.
		if (getValue(iLocation, jLocation) == NULL && state == false) {
			board[iLocation][jLocation] = value;
			advancePlayerTurn();
		}

		// Checks for wins or cats.
		if (gameOver()) {
			if (playerWon(X)) {
				stats[0]++;
				if (compEnable && compStart)
					compGameStats++;
				totalRounds++;
			} else if (playerWon(O)) {
				stats[1]++;
				if (compEnable && compStart == false)
					compGameStats++;
				totalRounds++;
			} else if (boardFull()) {
				stats[2]++;
				totalRounds++;
			}
		}

		// Returns false if the game is still going.
		return state;
	}

	/**
	 * Gets the value of a specified position.
	 * 
	 * @param iLocation
	 *            The row number.
	 * @param jLocation
	 *            The column number.
	 * @return The value of the board position.
	 */
	public int getValue(int iLocation, int jLocation) {
		return board[iLocation][jLocation];
	}

	/**
	 * Tests for a winning player.
	 * 
	 * @param value
	 *            The player number.
	 * @return True if the game has been won.
	 */
	public boolean playerWon(int value) {
		boolean gameState = false;

		// Loops through the possible win conditions for rows and columns
		for (int i = 0; i < 3; i++) {

			// Columns
			if (board[0][i] == value && board[1][i] == value
					&& board[2][i] == value) {
				gameState = true;
			}

			// Rows
			if (board[i][0] == value && board[i][1] == value
					&& board[i][2] == value) {
				gameState = true;
			}
		}

		// Diagonals
		if (board[0][0] == value && board[1][1] == value
				&& board[2][2] == value) {
			gameState = true;
		} else if (board[0][2] == value && board[1][1] == value
				&& board[2][0] == value) {
			gameState = true;
		}
		return gameState;
	}

	/**
	 * Tests to see if the game is over. Updates the state of the game when the
	 * game is over.
	 * 
	 * @return The state of the game.
	 */
	public boolean gameOver() {
		if (playerWon(X) || playerWon(O) || boardFull()) {
			state = true;
		}
		return state;
	}

	/**
	 * Tests to see if the board is full.
	 * 
	 * @return True if the board is full.
	 */
	public boolean boardFull() {
		int emptySpace = 0;
		for (int i = 0; i != 3; i++)
			for (int j = 0; j != 3; j++)
				if (board[i][j] == NULL)
					emptySpace++;

		// No more spaces tells us there is a cat.
		if (emptySpace == 0)
			return true;
		else
			return false;
	}

	/**
	 * Gets the computers starting turn for each round.
	 * 
	 * @return True means that the computer goes first.
	 */
	public boolean getCompTurn() {
		return compStart;
	}

	/**
	 * Advances the computer's turn so the starting player will cycle.
	 */
	public void advanceCompStart() {
		if (compStart)
			compStart = false;
		else
			compStart = true;
	}

	/**
	 * Resets the initial computer turn when a new game is started.
	 */
	public void resetCompTurn() {
		double starter = (Math.random() * 2);
		if (starter <= 1)
			compStart = false;
		else
			compStart = true;
	}

	/**
	 * Resets the game board.
	 */
	public void reset() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				board[i][j] = NULL;
		state = false;
		playerTurn = 1;
	}

	/**
	 * Completely resets the game.
	 */
	public void newGame() {
		reset();
		resetCompTurn();
		resetStats();
	}
	
	/**
	 * Resets for a new round.
	 */
	public void nextRound(){
		reset();
		advanceCompStart();
	}

	/**
	 * Resets the stats for a stats reset called by the GUI.
	 */
	public void resetStats() {
		stats = new int[3];
		totalRounds = 0;
		compGameStats = 0;
	}

	/**
	 * Gets the stats array.
	 * 
	 * @return The array of basic statistics.
	 */
	public int[] getStats() {
		return stats;
	}

	/**
	 * The total number of rounds for ingame stats.
	 * 
	 * @return The total number of rounds.
	 */
	public int getTotalRounds() {
		return totalRounds;
	}

	/**
	 * Gets the computer win count.
	 * 
	 * @return The total number of computer wins.
	 */
	public int getCompStats() {
		return compGameStats;
	}

	/**
	 * Gets the player turn.
	 * 
	 * @return The player turn integer.
	 */
	public int getPlayerTurn() {
		return playerTurn;
	}

	/**
	 * Advances the player turn
	 */
	public void advancePlayerTurn() {
		if (playerTurn == 1) {
			playerTurn++;
		} else if (playerTurn == 2) {
			playerTurn = 1;
		}
	}

	/**
	 * Enables and disables the computer player.
	 * 
	 * @param trueFalse
	 *            True enables the computer.
	 */
	public void enableDisableComp(boolean trueFalse) {
		if (trueFalse) {
			compEnable = true;
		} else
			compEnable = false;
	}

	/**
	 * Gets the computer players online/offline status
	 * 
	 * @return True if the computer player is activated.
	 */
	public boolean getCompStatus() {
		return compEnable;
	}

	/**
	 * The process for taking the best move. It looks at possible wins, then
	 * blocks, and then anything else that remains.
	 * 
	 * @return The array of the best move.
	 */
	public int[] takeTurn() {
		int[] bestMove = { -1, -1 };

		// Checks for a win move. If compTurn is true then the computer is X.
		if (compStart
				&& (spaceChecker(X)[0] != bestMove[X] && spaceChecker(X)[1] != bestMove[1])) {
			bestMove = spaceChecker(X);
		} else if (compStart == false
				&& (spaceChecker(O)[0] != bestMove[0] && spaceChecker(O)[1] != bestMove[1])) {
			bestMove = spaceChecker(O);
		}

		// Checks for a block move.
		else if (compStart == false
				&& (spaceChecker(X)[0] != bestMove[0] && spaceChecker(X)[1] != bestMove[1])) {
			bestMove = spaceChecker(X);
		} else if (compStart
				&& (spaceChecker(O)[0] != bestMove[0] && spaceChecker(O)[1] != bestMove[1])) {
			bestMove = spaceChecker(O);
		}

		// Checks to see if the player is tricking the computer's logic.
		else if (compStart
				&& (blockSpaceAlpha(O)[0] != bestMove[0] && blockSpaceAlpha(O)[1] != bestMove[1])) {
			bestMove = blockSpaceAlpha(X);
		} else if (compStart == false
				&& (blockSpaceAlpha(X)[0] != bestMove[0] && blockSpaceAlpha(X)[1] != bestMove[1])) {
			bestMove = blockSpaceAlpha(X);
		} else if (compStart
				&& (blockSpaceBeta(O)[0] != bestMove[0] && blockSpaceBeta(O)[1] != bestMove[1])) {
			bestMove = blockSpaceBeta(X);
		} else if (compStart == false
				&& (blockSpaceBeta(X)[0] != bestMove[0] && blockSpaceBeta(X)[1] != bestMove[1])) {
			bestMove = blockSpaceBeta(X);
		}

		// Sends whatever moves remain.
		else {
			bestMove[0] = otherMoves()[0];
			bestMove[1] = otherMoves()[1];
		}
		return bestMove;
	}

	/**
	 * Works with takeTurn to check for blocking moves or win moves.
	 * 
	 * @param value
	 *            The position value.
	 * @return The best move if a win or a block is available.
	 */
	private int[] spaceChecker(int value) {
		int[] best = { -1, -1 };
		for (int w = 0; w < 3; w++) {

			// Columns
			if (board[0][w] == value && board[1][w] == value
					&& board[2][w] == NULL) {
				best[0] = 2;
				best[1] = w;
			} else if (board[0][w] == value && board[2][w] == value
					&& board[1][w] == NULL) {
				best[0] = 1;
				best[1] = w;
			} else if (board[1][w] == value && board[2][w] == value
					&& board[0][w] == NULL) {
				best[0] = 0;
				best[1] = w;
			}

			// Rows
			else if (board[w][0] == value && board[w][1] == value
					&& board[w][2] == NULL) {
				best[0] = w;
				best[1] = 2;
			} else if (board[w][0] == value && board[w][2] == value
					&& board[w][1] == NULL) {
				best[0] = w;
				best[1] = 1;
			} else if (board[w][1] == value && board[w][2] == value
					&& board[w][0] == NULL) {
				best[0] = w;
				best[1] = 0;
			}
		}
		if (best[0] == -1 && best[1] == -1) {
			best[0] = diagonalSpaceChecker(value)[0];
			best[1] = diagonalSpaceChecker(value)[1];
		}

		return best;
	}

	/**
	 * Checks for wins or blocks in conjunction with the spaceChecker and
	 * takeTurn methods to check for diagonal blocks/wins.
	 * 
	 * @param value
	 *            The position value.
	 * @return The best move if a diagonal win or a block is available.
	 */
	private int[] diagonalSpaceChecker(int value) {
		int[] best = { -1, -1 };
		if (board[0][0] == value && board[1][1] == value && board[2][2] == NULL) {
			best[0] = 2;
			best[1] = 2;
		} else if (board[0][0] == value && board[2][2] == value
				&& board[1][1] == NULL) {
			best[0] = 1;
			best[1] = 1;
		} else if (board[1][1] == value && board[2][2] == value
				&& board[0][0] == NULL) {
			best[0] = 0;
			best[1] = 0;
		} else if (board[0][2] == value && board[1][1] == value
				&& board[2][0] == NULL) {
			best[0] = 2;
			best[1] = 0;
		} else if (board[0][2] == value && board[2][0] == value
				&& board[1][1] == NULL) {
			best[0] = 1;
			best[1] = 1;
		} else if (board[1][1] == value && board[2][0] == value
				&& board[0][2] == NULL) {
			best[0] = 0;
			best[1] = 2;
		}
		return best;
	}

	/**
	 * Blocks the human from creating two win conditions and tricking the
	 * computer.
	 * 
	 * @param value
	 *            The value of the position on the board.
	 * @return The best move if one exists.
	 */
	private int[] blockSpaceAlpha(int value) {
		int[] best = { -1, -1 };
		if (((board[0][0] == value && board[2][2] == value) 
				|| (board[0][2] == value && board[2][0] == value))
				&& board[1][2] == NULL) {
			best[0] = 1;
			best[1] = 2;
		} else if (((board[0][0] == value && board[2][2] == value) 
				|| (board[0][2] == value && board[2][0] == value))
				&& board[0][1] == NULL) {
			best[0] = 0;
			best[1] = 1;
		} else if (((board[0][0] == value && board[2][2] == value) 
				|| (board[0][2] == value && board[2][0] == value))
				&& board[1][0] == NULL) {
			best[0] = 1;
			best[1] = 0;
		} else if (((board[0][0] == value && board[2][2] == value) 
				|| (board[0][2] == value && board[2][0] == value))
				&& board[2][1] == NULL) {
			best[0] = 2;
			best[1] = 1;
		}
		return best;
	}

	/**
	 * Blocks the human from creating two win conditions (different from
	 * blockSpaceAlpha) and tricking the computer.
	 * 
	 * @param value
	 *            The value of the position on the board.
	 * @return The best move if one exists.
	 */
	private int[] blockSpaceBeta(int value) {
		int[] best = { -1, -1 };
		if ((board[1][2] == value && board[2][1] == value)
				&& board[2][2] == NULL) {
			best[0] = 2;
			best[1] = 2;
		} else if ((board[0][1] == value && board[1][0] == value)
				&& board[0][0] == NULL) {
			best[0] = 0;
			best[1] = 0;
		} else if ((board[0][1] == value && board[1][2] == value)
				&& board[0][2] == NULL) {
			best[0] = 0;
			best[1] = 2;
		} else if ((board[1][0] == value && board[2][1] == value)
				&& board[2][0] == NULL) {
			best[0] = 2;
			best[1] = 0;
		}
		return best;
	}

	/**
	 * Finds the best move when there are no blocks or wins. This chooses the
	 * center, then the corners, and then the outside spaces.
	 * 
	 * @return The best move of any remaining moves.
	 */
	private int[] otherMoves() {
		int[] best = { -1, -1 };

		// Picks the center.
		if (board[1][1] == NULL) {
			best[0] = 1;
			best[1] = 1;
		}

		// Picks a corner.
		else if (board[0][0] == NULL || board[0][2] == NULL
				|| board[2][0] == NULL || board[2][2] == NULL) {
			best = cornerSelect();
		}

		// If all else fails, picks one of the outer spaces that aren't corners.
		else if (board[1][0] == NULL || board[0][1] == NULL
				|| board[1][2] == NULL || board[2][1] == NULL) {
			best = spaceSelect();
		}
		return best;
	}

	/**
	 * Selects an open corner space.
	 * 
	 * @return An open corner space.
	 */
	private int[] cornerSelect() {
		int[] best = new int[2];
		if (board[0][0] == NULL) {
			best[0] = 0;
			best[1] = 0;
		} else if (board[2][2] == NULL) {
			best[0] = 2;
			best[1] = 2;
		} else if (board[2][0] == NULL) {
			best[0] = 2;
			best[1] = 0;
		} else if (board[0][2] == NULL) {
			best[0] = 0;
			best[1] = 2;
		}
		return best;
	}

	/**
	 * Picks an open space that is not the center or the corner.
	 * 
	 * @return An open non-corner/non-center space.
	 */
	private int[] spaceSelect() {
		int[] best = new int[2];
		if (board[1][0] == NULL) {
			best[0] = 1;
			best[1] = 0;
		} else if (board[0][1] == NULL) {
			best[0] = 0;
			best[1] = 1;
		} else if (board[1][2] == NULL) {
			best[0] = 1;
			best[1] = 2;
		} else if (board[2][1] == NULL) {
			best[0] = 2;
			best[1] = 1;
		}
		return best;
	}
}
