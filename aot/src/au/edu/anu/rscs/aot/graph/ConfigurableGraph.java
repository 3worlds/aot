package au.edu.anu.rscs.aot.graph;

/**
 * <p>A graph that can "configure" itself, i.e. basically:</p>
 * <ul>
 * <li>typecast its nodes to descendant classes as specified in a 'class' property of the
 * nodes;</li>
 * <li>run an initialise() method after graph construction, whatever this means. Typically
 * Nodes are expected to have an 'initialise()' method that will be invoked in turn by 
 * graph.initialise().</li>
 * </ul>
 * 
 * @author Jacques Gignoux - 24/11/2017
 *
 */
public interface ConfigurableGraph {
			
	/**
	 * Casts Nodes to specialized classes as found in their class properties
	 * @return a list of casting errors
	 */
	public NodeExceptionList castNodes();
	
	/**
	 * Initalises Specialized Nodes as found in their initialise() methods
	 * (in a precise order given by Annotations).
	 * @return a list of initialisation errors
	 */
	public NodeExceptionList initialise();

}
