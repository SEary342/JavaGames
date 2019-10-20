package game;

import java.io.*;
import java.text.ParseException;
import utility.*;

/**************************************************************************
 * A class to control the {@code MetroEngine} class.
 * 
 * @author Sam Eary and Tyler Blanchard
 * @version 1.0
 **************************************************************************/
public class MetroControl {

	/** The current game object. */
	private MetroEngine game;

	/**************************************************************************
	 * Constructs the control class and instantiates the {@code
	 * MetroEngine} game object.
	 **************************************************************************/
	public MetroControl() {
		game = new MetroEngine();
	}

	/**************************************************************************
	 * Gets the number of rows on the game board.
	 * 
	 * @return the number of rows on the game board.
	 **************************************************************************/
	public int getRows() {
		return game.getRows();
	}

	/**************************************************************************
	 * Gets the number of columns on the game board.
	 * 
	 * @return the number of columns on the game board.
	 **************************************************************************/
	public int getCols() {
		return game.getCols();
	}

	/**************************************************************************
	 * Gets the game in progress status from the {@code MetroEngine}.
	 * 
	 * @return {@code true} if a game in running, {@code false}
	 *         otherwise.
	 **************************************************************************/
	public boolean inProgress() {
		return game.inProgress();
	}

	/**************************************************************************
	 * Starts a new game of Metro.
	 * 
	 * @param playerNum
	 *            The number of players.
	 * @param rows
	 *            The number of rows in the game board.
	 * @param cols
	 *            The number of cols in the game board.
	 * @param scoreType
	 *            The type of scoring system.
	 **************************************************************************/
	public void newGame(int playerNum, int rows, int cols, int scoreType) {
		game.newGame(playerNum, rows, cols, scoreType);
	}

	/**************************************************************************
	 * Gets the current held tile for the specified player number.
	 * 
	 * @param playerNum
	 *            The number of the player to get the tile for.
	 * @return the current held tile for the specified player number.
	 **************************************************************************/
	public int getCurrentTile(int playerNum) {
		return game.getCurrentTile(playerNum).ordinal();
	}

	/**************************************************************************
	 * Gets the current active tile in the game. This is the tile that
	 * belongs to the player who is currently taking a turn.
	 * 
	 * @return the current active tile in the game.
	 **************************************************************************/
	public int getCurrentActiveTile() {
		return game.getCurrentActiveTile().ordinal();
	}

	/**************************************************************************
	 * Gets the number of the current player in the game. This is the
	 * number of the player whose turn it currently is.
	 * 
	 * @return the number of the current player in the game.
	 **************************************************************************/
	public int getCurrentPlayerNum() {
		return game.getCurrentPlayerNum();
	}

	/**************************************************************************
	 * Sets a tile in the game board.
	 * 
	 * @param position
	 *            The position of the tile.
	 * @throws MiddleOfNowhereException
	 *             If a tile is not adjacent to any others.
	 * @throws CutoffException
	 *             If a tile cuts off another player.
	 **************************************************************************/
	public void setTile(Position position)
			throws MiddleOfNowhereException, CutoffException {
		game.setTile(position.getX(), position.getY());
	}

	/**************************************************************************
	 * Gets the station owner of the given station.
	 * 
	 * @param station
	 *            The station number to get the owner of.
	 * @return the number of the station owner of the given station.
	 **************************************************************************/
	public int getStationOwner(int station) {
		return game.getStationOwner(station);
	}

	/**************************************************************************
	 * Gets the score of the selected player.
	 * 
	 * @param playerNum
	 *            The number of the selected player.
	 * @return the score of the selected player.
	 **************************************************************************/
	public int getScore(int playerNum) {
		try {
			return game.getScores()[playerNum];
		} catch (NullPointerException e) {
			return 0;
		}
	}

	/**************************************************************************
	 * Activates the Metro draw pile for instances when the player
	 * clicks on the draw pile.
	 **************************************************************************/
	public void activateDrawPile() {
		game.activateDrawPile();
	}

	/**************************************************************************
	 * Gets game completeness status.
	 * 
	 * @return {@code true} if the game has been completed, {@code
	 *         false} otherwise.
	 **************************************************************************/
	public boolean isComplete() {
		return game.isComplete();
	}

	/**************************************************************************
	 * Deactivates the draw pile if a player deselects the draw pile.
	 **************************************************************************/
	public void deactivateDrawPile() {
		game.deactivateDrawPile();
	}

	/**************************************************************************
	 * Gets the player that turned the draw pile on.
	 * 
	 * @return the number of the player that turned the draw pile on.
	 **************************************************************************/
	public int getDrawPlayer() {
		return game.getDrawPilePlayer();
	}

	/**************************************************************************
	 * Saves the current game to the specified file name.
	 * 
	 * @param filename
	 *            The name of the file to save the game to.
	 * @throws IOException
	 *             If there was a problem saving the game.
	 **************************************************************************/
	public void saveGame(String filename) throws IOException {
		game.saveGame(filename);
	}

	/**************************************************************************
	 * Loads a game from the specified file.
	 * 
	 * @param filename
	 *            The name of the specified file.
	 * @throws FileNotFoundException
	 *             If the file cannot be found.
	 * @throws ParseException
	 *             If the file is invalid.
	 **************************************************************************/
	public void loadGame(String filename) throws FileNotFoundException,
			ParseException {
		game.loadGame(filename);
	}

	/**************************************************************************
	 * Gets the array of integers representing the current game board.
	 * 
	 * @return the array of integers representing the current game
	 *         board.
	 **************************************************************************/
	public int[][] getGameBoard() {
		return game.getGameboard();
	}

	/**************************************************************************
	 * Gets the number of players in the game.
	 * 
	 * @return the number of players in the game.
	 **************************************************************************/
	public int getNumOfPlayers() {
		return game.getNumOfPlayers();
	}

	/**************************************************************************
	 * Gets the integer representing the type of scoring system in use.
	 * 
	 * @return the integer representing the type of scoring system in
	 *         use.
	 **************************************************************************/
	public int getScoreType() {
		return game.getScoreType();
	}
}
