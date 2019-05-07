package au.edu.anu.rscs.aot.init.old;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.AotException;
import au.edu.anu.rscs.aot.collections.DynamicList;
import au.edu.anu.rscs.aot.init.InitialiseMessage;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Tree;
import fr.cnrs.iees.graph.impl.TreeGraph;
import fr.cnrs.iees.graph.impl.TreeGraphFactory;
import fr.cnrs.iees.graph.impl.TreeGraphNode;

import static au.edu.anu.rscs.aot.queries.CoreQueries.*;
import static au.edu.anu.rscs.aot.queries.base.SequenceQuery.get;

/**
 * 
 * @author Shayne Flint 2012
 * 
 * refactored by JG 8/1/2019
 * NB: this code will most certainly fail because I dont know where the edge labels are set
 * (ie the only one set in this class is the DEPENDENCY label - the others come from outside,
 * but I dunno where).
 *
 */
public class Initialiser {

	public static final String USER_INITIALISE_AFTER = "_initialiseAfter";
	public static final String USER_INITIALISE_BEFORE = "_initialiseBefore";
	public static final String DEPENDENCY = "_NodeInitialiser_DEPENDENCY";
	public static final String REMOVED = "_NodeInitialiser_REMOVED";
	
	private static final String[][] labels = {
		{USER_INITIALISE_AFTER,	EdgeInitAfter.class.getName()},
		{USER_INITIALISE_BEFORE,EdgeInitBefore.class.getName()},
		{DEPENDENCY,			EdgeDependency.class.getName()},
		{REMOVED,				EdgeRemoved.class.getName()}
	};
	
	private EdgeFactory efactory; 

	private static Logger log = Logger.getLogger(Initialiser.class.getName());

	private TreeGraph<? extends TreeGraphNode, ? extends Edge> nodeList;

	private DynamicList<TreeGraphNode> initialisationList = new DynamicList<TreeGraphNode>();

	private List<InitialiseMessage> initialisationFailList = new LinkedList<>();

	public Initialiser(TreeGraph<? extends TreeGraphNode, ? extends Edge> initGraph) {
		super();
		// sel logging level to WARNING by default - set it to INFO to get debugging messages
		log.setLevel(Level.WARNING);
		// get a factory to make edges
		Map<String,String> labs = new HashMap<>();
		for (int i=0; i<labels.length; i++)
			labs.put(labels[i][0], labels[i][1]);
		efactory = new TreeGraphFactory("Initialiser",labs);
//		this.nodeList = new ArrayList<Initialisable>(initList); // make a local copy
		this.nodeList = initGraph; // no copy - JG - is this wrong?
		processInitialisationEdges();
		processInitialisationAnnotations();
		generateInitialisationList();
		cleanUpNodeList();
	}

	public Iterable<? extends TreeGraphNode> getInitialisationList() {
		return initialisationList;
	}
	
	public Iterable<InitialiseMessage> getInitialisationFailList() {
		return initialisationFailList;
	}

	public List<InitialiseMessage> initialise() {
		for (TreeGraphNode tgn : initialisationList)
			if (tgn instanceof Initialisable) {
				Initialisable node = (Initialisable) tgn;
				try {
					if (log.isLoggable(Level.INFO))
						if (hasInitialiseAtOrder(node))
							if (order(node) > -1) 
		//						System.out.println(node.uniqueId()+"\t"+order(node));
								log.info(tgn.toShortString()+"\t"+order(node));
					node.initialise();
				} catch (Exception e) {
					initialisationFailList.add(new InitialiseMessage(node, e));
//	//				// temp code to print the stack trace - Ian 14.12.2012
//					System.out.println(e);
//					StackTraceElement[] stes = e.getStackTrace();
//					for (StackTraceElement ste:stes)
//						System.out.println(ste);
					log.warning(()->"NodeInitialiser: Node " + node	+ " could not be initialised.");
				}
			}
		return initialisationFailList;
	}

	public void showInitialisationOrder(String title) {
		int count = 0;
		 String theTitle = title;
		if (title == null)
			theTitle = "";
		else
			theTitle = " " + title;
		final String finalTitle = theTitle;
		log.fine("Initialisation order");
		for (TreeGraphNode tgn : initialisationList) {
			if (tgn instanceof Initialisable) {
				Initialisable node = (Initialisable) tgn;
				count++;
				final int finalCount = count;
	//			this doesnt work but I dont know why ??? the lambda syntax is incorrect
				// Ian - the variables count, theTitle must be final(?)
				// Wont work if we use logback library but there is an alternative - see doco
				log.fine("  " + String.format("%05d", finalCount) + finalTitle + ": "
						+ node.getClass().getSimpleName() + " ["
						+ tgn.id() + "]" + annotationString(tgn));
			}
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
	private String annotationString(TreeGraphNode node) {
		String result = "";
		if (node instanceof Initialisable) {
			Initialisable init = (Initialisable) node;
			if (hasInitialiseAtOrder(init))
				result = withComma(result, "after nodes with order < "
						+ order(init));
			if (hasInitialiseAfterClasses(init)) {
				boolean subClasses = node.getClass()
						.getAnnotation(InitialiseAfterClass.class)
						.includeSubclasses();
				if (subClasses)
					result = result
							+ "after nodes of class(es) and subclass(es) of '";
				else
					result = result + "after nodes of class(es) '";
				for (Class<? extends Initialisable> afterClass : afterClasses(init))
					result = withComma(result, afterClass.getName());
				result = endWith(result, "'");
			}
			if (hasInitialiseBeforeClasses(init)) {
				boolean subClasses = node.getClass()
						.getAnnotation(InitialiseAfterClass.class)
						.includeSubclasses();
				if (subClasses)
					result = result
							+ "before nodes of class(es) and subclass(es) of '";
				else
					result = result + "before nodes of class(es) '";
				for (Class<? extends Initialisable> beforeClass : beforeClasses(init))
					result = withComma(result, beforeClass.getName());
				result = endWith(result, "'");
			}
			if (hasInitialiseAfterNodesMatching(init)) {
				result = result + "after nodes matching ref(s) '";
				for (String ref : afterNodesMatching(init))
					result = withComma(result, ref);
				result = endWith(result, "'");
			}
			if (hasInitialiseBeforeNodesMatching(init)) {
				result = result + "before nodes matching ref(s) '";
				for (String ref : beforeNodesMatching(init))
					result = withComma(result, ref);
				result = endWith(result, "'");
			}
			List<Edge> uiaEdges = (List<Edge>) get(node.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(USER_INITIALISE_AFTER)));
	//		for (Edge dep : node.getOutEdges(hasTheLabel(USER_INITIALISE_AFTER)))
			for (Edge dep : uiaEdges)
				result = withComma(result, "after node '"
						+ dep.endNode().id() + "'");			
			List<Edge> uibEdges = (List<Edge>) get(node.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(USER_INITIALISE_BEFORE)));
	//		for (Edge dep : node.getOutEdges(hasTheLabel(USER_INITIALISE_BEFORE)))
			for (Edge dep : uibEdges)
				result = withComma(result, "before node '"
						+ dep.endNode().id() + "'");
			if (result.length() == 0)
				return "";
			else
				return endWith(" will be initialised " + result, "");
		}
		else
			return "Error: node "+node.toShortString()+" has no initialise() method";
	}

	@SuppressWarnings("unchecked")
	private void processInitialisationEdges() {
		// Add dependency edges for user specified initialisation dependencies
		//
		for (TreeGraphNode node : nodeList.nodes()) {
			List<Edge> uiaEdges = (List<Edge>) get(node.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(USER_INITIALISE_AFTER)));
//			for (Edge edge : node.getOutEdges(hasTheLabel(USER_INITIALISE_AFTER))) {
			for (Edge edge : uiaEdges)
				edge.edgeFactory().makeEdge(node,edge.endNode(),DEPENDENCY);
//				node.newEdge((Configurable)edge.endNode(), DEPENDENCY);
		}
		for (TreeGraphNode node : nodeList.nodes()) {
			List<Edge> uibEdges = (List<Edge>) get(node.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(USER_INITIALISE_BEFORE)));
//			for (Edge edge : node.getOutEdges(hasTheLabel(USER_INITIALISE_BEFORE))) {
			for (Edge edge : uibEdges)
//				((Configurable)edge.endNode()).newEdge(node, DEPENDENCY);
				edge.edgeFactory().makeEdge(edge.endNode(),node,DEPENDENCY);
		}
	}

	@SuppressWarnings("unchecked")
	private void cleanUpNodeList() {
		for (TreeGraphNode node : nodeList.nodes()) {
			List<Edge> edges = (List<Edge>) get(node.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(DEPENDENCY)));
			for (Edge edge : edges)
				edge.disconnect();
		}
		for (TreeGraphNode node : nodeList.nodes()) {
			List<Edge> edges = (List<Edge>) get(node.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(REMOVED)));
			for (Edge edge : edges)
				edge.disconnect();
		}
	}

	private void processInitialisationAnnotations() {
		// convert
		for (TreeGraphNode node : nodeList.nodes()) {
			if (node instanceof Initialisable) {
				Initialisable init = (Initialisable) node;
				if (hasInitialiseAtOrder(init))
					if (order(init) > -1) {
						int order = order(init);
						for (TreeGraphNode dep : nodeList.nodes()) {
							if (dep instanceof Initialisable) {
								Initialisable initdep = (Initialisable) dep;
								if (order(initdep) > -1 && order(initdep) < order)
		//							node.newEdge(dep, DEPENDENCY);
									efactory.makeEdge(node,dep,DEPENDENCY);
							}
						}
					}
	
				if (hasInitialiseAfterClasses(init)) {
					boolean subClasses = node.getClass()
							.getAnnotation(InitialiseAfterClass.class)
							.includeSubclasses();
					for (Class<? extends Initialisable> afterClass : afterClasses(init))
						for (TreeGraphNode dep : nodeList.nodes())
							if (subClasses) {
								if (afterClass.isInstance(dep))
	//								node.newEdge(dep, DEPENDENCY);
									efactory.makeEdge(node,dep,DEPENDENCY);
							} else {
								if (afterClass.getName().equals(dep.classId()))
	//								node.newEdge(dep, DEPENDENCY);
									efactory.makeEdge(node,dep,DEPENDENCY);
							}
				}
				if (hasInitialiseBeforeClasses(init)) {
					boolean subClasses = node.getClass()
							.getAnnotation(InitialiseBeforeClass.class)
							.includeSubclasses();
					for (Class<? extends Initialisable> beforeClass : beforeClasses(init))
						for (TreeGraphNode dep : nodeList.nodes()) {
							if (subClasses) {
								if (beforeClass.isInstance(dep))
	//								dep.newEdge(node, DEPENDENCY);
									efactory.makeEdge(dep,node,DEPENDENCY);
							} else {
								if (beforeClass.getName().equals(dep.classId()))
	//								dep.newEdge(node, DEPENDENCY);
									efactory.makeEdge(dep,node,DEPENDENCY);
							}
						}
				}
				if (hasInitialiseAfterNodesMatching(init))
					for (String ref : afterNodesMatching(init))
						for (TreeGraphNode dep : nodeList.nodes())
							if (Tree.matchesReference(dep,ref))
	//						if (dep.matchesRef(ref))
	//							node.newEdge(dep, DEPENDENCY);
								efactory.makeEdge(node,dep,DEPENDENCY);
	
				if (hasInitialiseBeforeNodesMatching(init))
					for (String ref : beforeNodesMatching(init))
						for (TreeGraphNode dep : nodeList.nodes())
							if (Tree.matchesReference(dep,ref))
	//						if (dep.matchesRef(ref))
	//							dep.newEdge(node, DEPENDENCY);
								efactory.makeEdge(dep,node,DEPENDENCY);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void generateInitialisationList() {

		// based on http://en.wikipedia.org/wiki/Topological_sorting
		//
		DynamicList<TreeGraphNode> l = new DynamicList<TreeGraphNode>();
		DynamicList<TreeGraphNode> s = new DynamicList<TreeGraphNode>();

		for (TreeGraphNode node : nodeList.nodes()) {
			List<Edge> inEdges = (List<Edge>) get(node,
				inEdges(),
				selectZeroOrMany(hasTheLabel(DEPENDENCY)));
//			if (node.getInEdges(hasTheLabel(DEPENDENCY)).size() == 0)
			if (inEdges.size() == 0)
				s.add(node);
		}
		while (s.size() != 0) {
			TreeGraphNode n = s.removeLast();
			l.add(n);
			List<Edge> outEdges = (List<Edge>) get(n.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(DEPENDENCY)));
			for (Edge e : outEdges) {
//			for (Edge e : n.getOutEdges(hasTheLabel(DEPENDENCY))) {
				TreeGraphNode m =  (TreeGraphNode) e.endNode();
				
				// TODO: FLAW HERE;
				
//				e.setLabel(REMOVED);
				
				List<Edge> inEdges = (List<Edge>) get(m, 
					inEdges(),
					selectZeroOrMany(hasTheLabel(DEPENDENCY)));
				if (inEdges.size() == 0)
//				if (m.getInEdges(hasTheLabel(DEPENDENCY)).size() == 0)
					s.add(m);
			}
		}
		for (TreeGraphNode node : nodeList.nodes()) {
			List<Edge> edges = (List<Edge>) get(node.getEdges(Direction.OUT),
				selectZeroOrMany(hasTheLabel(DEPENDENCY)));
//			if (node.getOutEdges(hasTheLabel(DEPENDENCY)).size() > 0)
			if (edges.size() > 0)
				throw new AotException("Cyclic initialisation dependency to node " 
					+ node + " detected in NodeList " + nodeList);
		}
		LinkedList<TreeGraphNode> stack = new LinkedList<TreeGraphNode>();

		for (TreeGraphNode node : l)
			stack.push(node);
		while (!stack.isEmpty())
			initialisationList.add(stack.pop());

	}

	private boolean hasInitialiseAtOrder(Initialisable node) {
		return node.getClass().getAnnotation(InitialiseAtOrder.class) != null;
	}

	private boolean hasInitialiseAfterClasses(Initialisable node) {
		return node.getClass().getAnnotation(InitialiseAfterClass.class) != null;
	}

	private boolean hasInitialiseBeforeClasses(Initialisable node) {
		return node.getClass().getAnnotation(InitialiseBeforeClass.class) != null;
	}

	private boolean hasInitialiseAfterNodesMatching(Initialisable node) {
		return node.getClass()
				.getAnnotation(InitialiseAfterNodesMatching.class) != null;
	}

	private boolean hasInitialiseBeforeNodesMatching(Initialisable node) {
		return node.getClass().getAnnotation(
				InitialiseBeforeNodesMatching.class) != null;
	}

	private int order(Initialisable node) {
		if (hasInitialiseAtOrder(node))
			return node.getClass().getAnnotation(InitialiseAtOrder.class)
					.value();
		else
			return -1;
	}

	private Class<? extends Initialisable>[] afterClasses(Initialisable node) {
		return node.getClass().getAnnotation(InitialiseAfterClass.class)
				.value();
	}

	private Class<? extends Initialisable>[] beforeClasses(Initialisable node) {
		return node.getClass().getAnnotation(InitialiseBeforeClass.class)
				.value();
	}

	private String[] afterNodesMatching(Initialisable node) {
		return node.getClass()
				.getAnnotation(InitialiseAfterNodesMatching.class).value();
	}

	private String[] beforeNodesMatching(Initialisable node) {
		return node.getClass()
				.getAnnotation(InitialiseBeforeNodesMatching.class).value();
	}

}
