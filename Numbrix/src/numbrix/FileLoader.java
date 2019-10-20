package numbrix;

import java.io.File;

/**************************************************************************
 * A class to get all of the files in a directory.
 * 
 * @author Sam Eary
 * @version 1.0
 **************************************************************************/
public class FileLoader {

	/** The list of files. */
	private File[] fileList;

	/**************************************************************************
	 * Constructor for the FileLoader class.
	 * 
	 * @param filename
	 *            The path of the directory containing the files to be
	 *            loaded.
	 **************************************************************************/
	public FileLoader(String filename) {
		selectFolder(filename);
	}

	/**************************************************************************
	 * Gets a list of files in a specified directory.
	 * 
	 * @param filename
	 *            the path of the directory containing the files to be
	 *            added to the list.
	 **************************************************************************/
	public void selectFolder(String filename) {
		File folder = new File(filename);
		fileList = folder.listFiles();
	}

	/**************************************************************************
	 * Gets the array of files.
	 * 
	 * @return the array of the files.
	 **************************************************************************/
	public File[] getFileList() {
		return fileList;
	}

	/**************************************************************************
	 * Prints out the list of files.
	 **************************************************************************/
	public void showFileList() {
		for (int i = 0; i < fileList.length; i++)
			System.out.println(" " + fileList[i].getName());
	}
}
