/**************************************************************************
 *  AOT - Aspect-Oriented Thinking                                        *
 *                                                                        *
 *  Copyright 2018: Shayne Flint, Jacques Gignoux & Ian D. Davies         *
 *       shayne.flint@anu.edu.au                                          *
 *       jacques.gignoux@upmc.fr                                          *
 *       ian.davies@anu.edu.au                                            * 
 *                                                                        *
 *  AOT is a method to generate elaborate software code from a series of  *
 *  independent domains of knowledge. It enables one to manage and        *
 *  maintain software from explicit specifications that can be translated *
 *  into any programming language.          							  *
 **************************************************************************                                       
 *  This file is part of AOT (Aspect-Oriented Thinking).                  *
 *                                                                        *
 *  AOT is free software: you can redistribute it and/or modify           *
 *  it under the terms of the GNU General Public License as published by  *
 *  the Free Software Foundation, either version 3 of the License, or     *
 *  (at your option) any later version.                                   *
 *                                                                        *
 *  AOT is distributed in the hope that it will be useful,                *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *  GNU General Public License for more details.                          *                         
 *                                                                        *
 *  You should have received a copy of the GNU General Public License     *
 *  along with UIT.  If not, see <https://www.gnu.org/licenses/gpl.html>. *
 *                                                                        *
 **************************************************************************/
package au.edu.anu.rscs.aot.graph;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.AotException;
import au.edu.anu.rscs.aot.collections.QuickListOfLists;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Graph;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.tree.Tree;
import fr.cnrs.iees.tree.TreeNode;
import fr.cnrs.iees.tree.TreeNodeFactory;
import fr.ens.biologie.generic.Textable;

/**
 * <p>
 * Re-implementation of AotGraph as a tree, a graph, a tree and graph factory, a
 * configurable graph.
 * </p>
 * 
 * <p>
 * NOTE: This implementation assumes that every newly created node is inserted
 * within the graph (factory interfaces).
 * </p>
 * <p>
 * Should this class implement DynamicGraph ? it's already mutable because it is
 * a factory...
 * </p>
 * 
 * @author Jacques Gignoux - 21 déc. 2018
 *
 */

public class AotGraph implements Tree<AotNode>, //
		Graph<AotNode, AotEdge>, //
		ConfigurableGraph, //
		NodeFactory, //
		EdgeFactory, //
		TreeNodeFactory,
		Textable {

	private Logger log = Logger.getLogger(AotGraph.class.getName());

	private Set<AotNode> nodes; // no duplicate nodes permitted
	private int minDepth;
	private int maxDepth;
	private AotNode root;

	// constructors
	public AotGraph() {
		super();
		this.nodes = new HashSet<>();
	}

	public AotGraph(Iterable<AotNode> list) {
		this();
		for (AotNode n : list)
			nodes.add(n);
		// order is undefined so must search?
		root = root();
	}

	/**
	 * builds the graph from a tree root by inserting all children of the argument
	 * 
	 * @param root
	 */
	public AotGraph(AotNode root) {
		this();
		this.root = root;
		insertChildren(root);
		computeDepths(root);
	}

	private void insertChildren(TreeNode parent) {
		for (TreeNode child : parent.getChildren()) {
			nodes.add((AotNode) child);
			insertChildren(child);
		}
	}

	private void computeDepths(AotNode parent) {
		// TODO Auto-generated method stub
	}

	public EdgeFactory getEdgeFactory() {
		return this;
	}

	public TreeNodeFactory getTreeFactory() {
		return this;
	}

	@Override
	public Iterable<AotNode> leaves() {
		List<AotNode> result = new ArrayList<>(nodes.size());
		for (AotNode n : nodes)
			if (n.isLeaf())
				result.add(n);
		return result;
	}

	@Override
	public Iterable<AotNode> nodes() {
		return nodes;
	}

	@Override
	public int size() {
		return nodes.size();
	}

	// -------------------- GRAPH <AOTNODE, AOTEDGE>
	@Override
	public boolean contains(AotNode n) {
		return nodes.contains(n);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<AotEdge> edges() {
		QuickListOfLists<AotEdge> edges = new QuickListOfLists<>();
		for (AotNode n : nodes)
			edges.addList((Iterable<AotEdge>) n.getEdges(Direction.OUT));
		return edges;
	}

	@Override
	public Iterable<AotNode> roots() {
		List<AotNode> result = new ArrayList<>(nodes.size());
		for (AotNode n : nodes)
			if (n.getParent() == null)
				result.add(n);
		return result;
	}

	// ------------------ TREE -------------------------------------
	@Override
	public Iterable<AotNode> findNodesByReference(String reference) {
		List<AotNode> found = new ArrayList<>(nodes.size()); // this may be a bad idea for big graphs
		for (AotNode n : nodes)
			if (Tree.matchesReference(n, reference))
				found.add(n);
		return found;
	}

	@Override
	public int maxDepth() {
		// TODO not implemented
		return maxDepth;
	}

	@Override
	public int minDepth() {
		// TODO not implemented
		return minDepth;
	}

	private AotNode findRoot() {
		List<AotNode> roots = (List<AotNode>) roots();
		if (roots.size() == 1)
			return roots.get(0);
		return null;
	}

	@Override
	public AotNode root() {
		if (root == null)
			root = findRoot();
		return root;
	}

	@Override
	public Tree<AotNode> subTree(AotNode parent) {
		return new AotGraph(parent);
	}

	// ---------------------- NODE FACTORY -------------------------

	// This is disabled because any new node has to be inserted into the tree at the
	// proper spot. We dont want free-floating nodes in an AOT graph because it's a
	// tree.
	@Override
	public AotNode makeNode(String arg0, String arg1, ReadOnlyPropertyList arg2) {
		throw new AotException("Attempt to instantiate an AotNode outside of the tree context.");
	}

	// ---------------------- EDGE FACTORY -------------------------

	@Override
	public AotEdge makeEdge(Node start, Node end) {
		return makeEdge(start, end, null, null, null);
	}

	@Override
	public AotEdge makeEdge(Node start, Node end, ReadOnlyPropertyList props) {
		return makeEdge(start, end, null, null, props);
	}

	@Override
	public AotEdge makeEdge(Node start, Node end, String label, ReadOnlyPropertyList props) {
		return makeEdge(start, end, label, null, props);
	}

	@Override
	public AotEdge makeEdge(Node start, Node end, String label, String name) {
		return makeEdge(start, end, label, name, null);
	}

	@Override
	public AotEdge makeEdge(Node start, Node end, String label) {
		return makeEdge(start, end, label, null, null);
	}

	// use with caution - name+label must be unique within the graph
	@Override
	public AotEdge makeEdge(Node start, Node end, String label, String name, ReadOnlyPropertyList props) {
		AotEdge result;
		if (props != null)
			if (name!=null)
				result = new AotEdge(start, end, label, name, props, this);
			else
				result = new AotEdge(start, end, label, "", props, this);
		else
			if (name!=null)
				result = new AotEdge(start, end, label, name, this);
			else
				result = new AotEdge(start, end, label, "", this);
//		result.setLabel(label);
//		result.setName(name);
		return result;
	}

	// ----------------------TREE FACTORY -----------------------------

	@Override
	public AotNode makeTreeNode(TreeNode parent, SimplePropertyList props) {
		return makeTreeNode(parent, null, null, props);
	}

	@Override
	public AotNode makeTreeNode(TreeNode parent) {
		return makeTreeNode(parent, null, null, null);
	}

	@Override
	public AotNode makeTreeNode(TreeNode parent, String label, String name) {
		return makeTreeNode(parent, label, name, null);
	}

	@Override
	public AotNode makeTreeNode(TreeNode parent, String label) {
		return makeTreeNode(parent, label, null, null);
	}

	@Override
	public AotNode makeTreeNode(TreeNode parent, String label, SimplePropertyList properties) {
		return makeTreeNode(parent, label, null, properties);
	}

	/**
	 * A node which label and name duplicates a node already in the tree will not be
	 * created nor inserted, a warning will be issued instead. If no label is given,
	 * the label defaults to "AOTNode". If no label nor name are given, the name
	 * defaults to a unique ID, so that the node is always created.
	 */
	@Override
	public AotNode makeTreeNode(TreeNode parent, String label, String name, SimplePropertyList props) {
		AotNode node;
		if (label == null)
			if (name == null)
				node = new AotNode(this);
			else
				node = new AotNode(name, this);
		else
			if (name == null)
				node = new AotNode(label,"",this);
			else
				node = new AotNode(label, name, this);
		if (!nodes.add(node)) {
			log.warning(() -> "Duplicate Node insertion: " + node.toDetailedString());
			return null;
		} else {
			node.setParent(parent);
			if (parent != null)
				parent.addChild(node);
			if (props != null)
				node.addProperties(props);
			return node;
		}
	}

	// -------------------- CONFIGURABLE GRAPH ------------------------

	@SuppressWarnings("unchecked")
	@Override
	public NodeExceptionList castNodes() {
		NodeExceptionList castFailList = new NodeExceptionList();
		List<AotNode> removedNodes = new ArrayList<>(nodes.size());
		List<AotNode> addedNodes = new ArrayList<>(nodes.size());
		for (AotNode n : nodes) {
			try {
				String className;
				className = (String) n.getPropertyValue("class");
//				I dont like this, so try to remove it
//				if (className != null && !n.getLabel().equals("defaultPropertyList")) {				
				if (className != null) {
					AotNode newNode = n;
					try {
						ClassLoader c = Thread.currentThread().getContextClassLoader();
						Class<? extends AotNode> nodeClass = (Class<? extends AotNode>) Class.forName(className, false,
								c);
						Constructor<? extends AotNode> nodeConstructor = nodeClass.getConstructor();
						// NOTE: a node constructor always has a factory as argument...
						newNode = nodeConstructor.newInstance(this);
						
						// TODO: possible flaw here due to removing name and label
						
//						newNode.setLabel(n.getLabel());
//						newNode.setName(n.getName());
						newNode.connectLike(n);
						newNode.setProperties(n);
						n.disconnect();
						removedNodes.add(n);
						addedNodes.add(newNode);
					} catch (Exception e) {
						throw new AotException("Cannot clone " + this + " with class " + className, e);
					}
				}
			} catch (Exception e) {
				castFailList.add(n, e);
//				log.warning("AotGraph: Node " + n + " could not be cast.", e);
			}
		}
		nodes.removeAll(removedNodes);
		nodes.addAll(addedNodes);
		return castFailList;
	}

	@Override
	public NodeExceptionList initialise() {
		// Phew! maybe difficult to generalise 
		NodeInitialiser initialiser = new NodeInitialiser(this);
		initialiser.showInitialisationOrder();
		return initialiser.initialise();
	}
	
	// LOCAL

	private int nEdges() {
		int n=0;
		for (Node node:nodes)
			n += node.degree(Direction.OUT);
		return n;
	}

	// -------------------------- Textable --------------------------
	
	@Override
	public String toDetailedString() {
		StringBuilder sb = new StringBuilder();
		sb.append(toShortString())
			.append(" = {");
		int count = 0;
		for (AotNode n:nodes) {
			sb.append(n.toDetailedString());
			if (count<nodes.size()-1)
				sb.append(',');
			count++;
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	public String toShortString() {
		return toUniqueString() + "(" + nodes.size() + " tree nodes / " + nEdges() + " cross-links)"; 
	}

	@Override
	public String toUniqueString() {
		String ptr = super.toString();
		ptr = ptr.substring(ptr.indexOf('@'));
		return getClass().getSimpleName()+ptr; 
	}

	// -------------------------- Object --------------------------
	
	@Override
	public final String toString() {
		return "["+toDetailedString()+"]";
	}

}
