package au.edu.anu.rscs.aot.init;

import fr.cnrs.iees.graph.Node;

/**
 * 
 * @author Shayne Flint - long ago
 *
 */
public class NodeException {

	private Node node;
	private Exception exc;
	private StackTraceElement[] stackTrace;

	public NodeException(Node node, Exception exc) {
		this.node = node;
		this.exc = exc;
		if (exc != null)
			this.stackTrace = exc.getStackTrace();
	}

	public Node getNode() {
		return node;
	}

	public Exception getException() {
		return exc;
	}

	public String toString() {
		if (exc != null)
			return node.toString() + "[" + exc.toString() + "]";
		else
			return node.toString() + "[no exception specified]";
	}

	public String toLongString() {
		if (exc != null) {
			String result = node.toString() + "[" + exc.toString() + "]";
			for (StackTraceElement element : stackTrace) {
				result = result + "\n" + element;
			}
			return result;
		}
		else
			return toString();
	}

}
