package game;

import java.io.*;
import java.text.ParseException;
import java.util.Scanner;

/**************************************************************************
 * A class to save and load a Metro game.
 * 
 * @author Sam Eary and Tyler Blanchard
 * @version 1.0
 **************************************************************************/
public class MetroLoadSave {

	/** The data of the loaded Metro game. */
	private int rows, cols, players, turn, scoreType, id;

	/** The loaded version of the game board. */
	private String[][] gameBoard;

	/** The loaded version of the array of player tiles. */
	private String[] playerTiles;

	/**************************************************************************
	 * Loads a Metro game given a filename and stores its data as
	 * instance variables.
	 * 
	 * @param fileName
	 *            The name of the file storing the information.
	 * @throws FileNotFoundException
	 *             If the file cannot be found.
	 * @throws ParseException
	 *             If the data is invalid.
	 **************************************************************************/
	protected void loadGame(String fileName)
			throws FileNotFoundException, ParseException {
		Scanner sc = new Scanner(new File(fileName));
		loadGame(sc);
	}

	/**************************************************************************
	 * Loads the size data and then the data of the game board.
	 * 
	 * @param input
	 *            The scanner of the file to be read.
	 * @throws ParseException
	 *             If the file cannot be loaded.
	 **************************************************************************/
	private void loadGame(Scanner input) throws ParseException {
		sizeLoader(input);
		dataLoader(input);
	}

	/**************************************************************************
	 * Loads the size data of the save game files. This data includes
	 * size, number of players, the current turn, the score type, and
	 * the current tile id number.
	 * 
	 * @param input
	 *            The scanner of the file to be read.
	 * @throws ParseException
	 *             If the file cannot be loaded.
	 **************************************************************************/
	private void sizeLoader(Scanner input) throws ParseException {
		String in;
		Scanner sc;
		in = getNextDataLine(input);
		sc = new Scanner(in);
		while (sc.hasNext()) {
			rows = sc.nextInt();
			cols = sc.nextInt();
			players = sc.nextInt();
			turn = sc.nextInt();
			scoreType = sc.nextInt();
			id = sc.nextInt();
		}

		// This occurs only if there is extra data.
		if (sc.hasNext())
			throw new ParseException("Invalid file", 47);
		gameBoard = new String[rows][cols];
		playerTiles = new String[players + 1];
	}

	/**************************************************************************
	 * Loads the data for the game. This includes the gameboard and
	 * current player held tiles.
	 * 
	 * @param input
	 *            The scanner of the file to be read.
	 * @throws ParseException
	 *             If the file cannot be read.
	 **************************************************************************/
	private void dataLoader(Scanner input) throws ParseException {
		String data;
		Scanner sc = null;
		String temp;
		for (int i = 0; i < gameBoard.length; i++) {
			data = getNextDataLine(input);
			sc = new Scanner(data);
			for (int j = 0; j < gameBoard[i].length; j++)
				if (sc.hasNext()) {
					temp = sc.next();
					if (temp.equals("0"))
						gameBoard[i][j] = null;
					else
						gameBoard[i][j] = temp;
				}

			// Occurs if there is extra data on each line.
			if (sc.hasNext())
				throw new ParseException("Invalid File", 68);
		}
		data = getNextDataLine(input);
		sc = new Scanner(data);
		for (int k = 0; k < playerTiles.length; k++)
			if (sc.hasNext())
				playerTiles[k] = sc.next();

		// Occurs if there is extra data.
		if (sc.hasNext())
			throw new ParseException("Invalid File", 68);
	}

	/**************************************************************************
	 * Gets the next line of data from the file.
	 * 
	 * @param input
	 *            The scanner of the file being read.
	 * @return the next line of data.
	 **************************************************************************/
	private String getNextDataLine(Scanner input) {
		String data = "";
		if (input.hasNext())
			data = input.nextLine().trim();
		return data;
	}

	/**************************************************************************
	 * Creates a file representing a game of Metro.
	 * 
	 * @param numOfPlayers
	 *            The number of players in the game.
	 * @param size
	 *            The size of the game board.
	 * @param turn
	 *            The current turn.
	 * @param scoreType
	 *            The type of scoring method in the game.
	 * @param id
	 *            The current id number in the game.
	 * @param currentGame
	 *            The {@code TileCell} double array of the game board.
	 * @param currentPlayerTiles
	 *            The {@code TileCell} array of the current player
	 *            tiles.
	 * @param filename
	 *            The name of the file to be saved.
	 * @throws IOException
	 *             If there is a problem saving.
	 **************************************************************************/
	protected void saveGame(int numOfPlayers, int rows, int cols,
			int turn, int scoreType, int id, TileCell[][] currentGame,
			TileCell[] currentPlayerTiles, String filename)
			throws IOException {
		BufferedWriter out = new BufferedWriter(
				new FileWriter(filename));

		// The size data.
		out.write(rows + " " + cols + " " + numOfPlayers + " " + turn
				+ " " + scoreType + " " + id);
		out.newLine();
		writeData(currentGame, currentPlayerTiles, out);
		out.close();
	}

	/**************************************************************************
	 * Writes the game board and player tile data to the file.
	 * 
	 * @param currentGame
	 *            The {@code TileCell} double array of the game board.
	 * @param currentPlayerTiles
	 *            The {@code TileCell} array of the current player
	 *            tiles.
	 * @param out
	 *            The {@code BufferedWriter} of the file being written.
	 * @throws IOException
	 *             If there is a problem saving.
	 **************************************************************************/
	private void writeData(TileCell[][] currentGame,
			TileCell[] currentPlayerTiles, BufferedWriter out)
			throws IOException {
		TileCell curr;

		// The game data.
		for (int i = 0; i < currentGame.length; i++) {
			for (int j = 0; j < currentGame[i].length; j++) {
				curr = currentGame[i][j];
				if (curr != null)
					out.write(curr.getType().name() + curr.getID()
							+ curr.getOwner() + " ");
				else
					out.write(0 + " ");
			}
			out.newLine();
		}

		// The current player tile data.
		for (int k = 0; k < currentPlayerTiles.length; k++)
			out.write(currentPlayerTiles[k].getType().name() + " ");
	}

	/**************************************************************************
	 * Gets the array of the loaded game board.
	 * 
	 * @return the array of the loaded game board.
	 **************************************************************************/
	protected String[][] getGameboard() {
		return gameBoard;
	}

	/**************************************************************************
	 * Gets the array of the loaded player tiles.
	 * 
	 * @return the array of the loaded player tiles.
	 **************************************************************************/
	protected String[] getPlayerTiles() {
		return playerTiles;
	}

	/**************************************************************************
	 * Gets the size of the game board.
	 * 
	 * @return the array of the size of the game board.
	 **************************************************************************/
	protected int[] getSize() {
		int[] size = { rows, cols };
		return size;
	}

	/**************************************************************************
	 * Gets the number of players in a loaded game.
	 * 
	 * @return the number of players.
	 **************************************************************************/
	protected int getPlayers() {
		return players;
	}

	/**************************************************************************
	 * Gets the current turn of a loaded game.
	 * 
	 * @return the current turn.
	 **************************************************************************/
	protected int getTurn() {
		return turn;
	}

	/**************************************************************************
	 * Gets the current id number of the loaded game.
	 * 
	 * @return the current id number.
	 **************************************************************************/
	protected int getID() {
		return id;
	}

	/**************************************************************************
	 * Gets the type of scoring system in the loaded game.
	 * 
	 * @return the type of scoring system in the loaded game.
	 **************************************************************************/
	protected int getScoreType() {
		return scoreType;
	}
}
