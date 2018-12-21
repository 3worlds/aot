package au.edu.anu.rscs.aot;

import fr.cnrs.iees.graph.Node;

/**
 * 
 * @author Shayne Flint - veery long ago
 *
 */
public class AotException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AotException(Node item, String message) {
		super("[on " + item + "]\n[" + message + "]");
	}

	public AotException(String message) {
		super("[" + message + "]");
	}

	public AotException(Exception e) {
		super(e);
	}

	public AotException(String message, Exception e) {
		super("[" + message + "]\n[original exception: " + e + "]");
		e.printStackTrace();
	}

}
