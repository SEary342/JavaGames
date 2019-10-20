package game;

/**************************************************************************
 * An interface representing the three types of scoring methods in
 * Metro.
 * 
 * @author Sam Eary and Tyler Blanchard
 * @version 1.0
 **************************************************************************/
public interface ScoreKeeper {

	/** The integer representing simple scoring. */
	public static final int SIMPLE = 0;

	/** The integer representing the crossover scoring. */
	public static final int CROSS = 1;

	/** The integer representing the time placement scoring. */
	public static final int TIME = 2;

	/**************************************************************************
	 * Gets an array representing each players score.
	 * 
	 * @return the array representing the scores for each player.
	 **************************************************************************/
	int[] getScores();

	/**************************************************************************
	 * Updates the scores with the one point per tile method.
	 **************************************************************************/
	void updateSimpleScores();

	/**************************************************************************
	 * Updates the scores with the bonus points for tiles that are used
	 * more than once.
	 **************************************************************************/
	void updateCrossoverScores();

	/**************************************************************************
	 * Updates the scores with the time tiles are added as scores.
	 **************************************************************************/
	void updatePlacementTimeScores();

	/**************************************************************************
	 * Updates the scores given a type of scoring system.
	 * 
	 * @param type
	 *            The type of scoring system selected.
	 **************************************************************************/
	void updateScores(int type);
}
