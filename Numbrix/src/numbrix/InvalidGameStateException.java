package numbrix;

/****************************************************************************
 * Thrown to indicate that a Numbrix game is in an illegal or
 * unreachable state.
 * 
 * @author Zachary Kurmas
 * 
 ****************************************************************************/
// (C) 2009 Zachary Kurmas
// Created Aug 9, 2009
@SuppressWarnings("serial")
public class InvalidGameStateException extends Exception {
	public InvalidGameStateException(String message) {
		super(message);
	}

}
