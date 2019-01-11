package au.edu.anu.rscs.aot.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.AotException;
import au.edu.anu.rscs.aot.collections.DynamicList;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.tree.Tree;

import static au.edu.anu.rscs.aot.queries.CoreQueries.*;
import static au.edu.anu.rscs.aot.queries.base.SequenceQuery.get;

/**
 * 
 * @author Shayne Flint 2012
 * 
 * refactored by JG 8/1/2019
 *
 */
public class NodeInitialiser {

	private static Logger log = Logger.getLogger(NodeInitialiser.class.getName());

	private AotGraph nodeList;

	private DynamicList<AotNode> initialisationList = new DynamicList<AotNode>();

	public NodeInitialiser(AotGraph aotGraph) {
		this.nodeList = new AotGraph(aotGraph.nodes()); // make a local copy
		processInitialisationEdges();
		processInitialisationAnnotations();
		generateInitialisationList();
		cleanUpNodeList();
	}

	public DynamicList<AotNode> getInitialisationList() {
		return initialisationList;
	}

	public static final String USER_INITIALISE_AFTER = "_initialiseAfter";
	public static final String USER_INITIALISE_BEFORE = "_initialiseBefore";
	public static final String DEPENDENCY = "_NodeInitialiser_DEPENDENCY";
	public static final String REMOVED = "_NodeInitialiser_REMOVED";

	private NodeExceptionList initialisationFailList;

	public NodeExceptionList getInitialisationFailList() {
		return initialisationFailList;
	}

	public NodeExceptionList initialise() {
		initialisationFailList = new NodeExceptionList();
		for (AotNode node : initialisationList)
			try {
				// uncomment to check init order
//				if (hasInitialiseAtOrder(node))
//					if (order(node) > -1) 
//						System.out.println(node.uniqueId()+"\t"+order(node));
				node.initialise();
			} catch (Exception e) {
				initialisationFailList.add(node, e);
//				// temp code to print the stack trace - Ian 14.12.2012
				System.out.println(e);
				StackTraceElement[] stes = e.getStackTrace();
				for (StackTraceElement ste:stes)
					System.out.println(ste);
				log.warning(()->"NodeInitialiser: Node " + node	+ " could not be initialised.");
			}
		return initialisationFailList;
	}

	public void initialiseAndCheck() {
		if (initialise().size()>0)
			throw new AotException("NodeInitialiser.initialise() failed. "
					+ initialisationFailList.toLongString());
	}

	public void showInitialisationOrder(String title) {
		int count = 0;
		String theTitle = title;
		if (title == null)
			theTitle = "";
		else
			theTitle = " " + title;
		log.fine("Initialisation order");
		for (AotNode node : initialisationList) {
			count++;
			String msg = "  " + String.format("%05d", count) + theTitle + ": "
					+ node.getClass().getSimpleName() + " ["
					+ node.uniqueId() + "]" + annotationString(node);
			log.fine(msg);
//			this doesnt work but I dont know why ??? the lambda syntax is incorrect
//			log.fine(() -> "  " + String.format("%05d", count) + theTitle + ": "
//					+ node.getClass().getSimpleName() + " ["
//					+ node.uniqueId() + "]" + annotationString(node));
		}
	}

	public void showInitialisationOrder() {
		showInitialisationOrder(null);
	}

	private String withComma(String result, String str) {
		if (result.endsWith(","))
			return result + " " + str + ",";
		else
			return result + str + ",";
	}

	private String endWith(String result, String str) {
		if (result.endsWith(","))
			return result.substring(0, result.length() - 1) + str;
		else
			return result + str;
	}

	@SuppressWarnings("unchecked")
	private String annotationString(AotNode node) {
		String result = "";
		if (hasInitialiseAtOrder(node))
			result = withComma(result, "after nodes with order < "
					+ order(node));
		if (hasInitialiseAfterClasses(node)) {
			boolean subClasses = node.getClass()
					.getAnnotation(InitialiseAfterClass.class)
					.includeSubclasses();
			if (subClasses)
				result = result
						+ "after nodes of class(es) and subclass(es) of '";
			else
				result = result + "after nodes of class(es) '";
			for (Class<? extends AotNode> afterClass : afterClasses(node))
				result = withComma(result, afterClass.getName());
			result = endWith(result, "'");
		}
		if (hasInitialiseBeforeClasses(node)) {
			boolean subClasses = node.getClass()
					.getAnnotation(InitialiseAfterClass.class)
					.includeSubclasses();
			if (subClasses)
				result = result
						+ "before nodes of class(es) and subclass(es) of '";
			else
				result = result + "before nodes of class(es) '";
			for (Class<? extends AotNode> beforeClass : beforeClasses(node))
				result = withComma(result, beforeClass.getName());
			result = endWith(result, "'");
		}
		if (hasInitialiseAfterNodesMatching(node)) {
			result = result + "after nodes matching ref(s) '";
			for (String ref : afterNodesMatching(node))
				result = withComma(result, ref);
			result = endWith(result, "'");
		}
		if (hasInitialiseBeforeNodesMatching(node)) {
			result = result + "before nodes matching ref(s) '";
			for (String ref : beforeNodesMatching(node))
				result = withComma(result, ref);
			result = endWith(result, "'");
		}
		List<AotEdge> uiaEdges = (List<AotEdge>) get(node.getEdges(Direction.OUT),
			selectZeroOrMany(hasTheLabel(USER_INITIALISE_AFTER)));
//		for (Edge dep : node.getOutEdges(hasTheLabel(USER_INITIALISE_AFTER)))
		for (AotEdge dep : uiaEdges)
			result = withComma(result, "after node '"
					+ ((AotNode)dep.endNode()).uniqueId() + "'");			
		List<AotEdge> uibEdges = (List<AotEdge>) get(node.getEdges(Direction.OUT),
			selectZeroOrMany(hasTheLabel(USER_INITIALISE_BEFORE)));
//		for (Edge dep : node.getOutEdges(hasTheLabel(USER_INITIALISE_BEFORE)))
		for (AotEdge dep : uibEdges)
			result = withComma(result, "before node '"
					+ ((AotNode)dep.endNode()).uniqueId() + "'");
		if (result.length() == 0)
			return "";
		else
			return endWith(" will be initialised " + result, "");
	}

	@SuppressWarnings("unchecked")
	private void processInitialisationEdges() {
		// Add dependency edges for user specified initialisation dependencies
		//
		for (AotNode node : nodeList.nodes()) {
			List<AotEdge> uiaEdges = (List<AotEdge>) get(node.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(USER_INITIALISE_AFTER)));
//			for (Edge edge : node.getOutEdges(hasTheLabel(USER_INITIALISE_AFTER))) {
			for (AotEdge edge : uiaEdges)
				node.nodeFactory().makeEdge(node,edge.endNode()).setLabel(DEPENDENCY);
//				node.newEdge((AotNode)edge.endNode(), DEPENDENCY);
		}
		for (AotNode node : nodeList.nodes()) {
			List<AotEdge> uibEdges = (List<AotEdge>) get(node.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(USER_INITIALISE_BEFORE)));
//			for (Edge edge : node.getOutEdges(hasTheLabel(USER_INITIALISE_BEFORE))) {
			for (AotEdge edge : uibEdges)
//				((AotNode)edge.endNode()).newEdge(node, DEPENDENCY);
				node.nodeFactory().makeEdge(edge.endNode(),node).setLabel(DEPENDENCY);
		}
	}

	@SuppressWarnings("unchecked")
	private void cleanUpNodeList() {
		for (AotNode node : nodeList.nodes()) {
			List<AotEdge> edges = (List<AotEdge>) get(node.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(DEPENDENCY)));
//			for (Edge edge : node.getOutEdges(hasTheLabel(DEPENDENCY))) {
			for (AotEdge edge : edges)
				edge.disconnect();
		}
		for (AotNode node : nodeList.nodes()) {
			List<AotEdge> edges = (List<AotEdge>) get(node.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(REMOVED)));
//			for (Edge edge : node.getOutEdges(hasTheLabel(REMOVED))) {
			for (AotEdge edge : edges)
				edge.disconnect();
		}
	}

	private void processInitialisationAnnotations() {

		// convert
		for (AotNode node : nodeList.nodes()) {
			if (hasInitialiseAtOrder(node))
				if (order(node) > -1) {
					int order = order(node);
					for (AotNode dep : nodeList.nodes())
						if (order(dep) > -1 && order(dep) < order)
//							node.newEdge(dep, DEPENDENCY);
							node.nodeFactory().makeEdge(node,dep).setLabel(DEPENDENCY);
				}

			if (hasInitialiseAfterClasses(node)) {
				boolean subClasses = node.getClass()
						.getAnnotation(InitialiseAfterClass.class)
						.includeSubclasses();
				for (Class<? extends AotNode> afterClass : afterClasses(node))
					for (AotNode dep : nodeList.nodes())
						if (subClasses) {
							if (afterClass.isInstance(dep))
//								node.newEdge(dep, DEPENDENCY);
								node.nodeFactory().makeEdge(node,dep).setLabel(DEPENDENCY);
						} else {
							if (afterClass.getName().equals(dep.getName()))
//								node.newEdge(dep, DEPENDENCY);
								node.nodeFactory().makeEdge(node,dep).setLabel(DEPENDENCY);
						}
			}
			if (hasInitialiseBeforeClasses(node)) {
				boolean subClasses = node.getClass()
						.getAnnotation(InitialiseBeforeClass.class)
						.includeSubclasses();
				for (Class<? extends AotNode> beforeClass : beforeClasses(node))
					for (AotNode dep : nodeList.nodes()) {
						if (subClasses) {
							if (beforeClass.isInstance(dep))
//								dep.newEdge(node, DEPENDENCY);
								node.nodeFactory().makeEdge(dep,node).setLabel(DEPENDENCY);
						} else {
							if (beforeClass.getName().equals(dep.getName()))
//								dep.newEdge(node, DEPENDENCY);
								node.nodeFactory().makeEdge(dep,node).setLabel(DEPENDENCY);
						}
					}
			}
			if (hasInitialiseAfterNodesMatching(node))
				for (String ref : afterNodesMatching(node))
					for (AotNode dep : nodeList.nodes())
						if (Tree.matchesReference(dep,ref))
//						if (dep.matchesRef(ref))
//							node.newEdge(dep, DEPENDENCY);
							node.nodeFactory().makeEdge(node,dep).setLabel(DEPENDENCY);

			if (hasInitialiseBeforeNodesMatching(node))
				for (String ref : beforeNodesMatching(node))
					for (AotNode dep : nodeList.nodes())
						if (Tree.matchesReference(dep,ref))
//						if (dep.matchesRef(ref))
//							dep.newEdge(node, DEPENDENCY);
							node.nodeFactory().makeEdge(dep,node).setLabel(DEPENDENCY);
		}
	}

	@SuppressWarnings("unchecked")
	private void generateInitialisationList() {

		// based on http://en.wikipedia.org/wiki/Topological_sorting
		//
		DynamicList<AotNode> l = new DynamicList<AotNode>();
		DynamicList<AotNode> s = new DynamicList<AotNode>();

		for (AotNode node : nodeList.nodes()) {
			List<AotEdge> inEdges = (List<AotEdge>) get(node,
				inEdges(),
				selectZeroOrMany(hasTheLabel(DEPENDENCY)));
//			if (node.getInEdges(hasTheLabel(DEPENDENCY)).size() == 0)
			if (inEdges.size() == 0)
				s.add(node);
		}
		while (s.size() != 0) {
			AotNode n = s.removeLast();
			l.add(n);
			List<AotEdge> outEdges = (List<AotEdge>) get(n.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(DEPENDENCY)));
			for (AotEdge e : outEdges) {
//			for (Edge e : n.getOutEdges(hasTheLabel(DEPENDENCY))) {
				AotNode m = (AotNode) e.endNode();
				e.setLabel(REMOVED);
				List<AotEdge> inEdges = (List<AotEdge>) get(m, 
					inEdges(),
					selectZeroOrMany(hasTheLabel(DEPENDENCY)));
				if (inEdges.size() == 0)
//				if (m.getInEdges(hasTheLabel(DEPENDENCY)).size() == 0)
					s.add(m);
			}
		}
		for (AotNode node : nodeList.nodes()) {
			List<AotEdge> edges = (List<AotEdge>) get(node.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(DEPENDENCY)));
//			if (node.getOutEdges(hasTheLabel(DEPENDENCY)).size() > 0)
			if (edges.size() > 0)
				throw new AotException("Cyclic initialisation dependency to node " 
					+ node + " detected in NodeList " + nodeList);
		}
		LinkedList<AotNode> stack = new LinkedList<AotNode>();

		for (AotNode node : l)
			stack.push(node);
		while (!stack.isEmpty())
			initialisationList.add(stack.pop());

	}

	private boolean hasInitialiseAtOrder(AotNode node) {
		return node.getClass().getAnnotation(InitialiseAtOrder.class) != null;
	}

	private boolean hasInitialiseAfterClasses(AotNode node) {
		return node.getClass().getAnnotation(InitialiseAfterClass.class) != null;
	}

	private boolean hasInitialiseBeforeClasses(AotNode node) {
		return node.getClass().getAnnotation(InitialiseBeforeClass.class) != null;
	}

	private boolean hasInitialiseAfterNodesMatching(AotNode node) {
		return node.getClass()
				.getAnnotation(InitialiseAfterNodesMatching.class) != null;
	}

	private boolean hasInitialiseBeforeNodesMatching(AotNode node) {
		return node.getClass().getAnnotation(
				InitialiseBeforeNodesMatching.class) != null;
	}

	private int order(AotNode node) {
		if (hasInitialiseAtOrder(node))
			return node.getClass().getAnnotation(InitialiseAtOrder.class)
					.value();
		else
			return -1;
	}

	private Class<? extends AotNode>[] afterClasses(AotNode node) {
		return node.getClass().getAnnotation(InitialiseAfterClass.class)
				.value();
	}

	private Class<? extends AotNode>[] beforeClasses(AotNode node) {
		return node.getClass().getAnnotation(InitialiseBeforeClass.class)
				.value();
	}

	private String[] afterNodesMatching(AotNode node) {
		return node.getClass()
				.getAnnotation(InitialiseAfterNodesMatching.class).value();
	}

	private String[] beforeNodesMatching(AotNode node) {
		return node.getClass()
				.getAnnotation(InitialiseBeforeNodesMatching.class).value();
	}

}
