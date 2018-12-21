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

import fr.cnrs.iees.graph.DataEdge;
import fr.cnrs.iees.graph.DataNode;
import fr.cnrs.iees.graph.Edge;
import fr.cnrs.iees.graph.Graph;
import fr.cnrs.iees.graph.GraphElementFactory;
import fr.cnrs.iees.graph.Node;
import fr.cnrs.iees.graph.ReadOnlyDataEdge;
import fr.cnrs.iees.graph.ReadOnlyDataNode;
import fr.cnrs.iees.properties.ReadOnlyPropertyList;
import fr.cnrs.iees.properties.SimplePropertyList;
import fr.cnrs.iees.tree.DataTreeNode;
import fr.cnrs.iees.tree.Tree;
import fr.cnrs.iees.tree.TreeNode;
import fr.cnrs.iees.tree.TreeNodeFactory;
/**
 * Re-implementation of AotGraph as a tree, a graph, a tree and graph factory, a configurable graph
 * @author Jacques Gignoux - 21 déc. 2018
 *
 */
public class AotGraph 
	implements 	Tree<AotNode>, Graph<AotNode,AotEdge>,
				ConfigurableGraph,
				GraphElementFactory, TreeNodeFactory {

	@Override
	public Iterable<AotNode> leaves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<AotNode> nodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean contains(AotNode arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<AotEdge> edges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<AotNode> roots() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<AotNode> findNodesByReference(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int maxDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int minDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AotNode root() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tree<AotNode> subTree(AotNode arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edge makeEdge(Node arg0, Node arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReadOnlyDataEdge makeEdge(Node arg0, Node arg1, ReadOnlyPropertyList arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataEdge makeEdge(Node arg0, Node arg1, SimplePropertyList arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node makeNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReadOnlyDataNode makeNode(ReadOnlyPropertyList arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataNode makeNode(SimplePropertyList arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataTreeNode makeDataTreeNode(SimplePropertyList arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeNode makeTreeNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeExceptionList castNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeExceptionList initialise() {
		// TODO Auto-generated method stub
		return null;
	}

}
