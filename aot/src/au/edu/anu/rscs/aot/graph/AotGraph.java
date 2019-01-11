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
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Graph;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.tree.Tree;
import fr.cnrs.iees.tree.TreeNode;
import fr.cnrs.iees.tree.TreeNodeFactory;

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
		NodeFactory, EdgeFactory, TreeNodeFactory  {

	private List<AotNode> nodes;
	private int minDepth;
	private int maxDepth;
	private AotNode root;

	// constructors
	protected AotGraph() {
		this(new ArrayList<AotNode>());
	}

	// assumes the first element in the list is the root
	public AotGraph(Iterable<AotNode> list) {
		super();
		nodes = new DynamicList<>(list);
		if (list.iterator().hasNext())
			root = list.iterator().next();
	}
	
	// enables one to specify the root
	public AotGraph(Iterable<AotNode> list, AotNode root) {
		this(list);
		this.root = root;
	}

	public EdgeFactory getEdgeFactory() {
		return this;
	}

	public TreeNodeFactory getTreeFactory() {
		return this;
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
		return new AotGraph(parent);
	}

	// ---------------------- NODE FACTORY -------------------------

	// This is disabled because any new node has to be inserted into the tree at the
	// proper spot. We dont want free-floating nodes in an AOT graph because it's a tree.
	@Override
	public AotNode makeNode(String arg0, String arg1, ReadOnlyPropertyList arg2) {
		throw new AotException("Attempt to instantiate an AotNode outside of the tree context.");
	}
	
	// ---------------------- EDGE FACTORY -------------------------

	@Override
	public AotEdge makeEdge(Node start, Node end) {
		return new AotEdge(start, end, this);
	}

	@Override
	public AotEdge makeEdge(Node start, Node end, ReadOnlyPropertyList props) {
		return new AotEdge(start,end,props,this);
	}

	// use with caution - name+label must be unique within the graph
	@Override
	public AotEdge makeEdge(Node start, Node end, String classId, String instanceId, 
			ReadOnlyPropertyList props) {
		AotEdge result = new AotEdge(start,end,props,this);
		result.setLabel(classId);
		result.setName(instanceId);
		return result;
	}


	// ----------------------TREE FACTORY -----------------------------
	
	@Override
	public AotNode makeTreeNode(TreeNode parent,SimplePropertyList props) {
		AotNode node = new AotNode(this);
		node.setParent(parent);
		if (parent!=null)
			parent.addChild(node);
		node.addProperties(props);
		return node;
	}
	
	@Override
	public AotNode makeTreeNode(TreeNode parent) {
		AotNode node = new AotNode(this);
		node.setParent(parent);
		if (parent!=null)
			parent.addChild(node);
		return node;
	}

	// use with caution - name+label must be unique within the graph
	@Override
	public TreeNode makeTreeNode(TreeNode parent, String label, String name, 
			SimplePropertyList props) {
		AotNode node = new AotNode(label,name,this);
		node.setParent(parent);
		if (parent!=null)
			parent.addChild(node);
		node.addProperties(props);
		return node;
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
