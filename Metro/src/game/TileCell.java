package game;

import utility.Position;

/**************************************************************************
 * A class to determine entrance and exit points of any tile in the
 * Metro game.
 * 
 * @author Sam Eary and Tyler Blanchard
 * @version 1.0
 **************************************************************************/
public class TileCell extends Cell {

	/** The {@code Type} of this {@code TileCell}. */
	private Type tileType;

	/** The id of this {@code TileCell}. */
	private int id;

	/** The player who placed this {@code TileCell}. */
	private int owner;

	/** Types of game tiles. */
	protected static final Type[] types = { Type.a, Type.b, Type.c,
			Type.d, Type.e, Type.f, Type.g, Type.h, Type.i, Type.j,
			Type.k, Type.l, Type.m, Type.n, Type.o, Type.p, Type.q,
			Type.r, Type.s, Type.t, Type.u, Type.v, Type.w, Type.x };

	/** Directions of entrance and exit. */
	private Link[] links = { Link.NorthLeft, Link.NorthRight,
			Link.EastUpper, Link.EastLower, Link.SouthRight,
			Link.SouthLeft, Link.WestLower, Link.WestUpper };

	/**************************************************************************
	 * Primary constructor for the {@code TileCell} class. Sets the type
	 * of the tile given an input string.
	 * 
	 * @param type
	 *            The string representing the {@code Type} of the
	 *            {@code TileCell}.
	 **************************************************************************/
	public TileCell(String type) {
		typeSelection(type);
	}

	/**************************************************************************
	 * Gets the {@code Type} of the {@code TileCell}.
	 * 
	 * @return the {@code Type} of the {@code TileCell}.
	 **************************************************************************/
	protected Type getType() {
		return tileType;
	}

	/**************************************************************************
	 * Gets the position of the {@code TileCell}.
	 * 
	 * @return the position of the {@code TileCell}.
	 **************************************************************************/
	protected Position getPosition() {
		return super.getPosition();
	}

	/**************************************************************************
	 * Sets the owner of the {@code TileCell}.
	 * 
	 * @param owner
	 *            The integer representing the owner of the {@code
	 *            TileCell}.
	 **************************************************************************/
	protected void setOwner(int owner) {
		this.owner = owner;
	}

	/**************************************************************************
	 * Gets the owner of the {@code TileCell}.
	 * 
	 * @return the integer representing the owner of the {@code
	 *         TileCell}.
	 **************************************************************************/
	protected int getOwner() {
		return owner;
	}

	/**************************************************************************
	 * Sets the ID of the {@code TileCell}.
	 * 
	 * @param id
	 *            The ID number.
	 **************************************************************************/
	protected void setID(int id) {
		this.id = id;
	}

	/**************************************************************************
	 * Gets the ID of the {@code TileCell}.
	 * 
	 * @return the ID of the {@code TileCell}.
	 **************************************************************************/
	protected int getID() {
		return id;
	}

	/**************************************************************************
	 * Sets the type of the {@code TileCell}
	 * 
	 * @param type
	 *            The type to set the {@code TileCell} to.
	 **************************************************************************/
	private void typeSelection(String type) {
		for (int i = 0; i < types.length; i++)
			if (type.equals(types[i].toString())) {
				tileType = types[i];
				break;
			}
	}

	/**************************************************************************
	 * Finds the exit {@code Link} given an entrance {@code Link} for
	 * the {@code TileCell}.
	 * 
	 * @param entrance
	 *            The entrance {@code Link}.
	 * @return the exit {@code Link}.
	 **************************************************************************/
	protected Link getExitLink(Link entrance) {
		int[] tilePath = tileType.getPath();
		int startNum = 0;
		Link start = null;
		for (int i = 0; i < tilePath.length; i++)
			if (entrance == links[i]) {
				start = links[i];
				startNum = tilePath[i];
				break;
			}
		for (int j = 0; j < tilePath.length; j++)
			if (!links[j].equals(start) && tilePath[j] == startNum)
				return links[j];
		return null;
	}

	/**************************************************************************
	 * An enumerator to represent each of the possible entrances and
	 * exits to a Metro tile.
	 * 
	 * @author Sam Eary and Tyler Blanchard
	 * @version 1.0
	 **************************************************************************/
	protected enum Link {
		NorthLeft, NorthRight, EastUpper, EastLower, SouthRight,
			SouthLeft, WestLower, WestUpper
	}

	/**************************************************************************
	 * An enumerator to represent each of the possible types of the
	 * Metro tiles.
	 * 
	 * @author Sam Eary and Tyler Blanchard
	 * @version 1.0
	 **************************************************************************/
	protected enum Type {
		a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x;

		/** The path of the given {@code Type}. */
		private int[] path;

		/** The array of the paths for all 24 Metro tiles. */
		private int[][] paths = { { 1, 2, 3, 4, 2, 1, 4, 3 },// a
				{ 1, 1, 2, 3, 4, 4, 3, 2 },// b
				{ 1, 2, 3, 3, 2, 1, 4, 4 },// c
				{ 1, 2, 2, 3, 3, 1, 4, 4 },// d
				{ 1, 2, 3, 3, 2, 4, 4, 1 },// e
				{ 1, 2, 2, 3, 4, 4, 3, 1 },// f
				{ 1, 1, 2, 3, 3, 2, 4, 4 },// g
				{ 1, 1, 2, 2, 3, 4, 4, 3 },// h
				{ 1, 2, 3, 3, 4, 4, 2, 1 },// i
				{ 1, 2, 2, 1, 3, 3, 4, 4 },// j
				{ 1, 1, 2, 2, 3, 3, 4, 4 },// k
				{ 1, 2, 2, 3, 3, 4, 4, 1 },// l
				{ 1, 2, 3, 4, 4, 3, 2, 1 },// m
				{ 1, 2, 2, 1, 3, 4, 4, 3 },// n
				{ 1, 2, 3, 1, 4, 3, 2, 4 },// o
				{ 1, 1, 2, 3, 4, 2, 3, 4 },// p
				{ 1, 2, 3, 1, 4, 4, 2, 3 },// q
				{ 1, 2, 2, 3, 4, 1, 3, 4 },// r
				{ 1, 2, 3, 4, 2, 3, 4, 1 },// s
				{ 1, 2, 3, 4, 4, 1, 2, 3 },// t
				{ 1, 2, 3, 1, 2, 4, 4, 3 },// u
				{ 1, 2, 3, 1, 2, 3, 4, 4 },// v
				{ 1, 2, 3, 3, 4, 1, 2, 4 },// w
				{ 1, 1, 2, 3, 3, 4, 4, 2 } };// x

		/**************************************************************************
		 * Constructs the {@code Type} enum and selects the path based
		 * on the ordinal of the enum.
		 **************************************************************************/
		private Type() {
			path = new int[8];
			for (int i = 0; i < path.length; i++) {
				path[i] = paths[ordinal()][i];
			}
		}

		/**************************************************************************
		 * Gets the path of the selected tile.
		 * 
		 * @return the array representing the path of the selected tile.
		 **************************************************************************/
		private int[] getPath() {
			return path;
		}
	}
}
