package numbrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

/****************************************************************
 * Test {@code NumbrixEngine}
 * 
 * @author Zachary Kurmas
 * 
 ****************************************************************/
// (C) 2010 Zachary Kurmas
// Created Jan 6, 2010
public class NumbrixEngineTest
{

	// The directory in which all the test files are located
	private static String BASE = "C:/Users/Sam/Desktop/test/";

	// The prefixes for the three different types of tests.
	private static String WILL_NOT_PARSE = "willNotParse";
	private static String INVALID_STATE = "invalidState";
	private static String VALID_STATE = "valid";
	private static String COMPLETE = "complete";
	private static String PROBLEM = "problem";
	
	private static int numFailures = 0;

	/**************************************************************************
	 * Make sure loadGame correctly throws a FileNotFoundException.
	 **************************************************************************/
	private static void testFileNotFound() throws ParseException,
			InvalidGameStateException
	{
		NumbrixEngine game = new NumbrixEngine();

		// Calling loadGame should result in a FileNotFound exception.
		try {
			game.loadGame("ThisFileCant24234Possibly304958Exist!");
			System.out.println("FileNotFound test\tFailed");
			numFailures++;
		} catch (java.io.FileNotFoundException e) {
			System.out.println("FileNotFound test\tPASSED");
			// Good. We expect to catch this exception
		}
	}

	/**************************************************************************
	 * Make sure each file that begins with willNotParse throws a ParseException
	 * 
	 * @throws FileNotFoundException
	 **************************************************************************/
	private static void testWillNotParse() throws FileNotFoundException
	{
		// Test each file with the proper prefix.
		for (String fn : getPrefixedFiles(BASE, WILL_NOT_PARSE)) {
			System.out.print(fn + ":\t");

			NumbrixEngine game = new NumbrixEngine();
			try {
				game.loadGame(BASE + fn);
				System.out.println("FAILED to throw a ParseException");
				numFailures++;

			} catch (java.text.ParseException e) {
				System.out.println("PASSED");
			} catch (InvalidGameStateException e) {
				System.out
						.println("FAILED incorrectly threw InvalidGameState: "
								+ e.getMessage());
				numFailures++;
			} catch (java.util.NoSuchElementException nse) {
				System.out.println("FAILED threw NoSuchElementException");
				numFailures++;
			} catch (Exception e) {
				System.out.println("FAILED:  Unexpected exception: " + e);
				numFailures++;
			}
		} // end for

	} // end testWillNotParse

	/**************************************************************************
	 * Make sure that all files beginning with "invalidState_" throw an
	 * InvalidGameStateException
	 * 
	 * @throws FileNotFoundException
	 **************************************************************************/
	private static void testInvalidState() throws FileNotFoundException
	{
		// Test each file with the proper prefix.
		for (String fn : getPrefixedFiles(BASE, INVALID_STATE)) {
			System.out.print(fn + ":\t");

			NumbrixEngine game = new NumbrixEngine();
			try {
				game.loadGame(BASE + fn);
				System.out
						.println("FAILED to throw an InvalidGameStateException");
				numFailures++;
			} catch (InvalidGameStateException e) {
				System.out.println("PASSED");
			} catch (ParseException e) {
				System.out
						.println("FAILED: incorrectly threw a Parse Exception: "
								+ e.getMessage());
				numFailures++;
			} catch (Exception e) {
				System.out.println("FAILED:  Unexpected exception: " + e);
				numFailures++;
			}
		} // end for

	}

	/**************************************************************************
	 * Make sure that all files beginning with "problem_" throw some exception
	 * 
	 * @throws FileNotFoundException
	 **************************************************************************/
	private static void testProblem() throws FileNotFoundException
	{
		// Test each file with the proper prefix.
		for (String fn : getPrefixedFiles(BASE, PROBLEM)) {
			System.out.print(fn + ":\t");

			NumbrixEngine game = new NumbrixEngine();
			try {
				game.loadGame(BASE + fn);
				System.out
						.println("FAILED to throw an InvalidGameStateException");
				numFailures++;
			} catch (InvalidGameStateException e) {
				System.out.println("PASSED");
			} catch (ParseException e) {
				System.out.println("PASSED");
			} catch (Exception e) {
				System.out.println("FAILED:  Unexpected exception: " + e);
				numFailures++;
			}
		} // end for

	}

	/**************************************************************************
	 * Make sure that all files beginning with "valid_" load properly and return
	 * the correct value for isComplete();
	 * 
	 * @throws FileNotFoundException
	 **************************************************************************/
	public static void testValidState() throws FileNotFoundException
	{
		// Test each file with the proper prefix.
		for (String fn : getPrefixedFiles(BASE, VALID_STATE)) {
			System.out.print(fn + ":\t");

			// Files representing complete games should begin with
			// valid_complete_
			boolean expected_complete = fn.startsWith(VALID_STATE + "_"
					+ COMPLETE);

			NumbrixEngine game = new NumbrixEngine();

			try {
				game.loadGame(BASE + fn);
				if (expected_complete == game.isComplete()) {
					System.out.println("PASSED");
				} else {
					System.out
							.println("FAILED.  isComplete() returned incorrect answer.");
					numFailures++;
				}
			} catch (ParseException e) {
				System.out
						.println("FAILED.  Caught unexpected ParseException.  "
								+ e.getMessage());
				numFailures++;
			} catch (InvalidGameStateException e) {
				System.out
						.println("FAILED.  Caught unexpected InvalidGameStateException. "
								+ e.getMessage());
				numFailures++;
			} catch (Exception e) {
				System.out.println("FAILED:  Unexpected exception: " + e);
				numFailures++;
			}

		} // end for

	} // end method

	/**
	 * Returns the list of files in {@code directory_name} that begin with
	 * {@code prefix}.
	 */
	private static String[] getPrefixedFiles(String directory_name,
			final String prefix)
	{

		// Creates a File object to represent the directory to be searched.
		File dir = new File(directory_name);

		// Make sure the specified directory is, in fact, a directory.
		// Assert.assertTrue(dir + " is not a directory.", dir.isDirectory());

		// The method dir.list(FilenameFilter) returns all the file names in the
		// directory that pass through the filter.
		// The parameter below is an anonymous object: An implementation of the
		// FilenameFilter interface for which we never give a name.
		return dir.list(new java.io.FilenameFilter() {
			public boolean accept(File dir, String name)
			{
				return name.startsWith(prefix) && !name.endsWith("~");
			}
		});

	} // end getPrefixedFiles

	private static void runNoParams() throws FileNotFoundException,
			ParseException, InvalidGameStateException
	{
		java.util.Scanner input = new java.util.Scanner(System.in);
		System.out.print("Enter base directory [" + BASE + "]: ");
		String answer = input.nextLine().trim();
		if (answer.length() > 0) {
			BASE = answer;
		}

		while (true) {
			System.out
					.print("Enter file name, \"all\" to run all tests, or blank line to quit: ");
			answer = input.nextLine().trim();
			if (answer.equals("all")) {
				runAll();
			} else if (answer.equals("")) {
				System.out.println("Good Bye");
				return;
			} else {
				NumbrixEngine ne = new NumbrixEngine();
				ne.loadGame(BASE + answer);
			}
		}

	}

	private static void runAll() throws FileNotFoundException, ParseException,
			InvalidGameStateException
	{
		System.out.println("Running all tests.");
		testFileNotFound();
		testWillNotParse();
		testInvalidState();
		testProblem();
		testValidState();

	}

	private static void runWithParams(String[] args)
			throws FileNotFoundException, ParseException,
			InvalidGameStateException
	{
		java.util.ArrayList<String> filenames = new java.util.ArrayList<String>();
		boolean runAll = false;
		for (int x = 0; x < args.length; x++) {
			if (args[x].startsWith("--base")) {
				++x;
				BASE = args[x];
				System.out.println("Base is now: " + BASE);
			} else if (args[x].startsWith("run")) {
				runAll = true;
			} else {
				filenames.add(args[x]);
			}
		}

		if (runAll || filenames.size() == 0) {
			runAll();
		}

		for (String file : filenames) {
			NumbrixEngine ne = new NumbrixEngine();
			ne.loadGame(BASE + file);
		}
		System.out.println("Done");

	}

	public static void main(String[] args) throws FileNotFoundException,
			ParseException, InvalidGameStateException
	{
		// If the user doesn't pass any command-line arguments, prompt the user
		// for the data. Otherwise, take data from the command line
		if (args.length == 0) {
			runNoParams();
		} else {
			runWithParams(args);
		}
		System.out.println("Number of failures:  " + numFailures);
	}

}
