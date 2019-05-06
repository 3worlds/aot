package au.edu.anu.rscs.aot.init;

import au.edu.anu.rscs.aot.collections.DynamicList;
import fr.cnrs.iees.graph.Node;

/**
 * 
 * @author Shayne Flint - long ago.
 *
 */
public class NodeExceptionList extends DynamicList<NodeException> {

	public void add(Node node, Exception exc) {
		add(new NodeException(node, exc));
	}
}
