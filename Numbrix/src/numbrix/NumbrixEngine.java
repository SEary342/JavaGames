package numbrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**************************************************************************
 * A class to represent the state of a Numbrix game. Loads Numbrix files
 * and checks for validity and completeness.
 * 
 * @author Sam Eary
 * @version 1.0
 **************************************************************************/
public class NumbrixEngine extends java.util.Observable {

	/** The size of an empty row. */
	public static final int EMPTY = 0;

	/** The number added or subtracted to go to an adjacent element. */
	public static final int INCREMENT = 1;

	/** Represents values out an array's lower range. */
	public static final int OUT_OF_RANGE = -1;

	/** The message passed when there is a duplicate value. */
	public static final String DUPLICATE = "Data duplicated";

	/** The message passed when a number is out of the possible range. */
	public static final String RANGE_ERROR = "Data out of range";

	/** The message passed when a number has a directly invalid error. */
	public static final String INVALID_ERROR = "Data directly invalid";

	/** Specifies if the uniqueness test is in an add number state. */
	private static final boolean ADD = false;

	/** Specifies if the uniqueness test is in a board loading state. */
	private static final boolean LOAD = true;

	/** Turns intentional bugs on or off. */
	public static boolean INTENTIONAL_BUGS;

	/** The array representing the game board. */
	private int[][] board, originalBoard;

	/** The number of user defined changes to the original board. */
	private int changes;

	/** The number of rows, of columns, and the size of the board. */
	private int rows, columns, board_size;

	/** The array representing whether or not data has been duplicated. */
	private boolean[] unique, uniqueOriginal;

	/**************************************************************************
	 * Primary constructor for the {@code NumbrixEngine} class.
	 * Instantiates the class.
	 **************************************************************************/
	public NumbrixEngine() {
		changes = 0;
		rows = 0;
		columns = 0;
		board_size = 0;
	}

	/**************************************************************************
	 * Loads a game from a text file and verifies that it is valid.
	 * 
	 * @param fileName
	 *            The filename of the text file to be loaded.
	 * @throws ParseException
	 *             if there is an error while parsing.
	 * @throws InvalidGameStateException
	 *             if the file parses but the game is invalid.
	 * @throws FileNotFoundException
	 *             if the filename cannot be found.
	 **************************************************************************/
	public void loadGame(String fileName) throws ParseException,
			InvalidGameStateException, FileNotFoundException {
		Scanner sc = new Scanner(new File(fileName));
		loadGame(sc);
	}

	/**************************************************************************
	 * Loads the size and data sections of the text file. Also
	 * instantiates the instance variables defendant on board
	 * dimensions.
	 * 
	 * @param input
	 *            The scanner representing the text file being parsed.
	 * @throws ParseException
	 *             if there is an error while parsing.
	 * @throws InvalidGameStateException
	 *             if the file parses, but the game is invalid.
	 **************************************************************************/
	private void loadGame(Scanner input) throws ParseException,
			InvalidGameStateException {
		sizeLoader(input);

		// Setting the size of the board.
		update();
		dataLoader(input);
		finalValidityChecker();
		duplicateBoard();
	}

	/**************************************************************************
	 * Loads the game size.
	 * 
	 * @param input
	 *            The scanner representing the text file being parsed.
	 * @throws ParseException
	 *             if there is an error while parsing.
	 * @throws InvalidGameStateException
	 *             if the file parses, but the game is invalid.
	 **************************************************************************/
	private void sizeLoader(Scanner input) throws ParseException,
			InvalidGameStateException {
		String size = "";
		Scanner sc = null;
		size = getNextDataLine(input);
		sc = new Scanner(size);
		while (sc.hasNext()) {
			try {
				rows = sc.nextInt();
				columns = sc.nextInt();

				// This happens if there are too many numbers in the
				// size.
				if (sc.hasNextInt())
					throw new ParseException("Size too Long", 111);
			} catch (InputMismatchException e) {
				throw new ParseException(
						"Improper size input.  Must contain integers only",
						113);
			} catch (NoSuchElementException e1) {
				throw new ParseException("Missing Size", 117);
			}
			if (rows < 1 || columns < 1)
				throw new InvalidGameStateException(RANGE_ERROR);
		}
	}

	/**************************************************************************
	 * Gets the next data line of the file. Also, it will remove all
	 * information after the # symbol.
	 * 
	 * @param input
	 *            The scanner representing the text file being parsed.
	 * @return the next trimmed and edited data line of the text file.
	 * @throws ParseException
	 *             if there is an error while parsing.
	 **************************************************************************/
	private String getNextDataLine(Scanner input) throws ParseException {
		String data = "";
		while (input.hasNext()) {
			data = input.nextLine().trim();
			if (data.length() != 0 && data.charAt(0) != '#') {
				if (data.indexOf('#') == OUT_OF_RANGE)
					return data;
				else
					return data.substring(0, data.indexOf('#')).trim();
			}
		}

		// This only happens if there is nothing left to read or there
		// is an extra row.
		return "";
	}

	/**************************************************************************
	 * Loads the data section. Also tests for {@code ParseExceptions}
	 * and {@code InvalidGameStateExeptions}.
	 * 
	 * @param input
	 *            The scanner representing the text file being parsed.
	 * @throws ParseException
	 *             if there is an error while parsing.
	 * @throws InvalidGameStateException
	 *             if the file parses, but the game is invalid.
	 **************************************************************************/
	private void dataLoader(Scanner input) throws ParseException,
			InvalidGameStateException {
		String data = "";
		Scanner sc = null;

		// Scanning in each element of a single line with another
		// scanner.
		for (int i = 0; i < board.length; i++) {
			data = getNextDataLine(input);
			sc = new Scanner(data);
			for (int j = 0; j < board[i].length; j++) {
				if (sc.hasNextInt())
					board[i][j] = invalidEntryChecker(sc.nextInt(),
							LOAD);
				else
					throw new ParseException("Invalid Data", 176);
				if (j + INCREMENT == board[i].length && sc.hasNextInt())
					throw new ParseException("Too many columns", 178);
			}
		}
		if (getNextDataLine(input) != "")
			throw new ParseException("Too Many Rows", 182);
	}

	/**************************************************************************
	 * Checks for invalid or duplicate data. It only checks for data
	 * that has problems when it is input. For directly invalid data see
	 * {@code directlyInvalidChecker()}. It will also return the input
	 * data if it is valid for these simple rules.
	 * 
	 * @param data
	 *            The entry to be checked.
	 * @param type
	 *            {@code true} if the game is in a load state, {@code
	 *            false} if the game is in an add state.
	 * @return the data if it is valid.
	 * @throws InvalidGameStateException
	 *             if data is out of range or there are duplicates.
	 **************************************************************************/
	private int invalidEntryChecker(int data, boolean type)
			throws InvalidGameStateException {

		// Testing for data values out of the possible range.
		if ((data < 1 || data > board_size) && data != EMPTY)
			throw new InvalidGameStateException(RANGE_ERROR);

		// Testing for duplicates
		if (!uniquenessTest(data, type))
			throw new InvalidGameStateException(DUPLICATE);
		return data;
	}

	/**************************************************************************
	 * Tests for uniqueness of numbers input. Also sets the unique array
	 * boolean value at the index of the input number to true if the
	 * value at that index is false.
	 * 
	 * @param data
	 *            The data entry to be tested for uniqueness.
	 * @param type
	 *            {@code true} if the game is in a load state, {@code
	 *            false} if the game is in an add state.
	 * @return {@code true} if data is unique. {@code false} if it is
	 *         not unique.
	 **************************************************************************/
	private boolean uniquenessTest(int data, boolean type) {

		// This only occurs if the data value is zero.
		if (data == EMPTY)
			return true;

		// The actual testing.
		if (!unique[data] && type) {
			unique[data] = true;
			return unique[data];
		} else if (!unique[data] && !type)
			return true;
		else
			return false;
	}

	/**************************************************************************
	 * Iterates through each element of the {@code board} and checks for
	 * any direct errors.
	 * 
	 * @throws InvalidGameStateException
	 *             if there is a directly invalid entry on the board.
	 **************************************************************************/
	private void finalValidityChecker()
			throws InvalidGameStateException {
		int errors = 0;
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++)
				if (!directlyInvalidChecker(board[i][j], i, j))
					errors++;
		if (errors != EMPTY)
			throw new InvalidGameStateException(INVALID_ERROR);
	}

	/**************************************************************************
	 * Tests input data to see if it is directly invalid. For, example
	 * if an entry is 5, and is surrounded by 25, 81, 56, and 42, then
	 * it is directly invalid.
	 * 
	 * @param data
	 *            The value of the input data.
	 * @param row
	 *            The row where the data resides in the game board.
	 * @param col
	 *            The column where the data resides in the game board.
	 * @return {@code true} if the specified data value is not directly
	 *         invalid compared to its neighbors. {@code false}
	 *         otherwise.
	 **************************************************************************/
	private boolean directlyInvalidChecker(int data, int row, int col) {
		int dataHigh = data + INCREMENT;
		int dataLow = data - INCREMENT;
		int zeros = 0;
		int match = 0;
		int[] grid = adjacentLoader(row, col);

		// Iterates through the grid array looking for adjacent zeros
		// and +1, -1 numbers.
		for (int i = 0; i < grid.length; i++) {
			if (grid[i] == EMPTY)
				zeros++;
			if (grid[i] == dataHigh || grid[i] == dataLow)
				match++;
			if (data == EMPTY)
				for (int j = 0; j < grid.length; j++)
					if (grid[i] - grid[j] == 2
							|| grid[i] - grid[j] == -2)
						return true;
		}

		// Valid numbers
		if ((zeros > 1 || match > 1))
			return true;

		// The cases of the smallest and largest numbers.
		else if ((data == 1 || data == board_size)
				&& (match == 1 || zeros >= 1))
			return true;

		// // The case of exactly one open neighbor.
		else if (match == 1 && zeros == 1)
			return true;
		else
			return false;
	}

	/**************************************************************************
	 * Loads the spaces adjacent to the space being tested in {@code
	 * DirectlyInvalid()}.
	 * 
	 * @param row
	 *            The row of the current data entry.
	 * @param col
	 *            The column of the current data entry.
	 * @return the array of the values of the adjacent spaces.
	 **************************************************************************/
	private int[] adjacentLoader(int row, int col) {

		// This array is filled with numbers out of range of anything on
		// the board.
		int[] grid = { OUT_OF_RANGE, OUT_OF_RANGE, OUT_OF_RANGE,
				OUT_OF_RANGE };
		if (row > 0)
			grid[Direction.North.ordinal()] = board[row - INCREMENT][col];
		if (row < board.length - 1)
			grid[Direction.South.ordinal()] = board[row + INCREMENT][col];
		if (col > 0)
			grid[Direction.West.ordinal()] = board[row][col - INCREMENT];
		if (col < board[row].length - 1)
			grid[Direction.East.ordinal()] = board[row][col + INCREMENT];
		return grid;
	}

	/**************************************************************************
	 * Tests the board for completeness.
	 * 
	 * @return {@code true} if the puzzle has no empty spaces, {@code
	 *         false} otherwise.
	 **************************************************************************/
	public boolean isComplete() {

		// We don't need to look in the zero position, because there
		// should be no zeros if the game is complete.
		for (int i = 1; i < unique.length; i++)
			if (unique[i] == false)
				return false;
		return true;
	}

	/**************************************************************************
	 * Checks to see if a game has been loaded.
	 * 
	 * @return {@code true} if a board has been loaded. {@code false}
	 *         otherwise.
	 **************************************************************************/
	public boolean loadStatus() {
		return (board != null);
	}

	/**************************************************************************
	 * Gets the number of rows in the current game board.
	 * 
	 * @return the number of rows.
	 **************************************************************************/
	public int getRows() {
		return rows;
	}

	/**************************************************************************
	 * Gets the number of columns.
	 * 
	 * @return the number of columns.
	 **************************************************************************/
	public int getCols() {
		return columns;
	}

	/**************************************************************************
	 * Gets the game board array.
	 * 
	 * @return the array representing the game board.
	 **************************************************************************/
	public int[][] getBoard() {
		return board;
	}

	/**************************************************************************
	 * Gets the size of the game board.
	 * 
	 * @return the size of the game board.
	 **************************************************************************/
	public int getBoardSize() {
		return board_size;
	}

	/**************************************************************************
	 * Allows numbers to be added to the game board.
	 * 
	 * @param number
	 *            the number to be added to the board.
	 * @param row
	 *            the row of the number to be added.
	 * @param col
	 *            the column of the number to be added.
	 * @throws InvalidGameStateException
	 *             if there is a problem with the number being added.
	 **************************************************************************/
	public void addNumber(int number, int row, int col)
			throws InvalidGameStateException {

		// Checks for all invalid exceptions and then adds the new
		// number to the board.
		invalidEntryChecker(number, ADD);
		if (directlyInvalidChecker(number, row, col)) {
			unique[board[row][col]] = false;
			unique[number] = true;
			board[row][col] = number;
			changes++;
		} else {

			// Duplicates throw an exception earlier so it causes no
			// problems to change unique.
			unique[number] = false;
			throw new InvalidGameStateException(INVALID_ERROR);
		}
	}

	/**************************************************************************
	 * Checks to see if the original board has been modified.
	 * 
	 * @return {@code true} if the board has not been changed, {@code
	 *         false} otherwise.
	 **************************************************************************/
	public boolean hasBeenModified() {
		return changes == 0;
	}

	/**************************************************************************
	 * Duplicates the board that was loaded, before it is modified.
	 **************************************************************************/
	private void duplicateBoard() {
		originalBoard = new int[rows][columns];
		uniqueOriginal = new boolean[unique.length];
		copyBooleanArray(uniqueOriginal, unique);
		copyBoards(originalBoard, board);
	}

	/**************************************************************************
	 * Resets a board to its original form.
	 **************************************************************************/
	public void resetBoard() {
		copyBoards(board, originalBoard);
		copyBooleanArray(unique, uniqueOriginal);
		changes = 0;
	}

	/**************************************************************************
	 * Duplicates one board and stores it in another one.
	 * 
	 * @param copy
	 *            The board that will become the copy.
	 * @param base
	 *            The board to be copied.
	 **************************************************************************/
	private void copyBoards(int[][] copy, int[][] base) {

		// Makes a copy of one board for resetting purposes.
		for (int i = 0; i < base.length; i++)
			for (int j = 0; j < base[i].length; j++)
				copy[i][j] = base[i][j];
	}

	/**************************************************************************
	 * Makes a copy of a boolean array.
	 * @param copy The array that will become the copy.
	 * @param base The array to be copied.
	 **************************************************************************/
	private void copyBooleanArray(boolean[] copy, boolean[] base) {
		for (int i = 0; i < base.length; i++)
			copy[i] = base[i];
	}

	/**************************************************************************
	 * Starts a totally blank game with size inputs only.
	 * 
	 * @param rows
	 *            The number of rows in the new board.
	 * @param cols
	 *            The number of columns in the new board.
	 * @throws InvalidGameStateException
	 *             if the size is too small.
	 **************************************************************************/
	public void newBlankGame(int rows, int cols)
			throws InvalidGameStateException {
		if (rows < 1 || cols < 1)
			throw new InvalidGameStateException(RANGE_ERROR);
		this.rows = rows;
		this.columns = cols;
		update();
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++)
				board[i][j] = 0;
		for (int k = 0; k < unique.length; k++)
			unique[k] = false;
		duplicateBoard();
	}

	/**************************************************************************
	 * Updates the board when the board array's size is modified.
	 **************************************************************************/
	private void update() {
		board = new int[rows][columns];
		board_size = rows * columns;
		unique = new boolean[board_size + 1];
		changes = 0;
	}

	/**************************************************************************
	 * Enumerates the four cardinal directions of the squares adjacent
	 * to a square being tested for validity.
	 * 
	 * @author Sam Eary
	 * @version 1.0
	 **************************************************************************/
	private enum Direction {
		North, South, West, East
	};
}
