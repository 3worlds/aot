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
import java.util.logging.Logger;

import au.edu.anu.rscs.aot.AotException;
import fr.cnrs.iees.graph.EdgeFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.NodeFactory;
import fr.cnrs.iees.graph.impl.TreeGraph;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.graph.TreeNode;
import fr.cnrs.iees.graph.TreeNodeFactory;
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

public class AotGraph extends TreeGraph<AotNode,AotEdge> 
	implements ConfigurableGraph, NodeFactory, EdgeFactory, TreeNodeFactory, Textable {

	private Logger log = Logger.getLogger(AotGraph.class.getName());

	// constructors
	public AotGraph() {
		super();
	}

	public AotGraph(Iterable<AotNode> list) {
		super(list);
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
		return this;
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

	// creates a node with a generated unique id as name
	@Override
	public AotNode makeTreeNode(TreeNode parent, SimplePropertyList props) {
		return makeTreeNode(parent, null, null, props);
	}

	// creates a node with a generated unique id as name
	@Override
	public AotNode makeTreeNode(TreeNode parent) {
		return makeTreeNode(parent, null, null, null);
	}

	// create a node with a name - will crash if name is null
	@Override
	public AotNode makeTreeNode(TreeNode parent, String label, String name) {
		return makeTreeNode(parent, label, name, null);
	}

	// creates a node with an empty name
	@Override
	public AotNode makeTreeNode(TreeNode parent, String label) {
		return makeTreeNode(parent, label, "", null);
	}

	// creates a node with an empty name
	@Override
	public AotNode makeTreeNode(TreeNode parent, String label, SimplePropertyList properties) {
		return makeTreeNode(parent, label, "", properties);
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
		if (name==null)
			node = new AotNode(label,this,props);
		else
			node = new AotNode(label,name,this,props);
		if (!nodes.add(node)) {
			log.warning(() -> "Duplicate Node insertion: " + node.toDetailedString());
			return null;
		} else {
			node.setParent(parent);
			if (parent != null)
				parent.addChild(node);
			return node;
		}
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

}
