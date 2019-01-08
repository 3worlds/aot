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
import java.util.List;

import au.edu.anu.rscs.aot.AotException;
import au.edu.anu.rscs.aot.collections.DynamicList;
import au.edu.anu.rscs.aot.collections.QuickListOfLists;
import fr.cnrs.iees.graph.Direction;
import fr.cnrs.iees.graph.Graph;
import fr.cnrs.iees.graph.GraphElementFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.impl.DefaultGraphFactory;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.tree.Tree;
import fr.cnrs.iees.tree.TreeNode;
import fr.cnrs.iees.tree.TreeNodeFactory;
import fr.cnrs.iees.tree.impl.DefaultTreeFactory;

/**
 * <p>Re-implementation of AotGraph as a tree, a graph, a tree and graph factory, a
 * configurable graph.</p>
 * 
 * <p>NOTE: This implementation assumes that every newly created node is inserted within the
 * graph (factory interfaces). </p>
 * <p>Should this class implement DynamicGraph ? it's already mutable because it is a factory...</p>
 * 
 * @author Jacques Gignoux - 21 déc. 2018
 *
 */

public class AotGraph implements Tree<AotNode>, Graph<AotNode, AotEdge>, ConfigurableGraph,
		GraphElementFactory, TreeNodeFactory  {

	private List<AotNode> nodes;
	private int minDepth;
	private int maxDepth;
	private AotNode root;
	private GraphElementFactory graphElementFactory;
	private TreeNodeFactory treeFactory;

	// constructors
	protected AotGraph() {
		this(new ArrayList<AotNode>());
	}

	protected AotGraph(Iterable<AotNode> list) {
		super();
		nodes = new DynamicList<>(list);
		graphElementFactory = new DefaultGraphFactory();
		treeFactory = new DefaultTreeFactory();
	}

	public GraphElementFactory getGraphElementFactory() {
		return graphElementFactory;
	}

	public TreeNodeFactory getTreeFactory() {
		return treeFactory;
	}

	/**
	 * builds the graph from a tree root by inserting all children of the argument
	 * @param root
	 */
	public AotGraph(AotNode root) {
		this(new ArrayList<AotNode>());
		this.root = root;
		insertOnlyChildren(root, nodes);
		computeDepths(root);
	}

	private void computeDepths(AotNode parent) {
		// TODO Auto-generated method stub

	}

	// Bit of a mess here
	private void insertOnlyChildren(TreeNode parent, List<AotNode> list) {
		for (TreeNode child : parent.getChildren()) {
			list.add((AotNode) child);
			insertOnlyChildren(child, list);
		}
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
			if (n.isRoot())
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
		Iterable<AotNode> roots = roots();
		int count = 0;
		AotNode result = null;
		for (AotNode n : roots) {
			result = n;
			count++;
			if (count > 1)
				return null;
		}
		return result;
	}

	@Override
	public AotNode root() {
		// Can only be asked of a valid graph so when
		// should it be set? Use lazy method but is dodgy
		if (root == null)
			root = findRoot();
		return root;
	}

	@Override
	public Tree<AotNode> subTree(AotNode parent) {
		// TODO Auto-generated method stub
		// So we make a new AotGraph from this node using a Tree constructor.
		// BUT what happens to xlinks? They must be removed!!
		// trees dont know about edges, so no need to remove them. that's the power of interfaces
		return new AotGraph(parent);
	}

	// ----------------------GRAPH ELEMENT FACTORY -------------------------

	@Override
	public AotEdge makeEdge(Node start, Node end) {
		return new AotEdge(start, end, this);
	}

	@Override
	public AotEdge makeEdge(Node arg0, Node arg1, ReadOnlyPropertyList arg2) {
		return new AotEdge(arg0,arg1,arg2,this);
	}

	@Override
	public AotEdge makeEdge(Node arg0, Node arg1, SimplePropertyList arg2) {
		return new AotEdge(arg0,arg1,arg2,this);
	}

	@Override
	public AotNode makeNode() {
		AotNode node = new AotNode(this);
		nodes.add(node);
		return node;
	}

	@Override
	public AotNode makeNode(ReadOnlyPropertyList arg0) {
		AotNode node = new AotNode(this);
		nodes.add(node);
		node.addProperties(arg0);
		return node;
	}

	@Override
	public AotNode makeNode(SimplePropertyList arg0) {
		AotNode node = new AotNode(this);
		nodes.add(node);
		node.addProperties(arg0);
		return node;
	}

	// ----------------------TREE FACTORY -----------------------------
	// These methods are disabled because one cannot know where to insert the tree node in the
	// tree hierarchy - makeNode() methods should be used instead.
	// TODO: we may need these one day - how do we insert tree nodes at the right spot otherwise?
	// the problem is that the tree structure is in the tree nodes, not in the tree.
	
	@Override
	public AotNode makeDataTreeNode(SimplePropertyList props) {
		throw new AotException("Cannot create a tree node in AotGraph: where should it be inserted?");
	}
	
	@Override
	public AotNode makeTreeNode() {
		throw new AotException("Cannot create a tree node in AotGraph: where should it be inserted?");
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
						Class<? extends AotNode> nodeClass = (Class<? extends AotNode>) Class.forName(className,false,c);
						Constructor<? extends AotNode> nodeConstructor = nodeClass.getConstructor();
						// NOTE: a node constructor always has a factory as argument...
						newNode = nodeConstructor.newInstance(this);
						newNode.setLabel(n.getLabel());
						newNode.setName(n.getName());
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
		NodeInitialiser initialiser = new NodeInitialiser(this);
		initialiser.showInitialisationOrder();
		return initialiser.initialise();
	}

}
