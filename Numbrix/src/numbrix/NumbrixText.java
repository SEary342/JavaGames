package numbrix;

import java.util.Scanner;
import java.io.*;

/**************************************************************************
 * A program to play a text-based version of the Numbrix game.
 * 
 * @author Sam Eary
 * @version 1.0
 **************************************************************************/
public class NumbrixText {

	/** The directory of all of the files that can be loaded. */
	private static final String BASE = "C:/Users/Sam/workspace/Numbrix/Games/";

	/** A constant defining the menu command. */
	private static final String MENU = "Menu";

	/** The integer passed when the menu command has been called. */
	private static final int MENU_NUM = -2;

	/** The integer added to get to the letters in the ASCII table. */
	private static final int ASCIIJUMP = 65;

	/** The highest numbers of rows that can still have letters. */
	private static final int MAX_ROW_LENGTH = 26;

	/** The number representing an incomplete prompt. */
	private static final int INCOMPLETE = 0;

	/** The integer representing yes. */
	private static final int YES = 1;

	/** The integer representing valid input and a game that has ended. */
	private static final int OVER = 1;

	/** The integer representing the no response. */
	private static final int NO = 2;

	/** The plus symbol. */
	private static final String PLUS = "+";

	/** The boolean sent to {@code restart()} at the end of the game. */
	private static final boolean ENDGAME = true;

	/** The boolean sent to {@code restart()} in the middle of a game. */
	private static final boolean MIDGAME = false;

	/** The vertical separator of the game's display. */
	private static final String SEPARATOR = "|";

	/** One character of the game's horizontal separators. */
	private static final String HORIZONTAL_BASE = "-";

	/** A string representing empty space. */
	private static final String SPACE = " ";

	/** The object that keeps track of the state of the game. */
	private NumbrixEngine game;

	/** Strings representing game board formating. */
	private String horizSep, empty, colString;

	/** The the length of the number representing the game board size. */
	private int gameSizeDigit;

	/**************************************************************************
	 * Constructor for the {@code NumbrixText} class. Prompts the user
	 * to start a new game.
	 **************************************************************************/
	public NumbrixText() {
		System.out.println("Type Menu at any time to go to the menu\n");

		// Eliminates null pointer exceptions.
		game = new NumbrixEngine();
		newGameType();
	}

	/**************************************************************************
	 * Displays the game menu and responds to the user's menu option
	 * choice.
	 **************************************************************************/
	public void menu() {
		boolean valid = false;
		while (!valid) {
			menuOptions();
			try {
				switch (menuOptions.valueOf(getInfo().toLowerCase())) {
				case newgame:
					valid = true;
					newGameType();
					break;
				case play:
					valid = true;
					inputData();
					break;
				case restart:
					if (game.hasBeenModified())
						break;
					else {
						valid = true;
						restart(MIDGAME);
						break;
					}
				case quit:
					System.exit(0);
				default:
					System.err.println("Unknown Command");
				}
			} catch (Exception ex) {
				System.out
						.println("That is not an option please try again.");
			}
		}
	}

	/**************************************************************************
	 * This is a helper method for {@code menu()}. It will print out the
	 * options possible for the menu.
	 **************************************************************************/
	private void menuOptions() {
		if (!game.hasBeenModified())
			System.out
					.println("\nMenu Options:\n NewGame\n Play\n Restart\n Quit");
		else
			System.out
					.println("\nMenu Options:\n NewGame\n Play\n Quit");
		System.out.print("What do you want to do? ");
	}

	/**************************************************************************
	 * Prompts the user to choose the initial game type. The choices are
	 * new blank game or load a game based on a file.
	 **************************************************************************/
	public void newGameType() {
		int response = INCOMPLETE;
		int response2 = INCOMPLETE;
		while (response == INCOMPLETE)
			response = yesNoResponse("Do you want to define"
					+ " the size of the game board? y/n");
		if (response == 2)
			newGame();
		else {
			while (response2 == INCOMPLETE)
				response2 = newBlankGame();
			if (response2 == MENU_NUM)
				menu();
			else
				inputData();
		}
	}

	/**************************************************************************
	 * Starts a prompt to get the size of a brand new blank game.
	 * 
	 * @return {@code INCOMPLETE} if the numbers were not entered
	 *         correctly, {@code MENU_NUM} if the main menu command was
	 *         used, and {@code OVER} if the game is complete.
	 **************************************************************************/
	private int newBlankGame() {
		int row = 0;
		int col = 0;
		System.out.println("Specify game board size:\n Row: ");
		try {
			row = getNumberInfo();
			System.out.println(" Column: ");
			col = getNumberInfo();
		} catch (NumberFormatException e) {
			System.out.println("Enter integers only please.");
			return INCOMPLETE;
		} catch (MenuException e) {
			return MENU_NUM;
		}
		game = new NumbrixEngine();
		try {
			game.newBlankGame(row, col);
		} catch (InvalidGameStateException e) {
			System.out.println("The game size is too small.");
			return INCOMPLETE;
		}
		setFormat();
		return OVER;
	}

	/**************************************************************************
	 * Starts the new game selection prompt and responds to any menu
	 * calls when in the prompt. It will also start the input prompt
	 * after a game has been loaded.
	 **************************************************************************/
	private void newGame() {
		int valid = INCOMPLETE;
		game = new NumbrixEngine();
		while (valid == INCOMPLETE)
			valid = newGameEngine();

		// Sends the user to the main menu.
		if (valid == MENU_NUM)
			menu();
		else
			inputData();
	}

	/**************************************************************************
	 * This is the engine for {@code menu()}. It lists out all possible
	 * games that can be loaded. After that it will allow the user to
	 * select a game to be loaded and loads that game.
	 * 
	 * @return {@code INCOMPLETE} if game filename is invalid, {@code
	 *         OVER} if the game has been loaded, and {@code MENU_NUM}
	 *         if the menu has been called.
	 **************************************************************************/
	private int newGameEngine() {
		String input = "";
		try {
			FileLoader files = new FileLoader(BASE);
			File[] fileList = files.getFileList();
			System.out.println("Games that can be loaded:");

			// Display all files in the pre-made games directory.
			files.showFileList();
			System.out
					.println("Type the name of the game that you want to play: ");
			try {
				input = getInfo();
			} catch (MenuException e1) {
				return MENU_NUM;
			}

			// Attempting to match the user's choice to a game file and
			// loading that file.
			for (int j = 0; j < fileList.length; j++)
				if (input.equals(fileList[j].getName().trim())
						|| (input + ".game").equals(fileList[j]
								.getName().trim())) {
					try {
						game.loadGame(fileList[j].getPath());
						setFormat();
						return OVER;
					} catch (Exception e) {
						System.out.println("File not valid.");
						return INCOMPLETE;
					}
				}
		} catch (NullPointerException e) {
			System.out
					.println("Invalid file path please check BASE directory path.");
			return MENU_NUM;
		}
		System.out.println("This is not a file.");
		return INCOMPLETE;
	}

	/**************************************************************************
	 * This will start the input-data prompt. It will also start the
	 * menu prompt if the menu command has been called. Finally, it will
	 * start the end of game dialogue if the game has been completed.
	 **************************************************************************/
	public void inputData() {
		int valid = INCOMPLETE;

		// Fail-safe for if the user never loaded a file.
		if (!game.loadStatus()) {
			System.out.println("No game loaded.");
			newGame();
		} else {
			while (valid == INCOMPLETE) {
				displayBoard();
				valid = inputDataPrompt();
			}
			if (valid == MENU_NUM)
				menu();
			else if (valid == OVER) {
				displayBoard();
				System.out.println("Game Over");
				restart(ENDGAME);
			}
		}
	}

	/**************************************************************************
	 * Prompts the user for each number describing a move on the game
	 * board.
	 * 
	 * @return {@code INCOMPLETE} if game is not over, {@code OVER} if
	 *         the game is complete, and {@code MENU_NUM} if the menu
	 *         has been called.
	 **************************************************************************/
	private int inputDataPrompt() {
		int number = 0;
		int row = 0;
		int col = 0;
		try {

			// Gets user information one prompt at a time. This
			// eliminates some confusion.
			System.out.println("Number: ");
			number = getNumberInfo();
			System.out.println("Row: ");
			row = getRowInfo();
			System.out.println("Column: ");
			col = getNumberInfo();
		} catch (NumberFormatException e) {
			System.out.println("Enter integers only please.");
			return INCOMPLETE;
		} catch (MenuException e) {
			return MENU_NUM;
		}
		inputErrorTest(number, row, col - 1);
		if (game.isComplete())
			return OVER;
		return INCOMPLETE;
	}

	/**************************************************************************
	 * Passes data points to the game engine and responds to any data
	 * entry errors.
	 * 
	 * @param number
	 *            The number to be added to the game board.
	 * @param row
	 *            The number representing the row of the number entered.
	 * @param col
	 *            The number representing the column of the number
	 *            entered.
	 **************************************************************************/
	private void inputErrorTest(int number, int row, int col) {
		String message = "";
		try {
			game.addNumber(number, row, col);

			// Catches the invalid exceptions thrown by the engine and
			// shows helpful messages.
		} catch (InvalidGameStateException e) {
			message = e.getMessage();
			if (message.equals(NumbrixEngine.DUPLICATE))
				System.out
						.println("This number is a duplicate. Try again.");
			else if (message.equals(NumbrixEngine.RANGE_ERROR))
				System.out
						.println("That number cannot be on the board. Try again.");
			else if (message.equals(NumbrixEngine.INVALID_ERROR))
				System.out
						.println("That entry is directly invalid. Try again.");
		} catch (ArrayIndexOutOfBoundsException e1) {
			System.out.println("That is not a space on the board.");
		}
	}

	/**************************************************************************
	 * Restarts the game in various states of game-play, such as in the
	 * middle of a game or at the end of a game.
	 * 
	 * @param type
	 *            {@code true} is when {@code restart()} is called at
	 *            the end of the game, {@code false} otherwise.
	 **************************************************************************/
	private void restart(boolean type) {
		int flag = INCOMPLETE;
		while (flag == INCOMPLETE) {
			if (type)
				flag = yesNoResponse("Do you wish to play a new game? y/n");
			else
				flag = yesNoResponse("Do you wish to restart? y/n");
		}
		if (flag == YES) {
			if (type)
				newGame();
			else {
				game.resetBoard();
				inputData();
			}
		} else {
			if (type)
				System.exit(0);
			else
				menu();
		}
	}

	/**************************************************************************
	 * Prompts the user for a yes or no response. Also, it will not
	 * allow the user to any other command besides yes or no.
	 * 
	 * @return 0 is when the input is invalid, {@code YES} when the user
	 *         has selected yes, or {@code NO} when the user has
	 *         selected no.
	 **************************************************************************/
	private int yesNoResponse(String message) {
		String input = "";
		int flag = INCOMPLETE;
		System.out.println(message);
		try {
			input = getInfo();
		} catch (MenuException e) {
		}

		// This will allow for multiple versions of yes or no.
		if (input.equalsIgnoreCase("y")
				|| input.equalsIgnoreCase("yes")) {
			flag = YES;
		} else if (input.equalsIgnoreCase("n")
				|| input.equalsIgnoreCase("no"))
			flag = NO;
		else {
			System.out.println("Please choose a y/n response only.");
		}
		return flag;
	}

	/**************************************************************************
	 * Converts user input from a String into a number.
	 * 
	 * @return the number entered by the user.
	 * @throws NumberFormatException
	 *             if the user did not enter an integer.
	 * @throws MenuException
	 *             if the user entered the menu command.
	 **************************************************************************/
	private int getNumberInfo() throws NumberFormatException,
			MenuException {
		return Integer.parseInt(getInfo());
	}

	/**************************************************************************
	 * Gets the row number from the user input.
	 * 
	 * @return the user input for the row number.
	 * @throws MenuException
	 *             if the user used the menu command.
	 * @throws NumberFormatException
	 *             if the user did not enter a valid number or letter.
	 **************************************************************************/
	private int getRowInfo() throws MenuException,
			NumberFormatException {
		String info = getInfo();
		int input = 0;
		info = info.toUpperCase();
		try {

			// Normal numbers.
			input = Integer.parseInt(info) - 1;
		} catch (NumberFormatException e) {

			// Looks for a char representing the row.
			if (info != null && info.length() == 1)
				input = (int) info.charAt(0) - ASCIIJUMP;

			// The input for row is completely invalid.
			else
				throw new NumberFormatException();
		}
		return input;
	}

	/**************************************************************************
	 * Checks to see if the command passed by the user is the menu
	 * command.
	 * 
	 * @param input
	 *            The command passed by the user.
	 * @return {@code true} if the input was menu, {@code false}
	 *         otherwise.
	 **************************************************************************/
	private boolean isMenuCmd(String input) {
		return input.equalsIgnoreCase(MENU);
	}

	/**************************************************************************
	 * Gets the user input from the console. It will also throw the menu
	 * exception if the menu command is used.
	 * 
	 * @return the user input
	 * @throws MenuException
	 *             if the user uses the menu command.
	 **************************************************************************/
	private String getInfo() throws MenuException {
		Scanner gameInput = new Scanner(System.in);
		String input = gameInput.nextLine().trim();

		// Throws an exception, so any running process is terminated
		// when menu is called.
		if (isMenuCmd(input))
			throw new MenuException();
		return input;
	}

	/**************************************************************************
	 * Displays the game board print-out.
	 **************************************************************************/
	public void displayBoard() {
		int[][] board = game.getBoard();
		System.out.println(SPACE + colString + "\n" + horizSep);
		for (int row = 0; row < game.getRows(); row++) {
			System.out.println(dataLineBuilder(board, row));
			System.out.println(horizSep);
		}
	}

	/**************************************************************************
	 * Sets the formatting of the game board.
	 **************************************************************************/
	private void setFormat() {
		int digit = 1;
		String horizBaseString = "";
		horizSep = "";
		empty = "";

		// Digit multiplied by 10 makes the jump to the next digit
		// length.
		while (game.getBoardSize() >= digit) {
			horizSep += SPACE;
			horizBaseString += HORIZONTAL_BASE;
			empty += SPACE;
			digit *= 10;
		}
		if (game.getRows() <= MAX_ROW_LENGTH)
			horizSep = SPACE + SPACE;
		horizSep += PLUS;
		gameSizeDigit = Integer.toString(game.getBoardSize()).length();
		for (int i = 0; i < game.getCols(); i++) {
			horizSep += horizBaseString + PLUS;
			setColString();
		}
	}

	/**************************************************************************
	 * Sets the formatting of the numbers representing the columns.
	 **************************************************************************/
	private void setColString() {
		colString = "";

		// This is only when the game is using letters for row numbers.
		if (game.getRows() <= MAX_ROW_LENGTH)
			colString += SPACE + SPACE;
		else
			colString += empty;
		for (int i = 0; i < game.getCols(); i++) {
			colString += dataPointFormatter(i + 1) + SPACE;
		}
	}

	/**************************************************************************
	 * Builds a line of data on the game board. Generates the specific
	 * formatting for the actual game board entries
	 * 
	 * @param board
	 *            The game board being printed.
	 * @param row
	 *            The row of the game board being printed.
	 * @return the formatted string representing the entire row of data.
	 **************************************************************************/
	private String dataLineBuilder(int[][] board, int row) {
		String line = "";
		char rowLet;

		// If the game has less than 26 rows, then characters will be
		// displayed.
		if (game.getRows() <= MAX_ROW_LENGTH) {
			rowLet = (char) (row + ASCIIJUMP);
			line = SPACE + rowLet + SEPARATOR;
		} else
			line = dataPointFormatter(row + 1) + SEPARATOR;

		// This adds the board values from the engine to the game board.
		for (int i = 0; i < board[row].length; i++) {
			if (board[row][i] == 0)
				line += empty;
			else {
				line += dataPointFormatter(board[row][i]);
			}
			line += SEPARATOR;
		}
		return line;
	}

	/**************************************************************************
	 * Formats the spaces around a number in the printed game board.
	 * 
	 * @param number
	 *            The number to be formatted.
	 * @return the formatted number.
	 **************************************************************************/
	private String dataPointFormatter(int number) {
		String dataPoint = Integer.toString(number);

		// Setting the spaces of numbers.
		while (dataPoint.length() < gameSizeDigit) {
			if (gameSizeDigit - dataPoint.length() == 1)
				dataPoint = SPACE + dataPoint;
			else
				dataPoint = SPACE + dataPoint + SPACE;
		}
		return dataPoint;
	}

	/**************************************************************************
	 * The main class of the NumbrixText program. This starts the
	 * program.
	 * 
	 * @param args
	 *            Arguments passed to the main method upon execution.
	 **************************************************************************/
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		NumbrixText a = new NumbrixText();
	}

	/**************************************************************************
	 * An enumerator to represent the menu commands.
	 * 
	 * @author Sam Eary
	 * @version 1.0
	 **************************************************************************/
	public enum menuOptions {
		newgame, play, restart, quit
	};
}
