package report;

import mulan.core.MulanException;


/**
 * Interface for generate reports with the format specified.
 * 
 * @author Álvaro A. Belmonte
 * @author Eva Gibaja
 * @author Amelia Zafra
 * @version 20180622
 */
public interface IReport {

	/**
	 * Convert to CSV the evaluator results.
	 *
	 * @return the string with CSV content
	 */
	public String toCSV() throws MulanException;
	
	/**
	 * Convert to plain text the evaluator results.
	 *
	 * @return the string with the content
	 */
	public String toString();
	
}
