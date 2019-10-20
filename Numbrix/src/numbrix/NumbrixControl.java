package numbrix;

/**************************************************************************
 * A controller for the the GUI of the Numbrix game. Performs all
 * actions and interactions related to the game.
 * 
 * @author Sam Eary
 * @version 1.0
 **************************************************************************/
public class NumbrixControl {

	/** The number representing an invalid size error. */
	public static final int INVALID_SIZE = 2;

	/** The number representing an input error. */
	public static final int INTEGERS_ONLY = 1;

	/** The number representing valid blank game creation. */
	public static final int VALID = 0;

	/** The model of the Numbrix game. */
	private NumbrixEngine game;

	/**************************************************************************
	 * Constructor for the {@code NumbrixControl} class. Instantiates
	 * the game class.
	 **************************************************************************/
	public NumbrixControl() {
		game = new NumbrixEngine();
	}

	/**************************************************************************
	 * Gets the game-board from the game.
	 * 
	 * @return an array representing the game-board.
	 **************************************************************************/
	public int[][] getGameBoard() {
		return game.getBoard();
	}

	/**************************************************************************
	 * Gets the number of rows in the game.
	 * 
	 * @return the number of columns.
	 **************************************************************************/
	public int getRows() {
		return game.getRows();
	}

	/**************************************************************************
	 * Gets the number of columns in the game.
	 * 
	 * @return the number of columns.
	 **************************************************************************/
	public int getCols() {
		return game.getCols();
	}

	/**************************************************************************
	 * Gets the modification status of the game.
	 * 
	 * @return {@code true} if the game has been modified, {@code false}
	 *         otherwise.
	 **************************************************************************/
	public boolean gameHasBeenModified() {
		return game.hasBeenModified();
	}

	/**************************************************************************
	 * Gets the load status of the game.
	 * 
	 * @return {@code true} if the game has been loaded, {@code false}
	 *         otherwise.
	 **************************************************************************/
	public boolean getLoadStatus() {
		return game.loadStatus();
	}

	/**************************************************************************
	 * Resets the game-board to its original gamestate.
	 **************************************************************************/
	public void reset() {
		game.resetBoard();
	}

	/**************************************************************************
	 * Loads the specified game from the file.
	 * 
	 * @param filename
	 *            The file representing the game to be loaded.
	 * @return {@code true} if the game has loaded, {@code false} if
	 *         there were load errors.
	 **************************************************************************/
	public boolean loadGame(String filename) {
		try {
			game.loadGame(filename);
			return true;
		} catch (Exception e1) {
			return false;
		}
	}

	/**************************************************************************
	 * Adds entries to the game-board.
	 * 
	 * @param entry
	 *            The string of the number to be sent to the game-board.
	 * @param row
	 *            The row of the number sent to the game-board.
	 * @param col
	 *            The column of the number sent to the game-board.
	 * @throws NumberFormatException
	 *             If an integer was entered for the entry.
	 * @throws InvalidGameStateException
	 *             If the entry is not valid.
	 **************************************************************************/
	public void addEntry(String entry, int row, int col)
			throws NumberFormatException, InvalidGameStateException {
		game.addNumber(Integer.parseInt(entry), row, col);
	}

	/**************************************************************************
	 * Gets the games completion status.
	 * 
	 * @return {@code true} if the game is complete, {@code false}
	 *         otherwise.
	 **************************************************************************/
	public boolean isComplete() {
		return game.isComplete();
	}
	
	/**************************************************************************
	 * Finds the first empty space on the game-board.
	 * @return an array containing the row and column of the empty space.
	 **************************************************************************/
	public int[] getFirstEmptySpace(){
		int[] empty = {0,0};
		int[][] board = game.getBoard();
		for(int i = 0; i< board.length; i++)
			for(int j = 0; j< board[i].length; j++)
				if(board[i][j] == 0){
					empty[0]=i;
					empty[1]=j;
					return empty;
				}
		return empty;
	}

	/**************************************************************************
	 * Creates a new blank game based on the sizes entered.
	 * 
	 * @param row
	 *            The string representing the number of rows.
	 * @param col
	 *            The string representing the number of columns.
	 * @return {@code VALID} if the game was created, {@code
	 *         INTEGERS_ONLY} if row or col entries were not integers,
	 *         or {@code INVALID_SIZE} if the size of the game was too
	 *         small.
	 **************************************************************************/
	public int createNewBlankGame(String row, String col) {
		try {
			game.newBlankGame(Integer.parseInt(row.trim()), Integer
					.parseInt(col.trim()));
			return VALID;
		} catch (NumberFormatException e) {
			return INTEGERS_ONLY;
		} catch (InvalidGameStateException e) {
			return INVALID_SIZE;
		}
	}
	
	/**************************************************************************
	 * Erases the current game and starts a new one.
	 **************************************************************************/
	public void unloadGame(){
		game = null;
		game = new NumbrixEngine();
	}
}
