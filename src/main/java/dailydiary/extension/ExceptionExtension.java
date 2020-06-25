package dailydiary.extension;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Extension class for exceptions
 *
 */
public class ExceptionExtension {

	/**
	 * Prints StackTrace form exception to a String
	 * 
	 * @param e Exception
	 * @return StackTrace as String
	 */
	public static String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
	// Hidding constructor
	private ExceptionExtension() {}
}
