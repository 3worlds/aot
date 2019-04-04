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
import java.util.Map;
import au.edu.anu.rscs.aot.AotException;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
import fr.cnrs.iees.graph.impl.DefaultEdgeFactory;
import fr.cnrs.iees.graph.impl.DefaultNodeFactory;
import fr.cnrs.iees.graph.impl.DefaultTreeNodeFactory;
import fr.cnrs.iees.graph.impl.TreeGraph;
import fr.cnrs.iees.graph.impl.TreeGraphFactory;
import fr.cnrs.iees.identity.IdentityScope;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
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
 * CAUTION: this class is broken, the scope() method returns null !
 * 
 * @author Jacques Gignoux - 21 déc. 2018
 *
 */

public class AotGraph extends TreeGraph<AotNode, AotEdge>
		implements ConfigurableGraph, 
			DefaultNodeFactory, 
			DefaultEdgeFactory, 
			DefaultTreeNodeFactory, Textable {
	// NodeFactory is only here because AotNodes must implement NodeFactory - if we
	// change the element hierarchy this would be unnecessary. See getLabel for the
	// confusion this causes.

	private TreeGraphFactory factory;

	// constructors
	public AotGraph() {
		super();
		factory = new TreeGraphFactory();
	}

	public AotGraph(Iterable<AotNode> list) {
		super(list);
		factory = new TreeGraphFactory();
	}

	public AotGraph(Iterable<AotNode> list, Map<String, String> labels) {
		super(list);
		factory = new TreeGraphFactory(labels);
	}

	public AotGraph(Map<String, String> labels) {
		super();
		factory = new TreeGraphFactory(labels);
	}

	/**
	 * builds the graph from a tree root by inserting all children of the argument
	 * 
	 * @param root
	 */
	public AotGraph(AotNode root) {
		super(root);
	}

	public EdgeFactory getEdgeFactory() {
		return this;
	}

	public TreeNodeFactory getTreeFactory() {
		return factory;
		// return this;
	}

	// ---------------------- NODE FACTORY -------------------------

	// This is disabled because any new node has to be inserted into the tree at the
	// proper spot. We dont want free-floating nodes in an AOT graph because it's a
	// tree.
	@Override
	public Node makeNode() {
		throw new AotException("Use of makeNode(...) is discouraged. use makeTreeNode(...) instead.");
	}

	@Override
	public Node makeNode(ReadOnlyPropertyList arg0) {
		return makeNode();
	}

	@Override
	public Node makeNode(String arg0) {
		return makeNode();
	}

	@Override
	public Node makeNode(Class<? extends Node> arg0) {
		return makeNode();
	}

	@Override
	public Node makeNode(String arg0, ReadOnlyPropertyList arg1) {
		return makeNode();
	}

	@Override
	public Node makeNode(Class<? extends Node> arg0, String arg1) {
		return makeNode();
	}

	@Override
	public Node makeNode(Class<? extends Node> arg0, ReadOnlyPropertyList arg1) {
		return makeNode();
	}

	@Override
	public Node makeNode(Class<? extends Node> arg0, String arg1, ReadOnlyPropertyList arg2) {
		return makeNode();
	}

	// ---------------------- EDGE FACTORY -------------------------

	@Override
	public AotEdge makeEdge(Node start, Node end) {
		return (AotEdge) factory.makeEdge(AotEdge.class, start, end);
	}

	@Override
	public AotEdge makeEdge(Node start, Node end, ReadOnlyPropertyList props) {
		return (AotEdge) factory.makeEdge(AotEdge.class, start, end, props);
	}

	@Override
	public AotEdge makeEdge(Node start, Node end, String proposedId, ReadOnlyPropertyList props) {
		return (AotEdge) factory.makeEdge(AotEdge.class, start, end, proposedId, props);
	}

	@Override
	public AotEdge makeEdge(Node start, Node end, String proposedId) {
		return (AotEdge) factory.makeEdge(AotEdge.class, start, end, proposedId);
	}


	@Override
	public AotEdge makeEdge(Class<? extends Edge> edgeClass, Node start, Node end) {
		return (AotEdge) factory.makeEdge(edgeClass, start, end);
	}

	@Override
	public AotEdge makeEdge(Class<? extends Edge> edgeClass, Node start, Node end, String proposedId) {
		return (AotEdge) factory.makeEdge(edgeClass, start, end, proposedId);
	}

	@Override
	public AotEdge makeEdge(Class<? extends Edge> edgeClass, Node start, Node end, ReadOnlyPropertyList props) {
		return (AotEdge) factory.makeEdge(edgeClass, start, end, props);
	}

	@Override
	public AotEdge makeEdge(Class<? extends Edge> edgeClass, Node start, Node end, String proposedId,
			ReadOnlyPropertyList props) {
		return (AotEdge) factory.makeEdge(edgeClass, start, end, proposedId, props);
	}

	// ----------------------TREE FACTORY -----------------------------

	private AotNode addNode(AotNode node) {
		if (nodes.add(node))
			return node;
		else
			throw new AotException("Attempt to add duplicate node:" + node.toString());
	}

	@Override
	public AotNode makeTreeNode(TreeNode parent, SimplePropertyList props) {
		return addNode((AotNode) factory.makeTreeNode(AotNode.class, parent, props));
	}

	@Override
	public AotNode makeTreeNode(TreeNode parent) {
		return addNode((AotNode) factory.makeTreeNode(AotNode.class, parent));
	}

	@Override
	public AotNode makeTreeNode(TreeNode parent, String proposedId) {
		return addNode((AotNode) factory.makeTreeNode(AotNode.class, parent, proposedId));
	}

	@Override
	public AotNode makeTreeNode(TreeNode parent, String proposedId, SimplePropertyList properties) {
		return addNode((AotNode) factory.makeTreeNode(AotNode.class, parent, proposedId, properties));
	}

	@Override
	public AotNode makeTreeNode(Class<? extends TreeNode> nodeClass, TreeNode parent) {
		return addNode((AotNode) factory.makeTreeNode(nodeClass, parent));
	}

	@Override
	public AotNode makeTreeNode(Class<? extends TreeNode> nodeClass, TreeNode parent, SimplePropertyList properties) {
		return addNode((AotNode) factory.makeTreeNode(nodeClass, parent, properties));
	}

	@Override
	public AotNode makeTreeNode(Class<? extends TreeNode> nodeClass, TreeNode parent, String proposedId) {
		return addNode((AotNode) factory.makeTreeNode(nodeClass, parent, proposedId));
	}

	@Override
	public AotNode makeTreeNode(Class<? extends TreeNode> nodeClass, TreeNode parent, String proposedId,
			SimplePropertyList properties) {
		return addNode((AotNode) factory.makeTreeNode(nodeClass, parent, proposedId, properties));
	}

	// -------------------- CONFIGURABLE GRAPH ------------------------

	@SuppressWarnings("unchecked")
	@Override
	public NodeExceptionList castNodes() {
		NodeExceptionList castFailList = new NodeExceptionList();
		List<AotNode> removedNodes = new ArrayList<>();
		List<AotNode> addedNodes = new ArrayList<>();
		for (AotNode n : nodes()) {
			try {
				String className;
				className = (String) n.getPropertyValue("class");
				// I dont like this, so try to remove it
				// if (className != null && !n.getLabel().equals("defaultPropertyList")) {
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

						// newNode.setLabel(n.getLabel());
						// newNode.setName(n.getName());
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
				// log.warning("AotGraph: Node " + n + " could not be cast.", e);
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

	@Override
	public IdentityScope scope() {
		// TODO Auto-generated method stub
		return null;
	}

}
