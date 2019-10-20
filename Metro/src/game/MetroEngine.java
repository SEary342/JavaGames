package game;

import game.TileCell.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import utility.*;

/**************************************************************************
 * A class to represent the state of a Metro game.
 * 
 * @author Sam Eary and Tyler Blanchard
 * @version 1.0
 **************************************************************************/
public class MetroEngine implements ScoreKeeper {

	/** The integer added to get to the letters in the ASCII table. */
	public static final int ASCIIJUMP = 97;

	/** The boolean for top or left terminals. */
	private static final boolean UPORLEFT = true;

	/** The boolean for bottom or right terminals. */
	private static final boolean DOWNORRIGHT = false;

	/** The number of rows and columns in the game board. */
	private int rows, cols;

	/** Boolean to represent an active game. */
	private boolean inProgress;

	/** The ArrayList of tiles not played yet. */
	private ArrayList<TileCell> tileStack;

	/** The current tiles held by each player. */
	private TileCell[] currentPlayerTiles;

	/** The array of tiles that represents the game board. */
	private TileCell[][] gameBoard;

	/** The current id number of the game. */
	private int id;

	/** The number of players in the game. */
	private int numOfPlayers;

	/** The number of the player that activated the draw pile. */
	private int drawPileActivePlayer;

	/** Represents and active or inactive draw pile. */
	private boolean drawPileActive;

	/** The number of the current active player. */
	private int currentPlayerNum;

	/** The ArrayList of the stations on the game board. */
	private ArrayList<StationCell> stations;

	/** Boolean to disable the exceptions. */
	private boolean override;

	/** An array of the player scores. */
	private int[] playerScores;

	/** The type of scoring system in use. */
	private int scoreType;

	/**************************************************************************
	 * Primary constructor for the {@code MetroEngine} class.
	 **************************************************************************/
	public MetroEngine() {
		inProgress = false;
	}

	/**************************************************************************
	 * Starts a new game with the given data.
	 * 
	 * @param playerNum
	 *            The number of players in the game.
	 * @param rows
	 *            The number of rows in the game board.
	 * @param cols
	 *            The number of columns in the game board.
	 * @param scoreType
	 *            The type of scoring system.
	 **************************************************************************/
	protected void newGame(int playerNum, int rows, int cols,
			int scoreType) {
		this.scoreType = scoreType;
		inProgress = true;
		this.rows = rows;
		this.cols = cols;
		numOfPlayers = playerNum;
		id = 1;
		currentPlayerNum = 0;
		gameBoard = new TileCell[rows][cols];
		playerScores = new int[numOfPlayers];
		currentPlayerTiles = new TileCell[playerNum + 1];
		stackGenerator((rows * cols) / 2);
		givePlayerTiles(playerNum);
		stations = new ArrayList<StationCell>();
		loadTerminals();
	}

	/**************************************************************************
	 * Gives each player a tile.
	 **************************************************************************/
	private void givePlayerTiles(int playerNum) {
		for (int i = 0; i < playerNum; i++)
			drawFromStack(i);
		updateDrawPile();
	}

	/**************************************************************************
	 * Generates a stack of tiles given the size of the game board.
	 * 
	 * @param sizeNum
	 *            The size of the game board.
	 **************************************************************************/
	private void stackGenerator(int sizeNum) {
		if (sizeNum <= 4)
			loadTiles(1);
		else if (sizeNum <= 6)
			loadTiles(2);
		else if (sizeNum <= 8)
			loadTiles(3);
		else if (sizeNum == 9)
			loadTiles(4);
		else if (sizeNum == 10)
			loadTiles(5);
		else
			loadTiles(6);
	}

	/**************************************************************************
	 * Loads the specified number of each of the {@code TileCells} into
	 * the stack.
	 * 
	 * @param numEach
	 *            The number of each tile in the game.
	 **************************************************************************/
	private void loadTiles(int numEach) {
		char tileId = (char) ASCIIJUMP;
		tileStack = new ArrayList<TileCell>();
		for (int i = 0; i < TileCell.types.length; i++) {
			for (int j = 0; j < numEach; j++)
				tileStack.add(new TileCell(String.valueOf(tileId)));
			tileId += (char) 1;
		}
	}

	/**************************************************************************
	 * Loads each of the terminals into the game and sets their owners.
	 **************************************************************************/
	private void loadTerminals() {
		terminalRowAdder(UPORLEFT);
		for (int i = 0; i < rows; i++) {
			terminalColAdder(UPORLEFT, i);
			terminalColAdder(DOWNORRIGHT, i);
		}
		terminalRowAdder(DOWNORRIGHT);
		setOwners();
	}

	/**************************************************************************
	 * Adds a terminal row.
	 * 
	 * @param position
	 *            {@code true} if an upper terminal, {@code false}
	 *            otherwise.
	 **************************************************************************/
	private void terminalRowAdder(boolean position) {
		for (int i = 0; i < cols; i++) {
			if (position)
				terminalAdder(-1, i);
			else
				terminalAdder(rows, i);
		}
	}

	/**************************************************************************
	 * Adds terminals to the columns
	 * 
	 * @param position
	 *            {@code true} if a left terminal, {@code false}
	 *            otherwise.
	 * @param row
	 *            The row of the terminal.
	 **************************************************************************/
	private void terminalColAdder(boolean position, int row) {
		if (position)
			terminalAdder(row, -1);
		else
			terminalAdder(row, cols);
	}

	/**************************************************************************
	 * Adds terminals to the board.
	 * 
	 * @param x
	 *            The x coordinate of the station to be added.
	 * @param y
	 *            The y coordinate of the station to be added.
	 **************************************************************************/
	private void terminalAdder(int x, int y) {
		stations.add(new StationCell(new Position(x, y), rows, cols));
	}

	/**************************************************************************
	 * Draws a new tile from the stack for the specified player number.
	 * 
	 * @param playerNum
	 *            The player drawing the tile.
	 **************************************************************************/
	private void drawFromStack(int playerNum) {
		if (tileStack.size() != 0) {
			int index = (int) (Math.random() * tileStack.size());
			currentPlayerTiles[playerNum] = tileStack.get(index);
			tileStack.remove(index);
		}
	}

	/**************************************************************************
	 * Updates the draw pile after a player takes a turn.
	 **************************************************************************/
	private void updateDrawPile() {
		drawFromStack(numOfPlayers);
	}

	/**************************************************************************
	 * Gets the in progress status of the game.
	 * 
	 * @return {@code true} if a game is running, {@code false}
	 *         otherwise.
	 **************************************************************************/
	protected boolean inProgress() {
		return inProgress;
	}

	/**************************************************************************
	 * Gets the number of rows in the game board.
	 * 
	 * @return the number of rows in the game board.
	 **************************************************************************/
	protected int getRows() {
		return rows;
	}

	/**************************************************************************
	 * Gets the number of columns in the game board.
	 * 
	 * @return the number of columns in the game board.
	 **************************************************************************/
	protected int getCols() {
		return cols;
	}

	/**************************************************************************
	 * Gets the type of the current tile from the array of player tiles
	 * for the specified player.
	 * 
	 * @param playerNum
	 *            The number of the specified player.
	 * @return the type of the current tile for the specified player.
	 **************************************************************************/
	private Type getPlayerTile(int playerNum) {
		return currentPlayerTiles[playerNum].getType();
	}

	/**************************************************************************
	 * Gets the type of the current tile from the array of player tiles
	 * for the specified player.
	 * 
	 * @param playerNum
	 *            The player number of the specified player.
	 * @return the type of the current tile for the specified player.
	 **************************************************************************/
	protected Type getCurrentTile(int playerNum) {
		return getPlayerTile(playerNum);
	}

	/**************************************************************************
	 * Gets the type of the tile that the current player holds.
	 * 
	 * @return the type of the tile that the current player holds.
	 **************************************************************************/
	protected Type getCurrentActiveTile() {
		return getPlayerTile(currentPlayerNum);
	}

	/**************************************************************************
	 * Activates the draw pile.
	 **************************************************************************/
	protected void activateDrawPile() {
		drawPileActivePlayer = currentPlayerNum;
		currentPlayerNum = numOfPlayers;
		drawPileActive = true;
	}

	/**************************************************************************
	 * Disables the draw pile and resets the current player number.
	 **************************************************************************/
	protected void deactivateDrawPile() {
		currentPlayerNum = drawPileActivePlayer;
		drawPileActive = false;
	}

	/**************************************************************************
	 * Advances the turn by advancing the {@code currentPlayerNum}.
	 **************************************************************************/
	private void advance() {
		if (drawPileActive)
			deactivateDrawPile();
		currentPlayerNum++;
		if (currentPlayerNum == numOfPlayers)
			currentPlayerNum = 0;
	}

	/**************************************************************************
	 * Sets a tile at the given x and y coordinates.
	 * 
	 * @param x
	 *            The x coordinate of the tile to be added.
	 * @param y
	 *            The y coordinate of the tile to be added.
	 * @throws MiddleOfNowhereException
	 *             if the tile is adjacent to no others.
	 * @throws CutoffException
	 *             if the tile cuts off another player.
	 **************************************************************************/
	protected void setTile(int x, int y)
			throws MiddleOfNowhereException, CutoffException {

		// Checks for overrides, then exceptions, then places a tile.
		exceptionOverride();
		validityChecker(x, y);
		gameBoard[x][y] = currentPlayerTiles[currentPlayerNum];
		gameBoard[x][y].setID(id);
		if (drawPileActive)
			gameBoard[x][y].setOwner(drawPileActivePlayer);
		else
			gameBoard[x][y].setOwner(currentPlayerNum);
		id++;
		drawFromStack(currentPlayerNum);
		advance();
	}

	/**************************************************************************
	 * Checks to see if a move is valid.
	 * 
	 * @param x
	 *            The x coordinate of the tile to be added.
	 * @param y
	 *            The y coordinate of the tile to be added.
	 * @throws MiddleOfNowhereException
	 *             if the tile is adjacent to no others.
	 * @throws CutoffException
	 *             if the tile cuts off another player.
	 **************************************************************************/
	private void validityChecker(int x, int y)
			throws MiddleOfNowhereException, CutoffException {
		if (!override) {
			if (x == 0 || y == 0 || x == rows - 1 || y == cols - 1)
				exceptionChecker(x, y);
			else
				adjacentChecker(x, y);
		}
	}

	/**************************************************************************
	 * Checks for a placement exception where tiles cutoff others.
	 * 
	 * @param x
	 *            The x coordinate of the tile to be added.
	 * @param y
	 *            The y coordinate of the tile to be added.
	 * @throws CutoffException
	 *             if the tile cuts off another player.
	 **************************************************************************/
	private void exceptionChecker(int x, int y) throws CutoffException {
		Link currentExit = null;
		try {
			currentExit = currentPlayerTiles[currentPlayerNum]
					.getExitLink(exitToEntrance(getNeighborTerminal(x,
							y).getTail().getExit()));
			if (isTerminalNext(currentExit, x, y))
				throw new CutoffException();
		} catch (CornerException e) {
			checkCorners(x, y);
		}
	}

	/**************************************************************************
	 * Checks to see if any tile can be placed without exceptions and
	 * enables the exception overrides if this is the case.
	 **************************************************************************/
	private void exceptionOverride() {
		StationCell station;
		Position pos;
		int errors = 0;
		int open = 0;
		for (int i = 0; i < stations.size(); i++)
			if (!stations.get(i).isStarted()) {
				open++;
				station = stations.get(i);
				pos = station.getPosition();
				try {
					if (pos.getX() == -1)
						exceptionChecker(pos.getX() + 1, pos.getY());
					else if (pos.getX() == rows)
						exceptionChecker(pos.getX() - 1, pos.getY());
					else if (pos.getY() == cols)
						exceptionChecker(pos.getX(), pos.getY() - 1);
					else if (pos.getY() == -1)
						exceptionChecker(pos.getX(), pos.getY() + 1);
				} catch (CutoffException e) {
					errors++;
				}
			}
		if (numOfInsideOpenTiles() != 0 && isStationStarted())
			override = false;
		else if (errors == open)
			override = true;
		else
			override = false;
	}

	/**************************************************************************
	 * Gets the number of open tiles that are not adjacent to terminals.
	 * 
	 * @return the number of open tiles that are not adjacent to
	 *         terminals.
	 **************************************************************************/
	private int numOfInsideOpenTiles() {
		int total = 0;
		if (rows == 2)
			return total;
		else {
			for (int i = 1; i < gameBoard.length - 1; i++)
				for (int j = 1; j < gameBoard[i].length - 1; j++)
					if (gameBoard[i][j] == null)
						total++;
			return total;
		}
	}

	/**************************************************************************
	 * Tests to see if there is at least one station started.
	 * 
	 * @return {@code true} if a station is started, {@code false}
	 *         otherwise.
	 **************************************************************************/
	private boolean isStationStarted() {
		for (int i = 0; i < stations.size(); i++)
			if (stations.get(i).isStarted())
				return true;
		return false;
	}

	/**************************************************************************
	 * Updates the stations with new tiles attached to them.
	 **************************************************************************/
	private void stationUpdate() {
		for (int i = 0; i < stations.size(); i++) {
			if (stations.get(i).isComplete())
				continue;
			else
				updateStation(stations.get(i).getTail(), stations
						.get(i));
		}
	}

	/**************************************************************************
	 * Tests to see if the game is completed.
	 * 
	 * @return {@code true} if the game is complete, {@code false}
	 *         otherwise.
	 **************************************************************************/
	protected boolean isComplete() {
		for (int i = 0; i < gameBoard.length; i++)
			for (int j = 0; j < gameBoard[i].length; j++)
				if (gameBoard[i][j] == null)
					return false;
		return true;
	}

	/**************************************************************************
	 * Updates the chain of tiles connected to a station. Continues
	 * recursively until no connected tiles remain.
	 * 
	 * @param posEx
	 *            The current {@code PositionExit} of the tail of the
	 *            station list.
	 * @param station
	 *            The station being added to.
	 **************************************************************************/
	private void updateStation(PositionExit posEx, StationCell station) {
		Position nextPos = getNextPos(posEx.getExit(), posEx.getX(),
				posEx.getY());
		TileCell temp;
		if (gameBoard[nextPos.getX()][nextPos.getY()] != null) {
			temp = gameBoard[nextPos.getX()][nextPos.getY()];
			station.addTile(nextPos.getX(), nextPos.getY(), temp
					.getExitLink(exitToEntrance(posEx.getExit())));
			if (isTerminalNext(temp.getExitLink(exitToEntrance(posEx
					.getExit())), nextPos.getX(), nextPos.getY()))
				station.setComplete();

			// The recursive step.
			else
				updateStation(station.getTail(), station);
		}
	}

	/**************************************************************************
	 * Tests to see if a terminal is the next tile.
	 * 
	 * @param exit
	 *            The exit link of the tile being tested.
	 * @param x
	 *            The x coordinate of the tile being tested.
	 * @param y
	 *            The y coordinate of the tile being tested.
	 * @return {@code true} if a terminal is next, {@code false}
	 *         otherwise.
	 **************************************************************************/
	private boolean isTerminalNext(Link exit, int x, int y) {
		Position next = getNextPos(exit, x, y);
		if (next == null)
			return false;
		else if (next.getX() == -1 || next.getY() == -1
				|| next.getX() == rows || next.getY() == cols)
			return true;
		return false;
	}

	/**************************************************************************
	 * Gets the next position given an exit and current position of a
	 * tile.
	 * 
	 * @param exit
	 *            The exit link of the tile.
	 * @param x
	 *            The x coordinate of the tile being tested.
	 * @param y
	 *            The y coordinate of the tile being tested.
	 * @return the position of the next tile in the direction of the
	 *         exit link.
	 **************************************************************************/
	private Position getNextPos(Link exit, int x, int y) {
		switch (exit) {
		case NorthRight:
			return new Position(x - 1, y);
		case EastLower:
			return new Position(x, y + 1);
		case SouthLeft:
			return new Position(x + 1, y);
		case WestUpper:
			return new Position(x, y - 1);
		default:
			return null;
		}
	}

	/**************************************************************************
	 * Gets the neighbor terminal of {@code TileCells} that are adjacent
	 * to a terminal.
	 * 
	 * @param x
	 *            The x position of the {@code TileCell} being tested.
	 * @param y
	 *            The y position of the {@code TileCell} being tested.
	 * @return the {@code StationCell} that is adjacent to the specified
	 *         {@code TileCell}.
	 * @throws CornerException
	 *             if the {@code TileCell} was placed on a corner.
	 **************************************************************************/
	private StationCell getNeighborTerminal(int x, int y)
			throws CornerException {
		if ((x == 0 && y == 0) || (x == rows - 1 && y == cols - 1)
				|| (x == 0 && y == cols - 1)
				|| (x == rows - 1 && y == 0))
			throw new CornerException();
		else {
			if (x == 0 || x == rows - 1)
				return getXStations(x, y);
			else
				return getYStations(x, y);
		}
	}

	/**************************************************************************
	 * Gets the stations that are adjacent to a top or bottom terminal.
	 * 
	 * @param x
	 *            The x position of the {@code TileCell} being tested.
	 * @param y
	 *            The y position of the {@code TileCell} being tested.
	 * @return the {@code StationCell} that is adjacent to the specified
	 *         {@code TileCell}.
	 **************************************************************************/
	private StationCell getXStations(int x, int y) {
		if (x == 0)
			return stations.get(y);
		else
			return stations.get(y + 2 * rows + cols);
	}

	/**************************************************************************
	 * Gets the stations that are adjacent to a right or left terminal.
	 * 
	 * @param x
	 *            The x position of the {@code TileCell} being tested.
	 * @param y
	 *            The y position of the {@code TileCell} being tested.
	 * @return the {@code StationCell} that is adjacent to the specified
	 *         {@code TileCell}.
	 **************************************************************************/
	private StationCell getYStations(int x, int y) {
		if (y == 0)
			return stations.get(2 * x + cols);
		else
			return stations.get(2 * x + cols + 1);
	}

	/**************************************************************************
	 * Checks the corners for a {@code CutoffException}.
	 * 
	 * @param x
	 *            The x position of the {@code TileCell} being tested.
	 * @param y
	 *            The y position of the {@code TileCell} being tested.
	 * @throws CutoffException
	 *             if the tile cuts off a terminal in a corner.
	 **************************************************************************/
	private void checkCorners(int x, int y) throws CutoffException {
		StationCell[] temp = getNeighborTerminals(x, y);
		Link currentExit1 = currentPlayerTiles[currentPlayerNum]
				.getExitLink(exitToEntrance(temp[0].getTail().getExit()));
		Link currentExit2 = currentPlayerTiles[currentPlayerNum]
				.getExitLink(exitToEntrance(temp[1].getTail().getExit()));
		if (isTerminalNext(currentExit1, x, y)
				|| isTerminalNext(currentExit2, x, y))
			throw new CutoffException();

	}

	/**************************************************************************
	 * Gets the neighbor terminals of a tile. This is only used if the
	 * tile is in a corner.
	 * 
	 * @param x
	 *            The x position of the {@code TileCell} being tested.
	 * @param y
	 *            The y position of the {@code TileCell} being tested.
	 * @return the array of {@code StationCells} that are adjacent to
	 *         the specified {@code TileCell}.
	 **************************************************************************/
	private StationCell[] getNeighborTerminals(int x, int y) {
		StationCell[] temp = new StationCell[2];
		temp[0] = getXStations(x, y);
		temp[1] = getYStations(x, y);
		return temp;
	}

	/**************************************************************************
	 * Converts an exit {@code Link} to an entrance {@code Link}.
	 * 
	 * @param exit
	 *            The link to be converted.
	 * @return the corresponding entrance link to the given exit.
	 **************************************************************************/
	private Link exitToEntrance(Link exit) {
		switch (exit) {
		case SouthLeft:
			return Link.NorthLeft;
		case NorthRight:
			return Link.SouthRight;
		case EastLower:
			return Link.WestLower;
		default:
			return Link.EastUpper;
		}
	}

	/**************************************************************************
	 * Tests to see if a {@code TileCell} is adjacent to something else.
	 * 
	 * @param x
	 *            The x position of the {@code TileCell} being tested.
	 * @param y
	 *            The y position of the {@code TileCell} being tested.
	 * @throws MiddleOfNowhereException
	 *             if the tile is not adjacent to anything.
	 **************************************************************************/
	private void adjacentChecker(int x, int y)
			throws MiddleOfNowhereException {
		int match = 0;
		match += adjacentTest(x - 1, y);
		match += adjacentTest(x + 1, y);
		match += adjacentTest(x, y + 1);
		match += adjacentTest(x, y - 1);
		if (match == 0)
			throw new MiddleOfNowhereException();
	}

	/**************************************************************************
	 * Tests to see if a cell exists.
	 * 
	 * @param x
	 *            The x position of the {@code TileCell} being tested.
	 * @param y
	 *            The y position of the {@code TileCell} being tested.
	 * @return {@code 1} if there is an adjacent, {@code 0} otherwise.
	 **************************************************************************/
	private int adjacentTest(int x, int y) {
		int match = 0;
		if (gameBoard[x][y] != null)
			match++;
		return match;
	}

	/**************************************************************************
	 * Gets the number of the current player.
	 * 
	 * @return the number of the current player.
	 **************************************************************************/
	protected int getCurrentPlayerNum() {
		return currentPlayerNum;
	}

	/**************************************************************************
	 * Sets the owners of the terminals.
	 **************************************************************************/
	private void setOwners() {
		int owner = 0;
		if (rows != 8)
			for (int i = 0; i < 4; i++) {
				owner = setOwnersLoop(getTerminals(i), owner);
			}
		else
			setNormalOwners();
	}

	/**************************************************************************
	 * Sets the owners of terminals in standard 8x8 games.
	 **************************************************************************/
	private void setNormalOwners() {
		int[] owners = getNormalOwners();
		for (int i = 0; i < stations.size(); i++) {
			stations.get(i).setOwner(owners[i]);
		}
	}

	/**************************************************************************
	 * Gets the array of terminal owners for standard 8x8 games.
	 * 
	 * @return the array of terminal owners for standard 8x8 games.
	 **************************************************************************/
	private int[] getNormalOwners() {
		int[][] eight = {
				{ 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1,
						1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1 },// 2
				// Players
				{ 2, 1, 1, 2, 0, 2, 1, 0, 1, 1, 2, 0, 0, 2, 1, 1, 2, 0,
						1, 1, 0, 2, 6, 0, 6, 2, 1, 0, 2, 1, 0, 2 },// 3
				// Players
				{ 1, 0, 2, 3, 0, 1, 3, 2, 3, 0, 2, 1, 0, 2, 1, 3, 2, 1,
						3, 0, 1, 3, 0, 2, 3, 2, 1, 0, 2, 3, 0, 1 },// 4
				// Players
				{ 4, 2, 1, 0, 4, 2, 3, 0, 3, 1, 0, 4, 4, 3, 1, 2, 3, 0,
						0, 1, 2, 3, 6, 2, 6, 1, 2, 4, 3, 0, 1, 4 },// 5
				// Players
				{ 2, 5, 3, 0, 2, 4, 1, 0, 4, 5, 0, 3, 1, 4, 5, 1, 4, 5,
						2, 0, 3, 2, 6, 1, 6, 1, 0, 3, 2, 5, 4, 3 } };// 6
		// Players
		return eight[numOfPlayers - 2];
	}

	/**************************************************************************
	 * Sets the owners of each terminal.
	 * 
	 * @param terminals
	 *            The array of terminals being set.
	 * @param ownerStart
	 *            The starting owner number.
	 * @return the updated owner number.
	 **************************************************************************/
	private int setOwnersLoop(StationCell[] terminals, int ownerStart) {
		int owner = ownerStart;
		for (int i = 0; i < terminals.length; i++) {
			terminals[i].setOwner(owner);
			owner++;
			if (owner == numOfPlayers)
				owner = 0;
		}
		return owner;
	}

	/**************************************************************************
	 * Gets the stations on the specified side.
	 * 
	 * @param side
	 *            The integer representing the specified side.
	 * @return the stations on the specified side.
	 **************************************************************************/
	private StationCell[] getTerminals(int side) {

		// 0 == north, 1 == east, 2== south, 3==west
		switch (side) {
		case 0:
			return getTerminalsLoop(-1, "x");
		case 1:
			return getTerminalsLoop(cols, "y");
		case 2:
			return arrayReverser(getTerminalsLoop(rows, "x"));
		default:
			return arrayReverser(getTerminalsLoop(-1, "y"));
		}
	}

	/**************************************************************************
	 * Reverses a {@code StationCell} array.
	 * 
	 * @param array
	 *            The array to be reversed.
	 * @return the reversed {@code StationCell} array.
	 **************************************************************************/
	private StationCell[] arrayReverser(StationCell[] array) {
		StationCell[] reverse = new StationCell[array.length];
		int p = 0;
		for (int i = array.length - 1; i > -1; i--) {
			reverse[p] = array[i];
			p++;
		}
		return reverse;
	}

	/**************************************************************************
	 * Gets the array of the stations of the specified type.
	 * 
	 * @param type
	 *            The type of terminals to find.
	 * @param test
	 *            {@code "x"} for x tests, {@code "y"} for y tests.
	 * @return the array of the stations of the specified type.
	 **************************************************************************/
	private StationCell[] getTerminalsLoop(int type, String test) {
		StationCell[] temp = new StationCell[rows];
		Position position;
		int j = 0;
		for (int i = 0; i < stations.size(); i++) {
			position = stations.get(i).getPosition();
			if (test.equals("x") && position.getX() == type) {
				temp[j] = stations.get(i);
				j++;
			} else if (test.equals("y") && position.getY() == type) {
				temp[j] = stations.get(i);
				j++;
			}
		}
		return temp;
	}

	/**************************************************************************
	 * Gets the station owner of the specified station.
	 * 
	 * @param station
	 *            The station to get the owner of.
	 * @return the station owner of the specified station.
	 **************************************************************************/
	protected int getStationOwner(int station) {
		return stations.get(station).getOwner();
	}

	/**************************************************************************
	 * Gets the player who activated the draw pile.
	 * 
	 * @return the player who activated the draw pile.
	 **************************************************************************/
	protected int getDrawPilePlayer() {
		return drawPileActivePlayer;
	}

	/**************************************************************************
	 * Gets the array of the current scores.
	 * 
	 * @return the array of the current scores.
	 **************************************************************************/
	public int[] getScores() {
		stationUpdate();
		updateScores(scoreType);
		return playerScores;
	}

	/**************************************************************************
	 * Updates the crossover scores.
	 **************************************************************************/
	public void updateCrossoverScores() {
		StationCell station;
		int owner;
		Position pos;
		ArrayList<PositionExit> cells;
		for (int i = 0; i < stations.size(); i++) {
			station = stations.get(i);
			if (station.isComplete() && !station.isScored()) {
				try {
					owner = station.getOwner();
					cells = station.getCells();
					pos = station.getFirstCell();
					if (station.numberOfCells() == 1
							&& gameBoard[pos.getX()][pos.getY()]
									.getOwner() != owner)
						playerScores[owner] += 10;
					else
						for (int j = 0; j < cells.size(); j++) {
							playerScores[owner] += 1;
							if (hasMatches(cells, j))
								playerScores[owner] += 1;
						}
				} catch (ArrayIndexOutOfBoundsException e) {

					// This is for when owner=6 in 8x8 games where there
					// are dead tiles owned by nobody.
				}
				station.setScored();
			}
		}
	}

	/**************************************************************************
	 * Tests to see if a {@code ArrayList<PositionExit>} has matching
	 * elements.
	 * 
	 * @param cells
	 *            The {@code ArrayList} to test.
	 * @param endIndex
	 *            The last index of the array list to test.
	 * @return {@code true} if there are matches, {@code false}
	 *         otherwise.
	 **************************************************************************/
	private boolean hasMatches(ArrayList<PositionExit> cells,
			int endIndex) {
		PositionExit cell;
		cell = cells.get(endIndex);
		for (int i = 0; i < endIndex; i++)
			if (cell.equals(cells.get(i)))
				return true;
		return false;
	}

	/**************************************************************************
	 * Updates the time placement scores.
	 **************************************************************************/
	public void updatePlacementTimeScores() {
		StationCell station;
		int owner;
		ArrayList<PositionExit> cells;
		for (int i = 0; i < stations.size(); i++) {
			station = stations.get(i);
			if (station.isComplete() && !station.isScored()) {
				owner = station.getOwner();
				cells = station.getCells();
				for (int j = 0; j < cells.size(); j++)
					try {
						playerScores[owner] += gameBoard[cells.get(j)
								.getX()][cells.get(j).getY()].getID();
					} catch (ArrayIndexOutOfBoundsException e) {

						// This is for when owner=6 in 8x8 games where
						// there
						// are dead tiles owned by nobody.
					}
				station.setScored();
			}
		}
	}

	/**************************************************************************
	 * Updates the simple scores.
	 **************************************************************************/
	public void updateSimpleScores() {
		StationCell station;
		Position pos;
		int owner;
		for (int i = 0; i < stations.size(); i++) {
			station = stations.get(i);
			if (station.isComplete() && !station.isScored()) {
				owner = station.getOwner();
				try {
					if (station.numberOfCells() == 1) {
						pos = station.getFirstCell();
						if (gameBoard[pos.getX()][pos.getY()]
								.getOwner() != owner)
							playerScores[owner] += 10;
						else
							playerScores[owner] += station
									.numberOfCells();
					} else
						playerScores[owner] += station.numberOfCells();
				} catch (ArrayIndexOutOfBoundsException e) {

					// This is for when owner=6 in 8x8 games where there
					// are dead tiles owned by nobody.
				}
				station.setScored();
			}
		}
	}

	/**************************************************************************
	 * Saves the current game to the file name.
	 * 
	 * @param filename
	 *            The name of the file to save.
	 * @throws IOException
	 *             If the file cannot be saved.
	 **************************************************************************/
	protected void saveGame(String filename) throws IOException {
		MetroLoadSave save = new MetroLoadSave();
		if (drawPileActive)
			save.saveGame(numOfPlayers, rows, cols,
					drawPileActivePlayer, scoreType, id, gameBoard,
					currentPlayerTiles, filename);
		else
			save.saveGame(numOfPlayers, rows, cols, currentPlayerNum,
					scoreType, id, gameBoard, currentPlayerTiles,
					filename);
	}

	/**************************************************************************
	 * Loads a game from the specified file name.
	 * 
	 * @param filename
	 *            The name of the file to be loaded.
	 * @throws FileNotFoundException
	 *             If the file cannot be found.
	 * @throws ParseException
	 *             If the file is not valid.
	 **************************************************************************/
	protected void loadGame(String filename)
			throws FileNotFoundException, ParseException {
		MetroLoadSave load = new MetroLoadSave();
		String[][] game;
		String[] current;
		load.loadGame(filename);
		newGame(load.getPlayers(), load.getSize()[0],
				load.getSize()[1], load.getScoreType());
		game = load.getGameboard();
		current = load.getPlayerTiles();
		id = load.getID();
		currentPlayerNum = load.getTurn();

		// Loading the state of the game board.
		for (int i = 0; i < gameBoard.length; i++)
			for (int j = 0; j < gameBoard[i].length; j++) {
				if (game[i][j] != null) {
					gameBoard[i][j] = new TileCell(game[i][j]
							.substring(0, 1));
					gameBoard[i][j].setID(Integer.parseInt(game[i][j]
							.substring(1, 2)));
					gameBoard[i][j].setOwner(Integer
							.parseInt(game[i][j].substring(2, 3)));
					gameBoard[i][j].setPosition(new Position(i, j));
				}
			}
		for (int k = 0; k < currentPlayerTiles.length; k++)
			currentPlayerTiles[k] = new TileCell(current[k]);
	}

	/**************************************************************************
	 * Gets an integer array representing the current game board.
	 * 
	 * @return the integer array representing the current game board.
	 **************************************************************************/
	protected int[][] getGameboard() {
		int[][] game = new int[rows][cols];
		for (int i = 0; i < gameBoard.length; i++)
			for (int j = 0; j < gameBoard[i].length; j++)
				if (gameBoard[i][j] != null)
					game[i][j] = gameBoard[i][j].getType().ordinal();
				else
					game[i][j] = -1;
		return game;
	}

	/**************************************************************************
	 * Updates the scores based on the selected scoring method.
	 * 
	 * @param type
	 *            The integer of the selected scoring method.
	 **************************************************************************/
	public void updateScores(int type) {
		switch (type) {
		case ScoreKeeper.SIMPLE:
			updateSimpleScores();
			break;
		case ScoreKeeper.CROSS:
			updateCrossoverScores();
			break;
		case ScoreKeeper.TIME:
			updatePlacementTimeScores();
			break;
		default:
			break;
		}
	}

	/**************************************************************************
	 * Gets the number of players in the game.
	 * 
	 * @return the number of players in the game.
	 **************************************************************************/
	protected int getNumOfPlayers() {
		return numOfPlayers;
	}

	/**************************************************************************
	 * Gets the integer representing the type of scoring system in use.
	 * 
	 * @return the integer representing the type of scoring system in
	 *         use.
	 **************************************************************************/
	protected int getScoreType() {
		return scoreType;
	}
}
